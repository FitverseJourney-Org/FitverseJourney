package com.example.data.features.auth.repository

import com.example.domain.model.authentication.login.UserToken
import com.example.domain.model.authentication.register.RegisterUser
import com.example.domain.repository.authentication.AuthRemoteRepository
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.authentication.AuthTokenStoreRepository

class AuthRemoteRepositoryImpl(
    private val remote: AuthRemoteRepository,
    private val tokenStore: AuthTokenStoreRepository
): AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Result<UserToken> {
        return try {
            val token = remote.login(email,password).getOrThrow()
            Result.success(token)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit>{
        remote.resetPassword(email)
        return Result.success(Unit)
    }

    override suspend fun register(data: RegisterUser): Result<Unit> {
        val result = remote.register(data = data)
        return result
    }

}