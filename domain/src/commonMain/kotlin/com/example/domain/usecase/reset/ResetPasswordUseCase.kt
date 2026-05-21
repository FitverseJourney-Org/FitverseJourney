package org.fitverse.domain.usecase.reset

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.user.UserRepository

class ResetPasswordUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String
    ): Result<Unit> = authRepository.resetPassword(email)
}