package com.example.expect.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import kotlin.toString

actual object LocalAppLocale {
    private var defaultLocale: Locale? = null
    actual val current: String
        @Composable
        get() = Locale.getDefault().toString()

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val configuration = LocalConfiguration.current

        if(defaultLocale == null) {
            defaultLocale = Locale.getDefault()
        }

        val newLocale = if(value == null) {
            defaultLocale!!
        } else {
            Locale.forLanguageTag(value)
        }
        Locale.setDefault(newLocale)
        configuration.setLocale(newLocale)

        val context = LocalContext.current
        context.createConfigurationContext(configuration)

        return LocalConfiguration provides configuration
    }
}
