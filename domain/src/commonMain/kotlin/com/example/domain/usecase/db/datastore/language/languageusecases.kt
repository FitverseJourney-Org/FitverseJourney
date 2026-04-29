package com.example.domain.usecase.db.datastore.language

import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/** Observa o idioma atual como Flow. */
class GetAppLanguageUseCase(
    private val repository: AppLanguageRepository
) {
    operator fun invoke(): Flow<String> = repository.languageCode
}

/** Retorna o idioma salvo uma única vez (suspend). */
class GetCurrentLanguageUseCase(
    private val repository: AppLanguageRepository
) {
    suspend operator fun invoke(): String = repository.languageCode.first()
}

/** Persiste o novo idioma escolhido pelo usuário. */
class ChangeAppLanguageUseCase(
    private val repository: AppLanguageRepository
) {
    suspend operator fun invoke(languageCode: String) {
        repository.setLanguageCode(languageCode)
    }
}

/** Retorna o locale do sistema operacional. */
class GetSystemLocaleUseCase(
    private val repository: AppLanguageRepository
) {
    operator fun invoke(): String = repository.getSystemLocale()
}
