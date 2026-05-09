package com.example.remote.mapper.progression
// data/remote/src/commonMain/kotlin/com/example/remote/mapper/progression/ProgressionMapper.kt

import com.example.domain.expect.toPlatformDate
import com.example.domain.models.progression.LoadProgressionPoint
import com.example.remote.dto.progression.ProgressionPointDto

// ── Dto → Entity (remote → local) ────────────────────────────────────────────

fun ProgressionPointDto.toEntity(): ProgressionPointDto = ProgressionPointDto(
    id             = id,
    exerciseId     = exerciseId,
    epochMillis    = epochMillis,
    weightKg       = weightKg,
    estimatedOneRm = estimatedOneRm,
    reps           = reps,
    sets           = sets,
)

fun List<ProgressionPointDto>.toEntity(): List<ProgressionPointDto> =
    map { it.toEntity() }

// ── Dto → Domain (remote → domínio) ──────────────────────────────────────────

fun ProgressionPointDto.toDomain(): LoadProgressionPoint = LoadProgressionPoint(
    date           = epochMillis.toPlatformDate(),
    weight         = weightKg,
    estimatedOneRm = estimatedOneRm,
)

fun List<ProgressionPointDto>.toDomain(): List<LoadProgressionPoint> =
    sortedBy(ProgressionPointDto::epochMillis).map { it.toDomain() }

// ── Domain → Dto (domínio → remote) ──────────────────────────────────────────

fun LoadProgressionPoint.toDto(id: String, exerciseId: String): ProgressionPointDto =
    ProgressionPointDto(
        id             = id,
        exerciseId     = exerciseId,
        epochMillis    = date.epochMillis,
        weightKg       = weight,
        estimatedOneRm = estimatedOneRm,
    )