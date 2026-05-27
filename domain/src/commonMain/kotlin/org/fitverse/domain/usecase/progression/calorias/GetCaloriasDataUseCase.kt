package org.fitverse.domain.usecase.progression.calorias

import org.fitverse.domain.models.progression.calorias.CaloriasData
import org.fitverse.domain.repository.CaloriasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetCaloriasDataUseCase(
    private val caloriasRepository: CaloriasRepository,
) {
    operator fun invoke(periodDays: Int): Flow<Result<CaloriasData>> =
        caloriasRepository.getCaloriasData(periodDays)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
}
