package com.example.domain.usecase.cache

import com.example.domain.repository.cache.CacheDataSourceOnboarding

class CacheSetOnboardingUseCase(
    private val cacheDataSourceOnboarding: CacheDataSourceOnboarding
) {
    suspend operator fun invoke(isCompleted: Boolean) {
        cacheDataSourceOnboarding.setOnboardingCompleted(isCompleted)
    }
}