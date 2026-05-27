package org.fitverse.presentation.expect

import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.Foundation.NSString
import platform.Foundation.numberWithDouble
import platform.Foundation.numberWithFloat
import platform.Foundation.stringWithFormat

private val ptBR = NSLocale(localeIdentifier = "pt_BR")

// ── String.format ─────────────────────────────────────────────────────────────

actual fun String.format(vararg args: Any?): String {
    return when (args.size) {
        0    -> this
        1    -> NSString.stringWithFormat(this, args[0])
        2    -> NSString.stringWithFormat(this, args[0], args[1])
        3    -> NSString.stringWithFormat(this, args[0], args[1], args[2])
        4    -> NSString.stringWithFormat(this, args[0], args[1], args[2], args[3])
        5    -> NSString.stringWithFormat(this, args[0], args[1], args[2], args[3], args[4])
        else -> {
            var result = this
            val pattern = Regex("%(?:\\d+\\\$)?[-+0 #]?\\d*\\.?\\d*[sdifoxXeEgGcb%]")
            var idx = 0
            result = pattern.replace(result) { match ->
                val arg = args.getOrNull(idx++)
                when {
                    match.value.endsWith("d") || match.value.endsWith("i") ->
                        (arg as? Number)?.toLong()?.toString() ?: "0"
                    match.value.endsWith("f") || match.value.endsWith("e") ||
                    match.value.endsWith("g") -> {
                        val d = (arg as? Number)?.toDouble() ?: 0.0
                        val dec = match.value.substringAfter(".")
                            .takeWhile { it.isDigit() }.toIntOrNull() ?: 6
                        NSString.stringWithFormat("%.${dec}f", d)
                    }
                    match.value.endsWith("s") -> arg?.toString() ?: ""
                    match.value.endsWith("%") -> "%"
                    else -> arg?.toString() ?: ""
                }
            }
            result
        }
    }
}

// ── formatPercent ─────────────────────────────────────────────────────────────

actual fun formatPercent(value: Float, decimals: Int): String {
    val fmt = NSNumberFormatter().apply {
        numberStyle           = NSNumberFormatterDecimalStyle
        locale                = ptBR
        minimumFractionDigits = decimals.toULong()
        maximumFractionDigits = decimals.toULong()
    }
    val pct = (value * 100f).toDouble()
    return "${fmt.stringFromNumber(NSNumber.numberWithDouble(pct)) ?: "0"}%"
}

// ── formatCurrency ────────────────────────────────────────────────────────────

actual fun formatCurrency(value: Double, symbol: String): String {
    val fmt = NSNumberFormatter().apply {
        numberStyle           = NSNumberFormatterDecimalStyle
        locale                = ptBR
        minimumFractionDigits = 2u
        maximumFractionDigits = 2u
    }
    return "$symbol ${fmt.stringFromNumber(NSNumber.numberWithDouble(value)) ?: "0,00"}"
}

// ── formatWeight ──────────────────────────────────────────────────────────────

actual fun formatWeight(kg: Float): String {
    if (kg == 0f) return "Peso corp."
    return if (kg % 1f == 0f) "${kg.toInt()} kg"
    else {
        val fmt = NSNumberFormatter().apply {
            numberStyle           = NSNumberFormatterDecimalStyle
            locale                = ptBR
            minimumFractionDigits = 0u
            maximumFractionDigits = 1u
        }
        "${fmt.stringFromNumber(NSNumber.numberWithFloat(kg)) ?: kg} kg"
    }
}
