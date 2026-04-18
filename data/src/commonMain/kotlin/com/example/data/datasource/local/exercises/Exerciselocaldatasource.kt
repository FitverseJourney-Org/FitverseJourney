package com.example.data.datasource.local.exercises

import com.example.data.model.dto.progression.ExerciseDto
import kotlinx.coroutines.flow.Flow

// ─────────────────────────────────────────────────────────────────────────────
// Interface
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Contrato para a fonte de dados local de exercícios.
 *
 * Em produção: implementado com **SQLDelight** (KMP) ou **Room** (Android).
 * Em testes/desenvolvimento: [ExerciseLocalDataSourceImpl] com dados em memória.
 *
 * Todos os métodos de leitura retornam [Flow] para emitir atualizações reativas
 * sempre que o banco subjacente mudar — padrão "single source of truth".
 */
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