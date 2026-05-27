package org.fitverse.data.remote.datasource.exercises

import org.fitverse.data.remote.dto.exercise.ExerciseDto
import kotlinx.coroutines.flow.Flow

interface ExerciseLocalDataSource {

    /** Emite todos os exercícios ativos. Re-emite quando o banco muda. */
    fun observeAll(): Flow<List<ExerciseDto>>

    /** Emite exercícios de uma ficha específica. */
    fun observeByTrainingSplit(trainingSplit: String): Flow<List<ExerciseDto>>

    /** Emite as fichas distintas existentes, ordenadas alfabeticamente. */
    fun observeTrainingSplits(): Flow<List<String>>

    /** Retorna um exercício pelo ID, ou `null` se não existir. */
    suspend fun findById(id: String): ExerciseDto?

    /** Insere ou atualiza exercícios (upsert). */
    suspend fun upsertAll(exercises: List<ExerciseDto>)

    /** Marca um exercício como inativo (soft-delete). */
    suspend fun softDelete(id: String)
}