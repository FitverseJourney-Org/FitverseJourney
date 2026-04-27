package com.example.expect


// Formata números decimais seguindo o locale do dispositivo
expect object NumberFormatter {
    // "3,1"  (pt_BR)
    // "3.1"  (en_US)
    // "10,0" (pt_BR)
    fun formatOneDecimal(value: Double): String
}