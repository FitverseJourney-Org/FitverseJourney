package com.example.data.dbRemote.repositories

import com.example.domain.model.authentication.login.UserToken
import com.example.domain.model.authentication.register.RegisterRequest
import com.example.domain.repository.authentication.AuthRemoteDataSource
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.authentication.AuthTokenStoreRepository

class AuthRepositoryImpl(
    private val remote: AuthRemoteDataSource,
    private val tokenStore: AuthTokenStoreRepository
): AuthRepository {
    override suspend fun login(email: String,password: String): Result<UserToken> = runCatching{
        val token = remote.login(email,password).getOrThrow()
        tokenStore.saveToken(token.tokenId)
        return Result.success(token)
    }

    override suspend fun resetPassword(email: String): Result<Unit>{
        remote.resetPassword(email)
        return Result.success(Unit)
    }

    override suspend fun register(data: RegisterRequest): Result<Unit> {
        val result = remote.register(data)
        println("result: $result")
        return result
    }

}