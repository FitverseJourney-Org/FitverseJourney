package com.example.domain.usecase.db.datastore.authentication

import com.example.domain.repository.dbLocal.datastore.AppAuthenticateRepository

class SetIsAuthenticatedUseCase(
    private val repository: AppAuthenticateRepository
) {
    suspend operator fun invoke(isAuthenticated: Boolean) {
        repository.setIsAuthenticated(isAuthenticated)
    }
}