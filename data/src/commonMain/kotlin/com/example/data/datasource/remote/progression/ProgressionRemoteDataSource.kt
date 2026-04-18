package com.example.data.datasource.remote.progression

import com.example.data.model.dto.progression.ProgressionPointDto

// ─────────────────────────────────────────────────────────────────────────────
// Interface
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Contrato para a fonte de dados remota de progressão de carga.
 *
 * Em produção, implementado com **Ktor Client** (KMP) fazendo chamadas
 * à API REST do Fitverse. Em testes, substituído por [ProgressionRemoteDataSourceImpl].
 *
 * ## Política de erros
 * Todos os métodos `suspend` retornam [Result] em vez de lançar exceções,
 * deixando a decisão de tratamento para o [ProgressionRepositoryImpl].
 */
interface ProgressionRemoteDataSource {

    /**
     * Busca todos os pontos de progressão de um exercício no servidor.
     * Retorna lista vazia (não erro) quando o exercício não tem histórico.
     *
     * @param exerciseId ID do exercício.
     */
    suspend fun fetchProgressionPoints(
        exerciseId: String,
    ): Result<List<ProgressionPointDto>>

    /**
     * Busca pontos filtrados por período específico (performance — evita
     * transferir anos de histórico quando só um mês é necessário).
     *
     * @param exerciseId  ID do exercício.
     * @param startEpoch  Início do período (Unix millis, inclusive).
     * @param endEpoch    Fim do período (Unix millis, inclusive).
     */
    suspend fun fetchProgressionPointsByPeriod(
        exerciseId: String,
        startEpoch: Long,
        endEpoch: Long,
    ): Result<List<ProgressionPointDto>>

    /**
     * Envia um novo ponto de progressão ao servidor.
     * Chamado após persistência local bem-sucedida (offline-first).
     *
     * @return O DTO com ID e campos gerados pelo servidor, ou [Result.failure].
     */
    suspend fun postProgressionPoint(
        point: ProgressionPointDto,
    ): Result<ProgressionPointDto>
}