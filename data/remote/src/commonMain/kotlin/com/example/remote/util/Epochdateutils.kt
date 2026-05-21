package org.fitverse.data.remote.util


// ─────────────────────────────────────────────────────────────────────────────
// Extração de ano e mês a partir de epoch milissegundos
// Sem java.util.Calendar · Sem kotlinx-datetime · KMP-safe em todos os targets
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Extrai ano e mês (1-based) de um timestamp Unix em milissegundos usando
 * **aritmética pura** — sem nenhuma dependência de plataforma.
 *
 * ## Algoritmo
 * Baseado no algoritmo de calendário civil de **Howard Hinnant** (domínio público).
 * Referência: https://howardhinnant.github.io/date_algorithms.html
 *
 * O algoritmo desloca a época para 1 de março do ano 0 (calendário civil),
 * onde os anos bissextos ficam no final — simplificando a aritmética de meses.
 *
 * ## Precisão
 * Cobre todas as datas do calendário Gregoriano proleptico de forma exata,
 * incluindo anos bissextos (divisíveis por 4, exceto séculos, exceto múltiplos de 400).
 *
 * ## Exemplo
 * ```kotlin
 * val (year, month) = 1_700_000_000_000L.toYearMonth()
 * // year = 2023, month = 11  (novembro)
 * ```
 *
 * @return [Pair] onde [Pair.first] é o ano e [Pair.second] é o mês (1 = Jan, 12 = Dez).
 */
fun Long.toYearMonth(): Pair<Int, Int> {
    // Converte ms → dias desde a época Unix (1970-01-01)
    val daysSinceEpoch = this / 86_400_000L

    // Desloca para 1 de março do ano 0 (onde cada "era" de 400 anos começa)
    val z   = daysSinceEpoch + 719_468L
    val era = (if (z >= 0L) z else z - 146_096L) / 146_097L
    val doe = (z - era * 146_097L).toInt()                           // dia do era   [0, 146096]
    val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365 // ano do era   [0, 399]
    val y   = yoe.toLong() + era * 400L                              // ano absoluto
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)               // dia do ano   [0, 365]
    val mp  = (5 * doy + 2) / 153                                    // mês interno  [0, 11]

    // Converte o mês interno (começa em março=0) para mês civil (Jan=1)
    val month = mp + if (mp < 10) 3 else -9
    val year  = (y + if (month <= 2) 1L else 0L).toInt()

    return year to month
}

/** Extrai apenas o **ano** de um epoch millis. */
fun Long.epochYear(): Int = toYearMonth().first

/** Extrai apenas o **mês** (1-based) de um epoch millis. */
fun Long.epochMonth(): Int = toYearMonth().second

// ─────────────────────────────────────────────────────────────────────────────
// Construção de epoch millis a partir de data civil (testes / seed data)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Retorna o epoch millis de 00:00:00 UTC para uma data civil (ano, mês, dia).
 *
 * Usado nos dados de seed para gerar timestamps legíveis sem depender de
 * `java.util.Calendar`, `java.time` ou qualquer API de plataforma.
 *
 * Algoritmo: inverso do `toYearMonth()` acima, também de Howard Hinnant.
 *
 * ```kotlin
 * val ts = epochMillisOf(year = 2024, month = 3, day = 15)
 * ```
 */
fun epochMillisOf(year: Int, month: Int, day: Int): Long {
    val y   = if (month <= 2) year.toLong() - 1L else year.toLong()
    val m   = if (month <= 2) month + 9       else month - 3
    val era = (if (y >= 0L) y else y - 399L) / 400L
    val yoe = (y - era * 400L).toInt()
    val doy = (153 * m + 2) / 5 + day - 1
    val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy
    val daysSinceEpoch = era * 146_097L + doe.toLong() - 719_468L
    return daysSinceEpoch * 86_400_000L
}