package com.example.domain.repository.dbLocal.datastore

import kotlinx.coroutines.flow.Flow

interface AppPreferencesRepository {
    // Autenticação
    val isAuthenticated: Flow<Boolean>
    suspend fun setIsAuthenticated(isAuthenticated: Boolean)

    // Onboarding
    val isOnboardingCompleted: Flow<Boolean>
    suspend fun setOnboardingCompleted(isCompleted: Boolean)

    // Idioma (Language)
    val appLanguage: Flow<String>
    suspend fun setAppLanguage(languageCode: String)
}