package com.example.domain.repository.cache

interface CacheDataSourceLanguage {

    suspend fun saveLanguage(language: String)
    suspend fun getLanguage(): String?
}