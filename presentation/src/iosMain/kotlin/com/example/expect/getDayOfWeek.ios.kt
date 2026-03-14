package com.example.expect

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitWeekday
import platform.Foundation.NSDate

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