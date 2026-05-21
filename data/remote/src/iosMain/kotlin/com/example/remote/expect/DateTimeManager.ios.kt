package org.fitverse.data.remote.expect

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.timeZoneWithName

actual object DateTimeManager {
    actual fun dateTimeGetDefaultLocale(): String {
        TODO("Not yet implemented")
    }

    actual fun dateTimeNow(): Long {
        return (NSDate().timeIntervalSince1970 * 1000).toLong()
    }

    actual fun dateTimeFormatMillisToDate(millis: Long): String {
        val date = NSDate.dateWithTimeIntervalSince1970(millis / 1000.0)
        val formatter = NSDateFormatter()
        formatter.dateFormat = "dd/MM/yyyy"
        formatter.timeZone = NSTimeZone.timeZoneWithName("UTC")!!
        return formatter.stringFromDate(date)
    }

    actual fun dateTimeNowMillis(): Long {
        return (NSDate().timeIntervalSince1970 * 1000).toLong()
    }

    actual fun dateTimeGetCurrentYear(): Int {
        val calendar = NSCalendar.currentCalendar
        val components = calendar.components(NSCalendarUnitYear, fromDate = NSDate())
        return components.year.toInt()
    }
}
