package com.example.data.features.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.data.features.cache.CacheDataSourceImpl.Companion.KEY_ONBOARDING_COMPLETED
import com.example.domain.repository.cache.CacheDataSourceOnboarding
import kotlinx.coroutines.flow.first
import kotlinx.io.IOException

class CacheDataSourceKeyOnboardingCompletedImpl(
    private val dataStore: DataStore<Preferences>
) : CacheDataSourceOnboarding {

    override suspend fun setOnboardingCompleted(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_ONBOARDING_COMPLETED] = value
        }
    }

    override suspend fun isOnboardingCompleted(): Boolean {
        return try {
            val prefs = dataStore.data.first()
            prefs[KEY_ONBOARDING_COMPLETED] ?: false
        } catch (e: IOException) {
            false
        }
    }
}