package com.example.domain.repository.dbLocal.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


interface AppLanguageRepository {
    val languageCode: Flow<String>

    suspend fun setLanguageCode(languageCode: String)
    suspend fun changeLanguageCode(languageCode: String)
    fun getLanguageNameByCode(languageCode: String): String
    suspend fun getCurrentLanguageCode(): String
    fun getLocale(): String
}