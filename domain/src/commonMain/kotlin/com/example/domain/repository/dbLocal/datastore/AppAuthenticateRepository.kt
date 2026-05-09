package com.example.domain.repository.dbLocal.datastore

import kotlinx.coroutines.flow.Flow

interface AppAuthenticateRepository {
    val isAuthenticated: Flow<Boolean>
    suspend fun setIsAuthenticated(isAuthenticated: Boolean)

    suspend fun saveToken(token: String)  // chama após login bem-sucedido
    fun getToken(): String?               // Retrofit usa no AuthInterceptor
}