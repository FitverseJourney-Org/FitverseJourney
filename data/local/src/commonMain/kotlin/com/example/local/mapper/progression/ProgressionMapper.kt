package org.fitverse.data.local.mapper.progression

import org.fitverse.domain.expect.toPlatformDate
import org.fitverse.domain.models.progression.LoadProgressionPoint
import org.fitverse.data.local.model.ProgressionPointEntity
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









