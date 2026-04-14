package com.example.data.datasource.local.user

import com.example.domain.model.local.User
import kotlinx.coroutines.flow.Flow

/**
 * Interface para operações locais de User
 * Define o contrato para acesso ao banco de dados
 */
interface UserLocalDataSource {
    suspend fun insertUser(user: User): Unit
    suspend fun updateUser(user: User): Unit
    suspend fun deleteUser(userId: Long): Unit
    suspend fun deleteAllUsers(): Unit

    suspend fun getUserById(userId: Long): User?   // ← era UserDto
    suspend fun getAllUsers(): List<User>           // ← era List<UserDto>

    fun observeUser(userId: Long): Flow<User?>     // ← era Flow<UserDto>
    fun observeAllUsers(): Flow<List<User>>        // ← era Flow<List<UserDto>>
}