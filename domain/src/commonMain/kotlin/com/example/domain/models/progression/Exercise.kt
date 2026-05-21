package org.fitverse.domain.models.progression

import androidx.compose.runtime.Immutable

/**
 * Entidade de domínio representando um exercício cadastrado.
 *
 * Pertence à camada de **domínio** — não conhece nada de UI, banco ou rede.
 * Imutável por design para ser seguro em ambientes concorrentes e Compose.
 *
 * @param id            Identificador único (UUID ou primary key do banco local).
 * @param name          Nome legível (ex.: "Supino Reto com Barra").
 * @param muscleGroup   Grupo muscular primário (ex.: "Peitoral").
 * @param trainingSplit Letra/nome da ficha (ex.: "A", "Empurrar").
 */
@Immutable
data class Exercise(
    val id: String,
    val name: String,
    val muscleGroup: String,
    val trainingSplit: String,
    val isActive: Boolean = true,
)

