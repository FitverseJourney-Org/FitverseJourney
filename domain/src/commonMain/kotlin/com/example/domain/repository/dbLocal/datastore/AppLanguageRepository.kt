package com.example.domain.repository.dbLocal.datastore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AppLanguageRepository {

    /** Emite o código do idioma salvo sempre que ele mudar. */
    val languageCode: Flow<String>

    /** Persiste o novo código de idioma. */
    suspend fun setLanguageCode(languageCode: String)

    /** Retorna o nome legível de um código (ex: "pt" → "Português"). */
    fun getLanguageNameByCode(languageCode: String): String

    /** Retorna o locale do sistema operacional na primeira execução. */
    fun getSystemLocale(): String
}
