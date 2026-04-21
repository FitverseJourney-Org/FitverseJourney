package com.example.repository

import com.example.local.datasource.user.UserLocalDataSource
import com.example.remote.datasource.user.UserRemoteDataSource
import com.example.domain.model.user.dto.UserRequestDto
import com.example.remote.expect.NetworkMonitor
import com.example.domain.model.local.User
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.user.UserRepository
import com.example.local.mapper.UserMapper
import kotlinx.coroutines.flow.Flow

/**
 * Implementação do UserRepository seguindo Clean Architecture
 *
 * Responsabilidades:
 * - Orquestrar data sources (local + remote se houver)
 * - Converter entre Entity e Domain usando Mapper
 * - Implementar lógica de cache/sincronização
 *
 * NÃO acessa diretamente o database - usa LocalDataSource
 */
class UserRepositoryImpl(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource,  // Adicionar quando tiver API
    private val userMapper: UserMapper,
    private val authRepository: AuthRepository,
    private val networkMonitor: NetworkMonitor  // Adicionar quando tiver rede
) : UserRepository {

    override suspend fun login(email: String, password: String): User {
        val authResult = authRepository.login(email, password)

        // Após auth, busca dados completos do usuário na sua API
        val userDto = remoteDataSource.getUserById(authResult.uid)
        val user = userMapper.mapDtoToDomain(userDto)

        // Persiste localmente
        localDataSource.insertUser(user)
        return user
    }

    override suspend fun register(email: String, password: String, userData: User): User {
        // 1. Cria no Firebase Authentication
        val authResult = authRepository.register(email, password)

        // 2. Cria na sua API com o uid do Firebase como ID
        val requestDto = userMapper.mapDomainToDto(
            domain = userData.copy(id = authResult.uid.toLongOrNull() ?: 0)
        )

        val createdDto = remoteDataSource.createUser(
            user = UserRequestDto(
                id = requestDto.id,
                name = requestDto.name,
                email = requestDto.email,
                gender = requestDto.gender,
                birthDate = requestDto.age.toString(),
                heightCm = requestDto.height,
                weightKg = requestDto.weight,
                fitnessLevel = requestDto.experienceLevel,
                fitnessGoal = requestDto.goals,
            )
        )
        val user = userMapper.mapDtoToDomain(createdDto)

        // 3. Persiste localmente
        localDataSource.insertUser(user)
        return user
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return authRepository.resetPassword(email)
    }

    override suspend fun logout() {
        authRepository.logout()
        // limpar dados locais se necessário
    }

    override suspend fun createUser(user: User): User {
        return try {
            localDataSource.insertUser(user)
            user
        } catch (e: Exception) {
            println("Erro ao criar usuário: ${e.message}")
            throw e
        }
    }

    override suspend fun updateUser(user: User): User {
        return try {
            localDataSource.updateUser(user)
            user
        } catch (e: Exception) {
            println("Erro ao atualizar usuário: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteUser(userId: Long) {
        try {
            localDataSource.deleteUser(userId)
        } catch (e: Exception) {
            println("Erro ao deletar usuário: ${e.message}")
            throw e
        }
    }

    override fun observeUser(userId: Long): Flow<User?> = localDataSource.observeUser(userId)

    override suspend fun syncOfflineData(dto: User) {
        val dto = userMapper.mapDomainToDto(dto)    // Domínio → API
        val user = userMapper.mapDtoToDomain(dto)            // API → Domínio
        localDataSource.insertUser(user)                     // Domínio → Local
    }
}