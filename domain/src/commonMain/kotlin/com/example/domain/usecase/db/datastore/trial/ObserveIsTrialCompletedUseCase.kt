package org.fitverse.domain.usecase.db.datastore.trial

import org.fitverse.domain.repository.dbLocal.datastore.AppTrialRepository
import kotlinx.coroutines.flow.Flow

class ObserveIsTrialCompletedUseCase(
    private val repository: AppTrialRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.isTrialCompleted
}