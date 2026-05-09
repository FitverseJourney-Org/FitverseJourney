/**
 * FitverseJourney — String Format Utilities
 * KMP · iosMain  (Kotlin/Native · Darwin)
 *
 * actual implementations usando:
 *   - NSString.localizedStringWithFormat  (printf-style)
 *   - NSNumberFormatter                   (locale-aware numbers/currency)
 *   - NSDateFormatter                     (datas)
 *   - platform.Foundation.*
 *
 * Locale fixado em pt_BR para consistência com Android.
 */

package com.example.expect

import platform.Foundation.*

// Locale pt-BR compartilhado dentro do arquivo
private val ptBR: NSLocale = NSLocale(localeIdentifier = "pt_BR")

// ─────────────────────────────────────────────────────────────
// CORE
// ─────────────────────────────────────────────────────────────

actual fun String.format(vararg args: Any?): String {
    // NSString.localizedStringWithFormat recebe CVarArgList
    // Para KMP, usamos a versão via stringWithFormat
    return when (args.size) {
        0    -> this
        1    -> NSString.stringWithFormat(this, args[0])
        2    -> NSString.stringWithFormat(this, args[0], args[1])
        3    -> NSString.stringWithFormat(this, args[0], args[1], args[2])
        4    -> NSString.stringWithFormat(this, args[0], args[1], args[2], args[3])
        5    -> NSString.stringWithFormat(this, args[0], args[1], args[2], args[3], args[4])
        else -> {
            // fallback manual para listas longas — substitui %s/%d/%f sequencialmente
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
                        val decimals = match.value
                            .substringAfter(".")
                            .takeWhile { it.isDigit() }
                            .toIntOrNull() ?: 6
                        NSString.stringWithFormat("%.${decimals}f", d)
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

// ─────────────────────────────────────────────────────────────
// NUMBERS
// ─────────────────────────────────────────────────────────────

actual fun formatNumber(value: Number, decimals: Int): String {
    val fmt = NSNumberFormatter().apply {
        numberStyle             = NSNumberFormatterDecimalStyle
        locale                  = ptBR
        minimumFractionDigits   = decimals.toULong()
        maximumFractionDigits   = decimals.toULong()
        usesGroupingSeparator   = true
    }
    return fmt.stringFromNumber(NSNumber.numberWithDouble(value.toDouble())) ?: value.toString()
}

actual fun formatCompact(value: Long): String = when {
    value >= 1_000_000L -> {
        val v = value / 100_000L / 10.0
        val formatted = if (v == v.toLong().toDouble())
            "${v.toLong()}"
        else
            NSString.stringWithFormat("%.1f", v)
                .replace(".", decimalSeparatorPT())
        "${formatted}M"
    }
    value >= 1_000L -> {
        val v = value / 100L / 10.0
        val formatted = if (v == v.toLong().toDouble())
            "${v.toLong()}"
        else
            NSString.stringWithFormat("%.1f", v)
                .replace(".", decimalSeparatorPT())
        "${formatted}K"
    }
    else -> value.toString()
}

// ─────────────────────────────────────────────────────────────
// CURRENCY
// ─────────────────────────────────────────────────────────────

actual fun formatCurrency(value: Double, symbol: String): String {
    val fmt = NSNumberFormatter().apply {
        numberStyle           = NSNumberFormatterDecimalStyle
        locale                = ptBR
        minimumFractionDigits = 2u
        maximumFractionDigits = 2u
    }
    val formatted = fmt.stringFromNumber(NSNumber.numberWithDouble(value)) ?: "0,00"
    return "$symbol $formatted"
}

// ─────────────────────────────────────────────────────────────
// PERCENT
// ─────────────────────────────────────────────────────────────

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

// ─────────────────────────────────────────────────────────────
// FITNESS
// ─────────────────────────────────────────────────────────────

actual fun formatWeight(kg: Float): String {
    if (kg == 0f) return "Peso corp."
    return if (kg % 1f == 0f) {
        "${kg.toInt()} kg"
    } else {
        val fmt = NSNumberFormatter().apply {
            numberStyle           = NSNumberFormatterDecimalStyle
            locale                = ptBR
            minimumFractionDigits = 0u
            maximumFractionDigits = 1u
        }
        "${fmt.stringFromNumber(NSNumber.numberWithFloat(kg)) ?: kg} kg"
    }
}

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
    // NSString.stringWithFormat com %02ld para Long no iOS
    return NSString.stringWithFormat("%02ld:%02ld", min, sec)
}

actual fun formatRelativeTime(epochMillis: Long): String {
    val nowMs = (NSDate.date().timeIntervalSince1970 * 1000.0).toLong()
    val diff  = nowMs - epochMillis
    val secs  = diff / 1_000L
    val mins  = secs / 60
    val hrs   = mins / 60
    val days  = hrs  / 24

    return when {
        secs  < 60  -> "agora mesmo"
        mins  < 60  -> "há ${pluralPT(mins, "minuto",  "minutos")}"
        hrs   < 24  -> "há ${pluralPT(hrs,  "hora",    "horas")}"
        days  == 1L -> "ontem"
        days  < 30  -> "há ${pluralPT(days, "dia",     "dias")}"
        days  < 365 -> {
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
    val date = NSDate.dateWithTimeIntervalSince1970(epochMillis / 1000.0)
    val fmt  = NSDateFormatter().apply {
        locale     = ptBR
        dateFormat = "d MMM yyyy"        // → "15 Mai 2025"
    }
    return fmt.stringFromDate(date)
}

// ─────────────────────────────────────────────────────────────
// INTERNOS
// ─────────────────────────────────────────────────────────────

private fun pluralPT(value: Long, singular: String, plural: String) =
    if (value == 1L) "1 $singular" else "$value $plural"

private fun decimalSeparatorPT(): String {
    val fmt = NSNumberFormatter().apply { locale = ptBR }
    return fmt.decimalSeparator ?: ","
}