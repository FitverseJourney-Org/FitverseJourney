package org.fitverse.domain.models.progression.cardio

/**
 * Métricas de desempenho cardiovascular para o período solicitado.
 *
 * @param paceMin         Parte inteira do pace médio (min/km).
 * @param paceSec         Parte fracionária do pace médio (seg/km).
 * @param changePercent   Variação percentual do pace em relação ao período anterior.
 * @param vo2Max          VO2 Máximo estimado (ml/kg/min).
 * @param distKm          Distância total percorrida no período (km).
 * @param aerobicZoneFrac Fração do tempo em zona aeróbica (0.0–1.0).
 * @param paceHistory     Histórico de pace por sessão (min/km como Float).
 */
data class CardioData(
    val paceMin         : Int,
    val paceSec         : Int,
    val changePercent   : Int,
    val vo2Max          : Int,
    val distKm          : Float,
    val aerobicZoneFrac : Float,
    val paceHistory     : List<Float>,
)
