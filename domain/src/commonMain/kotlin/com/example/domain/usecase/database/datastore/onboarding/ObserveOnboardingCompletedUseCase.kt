package com.example.domain.usecase.database.datastore.onboarding

import com.example.domain.repository.dbLocal.datastore.AppOnboardingRepository
import kotlinx.coroutines.flow.Flow

class ObserveOnboardingCompletedUseCase(
    private val repository: AppOnboardingRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.isOnboardingCompleted
    }
}