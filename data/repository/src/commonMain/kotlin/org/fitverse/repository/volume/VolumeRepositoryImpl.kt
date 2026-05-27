package org.fitverse.data.repository.volume

import org.fitverse.domain.models.progression.volume.VolumeData
import org.fitverse.domain.repository.VolumeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class VolumeRepositoryImpl : VolumeRepository {

    override fun getVolumeData(periodDays: Int): Flow<VolumeData> = flowOf(
        VolumeData(
            totalTons  = 86.4f,
            sessions   = 30,
            weeklyGrid = List(7) { r -> List(7) { c -> ((r * 3 + c * 2) % 11 / 10f).coerceIn(0f, 1f) } },
            rowLabels  = listOf("S", "0", "3", "4", "3", "5", "0"),
        )
    )
}
