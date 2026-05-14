package com.example.expect


// Formata datas completas usando o calendário do sistema
expect object PlatformDateFormatter {
    // actual fun getFormattedDate(0) "abr 25, 2026"  (pt_BR) — hoje
    // actual fun getFormattedDate(1) "abr 26, 2026"  — amanhã
    // actual fun getFormattedDate(-1) "abr 24, 2026"  — ontem
    // actual fun formatFullDate(1745580000000L) "Sábado, abr 25, 2026 at 14:00"
    fun getFormattedDate(daysOffset: Int = 0): String
    fun formatFullDate(timestamp: Long): String
    // Returns today as "yyyy-MM-dd" (ISO 8601) — used for DB queries
    fun getTodayIsoDate(): String
    // Returns current epoch time in milliseconds — used for timestamps
    fun getCurrentTimeMillis(): Long
}