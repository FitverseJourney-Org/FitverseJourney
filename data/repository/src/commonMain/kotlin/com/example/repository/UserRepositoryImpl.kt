package org.fitverse.data.repository

import org.fitverse.domain.models.user.User
import org.fitverse.domain.repository.dbLocal.sqldelight.user.UserRepository
import org.fitverse.data.local.datasource.user.UserLocalDataSource
import org.fitverse.data.local.mapper.user.UserEntityMapper
import org.fitverse.data.remote.datasource.user.UserRemoteDataSource
import org.fitverse.data.remote.expect.NetworkMonitor
import org.fitverse.data.remote.mapper.user.UserDtoMapper
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
    private val networkMonitor: NetworkMonitor,
) : UserRepository {

    // local-first; se remoto falha mas cache existe, usa cache (suporta backend offline)
    override suspend fun getUser(userId: String): User {
        val local = localDataSource.getUser(userId)
        if (local != null) return entityMapper.mapEntityToDomain(local)

        // Sem cache local: precisa do backend — propaga o erro para quem chamou
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
        localDataSource.deleteUser(userId = userId)
        if (networkMonitor.isConnected()) {
            remoteDataSource.deleteUser(userId)
        }
    }

    override suspend fun createUser(user: User) {
        // local-first: persiste localmente antes de tentar o backend
        // se o backend estiver down, o dado fica disponível para login offline
        localDataSource.insertUser(
            entityMapper.mapDomainToEntity(domain = user)
        )
        runCatching {
            remoteDataSource.createUser(
                user = dtoMapper.mapDomainToRequestDto(domain = user)
            )
        }
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