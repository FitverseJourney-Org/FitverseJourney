package com.example.domain.usecase.db.datastore.trial

import com.example.domain.repository.dbLocal.datastore.AppTrialRepository

class SetIsTrialCompletedUseCase(
    private val repository: AppTrialRepository
) {
    suspend operator fun invoke(isTrialCompleted: Boolean) {
        repository.setIsTrialCompleted(isTrialCompleted)
    }
}
