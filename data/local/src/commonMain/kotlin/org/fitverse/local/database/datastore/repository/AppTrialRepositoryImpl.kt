package org.fitverse.data.local.database.datastore.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import org.fitverse.domain.repository.dbLocal.datastore.AppTrialRepository
import org.fitverse.data.local.database.datastore.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AppTrialRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : AppTrialRepository {

    override val isTrialCompleted: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.IS_TRIAL_COMPLETED] ?: false
        }

    override suspend fun setIsTrialCompleted(isTrialCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_TRIAL_COMPLETED] = isTrialCompleted
        }
    }
}