package com.example.remote.util

import com.example.remote.datasource.HttpClientFactory
import io.ktor.client.*
import org.koin.dsl.module

val networkModule = module {

    single<HttpClient> {

        HttpClientFactory.create(
            enableLogging = true,

            // ✅ lê o token do Keychain/EncryptedPrefs a cada request
            tokenProvider = {
//                tokenStorage.getToken()
                ""
            },

            // ✅ chamado quando API retorna 401
            onUnauthorized = {
//                tokenStorage.clearToken()
                // Opcional: emitir evento global de logout via SharedFlow
                // get<SessionEventBus>().emitLogout()
            }
        )
    }
}

