package com.example.expect

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitWeekday
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

    actual fun getDayOfWeek(): String {
        val calendar = NSCalendar.currentCalendar
        val weekday = calendar.component(NSCalendarUnitWeekday, fromDate = NSDate()).toInt()

        return when (weekday) {
            1 -> "SUN"
            2 -> "MON"
            3 -> "TUE"
            4 -> "WED"
            5 -> "THU"
            6 -> "FRI"
            7 -> "SAT"
            else -> ""
        }
    }

    actual fun getHourOfDay(): Int {
        val calendar = NSCalendar.currentCalendar
        return calendar.component(NSCalendarUnitHour, fromDate = NSDate())
            .toInt()
    }
}