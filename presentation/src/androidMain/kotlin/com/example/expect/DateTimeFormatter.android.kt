package com.example.expect

import android.annotation.SuppressLint
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Calendar
import java.time.format.DateTimeFormatter as JavaDateTimeFormatter
import java.util.Locale

actual object DateTimeFormatter {
    // Criamos o formatador uma única vez (performance)
    @SuppressLint("ConstantLocale")
    private val formatter = JavaDateTimeFormatter.ofPattern("dd/MM")
        .withLocale(Locale.getDefault())
        .withZone(ZoneOffset.UTC)

    actual fun formatShortDate(millis: Long): String {
        val instant = Instant.ofEpochMilli(millis)
        return formatter.format(instant)
    }

    // Ex: "segunda-feira" → "Segunda-feira"
    actual fun getDayOfWeek(): String {
        val now = LocalDateTime.now()
        return now.dayOfWeek
            .getDisplayName(TextStyle.FULL, Locale.getDefault())
            .replaceFirstChar { it.uppercase() }
    }

    // Ex: 14
    actual fun getHourOfDay(): Int {
        return LocalDateTime.now().hour
    }
}