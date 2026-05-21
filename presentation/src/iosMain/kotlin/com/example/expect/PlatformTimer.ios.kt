package org.fitverse.presentation.expect

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSString
import platform.Foundation.stringWithFormat
import platform.Foundation.timeIntervalSince1970

// ── TimerManager ──────────────────────────────────────────────────────────────

actual object TimerManager {

    actual fun nowMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()

    actual fun getCurrentYear(): Int {
        val components = NSCalendar.currentCalendar
            .components(NSCalendarUnitYear, fromDate = NSDate())
        return components.year.toInt()
    }
}

// ── formatWorkoutTime ─────────────────────────────────────────────────────────

actual fun formatWorkoutTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return NSString.stringWithFormat("%02d:%02d", min, sec)
}

// ── formatDuration ────────────────────────────────────────────────────────────

actual fun formatDuration(seconds: Long): String {
    val h   = seconds / 3600
    val min = (seconds % 3600) / 60
    val sec = seconds % 60
    return buildString {
        if (h > 0)   append("${h}h ")
        if (min > 0) append("${min}min ")
        if (sec > 0 || (h == 0L && min == 0L)) append("${sec}s")
    }.trim()
}
