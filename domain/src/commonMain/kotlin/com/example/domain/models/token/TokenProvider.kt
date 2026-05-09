package com.example.domain.models.token

interface TokenProvider {
    fun getToken(): String?
    fun saveToken(token: String)
    fun clearToken()
}