package com.example.expect

import platform.Foundation.*

actual object PlatformDateFormatter {
    actual fun getFormattedDate(daysOffset: Int): String {
        // 1. Pega o calendário e calcula a data com o offset
        val calendar = NSCalendar.currentCalendar
        val calculatedDate = calendar.dateByAddingUnit(
            NSCalendarUnitDay,
            value = daysOffset.toLong(),
            toDate = NSDate(), // NSDate() pega o momento atual
            options = 0UL
        ) ?: NSDate() // Fallback de segurança para a data atual

        // 2. Cria o formatador e configura as propriedades uma a uma
        val formatter = NSDateFormatter()
        formatter.dateFormat = "MMM dd, yyyy"
        formatter.locale = NSLocale.currentLocale

        // 3. Retorna a string final
        return formatter.stringFromDate(calculatedDate)
    }
    actual fun formatFullDate(timestamp: Long): String {
        val date = NSDate.dateWithTimeIntervalSince1970(timestamp / 1000.0)
        val formatter = NSDateFormatter()
        formatter.dateFormat = "EEEE, MMM dd, yyyy 'at' HH:mm"
        formatter.locale = NSLocale.currentLocale
        return formatter.stringFromDate(date)
    }

    actual fun getTodayIsoDate(): String {
        val formatter = NSDateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        formatter.locale = NSLocale.currentLocale
        return formatter.stringFromDate(NSDate())
    }

    actual fun getCurrentTimeMillis(): Long =
        (NSDate().timeIntervalSince1970 * 1000).toLong()
}