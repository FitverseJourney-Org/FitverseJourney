package com.example.expect

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970


actual object TimerManager {
    actual fun nowMillis(): Long {
        return (NSDate().timeIntervalSince1970 * 1000).toLong()
    }

    actual fun getCurrentYear(): Int {
        val calendar = NSCalendar.currentCalendar
        val components = calendar.components(NSCalendarUnitYear, fromDate = NSDate())
        return components.year.toInt()
    }
}