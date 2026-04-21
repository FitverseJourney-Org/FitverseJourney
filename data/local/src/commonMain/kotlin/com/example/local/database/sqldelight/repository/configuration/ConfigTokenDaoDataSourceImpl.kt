package com.example.local.database.sqldelight.repository.configuration

import com.example.local.database.sqldelight.DatabaseSqlDeLightHelper
import com.example.domain.repository.dbLocal.sqldelight.configurations.ConfigTokenDataSourceDao

class ConfigTokenDaoDataSourceImpl(
    private val databaseSqlDeLightHelper: DatabaseSqlDeLightHelper
) : ConfigTokenDataSourceDao {

    override suspend fun getAuthToken(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun setAuthToken(token: String) {
        TODO("Not yet implemented")
    }
}