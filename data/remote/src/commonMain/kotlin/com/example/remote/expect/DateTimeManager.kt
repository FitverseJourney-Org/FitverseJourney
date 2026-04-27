package com.example.remote.expect

import com.example.domain.expect.PlatformDate

expect object DateTimeManager {
    fun dateTimeGetDefaultLocale() : String
    fun dateTimeNow(): Long
    fun dateTimeFormatMillisToDate(millis: Long): String
    fun dateTimeNowMillis(): Long
    fun dateTimeGetCurrentYear(): Int
}

