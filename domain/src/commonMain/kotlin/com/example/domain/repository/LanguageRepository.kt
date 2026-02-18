package com.example.domain.repository

import com.example.domain.model.dbLocal.language.TagLanguage
import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    val languageFlow: Flow<TagLanguage> // observe
    suspend fun setLanguage(language: TagLanguage)
    suspend fun getLanguageOnce(): TagLanguage
}