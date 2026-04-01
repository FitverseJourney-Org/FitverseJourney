package com.example.expect

expect object DateTimeManager {
    fun now(): Long
    fun formatMillisToDate(millis: Long): String
    fun create(day: Int, month: Int, year: Int): PlatformDate
    fun nowMillis(): Long
    fun getCurrentYear(): Int
}

data class PlatformDate(
    val day: Int,
    val month: Int,
    val year: Int,
    val epochDay: Long
)