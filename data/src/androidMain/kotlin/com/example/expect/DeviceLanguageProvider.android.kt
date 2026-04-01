package com.example.expect

import java.util.Locale

actual object DeviceLanguageProvider {
    actual fun getSystemLanguage(): String {
        return Locale.getDefault().language
    }
}