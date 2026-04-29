package com.example.local.database.datastore.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository
import com.example.local.database.datastore.PreferencesKeys.APP_LANGUAGE
import com.example.local.expect.getDefaultLocale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class AppLanguageRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : AppLanguageRepository {

    override val languageCode: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[APP_LANGUAGE] ?: getSystemLocale()
        }

    override suspend fun setLanguageCode(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[APP_LANGUAGE] = languageCode
        }
    }

    override fun getLanguageNameByCode(languageCode: String): String =
        when (languageCode.lowercase()) {
            "pt" -> "Português"
            "en" -> "English"
            "es" -> "Español"
            "de" -> "Deutsch"
            "fr" -> "Français"
            "ko" -> "한국어"
            "ru" -> "Русский"
            else -> "Unknown"
        }

    // Implementação expect/actual — cada plataforma retorna o locale do SO
    override fun getSystemLocale(): String = getDefaultLocale()
}