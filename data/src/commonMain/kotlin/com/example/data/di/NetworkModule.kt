package com.example.data.di

import com.example.domain.repository.authentication.AuthTokenStoreRepository
import com.example.expect.createHttpClient
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        val tokenStore: AuthTokenStoreRepository = get()
        createHttpClient(tokenStore)
    }
}