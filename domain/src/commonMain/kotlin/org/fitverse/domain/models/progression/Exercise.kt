package org.fitverse.domain.models.progression

import androidx.compose.runtime.Immutable

/**
 * Representa um exercício disponível para seleção no card de Carga.
 *
 * @param id          Identificador único (vindo do backend / banco local).
 * @param name        Nome de exibição (e.g., "Supino Reto").
 * @param unit        Unidade de medida da carga (e.g., "kg", "lb").
 * @param muscleGroup Grupo muscular principal em caixa alta (e.g., "PEITO", "COSTAS").
 */
@Immutable
data class Exercise(
    val id          : String,
    val name        : String,
    val unit        : String,
    val muscleGroup : String,
)

