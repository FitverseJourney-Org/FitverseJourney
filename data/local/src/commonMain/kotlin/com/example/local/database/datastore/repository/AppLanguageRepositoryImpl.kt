package com.example.local.database.datastore.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.local.database.datastore.PreferencesKeys.APP_LANGUAGE
import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository
import com.example.local.expect.getDefaultLocale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppLanguageRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : AppLanguageRepository {

    // Agora é um Flow puro. Sempre que o DataStore mudar, ele emite aqui!
    override val languageCode: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[APP_LANGUAGE] ?: getDefaultLocale()
        }

    override suspend fun setLanguageCode(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[APP_LANGUAGE] = languageCode
        }
    }

    // nao esta usando
    override suspend fun changeLanguageCode(languageCode: String) {
        setLanguageCode(languageCode)
    }

    override suspend fun getCurrentLanguageCode(): String {
        return languageCode.first()
    }

    override fun getLocale(): String {
        return getDefaultLocale()
    }

    // nao esta usando
    override fun getLanguageNameByCode(languageCode: String): String {
        return when (languageCode.lowercase()) {
            "pt" -> "Português"
            "en" -> "English"
            "es" -> "Español"
            else -> "Unknown"
        }
    }
}