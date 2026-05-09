package com.example.expect



// Gerencia conversões e criação de datas a
// partir de timestamps ou campos individuais
expect object TimerManager {
    fun nowMillis(): Long  // actual fun nowMillis()      // 1745580000000
    fun getCurrentYear(): Int // actual fun getCurrentYear() // 2026
}

// Modelo de data com dia, mês, ano e
// representação em epochDay (dias desde 1970-01-01)
data class PlatformDate(
    val day: Int,
    val month: Int,
    val year: Int,
    val epochDay: Long
)