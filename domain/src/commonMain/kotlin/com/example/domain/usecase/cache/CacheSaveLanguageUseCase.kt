package com.example.domain.usecase.cache

import com.example.domain.repository.cache.CacheDataSource

class CacheSaveLanguageUseCase(
    private val cacheDataSource: CacheDataSource
) {
    suspend operator fun invoke(language: String) {
        cacheDataSource.saveLanguage(language)
    }
}