package com.example.local.datasource.progression

import com.example.local.model.ProgressionPointEntity
import kotlinx.coroutines.flow.Flow

interface ProgressionLocalDataSource {
    fun observeByExercise(exerciseId: String): Flow<List<ProgressionPointEntity>>
    fun observeByExerciseAndYearPeriod(
        exerciseId: String,
        year: Int,
        startMonth: Int,
        endMonth: Int,
    ): Flow<List<ProgressionPointEntity>>
    suspend fun insert(point: ProgressionPointEntity)
    suspend fun upsertAll(points: List<ProgressionPointEntity>)
    suspend fun deleteByExercise(exerciseId: String)
}