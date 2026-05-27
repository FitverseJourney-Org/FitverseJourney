package org.fitverse.domain.usecase.progression

import org.fitverse.domain.models.progression.ProgressionData
import org.fitverse.domain.repository.ProgressionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetProgressionDataUseCase(
    private val progressionRepository: ProgressionRepository,
) {
    operator fun invoke(
        exerciseId: String,
        periodDays: Int,
    ): Flow<Result<ProgressionData>> =
        progressionRepository.getProgressionPoints(exerciseId)
            .map { points ->
                val filtered = if (periodDays >= 365) points else points.takeLast(periodDays)
                Result.success(ProgressionData(current = filtered, previous = emptyList()))
            }
            .catch { emit(Result.failure(it)) }
}
