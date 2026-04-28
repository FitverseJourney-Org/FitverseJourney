package com.example.domain.usecase.db.datastore.language

import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository

class GetLocaleLanguageAppUseCase(
    private val repository: AppLanguageRepository
) {
    operator fun invoke(): String {
        return repository.getLocale()
    }
}