package com.example.domain.usecase.login

import com.example.domain.models.user.User
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import com.example.domain.repository.dbLocal.sqldelight.user.UserRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val appAuthenticateRepository: AppAuthenticateRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<User> =
        runCatching {
            val authResult = authRepository.login(email, password)
            appAuthenticateRepository.saveToken(authResult.token)
            appAuthenticateRepository.setIsAuthenticated(true)
            userRepository.getUser(authResult.uid)
        }
}