package com.example.domain.repository.cache

interface CacheDataSourceOnboarding {
    suspend fun setOnboardingCompleted(value: Boolean)
    suspend fun isOnboardingCompleted(): Boolean
}