package org.fitverse.data.remote.datasource.auth

import org.fitverse.domain.models.auth.AuthResult

interface AuthRemoteDataSource {
    suspend fun login(email: String, password: String): Result<AuthResult>
    suspend fun register(email: String, password: String): Result<AuthResult>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun logout()
    fun getCurrentUserId(): String?
}
