package com.example.domain.usecase.cache

import com.example.domain.repository.cache.CacheDataSourceAuthToken
import com.example.domain.repository.cache.CacheDataSourceOnboarding

class CacheGetTokenUseCase(
    private val cacheDataSourceAuthToken: CacheDataSourceAuthToken
) {
    suspend operator fun invoke(): String? {
        return cacheDataSourceAuthToken.getAuthToken()
    }
}