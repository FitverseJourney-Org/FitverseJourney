package com.example.domain.usecase.cache

import com.example.domain.repository.cache.CacheDataSource

class CacheSetOnboardingUseCase(
    private val cacheDataSource: CacheDataSource
) {
    suspend operator fun invoke(isCompleted: Boolean) {
        cacheDataSource.setOnboardingCompleted(isCompleted)
    }
}