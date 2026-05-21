package org.fitverse.domain.repository.dbLocal.sqldelight.user

import org.fitverse.domain.models.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(user: User): Unit
    fun observeUser(userId: String): Flow<User?>       // Stream local (Room/SQLDelight)
    suspend fun getUser(userId: String): User          // Busca remota/local
    suspend fun updateUser(user: User): User           // Atualiza local + remote
    suspend fun deleteUser(userId: String)             // Deleta local + remote
    suspend fun syncOfflineData(user: User)            // Sync quando volta conexão
}