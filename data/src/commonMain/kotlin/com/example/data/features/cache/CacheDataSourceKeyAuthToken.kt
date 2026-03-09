package com.example.data.features.cache

import com.example.data.features.cache.CacheDataSourceImpl.Companion.KEY_AUTH_TOKEN
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.domain.repository.cache.CacheDataSourceAuthToken
import kotlinx.coroutines.flow.first
import kotlinx.io.IOException

class CacheDataSourceKeyAuthTokenImpl(
    private val dataStore: DataStore<Preferences>
) : CacheDataSourceAuthToken {

    override suspend fun saveAuthToken(token: String) {
        dataStore.edit { prefs ->
            prefs[KEY_AUTH_TOKEN] = token
        }
    }

    override suspend fun getAuthToken(): String? {
        return try {
            val prefs = dataStore.data.first()
            prefs[KEY_AUTH_TOKEN]
        } catch (e: IOException) {
            // falha de I/O — retornar nulo é uma escolha segura; logue se necessário
            null
        }
    }
}