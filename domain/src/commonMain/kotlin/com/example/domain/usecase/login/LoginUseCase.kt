package org.fitverse.domain.usecase.login

import org.fitverse.domain.models.user.User
import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.user.UserRepository
import org.koin.core.component.getScopeId
import org.koin.core.component.getScopeName

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val appAuthenticateRepository: AppAuthenticateRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<User> =
        runCatching {
            val authResult = authRepository.login(email, password)

            // local-first: se há cache, funciona offline.
            // Token só é persistido após getUser ter êxito — evita estado
            // inconsistente (authenticated=true mas User ausente no DB local).
            if(authResult.isSuccess){
                val uid = authResult.getOrThrow().uid
                val user = userRepository.getUser(userId = uid)

                appAuthenticateRepository.saveToken(token = uid)
                appAuthenticateRepository.setIsAuthenticated(true)
                return Result.success(user)
            }else {
                throw authResult.exceptionOrNull() ?: Exception("Erro desconhecido")
            }
        }
}