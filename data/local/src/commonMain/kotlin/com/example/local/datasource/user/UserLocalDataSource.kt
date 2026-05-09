package com.example.local.datasource.user

import com.journey.database.migrations.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface para operações locais de User
 * Define o contrato para acesso ao banco de dados
 */
interface UserLocalDataSource {
    fun observeUser(userId: String): Flow<UserEntity?>      // ✅ String + Entity
    suspend fun getUser(userId: String): UserEntity?        // ✅ String + Entity
    suspend fun insertUser(entity: UserEntity)
    suspend fun updateUser(entity: UserEntity)
    suspend fun deleteUser(userId: String)                  // ✅ String
    suspend fun deleteAllUsers()                            // ✅ útil para logout
}