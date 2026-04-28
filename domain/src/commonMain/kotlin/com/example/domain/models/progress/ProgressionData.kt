package com.example.domain.models.progress

import androidx.compose.runtime.Immutable

/**
 * Agrupamento de dados de progressão para o período selecionado e seu
 * comparativo (mesmo período do ano anterior).
 *
 * Estruturado assim para que um único fluxo (Flow) da camada de dados
 * entregue tudo que o gráfico precisa sem múltiplas coletas paralelas.
 *
 * @param current  Pontos do período atual (filtro do usuário).
 * @param previous Pontos do mesmo intervalo de meses no ano anterior.
 */
@Immutable
data class ProgressionData(
    val current: List<LoadProgressionPoint>,
    val previous: List<LoadProgressionPoint>,
)
