package com.example.expect

import java.time.LocalDate
import java.util.Calendar

actual object TimerManager {

    actual fun millisToDate(millis: Long): String {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
        return sdf.format(java.util.Date(millis))
    }
    actual fun create(day: Int, month: Int, year: Int): PlatformDate {
        val date = LocalDate.of(year, month, day)
        return PlatformDate(day, month, year, date.toEpochDay())
    }

    actual fun nowMillis(): Long = System.currentTimeMillis()
    actual fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)
}