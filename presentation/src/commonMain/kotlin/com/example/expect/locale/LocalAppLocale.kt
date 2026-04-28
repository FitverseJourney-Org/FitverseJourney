package com.example.expect.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf


// Fornece e gerencia o locale (idioma/região) do app
// via Composition Local do Compose
expect object LocalAppLocale {
    // "pt_BR"
    val current: String @Composable get

    // injeta locale brasileiro na árvore do Compose
    @Composable
    infix fun provides(value: String?) : ProvidedValue<*>
}



