package com.example.presentation.core.utils

import com.example.domain.model.dbLocal.language.Language
import com.example.domain.model.dbLocal.language.TagLanguage
import fitversejorneyapp.presentation.generated.resources.Res
import fitversejorneyapp.presentation.generated.resources.locale_de
import fitversejorneyapp.presentation.generated.resources.locale_en
import fitversejorneyapp.presentation.generated.resources.locale_es
import fitversejorneyapp.presentation.generated.resources.locale_fr
import fitversejorneyapp.presentation.generated.resources.locale_ko
import fitversejorneyapp.presentation.generated.resources.locale_pt
import fitversejorneyapp.presentation.generated.resources.locale_ru

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
}