package com.example.expect

import kotlinx.coroutines.flow.Flow

expect class PlatformKeyValueStorage {
    fun observeString(key: String, defaultValue: String): Flow<String>
    suspend fun getString(key: String, defaultValue: String): String
    suspend fun putString(key: String, value: String)
    suspend fun remove(key: String)
}