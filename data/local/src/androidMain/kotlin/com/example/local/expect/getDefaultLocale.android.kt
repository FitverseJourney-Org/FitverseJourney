package com.example.local.expect

import java.util.Locale

actual fun getDefaultLocale(): String {
    // Retorna o código ISO 639 de 2 letras da linguagem atual do Android
    return Locale.getDefault().language
}