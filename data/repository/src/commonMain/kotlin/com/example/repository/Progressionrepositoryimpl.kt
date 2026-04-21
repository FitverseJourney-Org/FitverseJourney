package com.example.repository

import com.example.local.datasource.progression.ProgressionLocalDataSource
import com.example.remote.datasource.progression.ProgressionRemoteDataSource
import com.example.local.mapper.toDomain
import com.example.local.mapper.toDto
import com.example.data.model.dto.progression.ProgressionPointDto
import com.example.domain.model.progress.LoadProgressionPoint
import com.example.domain.repository.ProgressionRepository
import com.example.mapper.toDomain
import com.example.mapper.toDto
import kotlinx.coroutines.IO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Implementação concreta do [ProgressionRepository].
 *
 * ## Estratégia: Offline-First com Single Source of Truth
 *
 * ```
 * Remote API ──► LocalDataSource ──► Repository Flow ──► ViewModel
 *  (atualiza)     (fonte de verdade)   (re-emite)
 * ```
 *
 * 1. **Emissão imediata**: o Flow inicia emitindo dados do cache local —
 *    o usuário vê conteúdo instantaneamente, mesmo offline.
 * 2. **Refresh paralelo**: ao colecionar, dispara busca remota em background.
 * 3. **Upsert no cache**: novos dados remotos salvos localmente.
 * 4. **Re-emissão automática**: o Flow do banco emite os dados atualizados.
 *
 * ## Tratamento de erros
 * - Erros de rede são silenciados via `onFailure { }` — o cache continua válido.
 * - Erros de banco são capturados pelo `catch` do Flow e propagados como
 *   `Result.failure` para a ViewModel tratar.
 *
 * @param localDataSource  Fonte de dados local injetada.
 * @param remoteDataSource Fonte de dados remota injetada.
 */
class ProgressionRepositoryImpl(
    private val localDataSource: ProgressionLocalDataSource,
    private val remoteDataSource: ProgressionRemoteDataSource,
) : ProgressionRepository {

    /**
     * Escopo isolado com [SupervisorJob] para que falhas de sync remota
     * não cancelem o escopo pai (viewModelScope).
     */
    private val syncScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun getProgressionPoints(exerciseId: String): Flow<List<LoadProgressionPoint>> =
        flow {
            // 1. Cache local imediato
            emitAll(
                localDataSource
                    .observeByExercise(exerciseId)
                    .map { dtos -> dtos.toDomain() }
            )
        }.also {
            // 2. Refresh remoto em paralelo (não bloqueia a emissão do cache)
            syncScope.launch { refreshFromRemote(exerciseId) }
        }

    override fun getPreviousPeriodPoints(
        exerciseId: String,
        startMonth: Int,
        endMonth: Int,
        year: Int,
    ): Flow<List<LoadProgressionPoint>> =
        localDataSource
            .observeByExerciseAndYearPeriod(
                exerciseId   = exerciseId,
                year         = year - 1, // período comparativo = mesmo intervalo, ano anterior
                startMonth   = startMonth,
                endMonth     = endMonth,
            )
            .map { dtos -> dtos.toDomain() }
            .catch { emit(emptyList()) } // comparativo nunca quebra a UI

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun saveProgressionPoint(
        exerciseId: String,
        point: LoadProgressionPoint,
    ): Result<LoadProgressionPoint> = runCatching {
        val localId = Uuid.random().toString()
        val dto = point.toDto(id = localId, exerciseId = exerciseId)

        // Persistência local síncrona — garante que o dado não seja perdido offline
        localDataSource.insert(dto)

        // Sync remota disparada em background sem bloquear o retorno
        syncScope.launch { syncPointToRemote(dto) }

        point
    }

    // ── Helpers privados ──────────────────────────────────────────────────────

    private suspend fun refreshFromRemote(exerciseId: String) {
        remoteDataSource
            .fetchProgressionPoints(exerciseId)
            .onSuccess { remoteDtos -> localDataSource.upsertAll(remoteDtos) }
        // onFailure: silenciado — cache local continua sendo a fonte de verdade
    }

    private suspend fun syncPointToRemote(dto: ProgressionPointDto) {
        // Em produção: adicionar retry com WorkManager para garantia de entrega
        remoteDataSource.postProgressionPoint(dto)
    }
}