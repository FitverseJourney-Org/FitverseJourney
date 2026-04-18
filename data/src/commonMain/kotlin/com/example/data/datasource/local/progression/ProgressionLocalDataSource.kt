package com.example.data.datasource.local.progression

import com.example.data.model.dto.progression.ProgressionPointDto
import kotlinx.coroutines.flow.Flow

/**
 * Contrato para a fonte de dados local de pontos de progressão de carga.
 *
 * Em produção: implementado com **SQLDelight** (KMP) ou **Room** (Android).
 * Em testes: [ProgressionLocalDataSourceImpl] com dados em memória.
 */
interface ProgressionLocalDataSource {

    /** Emite todos os pontos de um exercício, ordenados por data (ASC). */
    fun observeByExercise(exerciseId: String): Flow<List<ProgressionPointDto>>

    /**
     * Emite pontos filtrados por exercício, ano e intervalo de meses.
     * Usado para buscar dados do período comparativo (ano anterior).
     *
     * @param year       Ano exato (ex.: 2023).
     * @param startMonth Mês inicial, 1-based (1 = Jan).
     * @param endMonth   Mês final, 1-based (12 = Dez).
     */
    fun observeByExerciseAndYearPeriod(
        exerciseId: String,
        year: Int,
        startMonth: Int,
        endMonth: Int,
    ): Flow<List<ProgressionPointDto>>

    /** Insere um único ponto (nova sessão registrada pelo usuário). */
    suspend fun insert(point: ProgressionPointDto)

    /** Insere ou atualiza múltiplos pontos vindos do servidor (sync). */
    suspend fun upsertAll(points: List<ProgressionPointDto>)

    /** Remove todos os pontos de um exercício ao excluí-lo. */
    suspend fun deleteByExercise(exerciseId: String)
}