package com.example.data.features.dbLocal.sqldelight.repository.configuration

import com.example.data.database.DatabaseSqlDeLightHelper
import com.example.domain.repository.dbLocal.sqldelight.configurations.ConfigOnboardingDataSourceDao

class ConfigOnboardingDataSourceDao(
    private val databaseSqlDeLightHelper: DatabaseSqlDeLightHelper
): ConfigOnboardingDataSourceDao {
    override suspend fun getOnboardingCompleted(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        TODO("Not yet implemented")
    }
}