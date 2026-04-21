package com.example.remote.util

import com.example.remote.datasource.HttpClientFactory
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