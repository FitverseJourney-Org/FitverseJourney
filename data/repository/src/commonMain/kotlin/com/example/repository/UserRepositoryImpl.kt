package com.example.repository

import com.example.domain.models.user.User
import com.example.domain.repository.dbLocal.sqldelight.user.UserRepository
import com.example.local.datasource.user.UserLocalDataSource
import com.example.local.mapper.user.UserEntityMapper
import com.example.remote.datasource.user.UserRemoteDataSource
import com.example.remote.expect.NetworkMonitor
import com.example.remote.mapper.user.UserDtoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
    private val remoteDataSource: UserRemoteDataSource,
    private val entityMapper: UserEntityMapper,
    private val dtoMapper: UserDtoMapper,
    private val networkMonitor: NetworkMonitor,  // ← authRepository removido
) : UserRepository {

    // ✅ busca local primeiro, fallback remoto
    override suspend fun getUser(userId: String): User {
        val local = localDataSource.getUser(userId)
        if (local != null) return entityMapper.mapEntityToDomain(local)

        val remote = remoteDataSource.getUserById(userId)
        val user   = dtoMapper.mapDtoToDomain(remote)
        localDataSource.insertUser(entityMapper.mapDomainToEntity(user))
        return user
    }

    // ✅ local + remoto
    override suspend fun updateUser(user: User): User {
        localDataSource.updateUser(entityMapper.mapDomainToEntity(user))
        if (networkMonitor.isConnected()) {
            remoteDataSource.updateUser(
                userId = user.uid,
                user   = dtoMapper.mapDomainToRequestDto(user),
            )
        }
        return user
    }

    // ✅ local + remoto
    override suspend fun deleteUser(userId: String) {
        localDataSource.deleteUser(userId)
        if (networkMonitor.isConnected()) {
            remoteDataSource.deleteUser(userId)
        }
    }

    override suspend fun createUser(user: User) {
        // ✅ salva no Firestore via remote
        remoteDataSource.createUser(dtoMapper.mapDomainToRequestDto(user))
        // ✅ salva local
        localDataSource.insertUser(entityMapper.mapDomainToEntity(user))
    }

    // ✅ stream local mapeado
    override fun observeUser(userId: String): Flow<User?> =
        localDataSource.observeUser(userId)
            .map { entity -> entity?.let { entityMapper.mapEntityToDomain(it) } }

    // ✅ sync quando volta conexão
    override suspend fun syncOfflineData(user: User) {
        if (!networkMonitor.isConnected()) return
        remoteDataSource.updateUser(
            userId = user.uid,
            user   = dtoMapper.mapDomainToRequestDto(user),
        )
        localDataSource.insertUser(entityMapper.mapDomainToEntity(user))
    }
}