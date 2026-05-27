package org.fitverse.domain.usecase.db.datastore.authentication

import org.fitverse.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import kotlinx.coroutines.flow.Flow

class ObserveIsAuthenticatedUseCase(
    private val repository: AppAuthenticateRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.isAuthenticated
    }
}