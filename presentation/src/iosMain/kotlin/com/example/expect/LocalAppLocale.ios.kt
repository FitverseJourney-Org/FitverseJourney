package com.example.expect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual object LocalAppLocale {

    private val LocalAppLocale = staticCompositionLocalOf {
        NSLocale.currentLocale.languageCode ?: "en"
    }

    actual val current: String
        @Composable get() = LocalAppLocale.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val lang = value ?: NSLocale.currentLocale.languageCode ?: "en"
        return LocalAppLocale.provides(lang)
    }
}