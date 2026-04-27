package com.example.expect



// Gerencia conversões e criação de datas a
// partir de timestamps ou campos individuais
expect object TimerManager {
    // actual fun millisToDate(1745580000000L) // "25/04/2026"
    fun millisToDate(millis: Long): String
    fun create(day: Int, month: Int, year: Int): PlatformDate
    // actual fun nowMillis()      // 1745580000000
    fun nowMillis(): Long
    // actual fun getCurrentYear() // 2026
    fun getCurrentYear(): Int
}

// Modelo de data com dia, mês, ano e
// representação em epochDay (dias desde 1970-01-01)
data class PlatformDate(
    val day: Int,
    val month: Int,
    val year: Int,
    val epochDay: Long
)