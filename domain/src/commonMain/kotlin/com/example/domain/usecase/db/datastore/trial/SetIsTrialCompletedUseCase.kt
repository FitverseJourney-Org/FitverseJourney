package org.fitverse.domain.usecase.db.datastore.trial

import org.fitverse.domain.repository.dbLocal.datastore.AppTrialRepository

class SetIsTrialCompletedUseCase(
    private val repository: AppTrialRepository
) {
    suspend operator fun invoke(isTrialCompleted: Boolean) {
        repository.setIsTrialCompleted(isTrialCompleted)
    }
}
