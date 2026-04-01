package com.example.domain.usecase.database.datastore.language

import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository

class SetAppLanguageUseCase(
    private val repository: AppLanguageRepository
) {
    suspend operator fun invoke(language: String) {
        repository.setAppLanguage(language)
    }
}