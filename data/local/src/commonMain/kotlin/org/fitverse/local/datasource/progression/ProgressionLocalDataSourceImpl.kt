package org.fitverse.data.local.datasource.progression

import org.fitverse.data.local.model.ProgressionPointEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.math.sin

// ─────────────────────────────────────────────────────────────────────────────
// Epoch anchors (UTC midnight)
// ─────────────────────────────────────────────────────────────────────────────

private const val EPOCH_2025_JAN_01: Long = 1_735_689_600_000L
private const val EPOCH_2026_JAN_01: Long = 1_767_225_600_000L
private const val WEEK_MS:           Long = 604_800_000L          // 7 × 24 × 3600 × 1000

// ─────────────────────────────────────────────────────────────────────────────
// Calendar math (pure arithmetic, no java.util.Calendar)
// ─────────────────────────────────────────────────────────────────────────────

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

// ─────────────────────────────────────────────────────────────────────────────
// Seed configuration
// ─────────────────────────────────────────────────────────────────────────────

private data class ExerciseSeedConfig(
    val exerciseId     : String,
    val baseWeight     : Double,
    val weeklyGain     : Double,
    val noiseAmplitude : Double,
    val reps           : Int,
)

private val EXERCISE_CONFIGS = listOf(
    // ── Ficha A ──────────────────────────────────────────────────────────────
    ExerciseSeedConfig("ex_001", baseWeight = 80.0,  weeklyGain = 0.40, noiseAmplitude = 1.50, reps = 8),
    ExerciseSeedConfig("ex_002", baseWeight = 28.0,  weeklyGain = 0.25, noiseAmplitude = 1.00, reps = 10),
    ExerciseSeedConfig("ex_003", baseWeight = 15.0,  weeklyGain = 0.15, noiseAmplitude = 0.75, reps = 12),
    ExerciseSeedConfig("ex_004", baseWeight = 40.0,  weeklyGain = 0.25, noiseAmplitude = 1.00, reps = 12),
    ExerciseSeedConfig("ex_005", baseWeight = 30.0,  weeklyGain = 0.20, noiseAmplitude = 0.80, reps = 10),
    // ── Ficha B ──────────────────────────────────────────────────────────────
    ExerciseSeedConfig("ex_006", baseWeight = 65.0,  weeklyGain = 0.35, noiseAmplitude = 1.25, reps = 8),
    ExerciseSeedConfig("ex_007", baseWeight = 70.0,  weeklyGain = 0.40, noiseAmplitude = 1.50, reps = 8),
    ExerciseSeedConfig("ex_008", baseWeight = 30.0,  weeklyGain = 0.20, noiseAmplitude = 0.75, reps = 10),
    ExerciseSeedConfig("ex_009", baseWeight = 35.0,  weeklyGain = 0.20, noiseAmplitude = 0.75, reps = 10),
    ExerciseSeedConfig("ex_010", baseWeight = 16.0,  weeklyGain = 0.10, noiseAmplitude = 0.50, reps = 15),
    // ── Ficha C ──────────────────────────────────────────────────────────────
    ExerciseSeedConfig("ex_011", baseWeight = 100.0, weeklyGain = 0.50, noiseAmplitude = 2.00, reps = 8),
    ExerciseSeedConfig("ex_012", baseWeight = 150.0, weeklyGain = 0.60, noiseAmplitude = 2.50, reps = 10),
    ExerciseSeedConfig("ex_013", baseWeight = 50.0,  weeklyGain = 0.30, noiseAmplitude = 1.00, reps = 12),
    ExerciseSeedConfig("ex_014", baseWeight = 24.0,  weeklyGain = 0.15, noiseAmplitude = 0.50, reps = 15),
    ExerciseSeedConfig("ex_015", baseWeight = 60.0,  weeklyGain = 0.35, noiseAmplitude = 1.25, reps = 10),
)

// ─────────────────────────────────────────────────────────────────────────────
// Week generator — continuous globalOffset ensures smooth year-over-year trend
// ─────────────────────────────────────────────────────────────────────────────

private fun generateWeeks(
    config       : ExerciseSeedConfig,
    startEpoch   : Long,
    weekCount    : Int,
    globalOffset : Int,
): List<ProgressionPointEntity> {
    // Deterministic per-exercise noise phase prevents all exercises moving in sync
    val noisePhase = config.exerciseId.sumOf { it.code.toDouble() } % 20.0
    val floor      = config.baseWeight * 0.60

    return buildList {
        repeat(weekCount) { w ->
            val absoluteWeek = (globalOffset + w).toDouble()
            val trend        = absoluteWeek * config.weeklyGain
            val noise        = config.noiseAmplitude * sin(absoluteWeek * 0.35 + noisePhase)
            // Every 8th week is a deload (-12 % of base)
            val deload       = if ((globalOffset + w) % 8 == 7) config.baseWeight * -0.12 else 0.0
            val rawWeight    = config.baseWeight + trend + noise + deload
            // Snap to 0.5 kg and enforce floor
            val weight       = ((rawWeight.coerceAtLeast(floor) * 2).toLong().toDouble()) / 2.0

            add(
                ProgressionPointEntity(
                    id             = "${config.exerciseId}_aw${globalOffset + w}",
                    exerciseId     = config.exerciseId,
                    epochMillis    = startEpoch + w.toLong() * WEEK_MS,
                    weightKg       = weight,
                    estimatedOneRm = weight * (1.0 + config.reps / 30.0),
                    reps           = config.reps,
                    sets           = 3,
                )
            )
        }
    }
}

// 52 weeks for 2025 + 19 weeks for Jan–May 2026 (to match today's date)
private fun seedProgressionPoints(): List<ProgressionPointEntity> =
    buildList {
        EXERCISE_CONFIGS.forEach { config ->
            addAll(generateWeeks(config, EPOCH_2025_JAN_01, 52, globalOffset = 0))
            addAll(generateWeeks(config, EPOCH_2026_JAN_01, 19, globalOffset = 52))
        }
    }

// ─────────────────────────────────────────────────────────────────────────────
// Data source
// ─────────────────────────────────────────────────────────────────────────────

class ProgressionLocalDataSourceImpl : ProgressionLocalDataSource {

    private val _store = MutableStateFlow(seedProgressionPoints())

    override fun observeByExercise(exerciseId: String): Flow<List<ProgressionPointEntity>> =
        _store.map { points ->
            points.filter { it.exerciseId == exerciseId }.sortedBy { it.epochMillis }
        }

    override fun observeByExerciseAndYearPeriod(
        exerciseId : String,
        year       : Int,
        startMonth : Int,
        endMonth   : Int,
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
