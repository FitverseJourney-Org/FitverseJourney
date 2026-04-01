package com.example.domain.repository.dbLocal.datastore

import kotlinx.coroutines.flow.Flow

interface AppOnboardingRepository {
    // Onboarding
    val isOnboardingCompleted: Flow<Boolean>
    suspend fun setOnboardingCompleted(isCompleted: Boolean)
}