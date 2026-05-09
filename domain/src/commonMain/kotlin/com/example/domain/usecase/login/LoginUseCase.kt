package com.example.domain.usecase.login

import com.example.domain.models.user.User
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.user.UserRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<User> =
        runCatching {
            // 1. autentica e obtém o uid
            val authResult = authRepository.login(email, password)
            // 2. busca o perfil do usuário
            userRepository.getUser(authResult.uid)
        }
}