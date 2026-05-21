package org.fitverse.data.repository.mapper

import org.fitverse.domain.models.progression.Exercise
import org.fitverse.data.repository.mapper.exercise.toDomain
import org.fitverse.data.remote.dto.exercise.ExerciseDto

/**
 * Funções de extensão puras para conversão [ExerciseDto] ↔ [Exercise].
 *
 * Sem estado, sem injeção, sem side-effects — testáveis diretamente.
 *
 * ## Regras de mapeamento
 * - [name] e [muscleGroup] recebem `.trim()` contra espaços vindos da API.
 * - [muscleGroup] é capitalizado para consistência visual na UI.
 * - [trainingSplit] é colocado em uppercase ("a" → "A").
 * - [isActive] == false é filtrado no DataSource antes de chegar aqui.
 */

fun ExerciseDto.toDomain(): Exercise = Exercise(
    id           = id,
    name         = name.trim(),
    muscleGroup  = muscleGroup.trim().replaceFirstChar { it.uppercaseChar() },
    trainingSplit = trainingSplit.trim().uppercase(),
)

fun List<ExerciseDto>.toDomain(): List<Exercise> = map { it.toDomain() }

fun Exercise.toDto(): ExerciseDto = ExerciseDto(
    id            = id,
    name          = name,
    muscleGroup   = muscleGroup,
    trainingSplit = trainingSplit,
    isActive      = true,
)