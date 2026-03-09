package org.fitverse.project.features.auth.db

import com.google.firebase.FirebaseApp
import org.fitverse.project.features.auth.models.UserToken
import org.fitverse.project.features.auth.routes.RegisterRequest

// auth/db/AuthRepository.kt
class AuthRepository(private val firebaseAdmin: FirebaseApp) {
    suspend fun saveUser(data: RegisterRequest): Result<UserToken> {
        return try {
            // Lógica pesada de banco ou Firebase aqui
            val user = "Usuário salvo no DB"
            Result.success(UserToken(tokenId = "token_gerado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}