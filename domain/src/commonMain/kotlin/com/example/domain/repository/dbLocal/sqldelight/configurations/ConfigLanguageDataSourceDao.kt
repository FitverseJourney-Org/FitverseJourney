package com.example.domain.repository.dbLocal.sqldelight.configurations

interface ConfigLanguageDataSourceDao {
    suspend fun getLanguage(): String?
    suspend fun setLanguage(language: String)
}