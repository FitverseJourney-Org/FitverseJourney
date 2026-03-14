package org.fitverse.project.features.auth.services

import org.fitverse.project.features.auth.db.AuthRepository
import org.fitverse.project.features.auth.models.UserToken
import org.fitverse.project.features.auth.routes.LoginRequest
import org.fitverse.project.features.auth.routes.RegisterRequest

// auth/services/AuthService.kt
class AuthService(private val authRepository: AuthRepository) {
    fun createUser(data: RegisterRequest): Result<UserToken> {
        // Exemplo de regra de negócio
        if (data.password.length < 6) {
            return Result.failure(Exception("Senha muito curta"))
        }

        // Se tudo ok, manda para o banco/repositório
        return authRepository.saveUser(data)
    }

    fun authenticate(data: LoginRequest): Result<UserToken> {
        // Validação básica de negócio
        if (!data.email.contains("@")) {
            return Result.failure(Exception("Formato de e-mail inválido"))
        }

        // Delegar a verificação pesada para o repositório (Firebase/DB)
        return authRepository.signInWithEmail(data.email, data.password)
    }
}