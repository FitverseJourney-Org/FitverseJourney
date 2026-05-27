package org.fitverse.domain.usecase.progression.volume

import org.fitverse.domain.models.progression.volume.VolumeData
import org.fitverse.domain.repository.VolumeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetVolumeDataUseCase(
    private val volumeRepository: VolumeRepository,
) {
    operator fun invoke(periodDays: Int): Flow<Result<VolumeData>> =
        volumeRepository.getVolumeData(periodDays)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
}
