package org.fitverse.presentation.expect

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// ─────────────────────────────────────────────────────────────────────────────
// STRINGS · TEXTO — printf, percentual, moeda e peso.
// actual: androidMain → java.lang.String.format / DecimalFormat(pt_BR)
//         iosMain     → NSString.stringWithFormat / NSNumberFormatter(pt_BR)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Printf-style com locale pt-BR fixado.
 *
 * ```
 * "Olá, %s! Você tem %d conquistas.".format("Ana", 42)  → "Olá, Ana! Você tem 42 conquistas."
 * "Carga: %.1f kg".format(82.5)                         → "Carga: 82,5 kg"
 * "Ranking: #%02d".format(5)                            → "Ranking: #05"
 * ```
 */
expect fun String.format(vararg args: Any?): String

/**
 * Formata [value] (0..1) como percentual com [decimals] casas decimais.
 *
 * ```
 * formatPercent(0.874f)      → "87,4%"
 * formatPercent(1.0f, 0)     → "100%"
 * formatPercent(0.26f, 0)    → "26%"
 * ```
 */
expect fun formatPercent(value: Float, decimals: Int = 1): String

/**
 * Formata [value] como valor monetário com [symbol].
 *
 * ```
 * formatCurrency(29.90)         → "R$ 29,90"
 * formatCurrency(299.0, "US$")  → "US$ 299,00"
 * ```
 */
expect fun formatCurrency(value: Double, symbol: String = "R$"): String

/**
 * Formata [kg] como carga de treino. Omite decimal para valores inteiros.
 *
 * ```
 * formatWeight(82.5f)  → "82,5 kg"
 * formatWeight(80.0f)  → "80 kg"
 * formatWeight(0f)     → "Peso corp."
 * ```
 */
expect fun formatWeight(kg: Float): String

// ── Extensions ────────────────────────────────────────────────────────────────

/** `0.874f.toPercentString()` → `"87,4%"` */
fun Float.toPercentString(decimals: Int = 1): String = formatPercent(this, decimals)

/** `29.90.toCurrencyString()` → `"R$ 29,90"` */
fun Double.toCurrencyString(symbol: String = "R$"): String = formatCurrency(this, symbol)

/** `82.5f.toWeightString()` → `"82,5 kg"` */
fun Float.toWeightString(): String = formatWeight(this)

@OptIn(ExperimentalUuidApi::class)
fun randomId(): String = Uuid.random().toString()
