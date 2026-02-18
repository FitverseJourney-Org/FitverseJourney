package com.example.domain.repository

import kotlinx.coroutines.flow.Flow

interface KeyValueStorageRepository {
    fun observeString(key: String, defaultValue: String): Flow<String>
    suspend fun getStringOnce(key: String, defaultValue: String): String
    suspend fun putString(key: String, value: String)
    suspend fun remove(key: String)
}