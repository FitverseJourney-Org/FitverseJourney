package com.example.expect

import com.example.domain.repository.authentication.AuthTokenStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual fun createHttpClient(tokenStore: AuthTokenStoreRepository): HttpClient {
    return HttpClient(Darwin){
        engine {
            configureRequest {
                setAllowsCellularAccess(true)
            }
        }
    }
}