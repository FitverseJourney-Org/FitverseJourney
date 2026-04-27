package com.example.local.mapper.progression

import com.example.domain.expect.toPlatformDate
import com.example.domain.models.progress.LoadProgressionPoint
import com.example.local.model.ProgressionPointEntity
import kotlin.jvm.JvmName

// ── Entity → Domain (local → domínio) ────────────────────────────────────────

fun ProgressionPointEntity.toDomain(): LoadProgressionPoint = LoadProgressionPoint(
    date           = epochMillis.toPlatformDate(),
    weight         = weightKg,
    estimatedOneRm = estimatedOneRm,
)

@JvmName("entityListToDomain")
fun List<ProgressionPointEntity>.toDomain(): List<LoadProgressionPoint> =
    sortedBy(ProgressionPointEntity::epochMillis).map { it.toDomain() }









