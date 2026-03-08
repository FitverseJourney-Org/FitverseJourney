package com.example.domain.usecase.cache

import com.example.domain.repository.cache.CacheDataSource

class CacheGetTokenUseCase(
    private val cacheDataSource: CacheDataSource
) {
    suspend operator fun invoke(): String? {
        return cacheDataSource.getAuthToken()
    }
}