package com.example.expect

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual object NumberFormatter {
    actual fun formatOneDecimal(value: Double): String {
        return NSString.stringWithFormat("%.1f", value)
    }
}