package com.example.domain.usecase.datastore

import com.example.domain.model.dbLocal.language.Language
import com.example.domain.model.dbLocal.language.TagLanguage
import com.example.domain.repository.LanguageRepository
import kotlinx.coroutines.flow.Flow


/**
 * Use case responsável por operações relacionadas ao idioma do app.
 * - observeLanguage(): fluxo contínuo que emite o idioma atual e atualizações.
 * - getLanguage(): leitura pontual (suspensa).
 * - setLanguage(): grava o idioma.
 */
class LanguageUseCase(
    private val languageRepository: LanguageRepository // com assinatura usando LanguageTag
) {
    // observe
    fun observeLanguage(): Flow<TagLanguage> = languageRepository.languageFlow

    // leitura pontual
    suspend fun getLanguage(): TagLanguage = languageRepository.getLanguageOnce()

    suspend fun setLanguage(language: TagLanguage) {
        languageRepository.setLanguage(language)
    }
}