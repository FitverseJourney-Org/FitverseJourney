package org.fitverse.presentation.expect

// ─────────────────────────────────────────────────────────────────────────────
// STRINGS · NÚMERO — decimal, agrupado, compacto e XP.
// actual: androidMain → DecimalFormat / DecimalFormatSymbols(pt_BR)
//         iosMain     → NSNumberFormatter(pt_BR)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Formatação de números respeitando locale e precisão.
 *
 * ```
 * NumberFormat.decimal(3.14)           → "3,1"            (pt_BR) / "3.1" (en_US)
 * NumberFormat.grouped(1_234_567, 2)   → "1.234.567,00"
 * NumberFormat.compact(12_580L)        → "12,5K"
 * NumberFormat.compact(1_300_000L)     → "1,3M"
 * ```
 */
expect object NumberFormat {

    /**
     * Uma casa decimal, respeita o locale do dispositivo.
     *
     * ```
     * decimal(10.0)  → "10,0"
     * decimal(3.14)  → "3,1"
     * ```
     */
    fun decimal(value: Double): String

    /**
     * Separador de milhar com [decimals] casas decimais em pt-BR.
     *
     * ```
     * grouped(6_240, 0)       → "6.240"
     * grouped(1234567.89, 2)  → "1.234.567,89"
     * ```
     *
     * @param decimals Casas decimais (padrão 0).
     */
    fun grouped(value: Number, decimals: Int = 0): String

    /**
     * Notação compacta com sufixo K ou M em pt-BR.
     *
     * ```
     * compact(840L)        → "840"
     * compact(12_580L)     → "12,5K"
     * compact(1_300_000L)  → "1,3M"
     * ```
     */
    fun compact(value: Long): String
}

/**
 * Formata [xp] com separador de milhar e sufixo "XP" em pt-BR.
 *
 * ```
 * formatXP(1_240)   → "1.240 XP"
 * formatXP(12_580)  → "12.580 XP"
 * formatXP(0)       → "0 XP"
 * ```
 */
expect fun formatXP(xp: Int): String

// ── Extensions ────────────────────────────────────────────────────────────────

/** `12_580L.toCompactString()` → `"12,5K"` */
fun Long.toCompactString(): String = NumberFormat.compact(this)

/** `12_580.toCompactString()` → `"12,5K"` */
fun Int.toCompactString(): String = NumberFormat.compact(this.toLong())

/** `1_240.toXPString()` → `"1.240 XP"` */
fun Int.toXPString(): String = formatXP(this)
