package org.fitverse.presentation.expect

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val PTBR = Locale("pt", "BR")

// ── String.format ─────────────────────────────────────────────────────────────

actual fun String.format(vararg args: Any?): String =
    java.lang.String.format(PTBR, this, *args)

// ── formatPercent ─────────────────────────────────────────────────────────────

actual fun formatPercent(value: Float, decimals: Int): String {
    val symbols = DecimalFormatSymbols(PTBR)
    val pattern = if (decimals > 0) "0.${"0".repeat(decimals)}" else "0"
    return "${DecimalFormat(pattern, symbols).format(value * 100f)}%"
}

// ── formatCurrency ────────────────────────────────────────────────────────────

actual fun formatCurrency(value: Double, symbol: String): String {
    val symbols = DecimalFormatSymbols(PTBR)
    return "$symbol ${DecimalFormat("0.00", symbols).format(value)}"
}

// ── formatWeight ──────────────────────────────────────────────────────────────

actual fun formatWeight(kg: Float): String {
    if (kg == 0f) return "Peso corp."
    return if (kg % 1f == 0f) "${kg.toInt()} kg"
    else "${DecimalFormat("0.#", DecimalFormatSymbols(PTBR)).format(kg)} kg"
}
