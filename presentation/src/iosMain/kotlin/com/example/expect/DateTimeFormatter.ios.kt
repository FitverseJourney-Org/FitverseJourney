package com.example.expect

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.currentLocale
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeZoneWithName

actual object DateTimeFormatter {
    actual fun formatShortDate(millis: Long): String {
        val date = NSDate.dateWithTimeIntervalSince1970(millis / 1000.0)
        val formatter = NSDateFormatter().apply {
            dateFormat = "dd/MM"
            timeZone = NSTimeZone.timeZoneWithName("UTC")!! // Garantir UTC aqui também
            locale = NSLocale.currentLocale
        }
        return formatter.stringFromDate(date)
    }
}