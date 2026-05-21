package org.fitverse.data.repository.progression

import org.fitverse.domain.models.progression.LoadProgressionPoint
import org.fitverse.domain.repository.ProgressionRepository
import org.fitverse.data.local.datasource.progression.ProgressionLocalDataSource
import org.fitverse.data.local.mapper.progression.toDomain
import org.fitverse.data.local.model.ProgressionPointEntity
import org.fitverse.data.repository.mapper.progression.toDto
import org.fitverse.data.repository.mapper.progression.toEntity
import org.fitverse.data.remote.datasource.progression.ProgressionRemoteDataSource
import org.fitverse.data.remote.dto.progression.ProgressionPointDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ProgressionRepositoryImpl(
    private val localDataSource: ProgressionLocalDataSource,
    private val remoteDataSource: ProgressionRemoteDataSource,
) : ProgressionRepository {

    private val syncScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun getProgressionPoints(exerciseId: String): Flow<List<LoadProgressionPoint>> =
        flow {
            emitAll(
                localDataSource
                    .observeByExercise(exerciseId)
                    .map { entities -> entities.toDomain() }
            )
        }.also {
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
                exerciseId = exerciseId,
                year       = year - 1,
                startMonth = startMonth,
                endMonth   = endMonth,
            )
            .map { entities -> entities.toDomain() }
            .catch { emit(emptyList()) }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun saveProgressionPoint(
        exerciseId: String,
        point: LoadProgressionPoint,
    ): Result<LoadProgressionPoint> = runCatching {
        val localId = Uuid.random().toString()
        val dto = point.toDto(id = localId, exerciseId = exerciseId)

        localDataSource.insert(dto.toEntity())  // Domain → Dto → Entity
        syncScope.launch { syncPointToRemote(dto) }
        point
    }

    private suspend fun refreshFromRemote(exerciseId: String) {
        remoteDataSource
            .fetchProgressionPoints(exerciseId)
            .onSuccess { remoteDtos ->
                val entities = remoteDtos.map { dto ->
                    ProgressionPointEntity(
                        id = dto.id,
                        exerciseId = dto.exerciseId,
                        epochMillis = dto.epochMillis,
                        weightKg = dto.weightKg,
                        estimatedOneRm = dto.estimatedOneRm,
                        reps = dto.reps,
                        sets = dto.sets,
                    )
                }
                localDataSource.upsertAll(entities)
            }
    }

    private suspend fun syncPointToRemote(dto: ProgressionPointDto) {
        remoteDataSource.postProgressionPoint(dto)
    }
}