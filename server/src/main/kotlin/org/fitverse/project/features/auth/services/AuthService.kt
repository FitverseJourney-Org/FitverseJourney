package org.fitverse.project.features.auth.services

import org.fitverse.project.features.auth.db.AuthRepository
import org.fitverse.project.features.auth.models.UserToken
import org.fitverse.project.features.auth.routes.RegisterRequest

// auth/services/AuthService.kt
class AuthService(private val authRepository: AuthRepository) {
    suspend fun createUser(data: RegisterRequest): Result<UserToken> {
        // Exemplo de regra de negócio
        if (data.password.length < 6) {
            return Result.failure(Exception("Senha muito curta"))
        }

        // Se tudo ok, manda para o banco/repositório
        return authRepository.saveUser(data)
    }
}