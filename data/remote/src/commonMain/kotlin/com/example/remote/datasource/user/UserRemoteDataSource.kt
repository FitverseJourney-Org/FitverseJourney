package org.fitverse.data.remote.datasource.user

import org.fitverse.data.remote.dto.user.UserRequestDto

/**
 * Interface para operações remotas de User
 * Define o contrato para chamadas à API
 */
interface UserRemoteDataSource {
    suspend fun getUserById(userId: String): UserRequestDto
    suspend fun createUser(user: UserRequestDto): Unit
    suspend fun updateUser(userId: String, user: UserRequestDto): UserRequestDto
    suspend fun deleteUser(userId: String)
}