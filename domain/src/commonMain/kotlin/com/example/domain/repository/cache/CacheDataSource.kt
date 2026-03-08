package com.example.domain.repository.cache

interface CacheDataSource {
    suspend fun saveAuthToken(token: String)
    suspend fun getAuthToken(): String?

    suspend fun saveLanguage(language: String)
    suspend fun getLanguage(): String?

    suspend fun setOnboardingCompleted(value: Boolean)
    suspend fun isOnboardingCompleted(): Boolean
}