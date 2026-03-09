package com.example.domain.usecase.cache

import com.example.domain.repository.cache.CacheDataSourceLanguage
import com.example.domain.repository.cache.CacheDataSourceOnboarding

class CacheGetLanguageUseCase(
    private val cacheDataSourceLanguage: CacheDataSourceLanguage
) {
    suspend operator fun invoke() : String? {
        return cacheDataSourceLanguage.getLanguage()
    }
}