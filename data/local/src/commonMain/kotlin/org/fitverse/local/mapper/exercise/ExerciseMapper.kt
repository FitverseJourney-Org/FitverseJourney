package org.fitverse.data.local.mapper.exercise

import org.fitverse.domain.models.progression.Exercise
import org.fitverse.data.local.model.ExerciseEntity

fun ExerciseEntity.toDomain(): Exercise = Exercise(
    id            = id,
    name          = name,
    muscleGroup   = muscleGroup,
    unit          = unit,
)

fun List<ExerciseEntity>.toDomain(): List<Exercise> = map { it.toDomain() }