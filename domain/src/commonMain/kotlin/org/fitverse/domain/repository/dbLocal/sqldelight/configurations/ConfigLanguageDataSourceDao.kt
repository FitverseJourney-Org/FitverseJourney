package org.fitverse.domain.repository.dbLocal.sqldelight.configurations

interface ConfigLanguageDataSourceDao {
    suspend fun getLanguage(): String?
    suspend fun setLanguage(language: String)
}