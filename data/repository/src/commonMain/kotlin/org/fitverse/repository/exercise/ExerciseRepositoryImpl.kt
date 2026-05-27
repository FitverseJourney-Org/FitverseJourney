package org.fitverse.data.repository.exercise

import org.fitverse.domain.models.progression.Exercise
import org.fitverse.domain.repository.ExerciseRepository
import org.fitverse.data.repository.mapper.exercise.toDomain
import org.fitverse.data.local.datasource.exercises.ExerciseLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ExerciseRepositoryImpl(
    private val localDataSource: ExerciseLocalDataSource,
) : ExerciseRepository {

    override fun getAllExercises(): Flow<List<Exercise>> =
        localDataSource
            .observeAll()
            .map { dtos -> dtos.toDomain() }

    override fun getExercisesByTrainingSplit(trainingSplit: String): Flow<List<Exercise>> =
        localDataSource
            .observeByTrainingSplit(trainingSplit)
            .map { dtos -> dtos.toDomain() }

    override fun getTrainingSplits(): Flow<List<String>> = localDataSource.observeTrainingSplits()

    override suspend fun getExerciseById(id: String): Exercise? = localDataSource.findById(id)?.toDomain()
}