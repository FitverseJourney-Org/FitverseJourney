package com.example.domain.usecase.database.datastore.language

import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository

class GetAppLanguageUseCase(
    private val repository: AppLanguageRepository
) {
    suspend operator fun invoke(): String {
        // Se retornar nulo (primeira vez), definimos "en" como padrão
        return repository.getCurrentLanguageCode()
    }
}