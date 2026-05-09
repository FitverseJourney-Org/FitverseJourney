package com.example.expect

/**
 * FitverseJourney — String Format Utilities
 * KMP · commonMain
 *
 * expect/actual pattern para formatação de strings
 * compatível com Android (JVM) e iOS (Native/Darwin)
 *
 * Uso:
 *   "%,d XP".format(6_240)          → "6,240 XP"
 *   formatNumber(1234567.89, 2)      → "1.234.567,89"
 *   formatCurrency(29.90)            → "R$ 29,90"
 *   formatPercent(0.874f)            → "87,4%"
 *   formatWeight(82.5f)              → "82,5 kg"
 *   formatDuration(5421)             → "1h 30min 21s"
 *   formatRelativeTime(epochMillis)  → "há 3 horas"
 */

// ─────────────────────────────────────────────────────────────
// CORE — format de string com args posicionais
// Espelha String.format() do Java / NSString stringWithFormat
// ─────────────────────────────────────────────────────────────

/**
 * Formata a string usando placeholders no estilo printf.
 *
 * Exemplos:
 *   "Olá, %s! Você tem %d conquistas.".format("Lucas", 14)
 *   "Carga: %.1f kg".format(82.5)
 *   "Ranking: #%02d".format(5)
 */
/**
 * FitverseJourney — String Format Utilities
 * KMP · commonMain
 *
 * expect/actual pattern para formatação de strings
 * compatível com Android (JVM) e iOS (Native/Darwin)
 *
 * Uso:
 *   "%,d XP".format(6_240)          → "6,240 XP"
 *   formatNumber(1234567.89, 2)      → "1.234.567,89"
 *   formatCurrency(29.90)            → "R$ 29,90"
 *   formatPercent(0.874f)            → "87,4%"
 *   formatWeight(82.5f)              → "82,5 kg"
 *   formatDuration(5421)             → "1h 30min 21s"
 *   formatRelativeTime(epochMillis)  → "há 3 horas"
 */

// ─────────────────────────────────────────────────────────────
// CORE — format de string com args posicionais
// Espelha String.format() do Java / NSString stringWithFormat
// ─────────────────────────────────────────────────────────────

/**
 * Formata a string usando placeholders no estilo printf.
 *
 * Exemplos:
 *   "Olá, %s! Você tem %d conquistas.".format("Lucas", 14)
 *   "Carga: %.1f kg".format(82.5)
 *   "Ranking: #%02d".format(5)
 */
expect fun String.format(vararg args: Any?): String

// ─────────────────────────────────────────────────────────────
// NUMBERS
// ─────────────────────────────────────────────────────────────

/**
 * Formata um número com separador de milhar e casas decimais.
 *
 * formatNumber(1234567.89, 2)  → "1.234.567,89"
 * formatNumber(6240, 0)        → "6.240"
 */
expect fun formatNumber(value: Number, decimals: Int = 0): String

/**
 * Formata número compacto para exibição em UI densa.
 *
 * formatCompact(12_580)   → "12,5K"
 * formatCompact(1_300_000) → "1,3M"
 * formatCompact(840)      → "840"
 */
expect fun formatCompact(value: Long): String

// ─────────────────────────────────────────────────────────────
// CURRENCY
// ─────────────────────────────────────────────────────────────

/**
 * Formata valor monetário com símbolo.
 *
 * formatCurrency(29.90)         → "R$ 29,90"
 * formatCurrency(299.0, "US$")  → "US$ 299,00"
 */
expect fun formatCurrency(value: Double, symbol: String = "R$"): String

// ─────────────────────────────────────────────────────────────
// PERCENT
// ─────────────────────────────────────────────────────────────

/**
 * Formata valor float (0..1) como porcentagem.
 *
 * formatPercent(0.874f)        → "87,4%"
 * formatPercent(1.0f, 0)       → "100%"
 * formatPercent(0.26f, 0)      → "26%"
 */
expect fun formatPercent(value: Float, decimals: Int = 1): String

// ─────────────────────────────────────────────────────────────
// FITNESS — helpers específicos do app
// ─────────────────────────────────────────────────────────────

/**
 * Formata carga de treino.
 *
 * formatWeight(82.5f)   → "82,5 kg"
 * formatWeight(82.0f)   → "82 kg"    (sem decimal quando .0)
 * formatWeight(0f)      → "Peso corp."
 */
expect fun formatWeight(kg: Float): String

/**
 * Formata XP com separador de milhar e sufixo.
 *
 * formatXP(1_240)   → "1.240 XP"
 * formatXP(12_580)  → "12.580 XP"
 */
expect fun formatXP(xp: Int): String

/**
 * Formata duração em segundos para exibição legível.
 *
 * formatDuration(90)     → "1min 30s"
 * formatDuration(3661)   → "1h 1min 1s"
 * formatDuration(45)     → "45s"
 */
expect fun formatDuration(seconds: Long): String

/**
 * Formata duração apenas como mm:ss (timer de treino/descanso).
 *
 * formatTimer(90)   → "01:30"
 * formatTimer(3661) → "61:01"
 */
expect fun formatTimer(seconds: Long): String

/**
 * Formata data de epoch millis para data relativa em pt-BR.
 *
 * formatRelativeTime(now - 60_000)       → "há 1 minuto"
 * formatRelativeTime(now - 7_200_000)    → "há 2 horas"
 * formatRelativeTime(now - 86_400_000)   → "ontem"
 * formatRelativeTime(now - 172_800_000)  → "há 2 dias"
 */
expect fun formatRelativeTime(epochMillis: Long): String

/**
 * Formata data de epoch millis para data formatada pt-BR.
 *
 * formatDate(epoch)  → "15 Mai 2025"
 */
expect fun formatDate(epochMillis: Long): String

// ─────────────────────────────────────────────────────────────
// EXTENSION — helpers inline sobre tipos primitivos
// ─────────────────────────────────────────────────────────────

/** 6_240.toXPString()  → "6.240 XP" */
fun Int.toXPString(): String = formatXP(this)

/** 82.5f.toWeightString()  → "82,5 kg" */
fun Float.toWeightString(): String = formatWeight(this)

/** 0.874f.toPercentString()  → "87,4%" */
fun Float.toPercentString(decimals: Int = 1): String = formatPercent(this, decimals)

/** 29.90.toCurrencyString()  → "R$ 29,90" */
fun Double.toCurrencyString(symbol: String = "R$"): String = formatCurrency(this, symbol)

/** 90L.toTimerString()  → "01:30" */
fun Long.toTimerString(): String = formatTimer(this)

/** 3661L.toDurationString()  → "1h 1min 1s" */
fun Long.toDurationString(): String = formatDuration(this)

/** 12580L.toCompactString()  → "12,5K" */
fun Long.toCompactString(): String = formatCompact(this)
fun Int.toCompactString(): String = formatCompact(this.toLong())