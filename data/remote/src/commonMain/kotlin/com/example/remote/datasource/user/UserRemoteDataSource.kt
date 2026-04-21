package com.example.remote.datasource.user

import com.example.data.model.dto.user.UserDto
import com.example.domain.model.user.dto.UserRequestDto

/**
 * Interface para operações remotas de User
 * Define o contrato para chamadas à API
 */
interface UserRemoteDataSource {
    suspend fun getUserById(userId: String): UserDto
    suspend fun getAllUsers(): List<UserDto>
    suspend fun createUser(user: UserRequestDto): UserDto
    suspend fun updateUser(userId: String, user: UserRequestDto): UserDto
    suspend fun deleteUser(userId: String)
    suspend fun getUserByEmail(email: String): UserDto
}