package org.fitverse.presentation.expect

import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.Foundation.NSString
import platform.Foundation.numberWithDouble
import platform.Foundation.numberWithInt
import platform.Foundation.stringWithFormat

private val ptBR = NSLocale(localeIdentifier = "pt_BR")

// ── NumberFormat ──────────────────────────────────────────────────────────────

actual object NumberFormat {

    actual fun decimal(value: Double): String =
        NSString.stringWithFormat("%.1f", value)

    actual fun grouped(value: Number, decimals: Int): String {
        val fmt = NSNumberFormatter().apply {
            numberStyle           = NSNumberFormatterDecimalStyle
            locale                = ptBR
            minimumFractionDigits = decimals.toULong()
            maximumFractionDigits = decimals.toULong()
            usesGroupingSeparator = true
        }
        return fmt.stringFromNumber(NSNumber.numberWithDouble(value.toDouble()))
            ?: value.toString()
    }

    actual fun compact(value: Long): String {
        return when {
            value >= 1_000_000L -> {
                val v = value / 100_000L / 10.0
                val f = if (v == v.toLong().toDouble()) "${v.toLong()}"
                        else NSString.stringWithFormat("%.1f", v)
                            .replace(".", decimalSeparatorPT())
                "${f}M"
            }
            value >= 1_000L -> {
                val v = value / 100L / 10.0
                val f = if (v == v.toLong().toDouble()) "${v.toLong()}"
                        else NSString.stringWithFormat("%.1f", v)
                            .replace(".", decimalSeparatorPT())
                "${f}K"
            }
            else -> value.toString()
        }
    }
}

// ── formatXP ──────────────────────────────────────────────────────────────────

actual fun formatXP(xp: Int): String {
    val fmt = NSNumberFormatter().apply {
        numberStyle           = NSNumberFormatterDecimalStyle
        locale                = ptBR
        minimumFractionDigits = 0u
        maximumFractionDigits = 0u
        usesGroupingSeparator = true
    }
    return "${fmt.stringFromNumber(NSNumber.numberWithInt(xp)) ?: xp} XP"
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun decimalSeparatorPT(): String =
    NSNumberFormatter().apply { locale = ptBR }.decimalSeparator ?: ","
