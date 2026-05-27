package org.fitverse.domain.models.progression.consistencia

/**
 * Dados de consistência e streak de treino do usuário.
 *
 * @param currentStreak  Dias consecutivos de treino no streak atual.
 * @param recordStreak   Maior streak já registrado pelo usuário.
 * @param weekGrid       Grid 5 × 7 indicando dias treinados nas últimas 5 semanas.
 * @param marcosProgress Posição relativa na trilha de marcos (0.0 = início, 1.0 = 100 dias).
 */
data class ConsistenciaData(
    val currentStreak  : Int,
    val recordStreak   : Int,
    val weekGrid       : List<List<Boolean>>,
    val marcosProgress : Float,
)
