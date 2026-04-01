package com.example.presentation.core.utils

import com.example.domain.model.local.language.Language
import com.example.domain.model.local.language.TagLanguage
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.locale_de
import fitversejourneyapp.presentation.generated.resources.locale_en
import fitversejourneyapp.presentation.generated.resources.locale_es
import fitversejourneyapp.presentation.generated.resources.locale_fr
import fitversejourneyapp.presentation.generated.resources.locale_ko
import fitversejourneyapp.presentation.generated.resources.locale_pt
import fitversejourneyapp.presentation.generated.resources.locale_ru

object LanguageAvailableApp {

    val availableLanguages: List<Language> = listOf(
        Language(
            code = TagLanguage.ENGLISH,
            name = "English",
            flagRes = Res.drawable.locale_en
        ),
        Language(
            code = TagLanguage.GERMAN,
            name = "Deutsch",
            flagRes = Res.drawable.locale_de
        ),
        Language(
            code = TagLanguage.SPANISH,
            name = "Español",
            flagRes = Res.drawable.locale_es
        ),
        Language(
            code = TagLanguage.FRENCH,
            name = "Français",
            flagRes = Res.drawable.locale_fr
        ),
        Language(
            code = TagLanguage.KOREAN,
            name = "한국어",
            flagRes = Res.drawable.locale_ko
        ),
        Language(
            code = TagLanguage.PORTUGUESE,
            name = "Português",
            flagRes = Res.drawable.locale_pt
        ),
        Language(
            code = TagLanguage.RUSSIAN,
            name = "Русский",
            flagRes = Res.drawable.locale_ru
        )
    )


    fun fromCode(code: String): Language {
        val tag = TagLanguage.fromIso(code)

        // Log para depurar
        println("Buscando idioma para o código: $code | Tag resolvida: ${tag.name} | ISO da Tag: ${tag.iso}")

        return availableLanguages.find { it.code.iso == tag.iso }
            ?: availableLanguages.first().also { println("Idioma não encontrado, usando fallback: ${it.name}") }
    }
}