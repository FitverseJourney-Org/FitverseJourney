package com.example.expect


// Formata datas e horas de forma multiplataforma
expect object DateTimeFormatter {

    // "25/04"
    fun formatShortDate(millis: Long): String
    // "Sábado"
    fun getDayOfWeek(): String
    // 14
    fun getHourOfDay(): Int
}