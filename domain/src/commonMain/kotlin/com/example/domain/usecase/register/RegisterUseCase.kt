package org.fitverse.domain.usecase.register

import org.fitverse.domain.models.user.User
import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.user.UserRepository

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
        val uid = authResult.getOrThrow().uid
        val userWithUid = userData.copy(uid = uid)
        userRepository.createUser(userWithUid)
        // Token só é salvo após createUser ter êxito — garante que o User
        // existe localmente antes de marcar a sessão como autenticada.
        appAuthenticateRepository.saveToken(uid)
        appAuthenticateRepository.setIsAuthenticated(true)
    }
}