package com.example.remote.expect


import com.example.domain.expect.PlatformDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

actual object DateTimeManager {
    actual fun dateTimeGetDefaultLocale(): String {
        TODO("Not yet implemented")
    }
    actual fun dateTimeNow(): Long = System.currentTimeMillis()

    actual fun dateTimeFormatMillisToDate(millis: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date(millis))
    }

    actual fun dateTimeNowMillis(): Long {
        return System.currentTimeMillis()
    }

    actual fun dateTimeGetCurrentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }
}