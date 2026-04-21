package com.example.expect

import androidx.compose.material3.CalendarLocale
import kotlin.time.Duration

expect object PlatformDateFormatter {
    fun getFormattedDate(daysOffset: Int = 0): String

    fun formatFullDate(timestamp: Long): String
}