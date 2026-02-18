package com.example.expect

import com.example.domain.repository.authentication.AuthTokenStoreRepository
import io.ktor.client.HttpClient

expect fun createHttpClient(tokenStore: AuthTokenStoreRepository): HttpClient

