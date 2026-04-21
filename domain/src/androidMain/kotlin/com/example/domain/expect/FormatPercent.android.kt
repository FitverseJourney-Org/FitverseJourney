package com.example.domain.expect

import java.text.DecimalFormat

actual fun Double.formatPercent(): String {
    val format = DecimalFormat("#.##")
    return "${format.format(this)}%"
}

actual fun Float.formatPercent(): String {
    val format = DecimalFormat("#.##")
    return "${format.format(this)}%"
}