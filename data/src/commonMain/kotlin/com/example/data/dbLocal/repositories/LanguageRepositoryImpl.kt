package com.example.data.dbLocal.repositories

import com.example.domain.model.dbLocal.language.TagLanguage
import com.example.domain.repository.LanguageRepository
import com.example.expect.PlatformKeyValueStorage
import com.example.expect.getDefaultLocale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LanguageRepositoryImpl(
    private val storage: PlatformKeyValueStorage
) : LanguageRepository {

    private val LANGUAGE_KEY = "languageCode"

    override val languageFlow: Flow<TagLanguage> =
        storage.observeString(LANGUAGE_KEY, TagLanguage.SYSTEM.iso)
            .map { stored ->
                if (stored == TagLanguage.SYSTEM.iso) {
                    TagLanguage.fromIso(getDefaultLocale())
                } else {
                    TagLanguage.fromIso(stored)
                }
            }

    override suspend fun getLanguageOnce(): TagLanguage {
        val stored = storage.getString(LANGUAGE_KEY, TagLanguage.SYSTEM.iso)
        return if (stored == TagLanguage.SYSTEM.iso) {
            TagLanguage.fromIso(getDefaultLocale())
        } else {
            TagLanguage.fromIso(stored)
        }
    }

    override suspend fun setLanguage(language: TagLanguage) {
        storage.putString(LANGUAGE_KEY, language.iso)
    }
}