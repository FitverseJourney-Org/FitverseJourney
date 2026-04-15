package com.example.data.features.auth.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.data.database.datastore.PreferencesKeys
import com.example.domain.repository.authentication.AuthTokenStoreRepository
import kotlinx.coroutines.flow.first

class AuthTokenStoreImpl(
    val datastore: DataStore<Preferences>
): AuthTokenStoreRepository {

    override suspend fun saveToken(token: String?) {
        datastore.edit {
            it[PreferencesKeys.USER_TOKEN] = token ?: ""
        }
    }

    override suspend fun getToken(): String{
        val preferences = datastore.data.first()
        return preferences[PreferencesKeys.USER_TOKEN] ?: ""
    }

    override suspend fun clearToken() {
        datastore.edit {
            it.remove(PreferencesKeys.USER_TOKEN)
        }
    }
}