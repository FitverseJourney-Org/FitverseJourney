package org.fitverse.presentation.utils

import org.fitverse.domain.models.language.AppLanguageItem
import org.fitverse.domain.models.language.TagLanguage
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.locale_de
import fitversejourneyapp.presentation.generated.resources.locale_en
import fitversejourneyapp.presentation.generated.resources.locale_es
import fitversejourneyapp.presentation.generated.resources.locale_fr
import fitversejourneyapp.presentation.generated.resources.locale_ko
import fitversejourneyapp.presentation.generated.resources.locale_pt
import fitversejourneyapp.presentation.generated.resources.locale_ru

class LanguageAvailableApp {
    companion object {

        val availableAppLanguageItems: List<AppLanguageItem> = listOf(
            AppLanguageItem(
                code = TagLanguage.ENGLISH,
                name = "English",
                flagRes = Res.drawable.locale_en
            ),
            AppLanguageItem(
                code = TagLanguage.GERMAN,
                name = "Deutsch",
                flagRes = Res.drawable.locale_de
            ),
            AppLanguageItem(
                code = TagLanguage.SPANISH,
                name = "Español",
                flagRes = Res.drawable.locale_es
            ),
            AppLanguageItem(
                code = TagLanguage.FRENCH,
                name = "Français",
                flagRes = Res.drawable.locale_fr
            ),
            AppLanguageItem(
                code = TagLanguage.KOREAN,
                name = "한국어",
                flagRes = Res.drawable.locale_ko
            ),
            AppLanguageItem(
                code = TagLanguage.PORTUGUESE,
                name = "Português",
                flagRes = Res.drawable.locale_pt
            ),
            AppLanguageItem(
                code = TagLanguage.RUSSIAN,
                name = "Русский",
                flagRes = Res.drawable.locale_ru
            )
        )
    }
    fun fromCode(code: String): AppLanguageItem {
        val tag = TagLanguage.fromIso(code)

        // Log para depurar
        println("Buscando idioma para o código: $code | Tag resolvida: ${tag.name} | ISO da Tag: ${tag.iso}")

        return availableAppLanguageItems.find { it.code.iso == tag.iso }
            ?: availableAppLanguageItems.first().also { println("Idioma não encontrado, usando fallback: ${it.name}") }
    }
}