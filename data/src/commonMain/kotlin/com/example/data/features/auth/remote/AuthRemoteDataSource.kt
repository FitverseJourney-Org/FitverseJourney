package com.example.data.features.auth.remote

import com.example.data.HttpUrlStrings
import com.example.data.features.auth.local.model.LoginRequest
import com.example.domain.model.authentication.login.UserToken
import com.example.domain.model.authentication.register.RegisterRequest
import com.example.domain.repository.authentication.AuthRemoteRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthRemoteRepositoryImpl(
    private val client: HttpClient
) : AuthRemoteRepository {

    override suspend fun login(email: String, password: String): Result<UserToken> =
        runCatching {
            client.post(HttpUrlStrings.URL_LOGIN.url) {
                setBody(
                    LoginRequest(
                        email = email,
                        password = password
                    )
                )
            }.body<UserToken>()
        }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun register(data: RegisterRequest): Result<Unit> =
        runCatching {
            client.post(HttpUrlStrings.URL_REGISTER.url) {
                setBody(data)
            }.body()
        }
}