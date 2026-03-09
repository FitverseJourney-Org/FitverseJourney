package com.example.data.features.cache

import com.example.data.features.cache.CacheDataSourceImpl.Companion.KEY_LANGUAGE
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.domain.repository.cache.CacheDataSourceLanguage
import kotlinx.coroutines.flow.first
import kotlinx.io.IOException

class CacheDataSourceKeyLanguageImpl(
    private val dataStore: DataStore<Preferences>
) : CacheDataSourceLanguage {

    override suspend fun saveLanguage(language: String) {
        dataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = language
        }
    }

    override suspend fun getLanguage(): String? {
        return try {
            val prefs = dataStore.data.first()
            prefs[KEY_LANGUAGE]
        } catch (e: IOException) {
            null
        }
    }
}