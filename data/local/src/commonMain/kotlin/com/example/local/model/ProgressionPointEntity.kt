package org.fitverse.data.local.model


data class ProgressionPointEntity(
    val id: String,
    val exerciseId: String,
    val epochMillis: Long,
    val weightKg: Double,
    val estimatedOneRm: Double,
    val reps: Int,
    val sets: Int,
)