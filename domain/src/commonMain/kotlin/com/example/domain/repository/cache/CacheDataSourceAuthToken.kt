package com.example.domain.repository.cache

interface CacheDataSourceAuthToken {
    suspend fun saveAuthToken(token: String)
    suspend fun getAuthToken(): String?
}