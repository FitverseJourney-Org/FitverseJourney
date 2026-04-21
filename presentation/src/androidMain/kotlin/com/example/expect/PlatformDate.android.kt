package com.example.expect

import androidx.compose.material3.CalendarLocale
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

actual object PlatformDateFormatter {
    private val calendar = Calendar.getInstance()

    actual fun getFormattedDate(daysOffset: Int): String {
        // 1. Pega a data de hoje
        val calendar = Calendar.getInstance()

        // 2. Adiciona ou subtrai os dias
        calendar.add(Calendar.DAY_OF_YEAR, daysOffset)

        // 3. Formata e retorna
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    actual fun formatFullDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE, MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}