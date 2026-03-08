package com.example.domain.usecase.cache

import com.example.domain.repository.cache.CacheDataSource

class CacheStatusOnboardingUseCae(
    private val cacheDataSource: CacheDataSource
) {
    suspend operator fun invoke() : Boolean {
        return cacheDataSource.isOnboardingCompleted()
    }
}