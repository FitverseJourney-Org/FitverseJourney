package com.example.expect

import android.content.res.Resources
import android.os.Build

actual fun getDefaultLocale(): String {
    val configuration = Resources.getSystem().configuration
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        configuration.locales.get(0).toLanguageTag()
    } else {
        configuration.locale.toLanguageTag()
    }
}
