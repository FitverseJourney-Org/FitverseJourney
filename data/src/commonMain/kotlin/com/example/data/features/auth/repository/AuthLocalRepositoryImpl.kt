package com.example.data.features.auth.repository

import com.example.data.features.auth.local.AuthLocalDataSource
import com.example.domain.repository.authentication.AuthTokenStoreRepository

class AuthLocalRepositoryImpl(
    private val local: AuthLocalDataSource,
    private val tokenStore: AuthTokenStoreRepository
) {

}