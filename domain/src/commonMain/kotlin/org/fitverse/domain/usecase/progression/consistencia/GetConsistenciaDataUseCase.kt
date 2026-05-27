package org.fitverse.domain.usecase.progression.consistencia

import org.fitverse.domain.models.progression.consistencia.ConsistenciaData
import org.fitverse.domain.repository.ConsistenciaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetConsistenciaDataUseCase(
    private val consistenciaRepository: ConsistenciaRepository,
) {
    operator fun invoke(): Flow<Result<ConsistenciaData>> =
        consistenciaRepository.getConsistenciaData()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
}
