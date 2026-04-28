package com.example.local.datasource.progression

import com.example.local.model.ProgressionPointEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

private const val EPOCH_2025_JAN_01: Long = 1_735_689_600_000L

private fun Long.toCalendarYear(): Int {
    val z   = this / 86_400_000L + 719_468L
    val era = (if (z >= 0L) z else z - 146_096L) / 146_097L
    val doe = (z - era * 146_097L).toInt()
    val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
    val y   = yoe.toLong() + era * 400L
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val mp  = (5 * doy + 2) / 153
    val m   = mp + if (mp < 10) 3 else -9
    return (y + if (m <= 2) 1L else 0L).toInt()
}

private fun Long.toCalendarMonth(): Int {
    val z   = this / 86_400_000L + 719_468L
    val era = (if (z >= 0L) z else z - 146_096L) / 146_097L
    val doe = (z - era * 146_097L).toInt()
    val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val mp  = (5 * doy + 2) / 153
    return mp + if (mp < 10) 3 else -9
}

private data class ExerciseSeed(val exerciseId: String, val baseWeight: Double)

private val SEED_NOISE_PATTERN = listOf(
    0.0, 0.5, -0.5, 1.0, 0.0, 0.5, -0.25, 0.75, 0.0, 1.25, -0.5, 0.5,
)

private val EXERCISE_SEEDS = listOf(
    ExerciseSeed(exerciseId = "ex_001", baseWeight = 80.0),
    ExerciseSeed(exerciseId = "ex_004", baseWeight = 40.0),
    ExerciseSeed(exerciseId = "ex_006", baseWeight = 60.0),
    ExerciseSeed(exerciseId = "ex_011", baseWeight = 100.0),
)

private fun seedProgressionPoints(): List<ProgressionPointEntity> {
    val baseEpoch  = EPOCH_2025_JAN_01
    val weekMillis = 7L * 24L * 60L * 60L * 1_000L
    val reps       = 8

    return buildList {
        EXERCISE_SEEDS.forEach { seed ->
            (0 until 12).forEach { weekIndex ->
                val noise  = SEED_NOISE_PATTERN[weekIndex % SEED_NOISE_PATTERN.size]
                val weight = seed.baseWeight + (weekIndex * 0.75) + noise
                add(
                    ProgressionPointEntity(
                        id             = "${seed.exerciseId}_w$weekIndex",
                        exerciseId     = seed.exerciseId,
                        epochMillis    = baseEpoch + weekIndex * weekMillis,
                        weightKg       = (weight * 2).toLong().toDouble() / 2.0,
                        estimatedOneRm = weight * (1.0 + reps / 30.0),
                        reps           = reps,
                        sets           = 3,
                    )
                )
            }
        }
    }
}

class ProgressionLocalDataSourceImpl : ProgressionLocalDataSource {

    private val _store = MutableStateFlow(seedProgressionPoints())

    override fun observeByExercise(exerciseId: String): Flow<List<ProgressionPointEntity>> =
        _store.map { points ->
            points.filter { it.exerciseId == exerciseId }.sortedBy { it.epochMillis }
        }

    override fun observeByExerciseAndYearPeriod(
        exerciseId: String,
        year: Int,
        startMonth: Int,
        endMonth: Int,
    ): Flow<List<ProgressionPointEntity>> =
        _store.map { points ->
            points.filter { point ->
                point.exerciseId == exerciseId &&
                        point.epochMillis.toCalendarYear()  == year &&
                        point.epochMillis.toCalendarMonth() in startMonth..endMonth
            }.sortedBy { it.epochMillis }
        }

    override suspend fun insert(point: ProgressionPointEntity) {
        _store.value += point
    }

    override suspend fun upsertAll(points: List<ProgressionPointEntity>) {
        val current = _store.value.associateBy(ProgressionPointEntity::id).toMutableMap()
        points.forEach { current[it.id] = it }
        _store.value = current.values.sortedBy(ProgressionPointEntity::epochMillis)
    }

    override suspend fun deleteByExercise(exerciseId: String) {
        _store.value = _store.value.filter { it.exerciseId != exerciseId }
    }
}