# 📚 Boas Práticas e Patterns - Módulo Data

## 🎯 Princípios Fundamentais

### 1. **Single Responsibility Principle (SRP)**
Cada classe tem uma única responsabilidade:
- **Repository**: Orquestra data sources
- **DataSource**: Acessa dados (DB ou API)
- **Mapper**: Converte entre camadas
- **DTO**: Representa dados da API
- **Entity**: Representa dados do DB

### 2. **Dependency Inversion Principle (DIP)**
```kotlin
// ❌ ERRADO - Repository depende de implementação
class UserRepositoryImpl(
    private val database: AppDatabase  // Implementação concreta
)

// ✅ CORRETO - Repository depende de abstração
class UserRepositoryImpl(
    private val localDataSource: UserLocalDataSource,  // Interface
    private val remoteDataSource: UserRemoteDataSource // Interface
)
```

### 3. **Interface Segregation**
```kotlin
// ❌ ERRADO - Interface muito grande
interface UserDataSource {
    suspend fun getAllLocal()
    suspend fun getAllRemote()
    suspend fun insertLocal()
    suspend fun createRemote()
    // ... muitos métodos misturados
}

// ✅ CORRETO - Interfaces separadas
interface UserLocalDataSource { /* métodos locais */ }
interface UserRemoteDataSource { /* métodos remotos */ }
```

## 🏗️ Patterns Recomendados

### Pattern 1: Repository Pattern
```kotlin
/**
 * Repository centraliza acesso aos dados
 * Esconde complexidade de múltiplas fontes
 */
interface UserRepository {
    fun getUser(id: String): Flow<Resource<User>>
    suspend fun createUser(user: User): Resource<User>
    suspend fun syncData(): Resource<Unit>
}
```

### Pattern 2: Data Source Pattern
```kotlin
/**
 * DataSource encapsula acesso a uma fonte específica
 */
interface UserLocalDataSource {
    suspend fun getUser(id: String): UserEntity?
    suspend fun insertUser(user: UserEntity)
}

interface UserRemoteDataSource {
    suspend fun getUser(id: String): UserDto
    suspend fun createUser(user: UserRequestDto): UserDto
}
```

### Pattern 3: Mapper Pattern
```kotlin
/**
 * Mapper converte entre camadas
 * Mantém camadas desacopladas
 */
class UserMapper {
    fun mapDtoToDomain(dto: UserDto): User
    fun mapDomainToDto(domain: User): UserDto
    fun mapEntityToDomain(entity: UserEntity): User
    fun mapDomainToEntity(domain: User): UserEntity
}
```

### Pattern 4: Resource/Result Wrapper
```kotlin
/**
 * Encapsula estados de operações assíncronas
 */
sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
}
```

### Pattern 5: Cache Strategy Pattern
```kotlin
/**
 * Diferentes estratégias de cache
 */
enum class CacheStrategy {
    CACHE_FIRST,     // Cache → Network
    NETWORK_FIRST,   // Network → Cache
    CACHE_ONLY,      // Apenas Cache
    NETWORK_ONLY     // Apenas Network
}

class UserRepositoryImpl(
    private val cacheStrategy: CacheStrategy = CACHE_FIRST
) {
    fun getUser(id: String): Flow<Resource<User>> = flow {
        when (cacheStrategy) {
            CACHE_FIRST -> {
                // Emite cache primeiro, depois network
            }
            NETWORK_FIRST -> {
                // Tenta network, cache como fallback
            }
            // ...
        }
    }
}
```

## 📝 Convenções de Nomenclatura

### Classes e Interfaces
```
// DTOs (API)
UserDto, UserRequestDto, UserResponseDto

// Entities (Database)
UserEntity, WorkoutEntity (gerados pelo SQLDelight)

// Domain Models
User, Workout, Meal

// Repositories
UserRepository (interface)
UserRepositoryImpl (implementação)

// Data Sources
UserLocalDataSource (interface)
UserLocalDataSourceImpl (implementação)
UserRemoteDataSource (interface)
UserRemoteDataSourceImpl (implementação)

// Mappers
UserMapper, WorkoutMapper
```

### Packages
```kotlin
com.example.data.repository.user
com.example.data.datasource.local.user
com.example.data.datasource.remote.user
com.example.data.model.dto.user
com.example.data.mapper.user
com.example.data.di
com.example.data.util
```

## ✅ Checklist de Qualidade

### Antes de Criar uma Nova Feature

- [ ] Definir modelo de domínio (Domain Model)
- [ ] Criar DTO para API (se necessário)
- [ ] Criar Entity para DB (arquivo .sq)
- [ ] Implementar Mapper (DTO ↔ Domain, Entity ↔ Domain)
- [ ] Criar interfaces de DataSource (Local + Remote)
- [ ] Implementar DataSources
- [ ] Criar interface de Repository (no módulo domain)
- [ ] Implementar Repository
- [ ] Adicionar ao módulo DI
- [ ] Escrever testes

### Code Review Checklist

- [ ] Separação clara entre DTO, Entity e Domain Model?
- [ ] Repository não depende de implementações concretas?
- [ ] DataSources têm responsabilidade única?
- [ ] Mapper converte corretamente entre camadas?
- [ ] Tratamento de erros implementado?
- [ ] Cache strategy definida?
- [ ] Logs adequados para debugging?
- [ ] Documentação nos principais métodos?
- [ ] Código testável (uso de interfaces)?

## 🚨 Anti-Patterns (Evite!)

### ❌ Domain Pollution
```kotlin
// ❌ ERRADO - Modelo de domínio com anotações de framework
data class User(
    @SerialName("id") val id: String,  // Anotação do Kotlin Serialization
    @ColumnInfo(name = "name") val name: String  // Anotação do Room
)

// ✅ CORRETO - Domínio limpo, sem dependências
data class User(
    val id: String,
    val name: String
)
```

### ❌ God Repository
```kotlin
// ❌ ERRADO - Repository com muitas responsabilidades
class UserRepository {
    fun getUsers() { }
    fun createUser() { }
    fun loginUser() { }        // Auth não é responsabilidade de User
    fun uploadAvatar() { }     // File upload separado
    fun sendEmail() { }        // Email separado
}

// ✅ CORRETO - Responsabilidades separadas
class UserRepository { } // Apenas CRUD de User
class AuthRepository { } // Autenticação
class FileRepository { } // Upload de arquivos
class EmailRepository { } // Envio de emails
```

### ❌ Leaked Abstractions
```kotlin
// ❌ ERRADO - Detalhes de implementação vazando
interface UserRepository {
    suspend fun getUser(id: String): UserDto  // DTO vazando para domain
}

// ✅ CORRETO - Interface independente de implementação
interface UserRepository {
    suspend fun getUser(id: String): User  // Domain model
}
```

### ❌ Direct Database Access
```kotlin
// ❌ ERRADO - Repository acessando DB diretamente
class UserRepositoryImpl(
    private val database: AppDatabase
) {
    suspend fun getUser(id: String): User {
        return database.userQueries.getUser(id)  // Acesso direto
    }
}

// ✅ CORRETO - Repository usando DataSource
class UserRepositoryImpl(
    private val localDataSource: UserLocalDataSource
) {
    suspend fun getUser(id: String): User {
        return localDataSource.getUser(id)
    }
}
```

## 🔧 Configurações Úteis

### build.gradle.kts (data module)
```kotlin
plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("app.cash.sqldelight")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // Domain
                implementation(project(":domain"))
                
                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                
                // Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                
                // Ktor
                implementation("io.ktor:ktor-client-core:2.3.5")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
                implementation("io.ktor:ktor-client-logging:2.3.5")
                
                // SQLDelight
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.1")
                
                // Koin
                implementation("io.insert-koin:koin-core:3.5.0")
            }
        }
        
        androidMain {
            dependencies {
                implementation("io.ktor:ktor-client-android:2.3.5")
                implementation("app.cash.sqldelight:android-driver:2.0.1")
                implementation("io.insert-koin:koin-android:3.5.0")
            }
        }
        
        iosMain {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.5")
                implementation("app.cash.sqldelight:native-driver:2.0.1")
            }
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.example.database")
        }
    }
}
```

## 📊 Fluxo de Dados Recomendado

```
┌──────────────────────────────────────────────────────┐
│                   PRESENTATION                        │
│                   (ViewModels)                        │
└──────────────────┬───────────────────────────────────┘
                   │ observa/chama
                   ↓
┌──────────────────────────────────────────────────────┐
│                    DOMAIN                             │
│                  (Use Cases)                          │
└──────────────────┬───────────────────────────────────┘
                   │ usa
                   ↓
┌──────────────────────────────────────────────────────┐
│                     DATA                              │
│              (Repositories)                           │
│                      │                                │
│         ┌────────────┴────────────┐                  │
│         ↓                         ↓                   │
│  ┌──────────────┐        ┌──────────────┐           │
│  │   Local DS   │        │  Remote DS   │           │
│  │ (SQLDelight) │        │   (Ktor)     │           │
│  └──────────────┘        └──────────────┘           │
│         ↓                         ↓                   │
│  ┌──────────────┐        ┌──────────────┐           │
│  │   Entities   │        │     DTOs     │           │
│  └──────────────┘        └──────────────┘           │
│         │                         │                   │
│         └────────┬────────────────┘                  │
│                  ↓                                    │
│            ┌──────────┐                              │
│            │  Mapper  │                              │
│            └──────────┘                              │
│                  ↓                                    │
│          ┌──────────────┐                            │
│          │ Domain Model │                            │
│          └──────────────┘                            │
└──────────────────────────────────────────────────────┘
```

## 🎓 Resumo de Responsabilidades

| Camada | Responsabilidade | Conhece |
|--------|------------------|---------|
| **Repository** | Orquestra data sources, cache strategy | DataSources, Mappers, NetworkMonitor |
| **Local DataSource** | Acessa banco de dados (SQLDelight) | Database, Queries |
| **Remote DataSource** | Faz requests HTTP (Ktor) | HttpClient, DTOs |
| **Mapper** | Converte entre DTO/Entity/Domain | Todos os modelos |
| **DTO** | Representa dados da API | Nada (apenas dados) |
| **Entity** | Representa dados do DB | Nada (gerado pelo SQLDelight) |
| **Domain Model** | Representa regras de negócio | Nada (independente) |