package com.example.domain.usecase.cache

import com.example.domain.repository.cache.CacheDataSourceOnboarding

class CacheStatusOnboardingUseCae(
    private val cacheDataSourceOnboarding: CacheDataSourceOnboarding
) {
    suspend operator fun invoke() : Boolean {
        return cacheDataSourceOnboarding.isOnboardingCompleted()
    }
}