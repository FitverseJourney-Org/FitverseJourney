package com.example.expect

import com.example.domain.models.PlatformDate

expect object TimerManager {
    fun now(): Long
    fun formatMillisToDate(millis: Long): String
    fun create(day: Int, month: Int, year: Int): PlatformDate
    fun nowMillis(): Long
    fun getCurrentYear(): Int
}
