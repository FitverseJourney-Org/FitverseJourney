package com.example.domain.repository.authentication

interface AuthTokenStoreRepository {
    suspend fun saveToken(token: String?)
    suspend fun getToken(): String?
    suspend fun clearToken()
}