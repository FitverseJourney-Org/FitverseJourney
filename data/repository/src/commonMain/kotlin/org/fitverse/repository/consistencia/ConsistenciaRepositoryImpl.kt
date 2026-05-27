package org.fitverse.data.repository.consistencia

import org.fitverse.domain.models.progression.consistencia.ConsistenciaData
import org.fitverse.domain.repository.ConsistenciaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ConsistenciaRepositoryImpl : ConsistenciaRepository {

    override fun getConsistenciaData(): Flow<ConsistenciaData> = flowOf(
        ConsistenciaData(
            currentStreak  = 23,
            recordStreak   = 47,
            weekGrid       = listOf(
                listOf(false, true, true, true, true, true, true),
                listOf(false, true, true, true, true, true, true),
                listOf(true,  true, true, true, true, true, true),
                listOf(true,  true, true, true, true, true, true),
                listOf(true,  true, true, true, false, false, false),
            ),
            marcosProgress = 0.07f,
        )
    )
}
