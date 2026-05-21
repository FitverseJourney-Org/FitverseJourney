package org.fitverse.data.local.database.datastore.repository

import androidx.datastore.core.IOException
import org.fitverse.data.local.database.datastore.PreferencesKeys
import org.fitverse.data.local.datasource.token.TokenStorageImpl
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import org.fitverse.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AppAuthenticateRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val tokenStorageImpl: TokenStorageImpl
) : AppAuthenticateRepository {

    override val isAuthenticated: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            val flagIsTrue  = preferences[PreferencesKeys.IS_AUTHENTICATED] ?: false
            val tokenExists = tokenStorageImpl.hasToken()
            flagIsTrue && tokenExists  // ambos precisam ser true
        }

    override suspend fun setIsAuthenticated(isAuthenticated: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_AUTHENTICATED] = isAuthenticated
        }
        if (!isAuthenticated) {
            tokenStorageImpl.clearToken()  // limpa o JWT junto ao logout
        }
    }

    override suspend fun saveToken(token: String) {
        tokenStorageImpl.saveToken(token)
    }

    override fun getToken(): String? {
        return tokenStorageImpl.getToken()
    }
}