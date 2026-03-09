package com.example.domain.usecase.cache

import com.example.domain.repository.cache.CacheDataSourceLanguage
import com.example.domain.repository.cache.CacheDataSourceOnboarding

class CacheSaveLanguageUseCase(
    private val cacheDataSourceLanguage: CacheDataSourceLanguage
) {
    suspend operator fun invoke(language: String) {
        cacheDataSourceLanguage.saveLanguage(language)
    }
}