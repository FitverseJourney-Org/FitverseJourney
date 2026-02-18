package com.example.data.dbLocal.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.domain.repository.KeyValueStorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

class AndroidDataStoreStorageRepository(
    private val dataStore: DataStore<Preferences>
) : KeyValueStorageRepository {

    private fun keyOf(name: String) = stringPreferencesKey(name)

    override fun observeString(key: String, defaultValue: String): Flow<String> =
        dataStore.data.map { prefs -> prefs[keyOf(key)] ?: defaultValue }

    override suspend fun getStringOnce(key: String, defaultValue: String): String =
        dataStore.data.map { prefs -> prefs[keyOf(key)] ?: defaultValue }.first()

    override suspend fun putString(key: String, value: String) {
        dataStore.edit { prefs -> prefs[keyOf(key)] = value }
    }

    override suspend fun remove(key: String) {
        dataStore.edit { prefs -> prefs.remove(keyOf(key)) }
    }
}
