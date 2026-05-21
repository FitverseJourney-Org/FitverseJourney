package org.fitverse.data.local.datasource.cache

import org.fitverse.data.local.database.datastore.DataStoreFactory
import kotlinx.coroutines.flow.Flow

class PreferencesDataSourceImpl(
    private val dataStore: DataStoreFactory
): PreferencesDataSource {
    override suspend fun saveAuthToken(token: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getAuthToken(): String? {
        TODO("Not yet implemented")
    }

    override fun observeAuthToken(): Flow<String?> {
        TODO("Not yet implemented")
    }

    override suspend fun clearAuthToken() {
        TODO("Not yet implemented")
    }

    override suspend fun saveUserId(userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserId(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun setDarkMode(enabled: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun isDarkModeEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun observeDarkMode(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }
}
