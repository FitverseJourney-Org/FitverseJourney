package com.example.domain.repository.dbLocal.datastore

import kotlinx.coroutines.flow.Flow

interface AppTrialRepository {
    val isTrialCompleted: Flow<Boolean>
    suspend fun setIsTrialCompleted(isTrialCompleted: Boolean)
}
