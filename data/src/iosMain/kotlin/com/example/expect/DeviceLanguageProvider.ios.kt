package com.example.expect

import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages

actual object DeviceLanguageProvider {
    actual fun getSystemLanguage(): String {
        val lang = NSLocale.preferredLanguages.firstOrNull() as? String
        return lang?.substringBefore("-") ?: "en"
    }
}