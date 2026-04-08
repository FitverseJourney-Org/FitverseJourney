package com.example.expect

expect fun Double.formatPlatformDecimal(digits: Int): String

fun Double.formatDecimal(): String {
    return if (this % 1.0 == 0.0) {
        this.toInt().toString()
    } else {
        this.formatPlatformDecimal(1)
    }
}