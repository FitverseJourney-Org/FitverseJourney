package org.fitverse.domain.repository.dbLocal.sqldelight.configurations

interface ConfigTokenDataSourceDao {
    suspend fun getAuthToken(): String?
    suspend fun setAuthToken(token: String)


}