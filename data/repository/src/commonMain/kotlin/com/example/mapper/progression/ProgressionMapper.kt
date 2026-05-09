package com.example.mapper.progression

import com.example.domain.expect.toPlatformDate
import com.example.domain.models.progression.LoadProgressionPoint
import com.example.local.model.ProgressionPointEntity
import com.example.remote.dto.progression.ProgressionPointDto
import kotlin.jvm.JvmName

fun ProgressionPointDto.toDomain(): LoadProgressionPoint = LoadProgressionPoint(
    date           = epochMillis.toPlatformDate(),
    weight         = weightKg,
    estimatedOneRm = estimatedOneRm,
)

@JvmName("dtoListToDomain")
fun List<ProgressionPointDto>.toDomain(): List<LoadProgressionPoint> =
    sortedBy(ProgressionPointDto::epochMillis).map { it.toDomain() }

fun LoadProgressionPoint.toDto(id: String, exerciseId: String): ProgressionPointDto =
    ProgressionPointDto(
        id             = id,
        exerciseId     = exerciseId,
        epochMillis    = date.epochMillis,
        weightKg       = weight,
        estimatedOneRm = estimatedOneRm,
    )

fun ProgressionPointDto.toEntity(): ProgressionPointEntity = ProgressionPointEntity(
    id             = id,
    exerciseId     = exerciseId,
    epochMillis    = epochMillis,
    weightKg       = weightKg,
    estimatedOneRm = estimatedOneRm,
    reps           = reps,
    sets           = sets,
)