package com.example.expect

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

actual class PlatformKeyValueStorage(
    private val dataStore: DataStore<Preferences>
) {
    private fun keyOf(name: String) = stringPreferencesKey(name)

    actual fun observeString(key: String, defaultValue: String): Flow<String> =
        dataStore.data.map { prefs ->
            prefs[keyOf(key)] ?: defaultValue
        }

    actual suspend fun getString(key: String, defaultValue: String): String =
        dataStore.data.map { prefs ->
            prefs[keyOf(key)] ?: defaultValue
        }.first()

    actual suspend fun putString(key: String, value: String) {
        dataStore.edit { prefs ->
            prefs[keyOf(key)] = value
        }
    }

    actual suspend fun remove(key: String) {
        dataStore.edit { prefs ->
            prefs.remove(keyOf(key))
        }
    }
}