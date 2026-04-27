package com.example.domain.repository.authentication

import com.example.domain.models.auth.AuthResult


interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResult
    suspend fun register(email: String, password: String): AuthResult
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun logout()
    fun getCurrentUserId(): String?
}

// DTO simples para retorno de auth
data class AuthResultDto(
    val uid: String,
    val email: String?,
    val token: String?
)