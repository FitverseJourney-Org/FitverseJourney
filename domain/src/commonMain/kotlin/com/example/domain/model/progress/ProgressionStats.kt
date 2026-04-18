package com.example.domain.model.progress

import androidx.compose.runtime.Immutable

/**
 * Estatísticas agregadas de um exercício para o período filtrado.
 *
 * Produzida por [calculateProgressionStats] — não contém lógica própria.
 * Consumida diretamente pelo componente de UI `ProgressionStatsGrid`.
 *
 * @param personalRecord  Maior carga registrada no período (kg).
 * @param currentLoad     Carga da sessão mais recente (kg).
 * @param evolutionDelta  Diferença entre última e primeira sessão (kg).
 *                        Positivo = ganho, negativo = regressão.
 * @param sessionCount    Número de sessões no período.
 */
@Immutable
data class ProgressionStats(
    val personalRecord: Double,
    val currentLoad: Double,
    val evolutionDelta: Double,
    val sessionCount: Int,
) {
    companion object {
        /** Valor inicial seguro antes de qualquer dado carregar. */
        val Empty = ProgressionStats(
            personalRecord = 0.0,
            currentLoad = 0.0,
            evolutionDelta = 0.0,
            sessionCount = 0,
        )
    }
}

/**
 * Calcula [ProgressionStats] a partir de uma lista de pontos.
 *
 * Função pura — testável sem nenhuma dependência de framework.
 * Pré-condição: [data] está ordenada cronologicamente.
 */
fun calculateProgressionStats(data: List<LoadProgressionPoint>): ProgressionStats {
    if (data.isEmpty()) return ProgressionStats.Empty
    return ProgressionStats(
        personalRecord  = data.maxOf { it.weight },
        currentLoad     = data.last().weight,
        evolutionDelta  = data.last().weight - data.first().weight,
        sessionCount    = data.size,
    )
}
