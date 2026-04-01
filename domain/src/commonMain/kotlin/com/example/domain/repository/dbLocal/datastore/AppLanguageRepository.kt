package com.example.domain.repository.dbLocal.datastore

import com.example.domain.model.local.language.Language
import com.example.domain.model.local.language.TagLanguage
import kotlinx.coroutines.flow.Flow

interface AppLanguageRepository {
    val appLanguage: Flow<String>
    suspend fun setAppLanguage(language: String)
}