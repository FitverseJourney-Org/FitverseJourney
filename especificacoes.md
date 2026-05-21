# Especificações do Projeto — FitverseJourneyApp

> Documento gerado em 2026-05-20. Referência arquitetural oficial do projeto.

---

## 1. Visão Geral

| Campo | Valor |
|---|---|
| **Nome** | FitverseJourneyApp |
| **Application ID** | `org.fitverse.fitverseJourney` |
| **Plataformas** | Android, iOS |
| **Objetivo** | App fitness completo: treinos, nutrição, missões, conquistas, social e progressão |
| **Min SDK Android** | 26 (Android 8.0) |
| **Target / Compile SDK** | 36 |
| **Kotlin** | 2.3.0 |
| **Compose Multiplatform** | 1.10.0 |
| **AGP** | 9.0.0 |

---

## 2. Stack Tecnológica Completa

### Core

| Camada | Tecnologia | Versão |
|---|---|---|
| Linguagem | Kotlin Multiplatform | 2.3.0 |
| UI Framework | Compose Multiplatform | 1.10.0 |
| Build System | Gradle + AGP | 9.0.0 |
| Version Catalog | `gradle/libs.versions.toml` | — |

### UI & Navegação

| Biblioteca | Versão | Uso |
|---|---|---|
| JetBrains Navigation3 (KMP) | 1.0.1 | Navegação type-safe multiplataforma |
| Material Icons Extended | 1.7.3+ | Ícones padrão Google Material |
| Lucide Icons | — | Ícones alternativos |
| Font Awesome | — | Ícones de esporte/fitness |
| Eva Icons | — | Ícones de UI |
| Vico Charts | 3.0.3 | Gráficos de progressão |
| ehsannarmani/compose-charts | 0.2.5 | Gráficos complementares |
| Coil | 3.3.0 | Carregamento assíncrono de imagens |
| Lottie Compose (Android) | 6.7.1 | Animações Lottie no Android |

### Injeção de Dependência

| Biblioteca | Versão |
|---|---|
| Koin Core | 4.1.1 |
| Koin Android | 4.1.1 |
| Koin Compose | 4.1.1 |
| Koin Compose ViewModel | 4.1.1 |
| Koin Navigation3 | 4.1.1 |
| Koin Ktor | 4.1.1 |

### Banco de Dados & Armazenamento

| Biblioteca | Versão | Uso |
|---|---|---|
| SQLDelight | 2.3.2 | Banco de dados local KMP (SQLite) |
| SQLDelight Coroutines Extensions | 2.3.2 | Queries assíncronas |
| DataStore Preferences | 1.2.0 | Preferências chave-valor |
| Multiplatform-Settings | 1.3.0 | Configurações multi-plataforma |
| security-crypto (EncryptedSharedPreferences) | 1.1.0-alpha06 | Token storage seguro (Android) |

### Rede

| Biblioteca | Versão | Uso |
|---|---|---|
| Ktor Client Core | 3.0.3 | Cliente HTTP KMP |
| Ktor Client OkHttp (Android) | 3.0.3 | Engine HTTP no Android |
| Ktor Client Darwin (iOS) | 3.0.3 | Engine HTTP no iOS (NSURLSession) |
| Ktor ContentNegotiation | 3.0.3 | Serialização JSON automática |
| Ktor Auth (Bearer) | 3.0.3 | Autenticação via token |
| Ktor Logging | 3.0.3 | Log de requisições |
| Kotlinx Serialization JSON | 1.10.0 | Serialização/desserialização JSON |

### Firebase (apenas Android)

| Serviço | BOM |
|---|---|
| Firebase Auth | 34.11.0 |
| Firebase Firestore | 34.11.0 |
| Firebase Analytics | 34.11.0 |

### Async & DateTime

| Biblioteca | Versão |
|---|---|
| Kotlinx Coroutines Core | 1.10.2 |
| Kotlinx DateTime | — |

### Backend (:server)

| Biblioteca | Versão | Uso |
|---|---|---|
| Ktor Server Core | 3.0.3 | Framework de servidor |
| Ktor Netty Engine | 3.0.3 | Runtime do servidor |
| Ktor CallLogging | 3.0.3 | Log de chamadas HTTP |
| Ktor ContentNegotiation | 3.0.3 | JSON serialization |
| Ktor StatusPages | 3.0.3 | Tratamento de erros HTTP |
| Ktor CORS | 3.0.3 | Cross-origin requests |
| Firebase Admin SDK | 9.4.2 | Autenticação server-side |
| Koin JVM | 4.1.1 | DI no servidor |
| Logback Classic | — | Logging SLF4J |
| Shadow Plugin | — | Empacotamento fat JAR |

### Testes

| Biblioteca | Versão |
|---|---|
| JUnit 4 | 4.13.2 |
| Espresso Core | 3.6.1 |
| AndroidX Test Runner | 1.6.1 |
| Kotlin Test | 2.3.0 |

---

## 3. Estrutura de Módulos

```
FitverseJourneyApp/
├── :androidApp            → Entry point Android
├── :composeApp            → Navigation graph + DI aggregation (KMP)
├── :presentation          → ViewModels + Composables + Design System (KMP)
├── :domain                → Use Cases + Modelos + Interfaces (KMP puro)
├── :data:local            → SQLDelight + DataStore + DAOs (KMP)
├── :data:remote           → Ktor Client + Firebase + DTOs (KMP)
├── :data:repository       → Orquestração local+remote (KMP)
└── :server                → Backend Ktor/Netty (JVM)
```

### Grafo de Dependências

```
:androidApp
  └── :composeApp
  └── :presentation
  └── :domain
  └── :data:local
  └── :data:remote
  └── :data:repository

:composeApp
  └── :presentation
  └── :domain

:presentation
  └── :domain

:data:repository
  └── :data:local
  └── :data:remote
  └── :domain

:data:local  → :domain
:data:remote → :domain
:server      → (independente, Firebase Admin SDK)
```

---

## 4. Arquitetura: Clean Architecture + MVI

### Camadas

```
┌─────────────────────────────────────────────────┐
│          PRESENTATION LAYER                      │
│  Composables → ViewModels → State/Action/Event   │
│  Módulos: :composeApp, :presentation             │
├─────────────────────────────────────────────────┤
│          DOMAIN LAYER                            │
│  Use Cases → Modelos de Domínio → Interfaces     │
│  Módulo: :domain                                 │
├─────────────────────────────────────────────────┤
│          DATA LAYER                              │
│  Repositories → DataSources → Mappers            │
│  Módulos: :data:local, :data:remote, :data:repository │
└─────────────────────────────────────────────────┘
```

### Fluxo MVI por Tela

```
Composable (UI)
  │ dispara
  ▼
Action (sealed class)
  │ processado por
  ▼
ViewModel
  │ chama
  ▼
UseCase (suspend fun)
  │ delega para
  ▼
Repository (interface do :domain)
  │ implementado por
  ▼
DataSource (Local ou Remote)
```

### Exemplo de Contrato MVI (LoginViewModel)

```kotlin
// State
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val snackBarData: SnackBarData? = null
)

// Actions (Intent)
sealed class LoginAction {
    data class EmailChanged(val value: String) : LoginAction()
    data object LoginClicked : LoginAction()
    data object NavigateToRegister : LoginAction()
}

// Navigation Events (one-shot via Channel)
sealed class LoginNavigation {
    data object ToHome : LoginNavigation()
    data object ToRegister : LoginNavigation()
}

// ViewModel
class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private val _events = Channel<LoginNavigation>()
    val navigationState = _events.receiveAsFlow()

    fun onAction(action: LoginAction) { ... }
}
```

---

## 5. Navegação (Navigation3)

### Estrutura de Rotas

```kotlin
sealed interface NavRoutes : NavKey {
    // Root
    data object SplashScreen, OnboardingScreen, TrialScreen, LoadingLanguage

    // Auth
    data object AuthFlow {
        data object Home, Login, Register, ResetPassword
    }

    // Home (Bottom Bar)
    data object HomeFlow {
        data object Dashboard, Workout, Activity, Community, Nutrition, Profile
        data object SubFlow {
            data object AddPost, Notification, UserLevelUp
            data class GroupHome(val groupName: String)
        }
    }

    // Workout
    data object WorkoutFlow {
        data object Main, WorkoutSession, WorkoutCompleted
    }

    // Planos de Treino
    data object PlanWorkoutFlow {
        data object PlanList, Plan, Builder, Exercises, ExerciseDetails, PlanIA
    }

    // Missões/Tasks
    data object TasksFlow {
        data object TasksList
        data class TasksLibrary(val taskToReplaceId: String)
    }

    // Nutrição
    data class NutritionAddManualFood(val mealId: String, val mealName: String)

    // Modais (overlay screens)
    data object Shopping, WikiFitness, PlanPayment, Devices, Friends,
               Leaderboards, Historic, Progress, Achievements, HelpSupport
}
```

### Arquivos de Navegação

| Arquivo | Responsabilidade |
|---|---|
| `FitverseRootNavigation.kt` | NavDisplay raiz com transições |
| `AuthNavigation.kt` | Fluxo de autenticação |
| `HomeNavigation.kt` | Dashboard + bottom bar |
| `WorkoutNavigation.kt` | Sessão de treino |
| `DashboardNavigation.kt` | Tela principal |
| `CommunityNavigation.kt` | Social/comunidade |
| `NutritionNavigation.kt` | Refeições/nutrição |
| `PlanWorkoutNavigation.kt` | Planejamento de treinos |
| `TasksNavigation.kt` | Missões diárias |
| `ModalNavigation.kt` | Telas modais flutuantes |
| `ProfileNavigation.kt` | Perfil do usuário |

### Transições

- **Push:** slide horizontal da direita
- **Pop:** slide horizontal para a direita
- **Predictive Back:** animação nativa Android 14+
- Configurado em `PageTransitions.kt`

---

## 6. Design System

**Localização:** `presentation/src/commonMain/kotlin/com/example/presentation/theme/`

### Paleta de Cores (Colors.kt)

| Token | Valor | Uso |
|---|---|---|
| `Bg` | `#0A0A0F` | Fundo principal (OLED-friendly) |
| `Surface` | — | Superfície de cards |
| `Surface2`, `Surface3` | — | Hierarquia de superfícies |
| `SurfaceModal` | — | Fundo de modais |
| `Accent` | `#C8FF00` | CTA principal (Neon Lime) |
| `AccentDim`, `AccentDim2` | — | Variações do accent |
| `Secondary` | Aqua | Cor secundária |
| `Border` | 7% branco | Bordas sutis |
| `Border2` | 12% branco | Bordas de destaque |
| `TextPrimary` | — | Texto principal |
| `TextSecondary` | — | Texto secundário |
| `TextTertiary` | — | Texto terciário |
| `Success` / `Error` / `Warning` / `Info` | Verde/Vermelho/Laranja/Roxo | Status semânticos |

### Tipografia (Typography.kt)
- Scale Material 3 completa: Display, Headline, Title, Body, Label (Large/Medium/Small cada)
- FontFamily customizada importada via Resources

### Princípios do Design System
- **Dark-first:** otimizado para OLED
- **Alto contraste:** Neon Lime (#C8FF00) para CTAs
- **Semântico:** tokens nomeados por função, não por cor
- **Material 3:** conformidade com M3 color scheme

---

## 7. Injeção de Dependência (Koin)

### Fluxo de Inicialização

```
MainApplication.onCreate()
 ├── Firebase.initializeApp(this)
 ├── DataStoreFactory.context = this
 └── initKoin(platformModules = [androidLocal, androidRemote]) {
       androidLogger()
       androidContext(this@MainApplication)
       modules(platformModules + appModules)
     }

appModules = presentationModules + domainModule + dataModule
```

### Módulos Registrados

| Módulo | Responsabilidade |
|---|---|
| `GlobalModule` | `LanguageViewModel` |
| `AuthModule` | LoginVM, RegisterVM, ResetPasswordVM |
| `OnboardingModule` | SplashVM, OnboardingVM, TrialVM |
| `HomeModule` | DashboardVM, NotificationVM, WorkoutVM, MealsVM, ProfileVM, CommunityVM |
| `FeaturesModule` | WorkoutPlanVM, FriendsVM, WikiVM, ProgressVM, HistoricVM, AchievementsVM, TasksVM, LeaderboardsVM, ShoppingVM |
| `DomainModule` | ~150 use cases |
| `DatabaseModule` | SQLDelight driver, DataStore, DAOs |
| `DataSourceModule` | Remote + Local data sources |
| `MapperModule` | Entity/DTO mappers |
| `RepositoryModule` | Implementações de repositórios |
| `NetworkModule` | Ktor HttpClient singleton |

---

## 8. Banco de Dados Local (SQLDelight)

**Database:** `AppDatabase` | Package: `com.journey`

### Tabelas

| Tabela | Campos Principais |
|---|---|
| `UserEntity` | id, name, email, level, xp, avatar |
| `UserStatsEntity` | userId, steps, waterMl, xpToday |
| `WorkoutSessionEntity` | id, userId, date, duration, calories |
| `WorkoutSetEntity` | sessionId, exerciseId, reps, weight |
| `WorkoutPlanEntity` | id, userId, name, isActive |
| `MealEntryEntity` | id, userId, date, mealType, totalCalories |
| `FoodItemEntity` | id, mealId, name, calories, protein, carbs, fat |
| `DailyMissionEntity` | id, userId, date, status, progress |
| `CatalogMissionEntity` | id, title, description, xpReward |
| `AchievementEntity` | id, userId, type, unlockedAt |
| `StreakEntity` | userId, currentStreak, longestStreak |
| `NotificationEntity` | id, userId, type, readAt |

### Drivers por Plataforma

| Plataforma | Driver |
|---|---|
| Android | `AndroidSqliteDriver` |
| iOS | `NativeSqliteDriver` (SQLite via C interop) |
| JVM (testes) | `JdbcSqliteDriver` |

---

## 9. Camada Remota (Ktor + Firebase)

### HttpClient (commonMain)
- ContentNegotiation: Kotlinx JSON
- Auth: Bearer Token (injetado via DataStore)
- Logging: All (headers + body)
- Engine: OkHttp (Android) / Darwin (iOS)

### Data Sources Remotos

| DataSource | Responsabilidade |
|---|---|
| `UserRemoteDataSourceImpl` | Registro, perfil, sincronização |
| `ProgressionRemoteDataSourceImpl` | Dados de progressão de treino |
| `MissionCatalogRemoteDataSourceImpl` | Catálogo de missões do servidor |
| `ActivatePlanRemoteDataSourceImpl` | Ativação de planos de treino |
| `FriendsRemoteDataSourceImpl` | Dados sociais/amigos |

### DTOs

| DTO | Mapeado para |
|---|---|
| `UserRequestDto` | `UserProfile` (domain) |
| `ExerciseDto` | `Exercise` (domain) |
| `MissionDto` | `DailyMission` (domain) |
| `ProgressionPointDto` | `ProgressionData` (domain) |
| `ActivatePlanDto` | `WorkoutPlan` (domain) |

---

## 10. Backend (:server)

### Stack
- **Runtime:** Ktor + Netty (fat JAR via Shadow Plugin)
- **DI:** Koin para JVM
- **DB:** Firebase Firestore via Admin SDK
- **Auth:** Firebase Auth (verificação de tokens)
- **Main Class:** `org.fitverse.project.ApplicationKt`

### Endpoints

#### Missões (`/missions`)
| Método | Rota | Descrição |
|---|---|---|
| GET | `/missions` | Lista catálogo de missões |
| POST | `/missions` | Cria nova missão |
| GET | `/missions/seed` | Popula banco com dados iniciais |

#### Usuários (`/users`)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/users` | Cria/registra usuário |
| GET | `/users/{id}` | Busca perfil do usuário |
| PUT | `/users/{id}` | Atualiza perfil |

### Estrutura de Pastas

```
server/src/main/kotlin/org/fitverse/project/
├── Application.kt
├── di/ServerModule.kt
└── features/
    ├── missions/
    │   ├── controller/MissionController.kt
    │   ├── db/MissionRepository.kt
    │   ├── db/MissionSeedData.kt
    │   ├── models/MissionDocument.kt
    │   ├── routes/MissionRoutes.kt
    │   └── services/MissionService.kt
    └── user/
        ├── controller/UserController.kt
        ├── db/UserRepository.kt
        ├── models/UserDocument.kt
        ├── routes/UserRoutes.kt
        └── services/UserService.kt
```

---

## 11. Padrões Expect/Actual

| Expect | Android Actual | iOS Actual |
|---|---|---|
| `DatabaseDriverFactory` | `AndroidSqliteDriver` | `NativeSqliteDriver` |
| `createDataStore()` | DataStore com Context Android | DataStore com path iOS |
| `DateTimeManager` | Java/Kotlin datetime | NSDate (Darwin) |
| `NetworkMonitor` | ConnectivityManager | NWPathMonitor |
| `AppLocale` | `Locale.getDefault()` | `NSLocale` |
| `PlatformAnimation` | Lottie | Compose animation |
| `AppEnvironment` | `Locale.setDefault()` | `NSUserDefaults` |
| `AgeCalculator` | Java Calendar | NSCalendar |

---

## 12. Configuração de Build

### gradle.properties
- Build cache ativo
- Daemon Gradle ativo
- Temp dir configurado para evitar problemas de path no Windows

### Build Types (androidApp)

| Tipo | App ID Suffix | Minify | ProGuard |
|---|---|---|---|
| debug | `.dev` | false | — |
| release | — | true | `proguard-rules.pro` |

### CI/CD
- `google-services.json` injetado via GitHub Secret (não versionado)
- `.gitignore` exclui arquivos Firebase e `.env`

---

## 13. Telas do Aplicativo (30+ destinations)

### Fluxo de Auth
- Splash, Onboarding, Trial, Login, Register, ForgotPassword

### Home (Bottom Bar)
- Dashboard, Workout, Activity, Community (Feed), Nutrition, Profile

### Sub-telas
- Notifications, UserLevelUp, GroupHome (Comunidade)

### Workout
- WorkoutSession, WorkoutCompleted, WorkoutPlanList, WorkoutPlan, WorkoutPlanBuilder, WorkoutPlanExercises, WorkoutPlanExerciseDetails, WorkoutPlanIA

### Nutrition
- AddManualFood

### Modais
- Achievements, Devices, Friends, Historic, Leaderboards, Progress, Shopping, Tasks, WikiFitness, HelpSupport, PlanPayment

---

## 14. Sugestões de Melhoria Arquitetural

### 🔴 Alta Prioridade

#### 1. Padronizar package names
- **Problema:** mistura `com.example.*` (presentation, domain, data) com `org.fitverse.*` (composeApp, androidApp, server)
- **Risco:** colisão de pacotes, confusão de namespace, dificuldade para refatorar
- **Solução:** migrar todos os módulos para `org.fitverse.*`
  - `:presentation` → `org.fitverse.presentation`
  - `:domain` → `org.fitverse.domain`
  - `:data:*` → `org.fitverse.data.*`

#### 2. Firebase ausente no iOS
- **Problema:** Firebase (Auth, Firestore, Analytics) está somente em `androidMain` de `:data:remote`
- **Risco:** sincronização remota, autenticação e analytics não funcionam no iOS
- **Solução:** adicionar Firebase iOS SDK via CocoaPods no `iosApp/Podfile` e criar expect/actual para `FirebaseAuthDataSource` e `FirestoreDataSource`

#### 3. Inicialização do DataStore fora do Koin
- **Problema:** `DataStoreFactory.context = this` é chamado antes do `startKoin()` — variável global mutável
- **Risco:** race condition em testes, difícil de mockar
- **Solução:** definir `DataStoreFactory` como singleton Koin recebendo `Context` via `androidContext()`:
  ```kotlin
  single { DataStoreFactory(get()) }
  ```

#### 4. Ausência de testes automatizados
- **Problema:** dependências de teste existem mas não há evidência de testes escritos
- **Risco:** regressões silenciosas, sem segurança em refatorações
- **Solução mínima:**
  - Fake repositories para cada interface de domínio
  - ViewModel unit tests com `Turbine` + `UnconfinedTestDispatcher`
  - Use case tests com JUnit5

---

### 🟡 Média Prioridade

#### 5. Mappers duplicados
- **Problema:** `ExerciseMapper` aparece em `:data:repository/mapper` e em `:data:remote/mapper`
- **Solução:** regra clara:
  - `DTO → Domain` fica em `:data:remote`
  - `Entity → Domain` fica em `:data:local`
  - `:data:repository` apenas compõe, sem mappers próprios

#### 6. Navigation Channel → SharedFlow
- **Problema:** `Channel<NavigationEvent>` pode perder eventos se o collector não estiver ativo no momento do envio
- **Solução:** usar `SharedFlow(replay = 0, extraBufferCapacity = 1)` + `ObserveAsEvents()` no composable (padrão recomendado pelo time Android)

#### 7. security-crypto em versão alpha
- **Problema:** `security-crypto:1.1.0-alpha06` em build de produção
- **Solução:** migrar para `Multiplatform-Settings` com `KeychainSettings` (iOS) / `EncryptedSharedPreferencesSettings` (Android) — já é dependência do projeto (1.3.0)

#### 8. Snapshot repository ativo em produção
- **Problema:** `oss.sonatype.org/content/repositories/snapshots/` ativo no build
- **Risco:** builds não-determinísticos se um snapshot for atualizado
- **Solução:** remover após confirmar que Koin 4.1.1 (estável) é a única versão usada

---

### 🟢 Baixa Prioridade / Qualidade

#### 9. Nomenclatura inconsistente de arquivos
- `Exerciselocaldatasource.kt` (minúscula) deve ser renomeado para `ExerciseLocalDataSourceImpl.kt`
- Padronizar: todos os arquivos em PascalCase

#### 10. WorkoutFlow e PlanWorkoutFlow sobrepostos
- Dois flows distintos para workout pode causar confusão na navegação
- Considerar unificar sob `WorkoutFlow` com sub-rotas: `Session`, `PlanList`, `PlanBuilder`, etc.

###### 11. Testes de integração no servidor
- `:server` sem testes de endpoints
- Adicionar: `testApplication { }` do Ktor para testar cada rota

#### 12. Analytics não abstraído
- Firebase Analytics chamado diretamente no código
- Criar interface `AnalyticsTracker` em `:domain` para facilitar troca e mock em testes:
  ```kotlin
  interface AnalyticsTracker {
      fun track(event: AnalyticsEvent)
  }
  ```

---

*Documento gerado por análise arquitetural completa do projeto FitverseJourneyApp.*
