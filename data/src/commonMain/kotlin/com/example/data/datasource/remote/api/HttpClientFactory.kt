package com.example.data.datasource.remote.api

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


// ============================================================
// KTOR HTTP CLIENT CONFIGURATION
// ============================================================

/**
 * Factory para criar e configurar o HttpClient
 */
object HttpClientFactory {

    fun create(
        enableLogging: Boolean = true,
        authTokenProvider: () -> String? = { null }
    ): HttpClient {
        return HttpClient {
            // ============================================================
            // 1. Content Negotiation (JSON)
            // ============================================================
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }

            // ============================================================
            // 2. Logging
            // ============================================================
            if (enableLogging) {
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL

                    // Customização de logs
                    logger = object : Logger {
                        override fun log(message: String) {
                            println("🌐 HTTP Client: $message")
                        }
                    }
                }
            }

            // ============================================================
            // 3. Default Request (headers comuns)
            // ============================================================
            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)

                // Adiciona auth token se disponível
                authTokenProvider()?.let { token ->
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }

            // ============================================================
            // 4. Timeout Configuration
            // ============================================================
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000      // 30 segundos
                connectTimeoutMillis = 15_000      // 15 segundos
                socketTimeoutMillis = 15_000       // 15 segundos
            }

            // ============================================================
            // 5. Response Observer (para debugging)
            // ============================================================
            install(ResponseObserver) {
                onResponse { response ->
                    println("📥 Response: ${response.status.value} ${response.status.description}")
                }
            }

            // ============================================================
            // 6. HTTP Call Validator (tratamento de erros)
            // ============================================================
            HttpResponseValidator {
                validateResponse { response ->
                    when (response.status.value) {
                        in 400..499 -> {
                            throw ClientRequestException(
                                response,
                                "Client error: ${response.status.description}"
                            )
                        }
                        in 500..599 -> {
                            throw ServerResponseException(
                                response,
                                "Server error: ${response.status.description}"
                            )
                        }
                    }
                }

                handleResponseExceptionWithRequest { exception, _ ->
                    when (exception) {
                        is ClientRequestException -> {
                            println("❌ Client error: ${exception.message}")
                        }
                        is ServerResponseException -> {
                            println("❌ Server error: ${exception.message}")
                        }
                        is HttpRequestTimeoutException -> {
                            println("⏱️ Request timeout")
                        }
                        else -> {
                            println("❌ Unknown error: ${exception.message}")
                        }
                    }
                    throw exception
                }
            }
        }
    }
}