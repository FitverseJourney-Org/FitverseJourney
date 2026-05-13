# Offline-First Architecture — FitverseJourneyApp

> **Audiência:** Engenheiros que vão implementar ou revisar a camada de dados.  
> **Stack:** Kotlin Multiplatform · SQLDelight · DataStore · Ktor · Firebase · Koin

---

## 1. Princípio Central

> **A fonte de verdade do usuário é sempre o banco local.**  
> A rede é usada para sincronizar, nunca para bloquear.

O fluxo de qualquer dado segue a regra:

```
UI lê do local  →  local emite via Flow  →  background sincroniza com remoto
```

O usuário **nunca espera rede** para ver ou interagir com os dados. A sincronização acontece de forma transparente quando a conexão está disponível.

---

## 2. Visão Geral da Arquitetura em Camadas

```
┌─────────────────────────────────────────────────────────────────┐
│                       PRESENTATION (MVI)                        │
│  ViewModel → UiState → Screen                                   │
│  Observa Flows do domínio. Nunca acessa dados diretamente.      │
└──────────────────────────────┬──────────────────────────────────┘
                               │ observe / suspend
┌──────────────────────────────▼──────────────────────────────────┐
│                         DOMAIN                                  │
│  UseCase · Repository (interface) · Record types · DAO ifaces   │
│  Regras de negócio puras. Zero dependência de framework.        │
└──────────────┬───────────────────────────────┬──────────────────┘
               │                               │
┌──────────────▼──────────┐   ┌───────────────▼──────────────────┐
│   DATA / LOCAL          │   │   DATA / REMOTE                  │
│  SQLDelight · DataStore │   │  Ktor HttpClient · Firebase Auth  │
│  DaoImpl · DataSource   │   │  RemoteDataSourceImpl · DTOs      │
│  Única fonte de verdade │   │  Sincronização assíncrona        │
└─────────────────────────┘   └──────────────────────────────────┘
```

---

## 3. Fluxo de Dados — Read

```
ViewModel.observeX()
    │
    ▼
UseCase.invoke()                  (domínio puro)
    │
    ▼
Repository.observe()              (interface de domínio)
    │
    ├──► LocalDataSource.observeX()   ◄─────────────┐
    │        │  SQLDelight Flow                       │
    │        │  emite imediatamente                   │ upsertAll()
    │        ▼                                        │
    │    UI recebe dados locais                       │
    │                                                 │
    └──► syncScope.launch {                           │
             RemoteDataSource.fetchX()  ──────────────┘
                 .onSuccess { upsert local }
                 .onFailure { log / fila }
         }
```

**Regra:** O `Flow` retorna **antes** da chamada remota terminar. A UI sempre tem dados, mesmo que levemente desatualizados.

---

## 4. Fluxo de Dados — Write

```
ViewModel.onIntent(SaveX)
    │
    ▼
UseCase.save()
    │
    ▼
Repository.save()
    │
    ├──► 1. LocalDataSource.insert()     ← gravação imediata, UI reflete na hora
    │
    ├──► 2. syncScope.launch {
    │           RemoteDataSource.post()
    │               .onFailure {
    │                   SyncQueue.enqueue(pendingOp)  ← para retry posterior
    │               }
    │       }
    │
    └──► return Result.success(entity)   ← não aguarda resposta da rede
```

---

## 5. Tabelas e Estratégia de Sincronização

Cada tabela tem uma política de **sync**, **origem da verdade** e **TTL (tempo de validade do cache)**.

| Tabela | Direção de Sync | Source of Truth | TTL Cache | Estratégia de Conflito |
|---|---|---|---|---|
| `UserEntity` | Bidirecional | Remoto (Firebase) | Sem limite | Remote wins (última escrita vence) |
| `UserStatsEntity` | Local → Remoto | Local | 1 dia | Merge por campo (soma XP, max streak) |
| `DailyMissionEntity` | Remoto → Local | Remoto | 1 dia | Replace diário (gerado pelo servidor) |
| `WorkoutSessionEntity` | Local → Remoto | Local | 90 dias | Local wins (dado gerado pelo usuário) |
| `WorkoutSetEntity` | Local → Remoto | Local | 90 dias | Local wins (dado gerado pelo usuário) |
| `MealEntryEntity` | Local → Remoto | Local | 30 dias | Local wins |
| `FoodItemEntity` | Local → Remoto | Local | 30 dias | Local wins |
| `AchievementEntity` | Remoto → Local | Remoto | Permanente | Remote wins (desbloqueios validados) |
| `NotificationEntity` | Remoto → Local | Remoto | 7 dias | Append-only (nunca sobrescreve) |
| `StreakEntity` | Bidirecional | Remoto (validação) | 90 dias | Remoto valida, local aplica |

---

## 6. Estrutura do Repository — Padrão Offline-First

Todo repository que lida com dados persistidos deve seguir esta estrutura:

```kotlin
class WorkoutSessionRepositoryImpl(
    private val localDao: WorkoutSessionDao,        // domínio
    private val remoteDataSource: WorkoutRemoteDataSource,
    private val networkMonitor: NetworkMonitor,
) : WorkoutSessionRepository {

    // ── Scope isolado: falhas não propagam para o caller ──────────────────────
    private val syncScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // ── READ: sempre do local, sync em background ─────────────────────────────
    override fun observeSessions(userId: String): Flow<List<WorkoutSession>> =
        flow {
            emitAll(
                localDao.observeSessionsByUser(userId)
                    .map { records -> records.map { it.toDomain() } }
            )
        }.also {
            syncScope.launch { refreshFromRemote(userId) }
        }

    // ── WRITE: local primeiro, remote em background ───────────────────────────
    override suspend fun saveSession(session: WorkoutSession): Result<WorkoutSession> =
        runCatching {
            val record = session.toRecord()
            localDao.insertSession(record)               // ← imediato, UI atualiza

            syncScope.launch {
                remoteDataSource.postSession(session.toDto())
                    .onFailure { SyncQueue.enqueue(SyncOp.UpsertSession(record)) }
            }
            session
        }

    // ── SYNC: pull do remoto para o local ─────────────────────────────────────
    private suspend fun refreshFromRemote(userId: String) {
        remoteDataSource.fetchSessions(userId)
            .onSuccess { dtos ->
                val records = dtos.map { it.toRecord() }
                records.forEach { localDao.insertSession(it) }
            }
    }
}
```

### Regras obrigatórias para todos os repositories

- `syncScope` com `SupervisorJob()` — falhas de sync não cancelam o scope pai
- Escrita local **sempre** antes da escrita remota
- `runCatching` para capturar exceções sem crashar a UI
- Remote failures vão para `SyncQueue`, **nunca** para o usuário como erro bloqueante

---

## 7. Fila de Sincronização (SyncQueue)

Operações que falharam por falta de rede são enfileiradas e reprocessadas quando a conexão é restabelecida.

```
┌─────────────────────────────────────────────────────────────┐
│  NetworkMonitor.isConnectedFlow()                           │
│         │                                                   │
│         ▼  (connected = true)                               │
│  SyncWorker.drainQueue()                                    │
│         │                                                   │
│         ├─► SyncOp.UpsertSession  → remoteDataSource.post() │
│         ├─► SyncOp.UpsertMeal     → remoteDataSource.post() │
│         ├─► SyncOp.MarkNotifRead  → remoteDataSource.patch()│
│         └─► ...                                             │
└─────────────────────────────────────────────────────────────┘
```

### Modelo da fila (DataStore de pending ops)

```kotlin
sealed interface SyncOp {
    data class UpsertSession(val record: WorkoutSessionRecord) : SyncOp
    data class UpsertMeal(val record: MealEntryRecord)         : SyncOp
    data class CompleteAchievement(val id: String, val userId: String) : SyncOp
    data class MarkNotificationRead(val id: String, val userId: String) : SyncOp
    data class AddXp(val userId: String, val amount: Int)      : SyncOp
}
```

---

## 8. NetworkMonitor — Integração

```kotlin
// Interface no domain (expect/actual por plataforma)
interface NetworkMonitor {
    fun isConnectedFlow(): Flow<Boolean>
    suspend fun isConnected(): Boolean
}

// Uso no repository
override suspend fun updateUser(user: User): User {
    localDataSource.updateUser(entityMapper.mapDomainToEntity(user))  // sempre
    if (networkMonitor.isConnected()) {
        remoteDataSource.updateUser(user.uid, dtoMapper.mapDomainToRequestDto(user))
    }
    // se offline: SyncQueue garante que vai sincronizar depois
    return user
}
```

---

## 9. Invalidação de Cache e Limpeza

Dados antigos são removidos periodicamente para não lotar o banco:

```kotlin
// Chamado ao abrir o app (SplashViewModel ou AppInitializer)
suspend fun cleanupExpiredData(userId: String) {
    val now = Clock.System.now()
    
    missionDao.deleteMissionsOlderThan(userId, beforeDate = (now - 7.days).toDateString())
    sessionDao.deleteSession(...)    // sessões > 90 dias removidas do local
    notificationDao.deleteOldNotifications(userId, beforeEpoch = (now - 7.days).toEpochMs())
    streakDao.deleteOldRecords(userId, beforeDate = (now - 90.days).toDateString())

    userStatsDao.resetDailyStats(userId, now = now.toEpochMs())  // meia-noite
}
```

---

## 10. Mapeamento entre Camadas

Cada dado passa por transformações explícitas entre camadas. **Nunca** exponha um tipo de uma camada na outra.

```
[SQLDelight Generated]  →  toRecord()   →  [Domain Record]
[Domain Record]         →  toDomain()   →  [Domain Model]
[Domain Model]          →  toUi()       →  [UI Model / UiState]

[Domain Model]          →  toRecord()   →  [Domain Record]
[Domain Record]         →  toEntity()   →  [SQLDelight Insert params]

[Domain Model]          →  toDto()      →  [Remote DTO]
[Remote DTO]            →  toDomain()   →  [Domain Model]
```

### Exemplo completo: WorkoutSession

```
WorkoutSessionEntity (SQLDelight)
    │  .toRecord()
    ▼
WorkoutSessionRecord (domain/sqldelight)
    │  .toDomain()
    ▼
WorkoutSession (domain/models)
    │  .toUi()
    ▼
WorkoutHistory (presentation/ui/historic)  ← o que a UI consome
```

---

## 11. Tabelas por Módulo — Referência Rápida

### Dados do Usuário

| Tabela | Chave | Campos principais |
|---|---|---|
| `UserEntity` | `uid` | nome, email, peso, altura, metas nutricionais |
| `UserStatsEntity` | `userId` | XP atual, nível, passos hoje, água, streak, total treinos |

### Gamificação

| Tabela | Chave | Campos principais |
|---|---|---|
| `DailyMissionEntity` | `id` | userId, date, type, xpReward, isCompleted, completedAt |
| `AchievementEntity` | `id + userId` | rarity, status, progress, category, unlockedAt |
| `StreakEntity` | `userId + date` (UNIQUE) | isCheckedIn, streakCount |

### Treino

| Tabela | Chave | Campos principais |
|---|---|---|
| `WorkoutSessionEntity` | `id` | userId, title, duração, volume total, XP, PRs, grupos musculares |
| `WorkoutSetEntity` | `id` | sessionId, exercício, série, carga (kg), reps, isPR, RPE |

### Nutrição

| Tabela | Chave | Campos principais |
|---|---|---|
| `MealEntryEntity` | `id` | userId, date, mealType, kcal, proteína, carboidrato, gordura |
| `FoodItemEntity` | `id` | mealId, nome, porção, unidade, macros |

### Comunicação

| Tabela | Chave | Campos principais |
|---|---|---|
| `NotificationEntity` | `id` | userId, type, isRead, createdAt (epoch ms) |

---

## 12. Queries Críticas — Índices Existentes

Os índices foram criados nas tabelas mais consultadas. Não adicionar lógica que faça full-scan nas tabelas abaixo:

| Índice | Tabela | Colunas | Caso de uso |
|---|---|---|---|
| `idx_daily_mission_user_date` | `DailyMissionEntity` | userId, date | Missões do dia atual |
| `idx_workout_session_user_started` | `WorkoutSessionEntity` | userId, startedAt DESC | Histórico paginado |
| `idx_workout_set_session` | `WorkoutSetEntity` | sessionId | Séries de uma sessão |
| `idx_workout_set_exercise` | `WorkoutSetEntity` | exerciseName | Progressão por exercício |
| `idx_meal_entry_user_date` | `MealEntryEntity` | userId, date | Refeições do dia |
| `idx_food_item_meal` | `FoodItemEntity` | mealId | Alimentos de uma refeição |
| `idx_achievement_user_status` | `AchievementEntity` | userId, status | Filtro por status |
| `idx_achievement_user_category` | `AchievementEntity` | userId, category | Filtro por categoria |
| `idx_notification_user_created` | `NotificationEntity` | userId, createdAt DESC | Lista cronológica |
| `idx_notification_unread` | `NotificationEntity` | userId, isRead | Badge de não-lidas |
| `idx_streak_user_date` (UNIQUE) | `StreakEntity` | userId, date | Upsert de check-in |

---

## 13. Checklist — Adicionando uma Nova Feature com Dados

- [ ] Criar schema `.sq` em `data/local/sqldelight/migrations/` com CREATE TABLE + queries
- [ ] Criar entity data class em `data/local/model/`
- [ ] Criar DAO interface em `domain/repository/dbLocal/sqldelight/<feature>/`
- [ ] Criar DAO implementation em `data/local/datasource/<feature>/` com `withContext(Dispatchers.IO)`
- [ ] Registrar DAO no `dataSourceModule` em `DataModules.kt`
- [ ] Criar Repository interface em `domain/repository/`
- [ ] Criar RepositoryImpl em `data/repository/` seguindo o padrão `syncScope` + local-first
- [ ] Registrar repository no `repositoryModule`
- [ ] Criar UseCase(s) em `domain/usecase/<feature>/`
- [ ] Registrar use cases no `DomainModule.kt`
- [ ] Criar UiState/Intent/Event em `presentation/ui/<feature>/viewmodel/`
- [ ] Criar ViewModel em `presentation/ui/<feature>/viewmodel/`
- [ ] Criar Screen + Root composable em `presentation/ui/<feature>/`
- [ ] Registrar ViewModel no `PresentationModule.kt`
- [ ] Definir limpeza de cache em `cleanupExpiredData()` se dado tiver TTL

---

## 14. Decisões de Design — Justificativas

| Decisão | Alternativa considerada | Motivo da escolha |
|---|---|---|
| SQLDelight como banco local | Room (Android-only) | KMP: funciona em Android e iOS com o mesmo código |
| `syncScope + SupervisorJob` | Chamar remote no mesmo scope | Falha de rede não cancela operação local nem propaga erro para a UI |
| `Flow` em todos os DAOs de leitura | `suspend fun` retornando lista | UI recebe atualizações automáticas quando o banco muda (sem polling) |
| IDs gerados no cliente (`UUID.random()`) | IDs gerados pelo servidor | Permite inserção offline imediata; servidor aceita o ID do cliente |
| `INSERT OR REPLACE` no SQLDelight | `INSERT OR IGNORE` + UPDATE separado | Simplifica upsert; safe para sync bidirecional |
| DataStore para preferências e auth | SharedPreferences | API coroutine-native, type-safe com `Preferences.Key`, KMP compatível |
| Índices em userId+date (não só userId) | Índice só em userId | Queries de "hoje" ou "esta semana" são o padrão; índice composto elimina full-scan |
