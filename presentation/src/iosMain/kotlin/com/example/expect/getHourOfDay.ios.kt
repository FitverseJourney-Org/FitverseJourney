package com.example.expect

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSDate

actual fun getHourOfDay(): Int {
    val calendar = NSCalendar.currentCalendar
    return calendar.component(NSCalendarUnitHour, fromDate = NSDate())
        .toInt()
}