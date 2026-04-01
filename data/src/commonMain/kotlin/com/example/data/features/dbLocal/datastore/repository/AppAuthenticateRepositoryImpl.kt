package com.example.data.features.dbLocal.datastore.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.data.features.dbLocal.datastore.PreferencesKeys
import com.example.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AppAuthenticateRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : AppAuthenticateRepository {

    override val isAuthenticated: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.IS_AUTHENTICATED] ?: false
        }

    override suspend fun setIsAuthenticated(isAuthenticated: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_AUTHENTICATED] = isAuthenticated
        }
    }


}