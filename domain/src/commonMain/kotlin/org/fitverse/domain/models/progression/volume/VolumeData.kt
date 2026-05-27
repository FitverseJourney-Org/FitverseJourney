package org.fitverse.domain.models.progression.volume

/**
 * Volume total de treino para o período solicitado.
 *
 * @param totalTons   Carga total levantada em toneladas (kg / 1000).
 * @param sessions    Número de sessões no período.
 * @param weeklyGrid  Matriz 7 × 7 de intensidade relativa (0.0–1.0). Linha = semana, coluna = dia.
 * @param rowLabels   Rótulos de cada linha do heatmap (ex.: "S", "0", "3"…).
 */
data class VolumeData(
    val totalTons  : Float,
    val sessions   : Int,
    val weeklyGrid : List<List<Float>>,
    val rowLabels  : List<String>,
)
