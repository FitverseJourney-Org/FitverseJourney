package com.example.domain.usecase.cache

import com.example.domain.repository.cache.CacheDataSource

class CacheGetLanguageUseCase(
    private val cacheDataSource: CacheDataSource
) {
    suspend operator fun invoke() : String? {
        return cacheDataSource.getLanguage()
    }
}