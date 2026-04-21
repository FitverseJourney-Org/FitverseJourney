package com.example.domain.usecase.login

import com.example.domain.model.authentication.login.UserToken
import com.example.domain.repository.authentication.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<UserToken> {
        // 1. Valide PRIMEIRO
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Preencha todos os campos"))
        }

        // 2. Tente a operação de rede apenas se os dados forem válidos
        return try {
            val result = authRepository.login(email, password)

            if (result.token == null) {
                Result.failure(Exception("Login failed: Token nulo"))
            } else {
                Result.success(UserToken(result.token))
            }
        } catch (e: Exception) {
            // Captura erros do Firebase (credenciais inválidas, rede, etc)
            Result.failure(e)
        }
    }
}