package com.example.domain.usecase.db.datastore.language

import com.example.domain.models.local.language.TagLanguage.Companion.listOfLanguages
import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository

class ChangeAppLanguageUseCase(
    private val repository: AppLanguageRepository
) {
    suspend operator fun invoke(newLanguageCode: String): Result<Unit> {
        val supportedLanguages = listOfLanguages

        return if (supportedLanguages.contains(newLanguageCode.lowercase())) {
            repository.setLanguageCode(newLanguageCode)
            Result.success(Unit)
        } else {
            Result.failure(Exception("Idioma não suportado: $newLanguageCode"))
        }
    }
}