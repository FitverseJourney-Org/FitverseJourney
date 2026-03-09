package com.example.expect

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual fun httpClientCore(): HttpClient {
    return HttpClient(OkHttp) {
        // Serialização JSON
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true    // tolera campos extras
                    explicitNulls = false
                    prettyPrint = true
                }
            )
        }

        // Timeouts
        install(HttpTimeout) {
            connectTimeoutMillis = 10_000L
            socketTimeoutMillis  = 15_000L
            requestTimeoutMillis = 20_000L
        }

        // Logging (use com cuidado em produção)
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }

        // Retry com backoff simples
        install(HttpRequestRetry) {
            maxRetries = 2
            retryOnServerErrors()
            exponentialDelay()
        }

        // Cabeçalhos default
        defaultRequest {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }

//         Exemplo de instalação de Auth bearer (opcional)
         install(Auth) {
             bearer {
                 loadTokens {
                     BearerTokens(accessToken = "token", refreshToken = "refresh")
                 }
                 refreshTokens {
                     // lógica de refresh -> retornar novos BearerTokens
                     BearerTokens(accessToken = "newToken", refreshToken = "newRefresh")
                 }
             }
         }

        // Configurações específicas do engine (OkHttp)
        engine {
            // Ex.: habilitar connection pooling, configurar dispatcher, timeouts adicionais, proxy, etc.
            // configureDefaultClient { ... } // OkHttpClient.Builder ops aqui se necessário
        }

        // Se desejar que o HttpClient não lance exceção em status != 2xx:
        expectSuccess = false
    }
}