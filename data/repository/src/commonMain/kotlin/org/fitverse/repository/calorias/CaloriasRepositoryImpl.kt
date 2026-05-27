package org.fitverse.data.repository.calorias

import org.fitverse.domain.models.progression.calorias.CaloriasData
import org.fitverse.domain.repository.CaloriasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CaloriasRepositoryImpl : CaloriasRepository {

    override fun getCaloriasData(periodDays: Int): Flow<CaloriasData> = flowOf(
        CaloriasData(
            avgDaily      = 2_182,
            changePercent = -1,
            today         = 2_473,
            goal          = 2_200,
            streak        = 0,
            history       = listOf(
                2100f, 2350f, 2000f, 2420f, 2200f, 2100f,
                2300f, 2473f, 2150f, 2400f, 2250f, 2182f,
            ),
        )
    )
}
