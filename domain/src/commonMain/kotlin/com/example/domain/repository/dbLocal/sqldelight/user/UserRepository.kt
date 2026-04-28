package com.example.domain.repository.dbLocal.sqldelight.user

import com.example.domain.models.local.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(user: User): User
    fun observeUser(userId: String): Flow<User?>       // Stream local (Room/SQLDelight)
    suspend fun getUser(userId: String): User          // Busca remota/local
    suspend fun updateUser(user: User): User           // Atualiza local + remote
    suspend fun deleteUser(userId: String)             // Deleta local + remote
    suspend fun syncOfflineData(user: User)            // Sync quando volta conexão
}