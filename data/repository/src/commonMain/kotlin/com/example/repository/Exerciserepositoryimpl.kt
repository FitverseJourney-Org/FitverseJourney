package com.example.repository

import com.example.local.datasource.exercises.ExerciseLocalDataSource
import com.example.local.mapper.toDomain
import com.example.domain.model.progress.Exercise
import com.example.domain.repository.ExerciseRepository
import com.example.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementação concreta do [ExerciseRepository].
 *
 * ## Estratégia: Local-Only
 * Exercícios são criados pelo usuário no app e sincronizados em background
 * por um serviço dedicado. Esta classe consome apenas o [ExerciseLocalDataSource]
 * e emite atualizações reativas sempre que o banco local muda.
 *
 * ## Responsabilidades
 * - Traduzir chamadas de domínio para chamadas ao DataSource.
 * - Converter DTOs em entidades de domínio via [ExerciseMapper].
 * - Nunca expor tipos da camada de dados para cima (domínio/apresentação).
 *
 * @param localDataSource Injetado — pode ser SQLDelight, Room ou fake in-memory.
 */
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

    override fun getTrainingSplits(): Flow<List<String>> =
        localDataSource.observeTrainingSplits()

    override suspend fun getExerciseById(id: String): Exercise? =
        localDataSource.findById(id)?.toDomain()
}