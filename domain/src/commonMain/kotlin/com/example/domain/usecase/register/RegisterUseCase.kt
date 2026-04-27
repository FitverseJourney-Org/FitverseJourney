package com.example.domain.usecase.register

import com.example.domain.models.local.User
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
    ): Result<User> = runCatching {
        // 1. cria conta no Firebase Auth
        val authResult = authRepository.register(email, password)

        // 2. substitui uid vazio pelo uid real do Firebase
        val userWithUid = userData.copy(uid = authResult.uid)

        // 3. ✅ cria no Firestore + local (não update)
        userRepository.createUser(userWithUid)
    }
}