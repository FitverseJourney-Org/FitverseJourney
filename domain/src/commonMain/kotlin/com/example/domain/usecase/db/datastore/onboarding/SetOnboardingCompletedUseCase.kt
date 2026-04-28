package com.example.domain.usecase.db.datastore.onboarding

import com.example.domain.repository.dbLocal.datastore.AppOnboardingRepository

class SetOnboardingCompletedUseCase(
    private val repository: AppOnboardingRepository
) {
    suspend operator fun invoke(isCompleted: Boolean) {
        repository.setOnboardingCompleted(isCompleted)
    }
}