package com.example.local.expect

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun getDefaultLocale(): String {
    // Retorna o código de linguagem atual do iOS.
    // Caso seja nulo por algum motivo bizarro do sistema, criamos um fallback para "en"
    return NSLocale.currentLocale.languageCode ?: "en"
}