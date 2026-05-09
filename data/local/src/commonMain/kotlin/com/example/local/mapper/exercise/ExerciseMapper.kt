package com.example.local.mapper.exercise

import com.example.domain.models.progression.Exercise
import com.example.local.model.ExerciseEntity

fun ExerciseEntity.toDomain(): Exercise = Exercise(
    id            = id,
    name          = name,
    muscleGroup   = muscleGroup,
    trainingSplit = trainingSplit,
    isActive      = isActive,
)

fun List<ExerciseEntity>.toDomain(): List<Exercise> = map { it.toDomain() }