package com.example.domain.usecase.database.datastore.language

import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository

class SetNewAppLanguageUseCase(
    private val repository: AppLanguageRepository
) {
    suspend operator fun invoke(languageCode: String) {
        repository.setLanguageCode(languageCode)
    }
}