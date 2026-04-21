package com.example.domain.expect

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

actual fun Double.formatPercent(): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle
        maximumFractionDigits = 2u // Máximo de 2 casas
        minimumFractionDigits = 0u // Oculta se for zero
    }
    // O cast para NSNumber é necessário para a API do iOS
    val formattedString = formatter.stringFromNumber(NSNumber(this)) ?: this.toString()
    return "$formattedString%"
}

actual fun Float.formatPercent(): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle
        maximumFractionDigits = 2u
        minimumFractionDigits = 0u
    }
    val formattedString = formatter.stringFromNumber(NSNumber(this)) ?: this.toString()
    return "$formattedString%"
}