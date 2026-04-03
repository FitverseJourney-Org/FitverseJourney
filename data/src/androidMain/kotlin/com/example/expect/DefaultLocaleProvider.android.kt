package com.example.expect

import java.util.Locale

actual fun getDefaultLocale(): String {
    return Locale.getDefault().toString()
}