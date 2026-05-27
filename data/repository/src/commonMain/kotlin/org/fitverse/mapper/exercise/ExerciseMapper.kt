package org.fitverse.data.repository.mapper.exercise

import org.fitverse.domain.models.progression.Exercise
import org.fitverse.data.local.model.ExerciseEntity
import org.fitverse.data.remote.dto.exercise.ExerciseDto
import kotlin.jvm.JvmName

// ── Dto → Entity (remote → local) ────────────────────────────────────────────

fun ExerciseDto.toEntity(): ExerciseEntity = ExerciseEntity(
    id            = id,
    name          = name,
    muscleGroup   = muscleGroup,
    unit          = unit,
)

@JvmName("dtoListToEntity")
fun List<ExerciseDto>.toEntity(): List<ExerciseEntity> =
    map { it.toEntity() }

// ── Entity → Domain (local → domínio) ────────────────────────────────────────

fun ExerciseEntity.toDomain(): Exercise = Exercise(
    id            = id,
    name          = name,
    muscleGroup   = muscleGroup,
    unit          = unit,
)

@JvmName("entityListToDomain")
fun List<ExerciseEntity>.toDomain(): List<Exercise> =
    map { it.toDomain() }

// ── Dto → Domain (remote → domínio) ──────────────────────────────────────────

fun ExerciseDto.toDomain(): Exercise = Exercise(
    id            = id,
    name          = name,
    muscleGroup   = muscleGroup,
    unit          = unit,
)

@JvmName("dtoListToDomain")
fun List<ExerciseDto>.toDomain(): List<Exercise> =
    map { it.toDomain() }