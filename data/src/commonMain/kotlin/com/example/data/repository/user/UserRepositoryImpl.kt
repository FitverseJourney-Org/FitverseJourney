// ============================================================
// REPOSITORY IMPLEMENTATION (CORRETO)
// ============================================================
// Arquivo: data/src/commonMain/kotlin/com/example/data/repository/migrations/UserRepositoryImpl.kt

package com.example.data.repository.user

import com.example.data.datasource.local.user.UserLocalDataSource
import com.example.data.mapper.user.UserMapper
import com.example.data.mapper.user.toEntity
import com.example.data.model.dto.user.UserDto
import com.example.domain.model.local.User
import com.example.domain.repository.dbLocal.sqldelight.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

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
    private val userMapper: UserMapper
    // private val remoteDataSource: UserRemoteDataSource  // Adicionar quando tiver API
    // private val networkMonitor: NetworkMonitor  // Adicionar quando tiver rede
) : UserRepository {


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