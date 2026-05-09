package com.example.remote.datasource

import io.ktor.client.*
import io.ktor.client.engine.HttpClientEngineFactory
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
object HttpClientFactory {

    fun create(
        enableLogging   : Boolean      = true,
        tokenProvider   : () -> String? = { null },   // lido a cada request
        onUnauthorized  : () -> Unit    = {}           // callback para logout
    ): HttpClient {

        val client = HttpClient {

            // ── 1. Content Negotiation ────────────────────────────────────────
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient         = true
                    coerceInputValues = true
                    explicitNulls     = false
                })
            }

            // ── 2. Logging ────────────────────────────────────────────────────
            if (enableLogging) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            println("🌐 HTTP Client: $message")
                        }
                    }
                    level = LogLevel.ALL
                }
            }

            // ── 3. Default Request (headers fixos) ────────────────────────────
            defaultRequest {
                contentType(ContentType.Application.Json)
                // ⚠️ NÃO adicionar token aqui — seria lido só na criação do client
            }

            // ── 4. Timeout ────────────────────────────────────────────────────
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis  = 15_000
            }

            // ── 5. Response Observer ──────────────────────────────────────────
            install(ResponseObserver) {
                onResponse { response ->
                    println("📥 Response: ${response.status.value} ${response.status.description}")
                }
            }

            // ── 6. HTTP Response Validator ────────────────────────────────────
            HttpResponseValidator {
                validateResponse { response ->
                    when (response.status.value) {
                        in 400..499 -> throw ClientRequestException(
                            response, "Client error: ${response.status.description}"
                        )
                        in 500..599 -> throw ServerResponseException(
                            response, "Server error: ${response.status.description}"
                        )
                    }
                }

                handleResponseExceptionWithRequest { exception, _ ->
                    when (exception) {
                        is ClientRequestException    -> println("❌ Client error: ${exception.message}")
                        is ServerResponseException   -> println("❌ Server error: ${exception.message}")
                        is HttpRequestTimeoutException -> println("⏱️ Request timeout")
                        else                         -> println("❌ Unknown error: ${exception.message}")
                    }
                    throw exception
                }
            }
        }

        // ── 7. Auth Interceptor — token lido a cada request ───────────────────
        // Instalado APÓS a criação do client para ter acesso ao plugin HttpSend
        client.plugin(HttpSend).intercept { request ->

            // Lê o token fresco a cada requisição
            val token = tokenProvider()
            if (token != null) {
                request.headers.append(HttpHeaders.Authorization, "Bearer $token")
            }

            val call = execute(request)

            // 401 = token inválido/expirado → dispara logout global
            if (call.response.status.value == 401) {
                println("🔒 Token expirado ou inválido — fazendo logout")
                onUnauthorized()
            }

            call
        }

        return client
    }
}