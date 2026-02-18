package com.example.data.dbRemote.datasources

import com.example.data.dbRemote.models.LoginRequest
import com.example.domain.model.authentication.login.UserToken
import com.example.domain.model.authentication.register.RegisterRequest
import com.example.domain.repository.authentication.AuthRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class KtorAuthRemoteDataSource(
    private val client: HttpClient
) : AuthRemoteDataSource {

    override suspend fun login(email: String, password: String): Result<UserToken> =
        runCatching {
            client.post("/login") {
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
            client.post("/register") {
                setBody(data)
            }.body()
        }
}