package com.example.expect

import java.lang.String.format
import java.util.Locale

actual object NumberFormatter {
    actual fun formatOneDecimal(value: Double): String {
        return format(Locale.getDefault(), "%.1f", value)
    }
}