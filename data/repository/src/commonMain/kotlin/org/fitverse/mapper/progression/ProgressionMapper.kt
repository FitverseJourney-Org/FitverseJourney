package org.fitverse.data.repository.mapper.progression

import org.fitverse.domain.expect.toPlatformDate
import org.fitverse.domain.models.progression.LoadProgressionPoint
import org.fitverse.data.local.model.ProgressionPointEntity
import org.fitverse.data.remote.dto.progression.ProgressionPointDto
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