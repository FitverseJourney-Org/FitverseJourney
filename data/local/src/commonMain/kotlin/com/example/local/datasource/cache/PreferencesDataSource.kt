package com.example.local.datasource.cache

import kotlinx.coroutines.flow.Flow

/**
 * DataSource para preferências/configurações do app
 * Usa DataStore (KMP) ou SharedPreferences (Android)
 */
interface PreferencesDataSource {
    suspend fun saveAuthToken(token: String)
    suspend fun getAuthToken(): String?
    fun observeAuthToken(): Flow<String?>
    suspend fun clearAuthToken()

    suspend fun saveUserId(userId: String)
    suspend fun getUserId(): String?

    suspend fun setDarkMode(enabled: Boolean)
    suspend fun isDarkModeEnabled(): Boolean
    fun observeDarkMode(): Flow<Boolean>

    suspend fun clear()
}