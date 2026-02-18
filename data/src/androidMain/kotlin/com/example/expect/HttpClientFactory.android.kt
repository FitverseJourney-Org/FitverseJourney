package com.example.expect

import com.example.domain.repository.authentication.AuthTokenStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LoggingFormat
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.logging.Logger

actual fun createHttpClient(tokenStore: AuthTokenStoreRepository): HttpClient {
    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                }
            )
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = tokenStore.getToken()
                    if (accessToken != null) {
                        BearerTokens(accessToken, refreshToken = "")
                    } else {
                        null
                    }
                }
                // refreshTokens { ... } // se precisar
            }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }

        install(Logging){
            level = LogLevel.BODY
            format = LoggingFormat.Default
        }
    }
    return client
}