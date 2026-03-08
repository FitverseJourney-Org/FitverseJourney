package com.example.domain.usecase.cache

import com.example.domain.repository.cache.CacheDataSource

class CacheSaveTokenUseCase(
    private val cacheDataSource: CacheDataSource
) {
    suspend operator fun invoke(token: String) {
        cacheDataSource.saveAuthToken(token)
    }
}