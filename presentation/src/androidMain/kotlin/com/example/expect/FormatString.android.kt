/**
 * FitverseJourney — String Format Utilities
 * KMP · androidMain
 *
 * actual implementations usando:
 *   - java.lang.String.format  (printf-style)
 *   - java.text.NumberFormat   (locale-aware)
 *   - java.text.DecimalFormat
 *   - java.util.Date / Calendar
 */

package com.example.expect

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

// Locale pt-BR compartilhado dentro do arquivo
private val PTBR = Locale("pt", "BR")

// ─────────────────────────────────────────────────────────────
// CORE
// ─────────────────────────────────────────────────────────────

actual fun String.format(vararg args: Any?): String =
    java.lang.String.format(PTBR, this, *args)

// ─────────────────────────────────────────────────────────────
// NUMBERS
// ─────────────────────────────────────────────────────────────

actual fun formatNumber(value: Number, decimals: Int): String {
    val symbols = DecimalFormatSymbols(PTBR)          // . milhar, , decimal
    val pattern = buildString {
        append("#,##0")
        if (decimals > 0) append(".${"0".repeat(decimals)}")
    }
    return DecimalFormat(pattern, symbols).format(value)
}

actual fun formatCompact(value: Long): String = when {
    value >= 1_000_000L -> {
        val v = value / 100_000L / 10.0
        if (v == v.toLong().toDouble()) "${v.toLong()}M" else "${formatDecimal(v, 1)}M"
    }
    value >= 1_000L -> {
        val v = value / 100L / 10.0
        if (v == v.toLong().toDouble()) "${v.toLong()}K" else "${formatDecimal(v, 1)}K"
    }
    else -> value.toString()
}

// helper interno
private fun formatDecimal(value: Double, decimals: Int): String {
    val symbols = DecimalFormatSymbols(PTBR)
    val pattern = "0.${"0".repeat(decimals)}"
    return DecimalFormat(pattern, symbols).format(value)
}

// ─────────────────────────────────────────────────────────────
// CURRENCY
// ─────────────────────────────────────────────────────────────

actual fun formatCurrency(value: Double, symbol: String): String {
    val symbols = DecimalFormatSymbols(PTBR)
    val fmt = DecimalFormat("0.00", symbols)
    return "$symbol ${fmt.format(value)}"
}

// ─────────────────────────────────────────────────────────────
// PERCENT
// ─────────────────────────────────────────────────────────────

actual fun formatPercent(value: Float, decimals: Int): String {
    val symbols = DecimalFormatSymbols(PTBR)
    val pattern = if (decimals > 0) "0.${"0".repeat(decimals)}" else "0"
    val fmt = DecimalFormat(pattern, symbols)
    return "${fmt.format(value * 100f)}%"
}

// ─────────────────────────────────────────────────────────────
// FITNESS
// ─────────────────────────────────────────────────────────────

actual fun formatWeight(kg: Float): String {
    if (kg == 0f) return "Peso corp."
    return if (kg % 1f == 0f) {
        "${kg.toInt()} kg"
    } else {
        val symbols = DecimalFormatSymbols(PTBR)
        "${DecimalFormat("0.#", symbols).format(kg)} kg"
    }
}

actual fun formatXP(xp: Int): String {
    val symbols = DecimalFormatSymbols(PTBR)
    return "${DecimalFormat("#,##0", symbols).format(xp)} XP"
}

actual fun formatDuration(seconds: Long): String {
    val h   = seconds / 3600
    val min = (seconds % 3600) / 60
    val sec = seconds % 60
    return buildString {
        if (h > 0)   append("${h}h ")
        if (min > 0) append("${min}min ")
        if (sec > 0 || (h == 0L && min == 0L)) append("${sec}s")
    }.trim()
}

actual fun formatTimer(seconds: Long): String {
    val min = seconds / 60
    val sec = seconds % 60
    return "%02d:%02d".format(min, sec)
}

actual fun formatRelativeTime(epochMillis: Long): String {
    val diff = System.currentTimeMillis() - epochMillis
    val secs = diff / 1_000L
    val mins = secs / 60
    val hrs  = mins / 60
    val days = hrs  / 24

    return when {
        secs  < 60   -> "agora mesmo"
        mins  < 60   -> "há ${pluralPT(mins, "minuto",  "minutos")}"
        hrs   < 24   -> "há ${pluralPT(hrs,  "hora",    "horas")}"
        days  == 1L  -> "ontem"
        days  < 30   -> "há ${pluralPT(days, "dia",     "dias")}"
        days  < 365  -> {
            val months = days / 30
            "há ${pluralPT(months, "mês", "meses")}"
        }
        else -> {
            val years = days / 365
            "há ${pluralPT(years, "ano", "anos")}"
        }
    }
}

actual fun formatDate(epochMillis: Long): String {
    val cal = Calendar.getInstance(PTBR).apply { timeInMillis = epochMillis }
    val day   = cal.get(Calendar.DAY_OF_MONTH)
    val month = MONTHS_PT[cal.get(Calendar.MONTH)]
    val year  = cal.get(Calendar.YEAR)
    return "$day $month $year"
}

// ─────────────────────────────────────────────────────────────
// INTERNOS
// ─────────────────────────────────────────────────────────────

private fun pluralPT(value: Long, singular: String, plural: String) =
    if (value == 1L) "1 $singular" else "$value $plural"

private val MONTHS_PT = listOf(
    "Jan","Fev","Mar","Abr","Mai","Jun",
    "Jul","Ago","Set","Out","Nov","Dez"
)