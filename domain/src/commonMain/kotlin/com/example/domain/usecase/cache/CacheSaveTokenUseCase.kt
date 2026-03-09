package com.example.domain.usecase.cache

import com.example.domain.repository.cache.CacheDataSourceAuthToken

class CacheSaveTokenUseCase(
    private val cacheDataSourceAuthToken: CacheDataSourceAuthToken
) {
    suspend operator fun invoke(token: String) {
        cacheDataSourceAuthToken.saveAuthToken(token)
    }
}