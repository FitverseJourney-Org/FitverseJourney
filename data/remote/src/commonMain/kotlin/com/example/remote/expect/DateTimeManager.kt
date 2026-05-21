package org.fitverse.data.remote.expect

import org.fitverse.domain.expect.PlatformDate

expect object DateTimeManager {
    fun dateTimeGetDefaultLocale() : String
    fun dateTimeNow(): Long
    fun dateTimeFormatMillisToDate(millis: Long): String
    fun dateTimeNowMillis(): Long
    fun dateTimeGetCurrentYear(): Int
}

