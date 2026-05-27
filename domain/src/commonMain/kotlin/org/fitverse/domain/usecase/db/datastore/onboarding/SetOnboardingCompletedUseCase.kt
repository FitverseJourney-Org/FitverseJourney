package org.fitverse.domain.usecase.db.datastore.onboarding

import org.fitverse.domain.repository.dbLocal.datastore.AppOnboardingRepository

class SetOnboardingCompletedUseCase(
    private val repository: AppOnboardingRepository
) {
    suspend operator fun invoke(isCompleted: Boolean) {
        repository.setOnboardingCompleted(isCompleted)
    }
}