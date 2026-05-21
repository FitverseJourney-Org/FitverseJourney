package org.fitverse.domain.repository.authentication

import org.fitverse.domain.models.auth.AuthResult

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthResult>
    suspend fun register(email: String, password: String): Result<AuthResult>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun logout()
    fun getCurrentUserId(): String?
}