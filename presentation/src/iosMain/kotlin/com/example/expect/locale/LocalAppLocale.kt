package com.example.expect.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

actual object LocalAppLocale {
    private var defaultLocale = (NSLocale.preferredLanguages.firstOrNull() as? String) ?: "en"

    private val LocalAppLocale = staticCompositionLocalOf { defaultLocale }
    actual val current: String
        @Composable
        get() = LocalAppLocale.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val newLocale = value ?: defaultLocale

        if(value == null) {
            NSUserDefaults.standardUserDefaults.removeObjectForKey("AppleLanguages")
        } else {
            NSUserDefaults.standardUserDefaults.setObject(
                listOf(value),
                "AppleLanguages"
            )
        }

        return LocalAppLocale provides newLocale
    }
}