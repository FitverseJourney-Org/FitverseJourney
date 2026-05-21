package org.fitverse.presentation.expect

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.date
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970

private val ptBR = NSLocale(localeIdentifier = "pt_BR")

// ── DateFormatter ─────────────────────────────────────────────────────────────

actual object DateFormatter {

    actual fun getFormattedDate(daysOffset: Int): String {
        val calendar = NSCalendar.currentCalendar
        val calculatedDate = calendar.dateByAddingUnit(
            NSCalendarUnitDay,
            value   = daysOffset.toLong(),
            toDate  = NSDate(),
            options = 0UL,
        ) ?: NSDate()
        return NSDateFormatter().apply {
            dateFormat = "MMM dd, yyyy"
            locale     = NSLocale.currentLocale
        }.stringFromDate(calculatedDate)
    }

    actual fun formatFullDate(timestamp: Long): String {
        val date = NSDate.dateWithTimeIntervalSince1970(timestamp / 1000.0)
        return NSDateFormatter().apply {
            dateFormat = "EEEE, MMM dd, yyyy 'at' HH:mm"
            locale     = NSLocale.currentLocale
        }.stringFromDate(date)
    }

    actual fun getTodayIsoDate(): String =
        NSDateFormatter().apply {
            dateFormat = "yyyy-MM-dd"
            locale     = NSLocale.currentLocale
        }.stringFromDate(NSDate())

    actual fun getCurrentTimeMillis(): Long =
        (NSDate().timeIntervalSince1970 * 1000).toLong()
}

// ── formatDate ────────────────────────────────────────────────────────────────

actual fun formatDate(epochMillis: Long): String {
    val date = NSDate.dateWithTimeIntervalSince1970(epochMillis / 1000.0)
    return NSDateFormatter().apply {
        locale     = ptBR
        dateFormat = "d MMM yyyy"
    }.stringFromDate(date)
}

// ── formatRelativeTime ────────────────────────────────────────────────────────

actual fun formatRelativeTime(epochMillis: Long): String {
    val nowMs = (NSDate.date().timeIntervalSince1970 * 1000.0).toLong()
    val diff  = nowMs - epochMillis
    val secs  = diff / 1_000L
    val mins  = secs / 60
    val hrs   = mins / 60
    val days  = hrs  / 24
    return when {
        secs < 60    -> "agora mesmo"
        mins < 60    -> "há ${pluralPT(mins, "minuto",  "minutos")}"
        hrs  < 24    -> "há ${pluralPT(hrs,  "hora",    "horas")}"
        days == 1L   -> "ontem"
        days < 30    -> "há ${pluralPT(days, "dia",     "dias")}"
        days < 365   -> "há ${pluralPT(days / 30,  "mês",  "meses")}"
        else         -> "há ${pluralPT(days / 365, "ano",  "anos")}"
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun pluralPT(n: Long, singular: String, plural: String) =
    if (n == 1L) "1 $singular" else "$n $plural"
