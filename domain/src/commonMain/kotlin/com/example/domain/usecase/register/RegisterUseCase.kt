package com.example.domain.usecase.register

import com.example.domain.models.user.User
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import com.example.domain.repository.dbLocal.sqldelight.user.UserRepository

class RegisterUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val appAuthenticateRepository: AppAuthenticateRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        userData: User,
    ): Result<Unit> = runCatching {
        val authResult = authRepository.register(email, password)
        appAuthenticateRepository.saveToken(authResult.token)
        appAuthenticateRepository.setIsAuthenticated(true)
        val userWithUid = userData.copy(uid = authResult.uid)
        userRepository.createUser(userWithUid)
    }
}