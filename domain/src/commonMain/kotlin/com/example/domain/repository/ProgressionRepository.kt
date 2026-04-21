package com.example.domain.repository

import com.example.domain.model.progress.LoadProgressionPoint
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de domínio para acesso aos dados de progressão de carga.
 *
 * O repositório entrega **todos os pontos históricos** de um exercício.
 * O filtro por período é responsabilidade dos **UseCases** — mantendo
 * o repositório livre de lógica de negócio e fácil de testar.
 *
 * ## Estratégia de dados (implementada na camada de dados)
 * - Primeira emissão: cache local (Room/SQLDelight) → resposta imediata.
 * - Segunda emissão (opcional): dados atualizados da API remota.
 * - Isso garante que a UI exiba dados mesmo offline.
 */
interface ProgressionRepository {

    /**
     * Emite todos os pontos de progressão de um exercício específico,
     * ordenados cronologicamente (mais antigo → mais recente).
     *
     * @param exerciseId ID do exercício a consultar.
     */
    fun getProgressionPoints(exerciseId: String): Flow<List<LoadProgressionPoint>>

    /**
     * Emite os pontos do **mesmo exercício** no **mesmo intervalo de meses**
     * do **ano anterior**, usados como linha comparativa no gráfico.
     *
     * @param exerciseId  ID do exercício.
     * @param startMonth  Mês inicial do intervalo (1 = Janeiro).
     * @param endMonth    Mês final do intervalo (12 = Dezembro).
     * @param year        Ano de referência; a implementação busca `year - 1`.
     */
    fun getPreviousPeriodPoints(
        exerciseId: String,
        startMonth: Int,
        endMonth: Int,
        year: Int,
    ): Flow<List<LoadProgressionPoint>>

    /**
     * Persiste um novo ponto de progressão localmente.
     * A sincronização remota é tratada pela camada de dados via WorkManager/sync.
     *
     * @return O ponto salvo com ID gerado, ou falha encapsulada em [Result].
     */
    suspend fun saveProgressionPoint(
        exerciseId: String,
        point: LoadProgressionPoint,
    ): Result<LoadProgressionPoint>
}