package com.example.expect

import java.lang.String.format
import java.util.Locale

actual fun Double.formatPlatformDecimal(digits: Int): String {
    return format(Locale.US, "%.${digits}f", this)
}