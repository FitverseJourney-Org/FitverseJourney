package org.fitverse.domain.usecase.progression.cardio

import org.fitverse.domain.models.progression.cardio.CardioData
import org.fitverse.domain.repository.CardioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetCardioDataUseCase(
    private val cardioRepository: CardioRepository,
) {
    operator fun invoke(periodDays: Int): Flow<Result<CardioData>> =
        cardioRepository.getCardioData(periodDays)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
}
