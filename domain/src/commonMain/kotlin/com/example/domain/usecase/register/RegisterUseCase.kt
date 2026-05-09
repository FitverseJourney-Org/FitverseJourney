package com.example.domain.usecase.register

import com.example.domain.models.user.User
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.user.UserRepository

class RegisterUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        userData: User,
    ): Result<Unit> = runCatching {

        // runCatching já envolve tudo em try/catch
        // qualquer exceção vira Result.failure automaticamente
        val authResult = authRepository.register(email, password)
        val userWithUid = userData.copy(uid = authResult.uid)
        userRepository.createUser(userWithUid)
        // Unit é retornado implicitamente — runCatching vira Result.success(Unit)
    }
}