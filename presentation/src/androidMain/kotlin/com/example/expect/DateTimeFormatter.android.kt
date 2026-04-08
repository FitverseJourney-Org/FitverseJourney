package com.example.expect

import android.annotation.SuppressLint
import java.time.Instant
import java.time.ZoneOffset
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
}