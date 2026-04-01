package com.example.data.features.dbLocal.datastore.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.data.features.dbLocal.datastore.PreferencesKeys
import com.example.domain.model.local.language.Language
import com.example.domain.model.local.language.TagLanguage
import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository
import com.example.expect.DeviceLanguageProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AppLanguageRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : AppLanguageRepository {

    private val deviceSystemLanguage = DeviceLanguageProvider.getSystemLanguage()

    override val appLanguage: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.APP_LANGUAGE] ?: deviceSystemLanguage
        }

    override suspend fun setAppLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_LANGUAGE] = language
        }
    }
}