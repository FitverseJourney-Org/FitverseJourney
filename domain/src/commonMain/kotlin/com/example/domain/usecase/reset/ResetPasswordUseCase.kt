package com.example.domain.usecase.reset

import com.example.domain.repository.authentication.AuthRepository

class ResetPasswordUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        return authRepository.resetPassword(email)
    }
}