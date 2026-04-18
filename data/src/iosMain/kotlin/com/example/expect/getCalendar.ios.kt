package com.example.expect

import platform.Foundation.*

// ─────────────────────────────────────────────────────────────────────────────
// iosMain — implementação com NSDateComponents
// ─────────────────────────────────────────────────────────────────────────────
// Arquivo: iosMain/kotlin/com/example/expect/PlatformDate.ios.kt
actual class PlatformDate(private val nsDate: NSDate) {
    private val calendar = NSCalendar.currentCalendar
    private val comps get() = calendar.components(
        NSCalendarUnitDay or NSCalendarUnitMonth or NSCalendarUnitYear,
        fromDate = nsDate
    )
    actual val day: Int          get() = comps.day.toInt()
    actual val month: Int        get() = comps.month.toInt()  // 1-based
    actual val year: Int         get() = comps.year.toInt()
    actual val epochMillis: Long get() = (nsDate.timeIntervalSince1970 * 1000).toLong()
}

actual fun Long.toPlatformDate(): PlatformDate =
    PlatformDate(NSDate.dateWithTimeIntervalSince1970(this / 1000.0))
