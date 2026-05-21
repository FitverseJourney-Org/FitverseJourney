package org.fitverse.domain.usecase.db.datastore.authentication

import org.fitverse.domain.repository.dbLocal.datastore.AppAuthenticateRepository

class SetIsAuthenticatedUseCase(
    private val repository: AppAuthenticateRepository
) {
    suspend operator fun invoke(isAuthenticated: Boolean) {
        repository.setIsAuthenticated(isAuthenticated)
    }
}