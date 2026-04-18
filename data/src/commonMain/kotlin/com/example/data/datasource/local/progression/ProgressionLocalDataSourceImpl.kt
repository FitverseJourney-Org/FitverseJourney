package com.example.data.datasource.local.progression

import com.example.data.model.dto.progression.ProgressionPointDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Implementação fake usando [MutableStateFlow] como banco em memória.
 * Re-emite automaticamente após qualquer mutação, espelhando o comportamento
 * de queries reativas do SQLDelight/Room.
 */
class ProgressionLocalDataSourceImpl : ProgressionLocalDataSource {

    private val _store = MutableStateFlow(seedProgressionPoints())

    override fun observeByExercise(exerciseId: String): Flow<List<ProgressionPointDto>> =
        _store.map { points ->
            points.filter { it.exerciseId == exerciseId }.sortedBy { it.epochMillis }
        }

    override fun observeByExerciseAndYearPeriod(
        exerciseId: String,
        year: Int,
        startMonth: Int,
        endMonth: Int,
    ): Flow<List<ProgressionPointDto>> =
        _store.map { points ->
            points
                .filter { point ->
                    point.exerciseId == exerciseId &&
                            point.epochMillis.calendarYear() == year &&
                            point.epochMillis.calendarMonth() in startMonth..endMonth
                }
                .sortedBy { it.epochMillis }
        }

    override suspend fun insert(point: ProgressionPointDto) {
        _store.value = _store.value + point
    }

    override suspend fun upsertAll(points: List<ProgressionPointDto>) {
        val current = _store.value.associateBy(ProgressionPointDto::id).toMutableMap()
        points.forEach { current[it.id] = it }
        _store.value = current.values.sortedBy(ProgressionPointDto::epochMillis)
    }

    override suspend fun deleteByExercise(exerciseId: String) {
        _store.value = _store.value.filter { it.exerciseId != exerciseId }
    }
}