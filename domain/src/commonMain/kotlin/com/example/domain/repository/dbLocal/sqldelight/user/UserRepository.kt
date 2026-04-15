package com.example.domain.repository.dbLocal.sqldelight.user

import com.example.domain.model.local.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    // Leitura reativa (Cache-First)
    fun observeUser(userId: Long): Flow<User?>

    // Leitura pontual

    // Escrita
    suspend fun createUser(user: User): User
    suspend fun updateUser(user: User): User
    suspend fun deleteUser(userId: Long): Unit

    // Sincronização com remoto
    suspend fun syncOfflineData(dto: User)
}