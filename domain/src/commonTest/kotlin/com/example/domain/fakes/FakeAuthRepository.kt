package org.fitverse.domain.fakes

import org.fitverse.domain.models.auth.AuthResult
import org.fitverse.domain.repository.authentication.AuthRepository

class FakeAuthRepository(
    private var loginResult: Result<AuthResult> = Result.success(
        AuthResult(uid = "uid-test", email = "test@fit.com", token = "token-test")
    )
) : AuthRepository {

    var loginCallCount = 0
        private set

    fun willFailWith(error: Throwable) {
        loginResult = Result.failure(error)
    }

    override suspend fun login(email: String, password: String): Result<AuthResult> {
        loginCallCount++
        return loginResult
    }

    override suspend fun register(email: String, password: String): Result<AuthResult> =
        loginResult

    override suspend fun resetPassword(email: String): Result<Unit> = Result.success(Unit)

    override suspend fun logout() {}

    override fun getCurrentUserId(): String? = "uid-test"
}
