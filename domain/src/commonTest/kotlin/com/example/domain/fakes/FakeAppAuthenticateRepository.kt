package org.fitverse.domain.fakes

import org.fitverse.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAppAuthenticateRepository : AppAuthenticateRepository {

    private val _isAuthenticated = MutableStateFlow(false)
    private var _token: String? = null

    override val isAuthenticated: Flow<Boolean> = _isAuthenticated

    override suspend fun setIsAuthenticated(isAuthenticated: Boolean) {
        _isAuthenticated.value = isAuthenticated
    }

    override suspend fun saveToken(token: String) {
        _token = token
    }

    override fun getToken(): String? = _token
}
