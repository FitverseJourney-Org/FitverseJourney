package org.fitverse.data.repository.util

import org.fitverse.data.local.datasource.token.TokenStorageImpl
import org.fitverse.data.remote.datasource.HttpClientFactory
import io.ktor.client.*
import org.koin.dsl.module

val networkModule = module {

    single<HttpClient> {
        val tokenStorageImpl = get<TokenStorageImpl>()

        HttpClientFactory.create(
            enableLogging = true,

            // ✅ retorna o token diretamente
            tokenProvider = {
                tokenStorageImpl.getToken()
            },

            // ✅ limpa sessão Firebase no 401
            onUnauthorized = {
                tokenStorageImpl.clearToken()
                // get<SessionEventBus>().emitLogout()
            }
        )
    }
}

