package org.fitverse.domain.models.progression.calorias

/**
 * Dados de consumo calórico do período solicitado.
 *
 * @param avgDaily      Média diária de calorias consumidas (kcal).
 * @param changePercent Variação percentual em relação ao período anterior (pode ser negativo).
 * @param today         Calorias registradas hoje (kcal).
 * @param goal          Meta diária de calorias (kcal).
 * @param streak        Dias consecutivos atingindo a meta.
 * @param history       Histórico de calorias por dia no período (kcal por entry).
 */
data class CaloriasData(
    val avgDaily      : Int,
    val changePercent : Int,
    val today         : Int,
    val goal          : Int,
    val streak        : Int,
    val history       : List<Float>,
)
