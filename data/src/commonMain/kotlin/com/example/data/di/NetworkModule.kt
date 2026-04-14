package com.example.data.di

import com.example.data.datasource.local.cache.PreferencesDataSource
import com.example.data.datasource.remote.api.HttpClientFactory
import com.example.data.util.NetworkMonitor
import io.ktor.client.*
import org.koin.dsl.module

val networkModule = module {
    // HttpClient (singleton)
    single<HttpClient> {
        HttpClientFactory.create(
            enableLogging = true,
            authTokenProvider = {
                // Busca token do PreferencesDataSource
                // Pode ser null se não tiver token
                null // get<PreferencesDataSource>().getAuthToken() - seria assíncrono
            }
        )
    }

    // NetworkMonitor (platform-specific)
    // Será fornecido por cada plataforma
}