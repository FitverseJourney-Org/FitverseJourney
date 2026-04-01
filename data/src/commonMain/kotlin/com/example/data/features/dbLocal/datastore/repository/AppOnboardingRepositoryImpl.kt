package com.example.data.features.dbLocal.datastore.repository

import com.example.domain.repository.dbLocal.datastore.AppOnboardingRepository
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.data.features.dbLocal.datastore.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AppOnboardingRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : AppOnboardingRepository {

    override val isOnboardingCompleted: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.IS_ONBOARDING_COMPLETED] ?: false
        }

    override suspend fun setOnboardingCompleted(isCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_ONBOARDING_COMPLETED] = isCompleted
        }
    }
}