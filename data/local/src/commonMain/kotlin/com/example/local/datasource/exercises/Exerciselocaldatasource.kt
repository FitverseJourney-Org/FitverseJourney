package com.example.local.datasource.exercises

import com.example.local.model.ExerciseEntity
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
    fun observeAll(): Flow<List<ExerciseEntity>>
    fun observeByTrainingSplit(trainingSplit: String): Flow<List<ExerciseEntity>>
    fun observeTrainingSplits(): Flow<List<String>>
    suspend fun findById(id: String): ExerciseEntity?
    suspend fun upsertAll(entities: List<ExerciseEntity>)
}