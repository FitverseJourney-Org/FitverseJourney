package com.example.data.datasources.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class CacheDataSource(private val dataStore: DataStore<Preferences>) {
    companion object {
        const val KEY_AUTH_TOKEN = "auth_token"
        const val KEY_LANGUAGE = "language"
    }

    suspend fun saveAuthToken(token: String) {
        val key = stringPreferencesKey(KEY_AUTH_TOKEN)
        dataStore.edit { preferences ->
            preferences[key] = token
        }
    }

    suspend fun getAuthToken(): String? {
        val key = stringPreferencesKey(KEY_AUTH_TOKEN)
        val preferences = dataStore.data.first()
        return preferences[key]
    }

    suspend fun saveLanguage(language: String) {
        val key = stringPreferencesKey(KEY_LANGUAGE)
        dataStore.edit { preferences ->
            preferences[key] = language
        }
    }

    suspend fun getLanguage(): String? {
        val key = stringPreferencesKey(KEY_LANGUAGE)
        val preferences = dataStore.data.first()
        return preferences[key]
    }
}