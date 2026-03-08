package com.example.data.features.auth.repository

import com.example.domain.repository.authentication.AuthTokenStoreRepository

class AuthTokenStoreImpl : AuthTokenStoreRepository {

    private var token: String? = null

    override suspend fun saveToken(token: String?) {
        println("TOKEN: $token")
        this.token = token
    }

    override suspend fun getToken(): String? = token

    override suspend fun clearToken() {
        token = null
    }
}