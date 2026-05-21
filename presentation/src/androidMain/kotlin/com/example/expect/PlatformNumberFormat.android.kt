package org.fitverse.presentation.expect

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val PTBR = Locale("pt", "BR")

// ── NumberFormat ──────────────────────────────────────────────────────────────

actual object NumberFormat {

    actual fun decimal(value: Double): String {
        return DecimalFormat("0.0", DecimalFormatSymbols(Locale.getDefault())).format(value)
    }

    actual fun grouped(value: Number, decimals: Int): String {
        val symbols = DecimalFormatSymbols(PTBR)
        val pattern = if (decimals > 0) "#,##0.${"0".repeat(decimals)}" else "#,##0"
        return DecimalFormat(pattern, symbols).format(value)
    }

    actual fun compact(value: Long): String {
        val symbols = DecimalFormatSymbols(PTBR)
        return when {
            value >= 1_000_000L -> {
                val v = value / 100_000L / 10.0
                val f = if (v == v.toLong().toDouble()) "${v.toLong()}"
                        else DecimalFormat("0.0", symbols).format(v)
                "${f}M"
            }
            value >= 1_000L -> {
                val v = value / 100L / 10.0
                val f = if (v == v.toLong().toDouble()) "${v.toLong()}"
                        else DecimalFormat("0.0", symbols).format(v)
                "${f}K"
            }
            else -> value.toString()
        }
    }
}

// ── formatXP ──────────────────────────────────────────────────────────────────

actual fun formatXP(xp: Int): String {
    val symbols = DecimalFormatSymbols(PTBR)
    return "${DecimalFormat("#,##0", symbols).format(xp)} XP"
}
