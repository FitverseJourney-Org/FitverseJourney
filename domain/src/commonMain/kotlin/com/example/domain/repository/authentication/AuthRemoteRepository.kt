package com.example.domain.repository.authentication

import com.example.domain.model.authentication.login.UserToken
import com.example.domain.model.authentication.register.RegisterUser


interface AuthRemoteRepository {
    suspend fun login(email: String, password: String): Result<UserToken>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun register(data: RegisterUser): Result<Unit>
}