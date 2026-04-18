package com.example.presentation.utils
/**
 * Utilitários de data/período prontos para `commonMain` no KMP.
 *
 * Toda lógica aqui é pura (sem referências a plataforma), permitindo
 * compartilhamento entre Android, iOS e Desktop sem `expect/actual`.
 *
 * Os nomes de meses são obtidos através de [MonthNameProvider], que pode
 * ter implementações `actual` por plataforma caso seja necessário
 * localização com `java.time` (Android/JVM) ou `NSDateFormatter` (iOS).
 */

// ─────────────────────────────────────────────────────────────────────────────
// Nomes de meses — isolados para substituição por plataforma
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Provedor de nomes de mês.
 * Implementação padrão com strings fixas em PT-BR.
 * Em produção, substitua por uma implementação `actual` que use
 * `java.time.Month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))`.
 */
object MonthNames {
    private val MONTHS_PT_BR = listOf(
        "Janeiro", "Fevereiro", "Março", "Abril",
        "Maio", "Junho", "Julho", "Agosto",
        "Setembro", "Outubro", "Novembro", "Dezembro"
    )

    /** Retorna o nome completo do mês (1 = Janeiro … 12 = Dezembro). */
    fun full(month: Int): String = MONTHS_PT_BR.getOrElse(month - 1) { month.toString() }

    /** Retorna abreviação de 3 letras (ex: "Jan", "Fev"). */
    fun short(month: Int): String = full(month).take(3)

    /** Lista todos os meses de 1 a 12. */
    fun all(): List<Pair<Int, String>> = (1..12).map { it to full(it) }
}

// ─────────────────────────────────────────────────────────────────────────────
// Validação de período
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Valida e normaliza um par (startMonth, endMonth).
 * Garante que início ≤ fim e que ambos estejam dentro de [1, 12].
 */
fun validatePeriod(start: Int, end: Int): Pair<Int, Int> {
    val clampedStart = start.coerceIn(1, 12)
    val clampedEnd = end.coerceIn(clampedStart, 12)
    return clampedStart to clampedEnd
}

/**
 * Formata um período para exibição.
 * Exemplo: (3, 8) → "Mar – Ago"
 */
fun formatPeriodLabel(startMonth: Int, endMonth: Int): String =
    if (startMonth == endMonth) MonthNames.full(startMonth)
    else "${MonthNames.short(startMonth)} – ${MonthNames.short(endMonth)}"

// ─────────────────────────────────────────────────────────────────────────────
// Formatação de números decimais
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Formata um Double para exibição com até 1 casa decimal.
 * Remove zeros desnecessários: 10.0 → "10", 10.5 → "10,5".
 *
 * KMP-safe: usa apenas operações da stdlib Kotlin.
 */
fun Double.formatDecimalKmp(): String {
    val rounded = (this * 10).toLong().toDouble() / 10.0
    return if (rounded == rounded.toLong().toDouble()) {
        rounded.toLong().toString()
    } else {
        rounded.toString().replace('.', ',')
    }
}

/**
 * Formata variação percentual com sinal: "+12,3%" ou "-5,0%".
 */
fun Double.formatPercent(): String {
    val sign = if (this >= 0) "+" else ""
    return "$sign${this.formatDecimalKmp()}%"
}