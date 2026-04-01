package com.example.data.features.dbLocal.sqldelight.repository.configuration

import com.example.data.database.DatabaseSqlDeLightHelper
import com.example.domain.repository.dbLocal.sqldelight.configurations.ConfigLanguageDataSourceDao

class ConfigLanguageDataSourceDao(
    private val databaseSqlDeLightHelper: DatabaseSqlDeLightHelper
): ConfigLanguageDataSourceDao {
    override suspend fun getLanguage(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun setLanguage(language: String) {
        TODO("Not yet implemented")
    }
}