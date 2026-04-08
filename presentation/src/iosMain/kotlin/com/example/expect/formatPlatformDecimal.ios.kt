package com.example.expect

import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatterDecimalStyle

actual fun Double.formatPlatformDecimal(digits: Int): String {
    val formatter = NSNumberFormatter().apply {
        // Correção: converter para ULong para satisfazer a API do iOS
        minimumFractionDigits = digits.toULong()
        maximumFractionDigits = digits.toULong()
        numberStyle = NSNumberFormatterDecimalStyle
    }
    return formatter.stringFromNumber(NSNumber(this)) ?: this.toString()
}