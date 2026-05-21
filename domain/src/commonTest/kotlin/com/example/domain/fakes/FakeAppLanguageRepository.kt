package org.fitverse.domain.fakes

import org.fitverse.domain.repository.dbLocal.datastore.AppLanguageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAppLanguageRepository(
    initialCode: String = "pt"
) : AppLanguageRepository {

    private val _languageCode = MutableStateFlow(initialCode)

    override val languageCode: Flow<String> = _languageCode

    override suspend fun setLanguageCode(languageCode: String) {
        _languageCode.value = languageCode
    }

    override fun getLanguageNameByCode(languageCode: String): String = languageCode

    override fun getSystemLocale(): String = "pt"
}
