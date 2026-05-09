package com.example.repository.exercise

import com.example.domain.models.progression.Exercise
import com.example.domain.repository.ExerciseRepository
import com.example.mapper.exercise.toDomain
import com.example.local.datasource.exercises.ExerciseLocalDataSource
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