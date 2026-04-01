package com.example.domain.usecase.database.datastore.language

import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository
import kotlinx.coroutines.flow.Flow

class ObserveAppLanguageUseCase(
    private val repository: AppLanguageRepository
) {
    operator fun invoke(): Flow<String> {
        return repository.appLanguage
    }
}