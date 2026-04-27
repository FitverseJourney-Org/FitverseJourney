package com.example.remote.datasource.user

import com.example.remote.dto.user.UserDto
import com.example.remote.dto.user.UserRequestDto

/**
 * Interface para operações remotas de User
 * Define o contrato para chamadas à API
 */
interface UserRemoteDataSource {
    suspend fun getUserById(userId: String): UserRequestDto
    suspend fun createUser(user: UserRequestDto): UserRequestDto
    suspend fun updateUser(userId: String, user: UserRequestDto): UserRequestDto
    suspend fun deleteUser(userId: String)
}