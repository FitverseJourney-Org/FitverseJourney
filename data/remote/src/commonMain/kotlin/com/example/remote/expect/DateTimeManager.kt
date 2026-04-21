package com.example.remote.expect

import com.example.domain.models.PlatformDate

expect object DateTimeManager {
    fun dateTimeGetDefaultLocale() : String
    fun dateTimeNow(): Long
    fun dateTimeFormatMillisToDate(millis: Long): String
    fun dateTimeCreate(day: Int, month: Int, year: Int): PlatformDate
    fun dateTimeNowMillis(): Long
    fun dateTimeGetCurrentYear(): Int
}

