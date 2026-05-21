# Database Tables — SQLDelight Local (AppDatabase)

Generated schema: `com.journey` | Driver: SQLDelight 2.x · `generateAsync = true`

---

## AchievementEntity

**Pacote:** `com.journey.achievement`  
**Propósito:** Conquistas do usuário com progresso e raridade.

```sql
CREATE TABLE IF NOT EXISTS AchievementEntity (
    id          TEXT    NOT NULL PRIMARY KEY,
    userId      TEXT    NOT NULL,
    icon        TEXT    NOT NULL DEFAULT '🏆',
    title       TEXT    NOT NULL,
    description TEXT    NOT NULL DEFAULT '',
    xpReward    INTEGER NOT NULL DEFAULT 0,
    rarity      TEXT    NOT NULL DEFAULT 'COMMON',    -- COMMON, RARE, EPIC, LEGENDARY
    status      TEXT    NOT NULL DEFAULT 'LOCKED',    -- LOCKED, IN_PROGRESS, UNLOCKED
    category    TEXT    NOT NULL,                     -- TREINO, NUTRICAO, STREAK, SOCIAL, ESPECIAIS
    progress    REAL    NOT NULL DEFAULT 0.0,         -- 0.0 – 1.0
    maxProgress REAL    NOT NULL DEFAULT 1.0,
    condition   TEXT    NOT NULL DEFAULT '',
    unlockedAt  TEXT                                  -- data display, nullable
);
```

| Coluna | Tipo | Obrigatório | Padrão | Descrição |
|---|---|---|---|---|
| id | TEXT | ✅ | — | PK única da conquista |
| userId | TEXT | ✅ | — | FK lógica para o usuário |
| icon | TEXT | ✅ | 🏆 | Emoji representando a conquista |
| title | TEXT | ✅ | — | Nome da conquista |
| description | TEXT | ✅ | '' | Descrição detalhada |
| xpReward | INTEGER | ✅ | 0 | XP concedido ao desbloquear |
| rarity | TEXT | ✅ | COMMON | COMMON · RARE · EPIC · LEGENDARY |
| status | TEXT | ✅ | LOCKED | LOCKED · IN_PROGRESS · UNLOCKED |
| category | TEXT | ✅ | — | TREINO · NUTRICAO · STREAK · SOCIAL · ESPECIAIS |
| progress | REAL | ✅ | 0.0 | Progresso atual (0.0–1.0) |
| maxProgress | REAL | ✅ | 1.0 | Limite máximo do progresso |
| condition | TEXT | ✅ | '' | Condição textual de desbloqueio |
| unlockedAt | TEXT | ❌ | NULL | Data de desbloqueio (display) |

**Índices:**
- `idx_achievement_user_status` → `(userId, status)`
- `idx_achievement_user_category` → `(userId, category)`

---

## CatalogMissionEntity

**Pacote:** `com.journey.mission`  
**Propósito:** Catálogo fixo de missões disponíveis. Populado uma única vez via seeds locais.

```sql
CREATE TABLE IF NOT EXISTS CatalogMissionEntity (
    id          TEXT    NOT NULL PRIMARY KEY,
    title       TEXT    NOT NULL,
    description TEXT    NOT NULL DEFAULT '',
    xpReward    INTEGER NOT NULL DEFAULT 10,
    type        TEXT    NOT NULL DEFAULT 'CARDIO',
    isSpecial   INTEGER NOT NULL DEFAULT 0   -- 0 = regular, 1 = special/challenge
);
```

| Coluna | Tipo | Obrigatório | Padrão | Descrição |
|---|---|---|---|---|
| id | TEXT | ✅ | — | PK da missão |
| title | TEXT | ✅ | — | Título da missão |
| description | TEXT | ✅ | '' | Descrição |
| xpReward | INTEGER | ✅ | 10 | XP concedido ao completar |
| type | TEXT | ✅ | CARDIO | Tipo da missão |
| isSpecial | INTEGER | ✅ | 0 | 0 = regular · 1 = especial/challenge |

---

## DailyMissionEntity

**Pacote:** `com.journey.mission`  
**Propósito:** Missões diárias do usuário, geradas por dia. Resetadas com `deleteMissionsOlderThan`.

```sql
CREATE TABLE IF NOT EXISTS DailyMissionEntity (
    id          TEXT    NOT NULL PRIMARY KEY,
    userId      TEXT    NOT NULL,
    date        TEXT    NOT NULL,          -- YYYY-MM-DD
    title       TEXT    NOT NULL,
    description TEXT    NOT NULL DEFAULT '',
    xpReward    INTEGER NOT NULL DEFAULT 0,
    type        TEXT    NOT NULL,          -- STRETCH, HYDRATION, CARDIO, STRENGTH, NUTRITION, SLEEP, STEPS, MEDITATION, CHALLENGE
    isCompleted INTEGER NOT NULL DEFAULT 0,
    completedAt INTEGER                    -- epoch ms, NULL = não concluída
);
```

| Coluna | Tipo | Obrigatório | Padrão | Descrição |
|---|---|---|---|---|
| id | TEXT | ✅ | — | PK da missão diária |
| userId | TEXT | ✅ | — | FK lógica para o usuário |
| date | TEXT | ✅ | — | Data no formato YYYY-MM-DD |
| title | TEXT | ✅ | — | Título da missão |
| description | TEXT | ✅ | '' | Descrição |
| xpReward | INTEGER | ✅ | 0 | XP ao completar |
| type | TEXT | ✅ | — | STRETCH · HYDRATION · CARDIO · STRENGTH · NUTRITION · SLEEP · STEPS · MEDITATION · CHALLENGE |
| isCompleted | INTEGER | ✅ | 0 | 0 = pendente · 1 = concluída |
| completedAt | INTEGER | ❌ | NULL | Epoch ms de conclusão |

**Índices:**
- `idx_daily_mission_user_date` → `(userId, date)`

---

## FoodItemEntity

**Pacote:** `com.journey.nutrition`  
**Propósito:** Alimentos individuais vinculados a uma refeição (`MealEntryEntity`). Totais da refeição são recalculados via `sumMacrosByMeal` após cada insert.

```sql
CREATE TABLE IF NOT EXISTS FoodItemEntity (
    id      TEXT    NOT NULL PRIMARY KEY,
    mealId  TEXT    NOT NULL,
    name    TEXT    NOT NULL,
    portion REAL    NOT NULL DEFAULT 100.0,  -- quantidade
    unit    TEXT    NOT NULL DEFAULT 'g',    -- g, ml, unidade, colher...
    kcal    INTEGER NOT NULL DEFAULT 0,
    protein REAL    NOT NULL DEFAULT 0.0,
    carbs   REAL    NOT NULL DEFAULT 0.0,
    fat     REAL    NOT NULL DEFAULT 0.0
);
```

| Coluna | Tipo | Obrigatório | Padrão | Descrição |
|---|---|---|---|---|
| id | TEXT | ✅ | — | PK do alimento |
| mealId | TEXT | ✅ | — | FK lógica para `MealEntryEntity.id` |
| name | TEXT | ✅ | — | Nome do alimento |
| portion | REAL | ✅ | 100.0 | Quantidade consumida |
| unit | TEXT | ✅ | g | Unidade: g · ml · unidade · colher… |
| kcal | INTEGER | ✅ | 0 | Calorias |
| protein | REAL | ✅ | 0.0 | Proteína em gramas |
| carbs | REAL | ✅ | 0.0 | Carboidratos em gramas |
| fat | REAL | ✅ | 0.0 | Gordura em gramas |

**Índices:**
- `idx_food_item_meal` → `(mealId)`

---

## MealEntryEntity

**Pacote:** `com.journey.nutrition`  
**Propósito:** Refeições diárias do usuário. Totais (kcal, proteína, carbs, gordura) são agregados a partir de `FoodItemEntity` via `updateMealTotals`. Refeições de dias anteriores são deletadas no init do app via `deleteMealsByUserBeforeDate`.

```sql
CREATE TABLE IF NOT EXISTS MealEntryEntity (
    id           TEXT    NOT NULL PRIMARY KEY,
    userId       TEXT    NOT NULL,
    date         TEXT    NOT NULL,    -- YYYY-MM-DD
    mealType     TEXT    NOT NULL,    -- CUSTOM (user-defined name)
    name         TEXT    NOT NULL,
    totalKcal    INTEGER NOT NULL DEFAULT 0,
    totalProtein REAL    NOT NULL DEFAULT 0.0,  -- gramas
    totalCarbs   REAL    NOT NULL DEFAULT 0.0,
    totalFat     REAL    NOT NULL DEFAULT 0.0,
    loggedAt     INTEGER NOT NULL               -- epoch ms
);
```

| Coluna | Tipo | Obrigatório | Padrão | Descrição |
|---|---|---|---|---|
| id | TEXT | ✅ | — | PK da refeição |
| userId | TEXT | ✅ | — | FK lógica para o usuário |
| date | TEXT | ✅ | — | Data no formato YYYY-MM-DD |
| mealType | TEXT | ✅ | — | Tipo definido pelo usuário (CUSTOM) |
| name | TEXT | ✅ | — | Nome da refeição |
| totalKcal | INTEGER | ✅ | 0 | Total de calorias (calculado) |
| totalProtein | REAL | ✅ | 0.0 | Total de proteína em gramas (calculado) |
| totalCarbs | REAL | ✅ | 0.0 | Total de carboidratos em gramas (calculado) |
| totalFat | REAL | ✅ | 0.0 | Total de gordura em gramas (calculado) |
| loggedAt | INTEGER | ✅ | — | Epoch ms de registro |

**Índices:**
- `idx_meal_entry_user_date` → `(userId, date)`

---

## NotificationEntity

**Pacote:** `com.journey.notification`  
**Propósito:** Notificações persistidas do usuário. Notificações lidas antigas podem ser purgadas via `deleteOldNotifications`.

```sql
CREATE TABLE IF NOT EXISTS NotificationEntity (
    id          TEXT    NOT NULL PRIMARY KEY,
    userId      TEXT    NOT NULL,
    type        TEXT    NOT NULL,              -- XP, STREAK, CURTIDA, COMENTARIO, TREINO, CONQUISTA, DESAFIO, RANKING, SISTEMA
    title       TEXT    NOT NULL,
    description TEXT    NOT NULL DEFAULT '',
    isRead      INTEGER NOT NULL DEFAULT 0,   -- 0 = não lida, 1 = lida
    createdAt   INTEGER NOT NULL              -- epoch ms
);
```

| Coluna | Tipo | Obrigatório | Padrão | Descrição |
|---|---|---|---|---|
| id | TEXT | ✅ | — | PK da notificação |
| userId | TEXT | ✅ | — | FK lógica para o usuário |
| type | TEXT | ✅ | — | XP · STREAK · CURTIDA · COMENTARIO · TREINO · CONQUISTA · DESAFIO · RANKING · SISTEMA |
| title | TEXT | ✅ | — | Título da notificação |
| description | TEXT | ✅ | '' | Corpo da notificação |
| isRead | INTEGER | ✅ | 0 | 0 = não lida · 1 = lida |
| createdAt | INTEGER | ✅ | — | Epoch ms de criação |

**Índices:**
- `idx_notification_user_created` → `(userId, createdAt DESC)`
- `idx_notification_unread` → `(userId, isRead)`

---

## StreakEntity

**Pacote:** `com.journey.streak`  
**Propósito:** Registro diário de check-in e sequência acumulada. Um registro por `(userId, date)` garantido por índice UNIQUE.

```sql
CREATE TABLE IF NOT EXISTS StreakEntity (
    id          TEXT    NOT NULL PRIMARY KEY,
    userId      TEXT    NOT NULL,
    date        TEXT    NOT NULL,            -- YYYY-MM-DD (único por usuário + dia)
    isCheckedIn INTEGER NOT NULL DEFAULT 0, -- 0/1
    streakCount INTEGER NOT NULL DEFAULT 0  -- streak acumulado naquele dia
);
```

| Coluna | Tipo | Obrigatório | Padrão | Descrição |
|---|---|---|---|---|
| id | TEXT | ✅ | — | PK do registro |
| userId | TEXT | ✅ | — | FK lógica para o usuário |
| date | TEXT | ✅ | — | Data YYYY-MM-DD |
| isCheckedIn | INTEGER | ✅ | 0 | 0/1 — check-in realizado hoje |
| streakCount | INTEGER | ✅ | 0 | Streak acumulado até esse dia |

**Índices:**
- `idx_streak_user_date` (UNIQUE) → `(userId, date)`

---

## UserEntity

**Pacote:** `com.journey.user`  
**Propósito:** Perfil do usuário com dados pessoais, metas e preferências físicas.

```sql
CREATE TABLE IF NOT EXISTS UserEntity (
    uid             TEXT    NOT NULL PRIMARY KEY,
    name            TEXT    NOT NULL,
    email           TEXT    NOT NULL UNIQUE,
    lastname        TEXT    NOT NULL DEFAULT '',
    username        TEXT    NOT NULL DEFAULT '',
    birthDate       TEXT    NOT NULL DEFAULT '',
    gender          TEXT    NOT NULL,
    classType       TEXT    NOT NULL DEFAULT '',
    weight          REAL    NOT NULL,
    height          INTEGER NOT NULL,
    experienceLevel TEXT    NOT NULL,
    goals           TEXT    NOT NULL,
    isPremium       INTEGER NOT NULL DEFAULT 0,
    targetWeight    REAL,
    targetCalories  INTEGER,
    targetProtein   REAL,
    targetCarbs     REAL,
    targetFat       REAL,
    createdAt       INTEGER NOT NULL,
    updatedAt       INTEGER NOT NULL
);
```

| Coluna | Tipo | Obrigatório | Padrão | Descrição |
|---|---|---|---|---|
| uid | TEXT | ✅ | — | PK — ID do usuário (Firebase UID) |
| name | TEXT | ✅ | — | Primeiro nome |
| email | TEXT | ✅ | — | E-mail (UNIQUE) |
| lastname | TEXT | ✅ | '' | Sobrenome |
| username | TEXT | ✅ | '' | Nome de usuário público |
| birthDate | TEXT | ✅ | '' | Data de nascimento |
| gender | TEXT | ✅ | — | Gênero |
| classType | TEXT | ✅ | '' | Tipo de plano/classe |
| weight | REAL | ✅ | — | Peso atual (kg) |
| height | INTEGER | ✅ | — | Altura (cm) |
| experienceLevel | TEXT | ✅ | — | Nível de experiência |
| goals | TEXT | ✅ | — | Objetivos (serializado) |
| isPremium | INTEGER | ✅ | 0 | 0 = free · 1 = premium |
| targetWeight | REAL | ❌ | NULL | Peso alvo (kg) |
| targetCalories | INTEGER | ❌ | NULL | Meta calórica diária |
| targetProtein | REAL | ❌ | NULL | Meta de proteína (g) |
| targetCarbs | REAL | ❌ | NULL | Meta de carboidratos (g) |
| targetFat | REAL | ❌ | NULL | Meta de gordura (g) |
| createdAt | INTEGER | ✅ | — | Epoch ms de criação |
| updatedAt | INTEGER | ✅ | — | Epoch ms da última atualização |

---

## UserStatsEntity

**Pacote:** `com.journey.user`  
**Propósito:** Estatísticas agregadas do usuário: XP, nível, passos, água, streak e totais de treino. Um registro por usuário (PK = userId).

```sql
CREATE TABLE IF NOT EXISTS UserStatsEntity (
    userId        TEXT    NOT NULL PRIMARY KEY,
    currentXp     INTEGER NOT NULL DEFAULT 0,
    currentLevel  INTEGER NOT NULL DEFAULT 1,
    totalXp       INTEGER NOT NULL DEFAULT 0,
    stepsToday    INTEGER NOT NULL DEFAULT 0,
    stepsGoal     INTEGER NOT NULL DEFAULT 10000,
    waterGlasses  INTEGER NOT NULL DEFAULT 0,
    waterGoal     INTEGER NOT NULL DEFAULT 8,
    currentStreak INTEGER NOT NULL DEFAULT 0,
    longestStreak INTEGER NOT NULL DEFAULT 0,
    totalWorkouts INTEGER NOT NULL DEFAULT 0,
    totalPRs      INTEGER NOT NULL DEFAULT 0,
    updatedAt     INTEGER NOT NULL DEFAULT 0   -- epoch ms
);
```

| Coluna | Tipo | Obrigatório | Padrão | Descrição |
|---|---|---|---|---|
| userId | TEXT | ✅ | — | PK — ID do usuário |
| currentXp | INTEGER | ✅ | 0 | XP no nível atual |
| currentLevel | INTEGER | ✅ | 1 | Nível atual |
| totalXp | INTEGER | ✅ | 0 | XP total acumulado |
| stepsToday | INTEGER | ✅ | 0 | Passos no dia atual |
| stepsGoal | INTEGER | ✅ | 10000 | Meta diária de passos |
| waterGlasses | INTEGER | ✅ | 0 | Copos de água hoje |
| waterGoal | INTEGER | ✅ | 8 | Meta diária de água (copos) |
| currentStreak | INTEGER | ✅ | 0 | Streak atual (dias consecutivos) |
| longestStreak | INTEGER | ✅ | 0 | Maior streak já alcançado |
| totalWorkouts | INTEGER | ✅ | 0 | Total de treinos concluídos |
| totalPRs | INTEGER | ✅ | 0 | Total de PRs (Personal Records) |
| updatedAt | INTEGER | ✅ | 0 | Epoch ms da última atualização |

---

## WorkoutSessionEntity

**Pacote:** `com.journey.workout`  
**Propósito:** Sessões de treino concluídas (ou em andamento). `completedAt = NULL` indica sessão em curso.

```sql
CREATE TABLE IF NOT EXISTS WorkoutSessionEntity (
    id              TEXT    NOT NULL PRIMARY KEY,
    userId          TEXT    NOT NULL,
    workoutPlanId   TEXT    NOT NULL DEFAULT '',
    workoutTitle    TEXT    NOT NULL,
    startedAt       INTEGER NOT NULL,  -- epoch ms
    completedAt     INTEGER,           -- epoch ms, NULL = em andamento
    durationSeconds INTEGER NOT NULL DEFAULT 0,
    totalVolume     REAL    NOT NULL DEFAULT 0.0,  -- kg total (peso × reps)
    totalSets       INTEGER NOT NULL DEFAULT 0,
    totalReps       INTEGER NOT NULL DEFAULT 0,
    xpEarned        INTEGER NOT NULL DEFAULT 0,
    hasPR           INTEGER NOT NULL DEFAULT 0,    -- 0/1 boolean
    intensityLevel  INTEGER NOT NULL DEFAULT 3,    -- 1-5
    muscleGroups    TEXT    NOT NULL DEFAULT '',   -- separado por vírgula
    notes           TEXT    NOT NULL DEFAULT ''
);
```

| Coluna | Tipo | Obrigatório | Padrão | Descrição |
|---|---|---|---|---|
| id | TEXT | ✅ | — | PK da sessão |
| userId | TEXT | ✅ | — | FK lógica para o usuário |
| workoutPlanId | TEXT | ✅ | '' | ID do plano de treino |
| workoutTitle | TEXT | ✅ | — | Título do treino |
| startedAt | INTEGER | ✅ | — | Epoch ms de início |
| completedAt | INTEGER | ❌ | NULL | Epoch ms de conclusão (NULL = em andamento) |
| durationSeconds | INTEGER | ✅ | 0 | Duração total em segundos |
| totalVolume | REAL | ✅ | 0.0 | Volume total (kg × reps) |
| totalSets | INTEGER | ✅ | 0 | Total de séries |
| totalReps | INTEGER | ✅ | 0 | Total de repetições |
| xpEarned | INTEGER | ✅ | 0 | XP ganho nesta sessão |
| hasPR | INTEGER | ✅ | 0 | 0/1 — se houve PR nesta sessão |
| intensityLevel | INTEGER | ✅ | 3 | Intensidade subjetiva 1–5 |
| muscleGroups | TEXT | ✅ | '' | Grupos musculares treinados (vírgula) |
| notes | TEXT | ✅ | '' | Observações livres |

**Índices:**
- `idx_workout_session_user_started` → `(userId, startedAt DESC)`

---

## WorkoutSetEntity

**Pacote:** `com.journey.workout`  
**Propósito:** Séries individuais pertencentes a uma sessão de treino (`WorkoutSessionEntity`).

```sql
CREATE TABLE IF NOT EXISTS WorkoutSetEntity (
    id           TEXT    NOT NULL PRIMARY KEY,
    sessionId    TEXT    NOT NULL,
    exerciseName TEXT    NOT NULL,
    muscleGroup  TEXT    NOT NULL DEFAULT '',
    setNumber    INTEGER NOT NULL DEFAULT 1,
    reps         INTEGER NOT NULL DEFAULT 0,
    weight       REAL    NOT NULL DEFAULT 0.0,  -- kg
    isPR         INTEGER NOT NULL DEFAULT 0,    -- 0/1 boolean
    rpe          INTEGER,                       -- Rate of Perceived Exertion 1-10, nullable
    notes        TEXT    NOT NULL DEFAULT ''
);
```

| Coluna | Tipo | Obrigatório | Padrão | Descrição |
|---|---|---|---|---|
| id | TEXT | ✅ | — | PK da série |
| sessionId | TEXT | ✅ | — | FK lógica para `WorkoutSessionEntity.id` |
| exerciseName | TEXT | ✅ | — | Nome do exercício |
| muscleGroup | TEXT | ✅ | '' | Grupo muscular |
| setNumber | INTEGER | ✅ | 1 | Número da série no exercício |
| reps | INTEGER | ✅ | 0 | Repetições realizadas |
| weight | REAL | ✅ | 0.0 | Peso em kg |
| isPR | INTEGER | ✅ | 0 | 0/1 — se esta série é um PR |
| rpe | INTEGER | ❌ | NULL | Rate of Perceived Exertion (1–10) |
| notes | TEXT | ✅ | '' | Observações |

**Índices:**
- `idx_workout_set_session` → `(sessionId)`
- `idx_workout_set_exercise` → `(exerciseName)`

---

## Resumo das Tabelas

| Tabela | Pacote | Registros |
|---|---|---|
| AchievementEntity | `com.journey.achievement` | Conquistas com progresso e raridade |
| CatalogMissionEntity | `com.journey.mission` | Catálogo fixo de missões (seed) |
| DailyMissionEntity | `com.journey.mission` | Missões diárias por usuário/dia |
| FoodItemEntity | `com.journey.nutrition` | Alimentos individuais de refeições |
| MealEntryEntity | `com.journey.nutrition` | Refeições diárias com macros agregados |
| NotificationEntity | `com.journey.notification` | Notificações persistidas |
| StreakEntity | `com.journey.streak` | Check-ins diários e sequência |
| UserEntity | `com.journey.user` | Perfil completo do usuário |
| UserStatsEntity | `com.journey.user` | Estatísticas agregadas (XP, nível, etc.) |
| WorkoutSessionEntity | `com.journey.workout` | Sessões de treino |
| WorkoutSetEntity | `com.journey.workout` | Séries individuais de cada sessão |
