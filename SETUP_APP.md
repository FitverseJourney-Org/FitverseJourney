# 🔄 Exemplo End-to-End Completo

Este exemplo mostra o fluxo completo desde a UI até o banco de dados.

## Cenário: Buscar perfil do usuário

```
User toca no botão "Ver Perfil"
       ↓
   ViewModel chama UseCase
       ↓
   UseCase chama Repository
       ↓
   Repository orquestra Local + Remote DataSources
       ↓
   Mapper converte dados
       ↓
   ViewModel emite estado para UI
       ↓
   UI renderiza perfil
```

## 1️⃣ PRESENTATION LAYER (não é do módulo data, mas mostra o fluxo)

```kotlin
// ViewModel
class ProfileViewModel(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    
    private val _profileState = MutableStateFlow<Resource<User>>(Resource.Loading())
    val profileState: StateFlow<Resource<User>> = _profileState.asStateFlow()
    
    fun loadProfile(userId: String) {
        viewModelScope.launch {
            getUserUseCase(userId).collect { resource ->
                _profileState.value = resource
            }
        }
    }
}

// Screen (Compose)
@Composable
fun ProfileScreen(
    userId: String,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val profileState by viewModel.profileState.collectAsState()
    
    LaunchedEffect(userId) {
        viewModel.loadProfile(userId)
    }
    
    when (val state = profileState) {
        is Resource.Loading -> LoadingIndicator()
        is Resource.Success -> ProfileContent(user = state.data!!)
        is Resource.Error -> ErrorMessage(message = state.message)
    }
}
```

## 2️⃣ DOMAIN LAYER

```kotlin
// Use Case
class GetUserUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(userId: String): Flow<Resource<User>> {
        return userRepository.getUserById(userId)
    }
}

// Repository Interface (contrato)
interface UserRepository {
    fun getUserById(userId: String): Flow<Resource<User>>
}

// Domain Model
data class User(
    val id: String,
    val name: String,
    val email: String,
    val genderRequest: GenderRequest,
    val birthDate: String,
    val heightCm: Int,
    val weightKg: Double,
    val fitnessLevelRequest: FitnessLevelRequest,
    val fitnessGoal: FitnessGoalRequest,
    val isPremium: Boolean
)
```

## 3️⃣ DATA LAYER - Repository Implementation

```kotlin
class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
    private val userMapper: UserMapper,
    private val networkMonitor: NetworkMonitor
) : UserRepository {

    override fun getUserById(userId: String): Flow<Resource<User>> = flow {
        // PASSO 1: Loading
        emit(Resource.Loading())
        
        // PASSO 2: Buscar do cache local primeiro
        val cachedEntity = localDataSource.getUserById(userId)
        if (cachedEntity != null) {
            val cachedUser = userMapper.mapEntityToDomain(cachedEntity)
            emit(Resource.Success(cachedUser))
        }
        
        // PASSO 3: Se tiver rede, buscar dados atualizados
        if (networkMonitor.isConnected()) {
            try {
                // 3.1: Buscar da API
                val remoteDto = remoteDataSource.getUserById(userId)
                
                // 3.2: Converter DTO → Domain
                val remoteUser = userMapper.mapDtoToDomain(remoteDto)
                
                // 3.3: Salvar no cache (converter Domain → Entity)
                val entityToCache = userMapper.mapDomainToEntity(remoteUser)
                localDataSource.insertUser(entityToCache)
                
                // 3.4: Emitir dados atualizados
                emit(Resource.Success(remoteUser))
                
            } catch (e: Exception) {
                // Se falhar e não houver cache, emitir erro
                if (cachedEntity == null) {
                    emit(Resource.Error(e.message ?: "Erro ao buscar usuário"))
                }
                // Se houver cache, mantém os dados do cache (já emitido no passo 2)
            }
        }
    }
}
```

## 4️⃣ DATA LAYER - Local DataSource

```kotlin
class UserLocalDataSourceImpl(
    private val database: AppDatabase
) : UserLocalDataSource {
    
    private val queries = database.userQueries
    
    override suspend fun getUserById(userId: String): UserEntity? {
        return withContext(Dispatchers.Default) {
            queries.getUserById(userId).executeAsOneOrNull()
        }
    }
    
    override suspend fun insertUser(user: UserEntity) {
        withContext(Dispatchers.Default) {
            queries.insertUser(
                id = user.id,
                name = user.name,
                email = user.email,
                genderRequest = user.genderRequest,
                birthDate = user.birthDate,
                heightCm = user.heightCm,
                weightKg = user.weightKg,
                fitnessLevelRequest = user.fitnessLevelRequest,
                fitnessGoal = user.fitnessGoal,
                isPremium = user.isPremium
            )
        }
    }
}
```

## 5️⃣ DATA LAYER - Remote DataSource

```kotlin
class UserRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : UserRemoteDataSource {
    
    override suspend fun getUserById(userId: String): UserDto {
        return httpClient.get {
            url("${ApiConstants.BASE_URL}/users/$userId")
        }.body<ApiResponse<UserDto>>().data
    }
}
```

## 6️⃣ DATA LAYER - Models

```kotlin
// DTO (da API)
@Serializable
data class UserDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("gender") val gender: String,
    @SerialName("birth_date") val birthDate: String,
    @SerialName("height_cm") val heightCm: Int,
    @SerialName("weight_kg") val weightKg: Double,
    @SerialName("fitness_level") val fitnessLevel: String,
    @SerialName("fitness_goal") val fitnessGoal: String,
    @SerialName("is_premium") val isPremium: Boolean
)

// Entity (do banco - gerado pelo SQLDelight)
data class UserEntity(
    val id: String,
    val name: String,
    val email: String,
    val genderRequest: String,
    val birthDate: String,
    val heightCm: Long,
    val weightKg: Double,
    val fitnessLevelRequest: String,
    val fitnessGoal: String,
    val isPremium: Long
)
```

## 7️⃣ DATA LAYER - Mapper

```kotlin
class UserMapper {
    
    // DTO (API) → Domain (App)
    fun mapDtoToDomain(dto: UserDto): User {
        return User(
            id = dto.id,
            name = dto.name,
            email = dto.email,
            genderRequest = parseGender(dto.gender),
            birthDate = dto.birthDate,
            heightCm = dto.heightCm,
            weightKg = dto.weightKg,
            fitnessLevelRequest = parseFitnessLevel(dto.fitnessLevel),
            fitnessGoal = parseFitnessGoal(dto.fitnessGoal),
            isPremium = dto.isPremium
        )
    }
    
    // Entity (Database) → Domain (App)
    fun mapEntityToDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            name = entity.name,
            email = entity.email,
            genderRequest = GenderRequest.valueOf(entity.genderRequest),
            birthDate = entity.birthDate,
            heightCm = entity.heightCm.toInt(),
            weightKg = entity.weightKg,
            fitnessLevelRequest = FitnessLevelRequest.valueOf(entity.fitnessLevelRequest),
            fitnessGoal = FitnessGoalRequest.valueOf(entity.fitnessGoal),
            isPremium = entity.isPremium == 1L
        )
    }
    
    // Domain (App) → Entity (Database)
    fun mapDomainToEntity(domain: User): UserEntity {
        return UserEntity(
            id = domain.id,
            name = domain.name,
            email = domain.email,
            genderRequest = domain.genderRequest.name,
            birthDate = domain.birthDate,
            heightCm = domain.heightCm.toLong(),
            weightKg = domain.weightKg,
            fitnessLevelRequest = domain.fitnessLevelRequest.name,
            fitnessGoal = domain.fitnessGoal.name,
            isPremium = if (domain.isPremium) 1L else 0L
        )
    }
    
    private fun parseGender(value: String): GenderRequest {
        return when (value.uppercase()) {
            "MALE" -> GenderRequest.MALE
            "FEMALE" -> GenderRequest.FEMALE
            else -> GenderRequest.OTHER
        }
    }
    
    private fun parseFitnessLevel(value: String): FitnessLevelRequest {
        return FitnessLevelRequest.valueOf(value.uppercase())
    }
    
    private fun parseFitnessGoal(value: String): FitnessGoalRequest {
        return FitnessGoalRequest.valueOf(value.uppercase())
    }
}
```

## 8️⃣ DEPENDENCY INJECTION - Tudo Conectado

```kotlin
// Data Module
val dataModule = module {
    // Database
    single<AppDatabase> { 
        get<DatabaseFactory>().createDatabase() 
    }
    
    // Http Client
    single<HttpClient> { 
        HttpClientFactory.create() 
    }
    
    // Network Monitor
    single<NetworkMonitor> { 
        AndroidNetworkMonitor(androidContext()) 
    }
    
    // Data Sources
    single<UserLocalDataSource> { 
        UserLocalDataSourceImpl(get()) 
    }
    single<UserRemoteDataSource> { 
        UserRemoteDataSourceImpl(get()) 
    }
    
    // Mappers
    single { UserMapper() }
    
    // Repositories
    single<UserRepository> {
        UserRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get(),
            userMapper = get(),
            networkMonitor = get()
        )
    }
}

// Domain Module
val domainModule = module {
    factory { GetUserUseCase(get()) }
}

// Presentation Module
val presentationModule = module {
    viewModel { ProfileViewModel(get()) }
}

// Inicialização
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@MyApp)
            modules(
                dataModule,
                domainModule,
                presentationModule
            )
        }
    }
}
```

## 📊 Fluxo de Dados Completo

```
┌─────────────────────────────────────────────────────────┐
│  USER TAPS "VER PERFIL"                                 │
└────────────────────┬────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────────────┐
│  ProfileScreen                                          │
│  - Chama viewModel.loadProfile(userId)                  │
└────────────────────┬────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────────────┐
│  ProfileViewModel                                       │
│  - Chama getUserUseCase(userId)                         │
└────────────────────┬────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────────────┐
│  GetUserUseCase                                         │
│  - Chama userRepository.getUserById(userId)             │
└────────────────────┬────────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────────────┐
│  UserRepositoryImpl                                     │
│  1. Emite Loading                                       │
│  2. Busca do cache (localDataSource)                    │
│     → UserEntity → UserMapper → User                    │
│  3. Emite Success com dados do cache                    │
│  4. Busca da API (remoteDataSource)                     │
│     → UserDto → UserMapper → User                       │
│  5. Salva no cache                                      │
│     → User → UserMapper → UserEntity                    │
│  6. Emite Success com dados atualizados                 │
└────────────────────┬────────────────────────────────────┘
                     ↓
         ┌───────────┴──────────┐
         ↓                      ↓
┌──────────────────┐   ┌──────────────────┐
│ UserLocalDS      │   │ UserRemoteDS     │
│ (SQLDelight)     │   │ (Ktor)           │
│                  │   │                  │
│ getUserById()    │   │ getUserById()    │
│ ↓                │   │ ↓                │
│ UserEntity       │   │ UserDto          │
└────┬─────────────┘   └───────┬──────────┘
     │                         │
     └────────┬────────────────┘
              ↓
     ┌──────────────────┐
     │   UserMapper     │
     │                  │
     │ mapEntityToDomain│
     │ mapDtoToDomain   │
     │ mapDomainToEntity│
     └────┬─────────────┘
          ↓
     ┌──────────────────┐
     │   User (Domain)  │
     └────┬─────────────┘
          │
          ↓ Flow<Resource<User>>
     ┌──────────────────┐
     │  ProfileViewModel│
     └────┬─────────────┘
          │
          ↓ StateFlow
     ┌──────────────────┐
     │  ProfileScreen   │
     │  (UI renderiza)  │
     └──────────────────┘
```

## 🎯 Resumo do Fluxo

1. **UI** chama ViewModel
2. **ViewModel** chama UseCase
3. **UseCase** chama Repository
4. **Repository** orquestra:
    - Busca do **LocalDataSource** (SQLDelight)
    - Converte **Entity → Domain** via Mapper
    - Emite dados do cache
    - Busca do **RemoteDataSource** (Ktor)
    - Converte **DTO → Domain** via Mapper
    - Salva no cache (**Domain → Entity** via Mapper)
    - Emite dados atualizados
5. **UseCase** retorna Flow para ViewModel
6. **ViewModel** emite estado para UI
7. **UI** renderiza baseado no estado

Cada camada tem sua responsabilidade bem definida e está desacoplada das outras! 🎉