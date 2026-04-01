package com.example.expect

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.timeZoneWithName

actual object DateTimeManager {
    actual fun now(): Long {
        return (NSDate().timeIntervalSince1970 * 1000).toLong()
    }

    actual fun formatMillisToDate(millis: Long): String {
        val date = NSDate.dateWithTimeIntervalSince1970(millis / 1000.0)
        val formatter = platform.Foundation.NSDateFormatter()
        formatter.dateFormat = "dd/MM/yyyy"
        formatter.timeZone = platform.Foundation.NSTimeZone.timeZoneWithName("UTC")!!
        return formatter.stringFromDate(date)
    }

    actual fun create(day: Int, month: Int, year: Int): PlatformDate {
        val components = NSDateComponents()
        components.day = day.toLong()
        components.month = month.toLong()
        components.year = year.toLong()

        val calendar = NSCalendar.currentCalendar
        val date = calendar.dateFromComponents(components) ?: NSDate()
        val epochDay = (date.timeIntervalSince1970 / 86400).toLong()

        return PlatformDate(day, month, year, epochDay)
    }

    actual fun nowMillis(): Long {
        return (NSDate().timeIntervalSince1970 * 1000).toLong()
    }

    actual fun getCurrentYear(): Int {
        val calendar = NSCalendar.currentCalendar
        val components = calendar.components(NSCalendarUnitYear, fromDate = NSDate())
        return components.year.toInt()
    }
}