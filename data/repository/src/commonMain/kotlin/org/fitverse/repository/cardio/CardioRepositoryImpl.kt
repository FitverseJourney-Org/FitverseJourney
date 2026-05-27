package org.fitverse.data.repository.cardio

import org.fitverse.domain.models.progression.cardio.CardioData
import org.fitverse.domain.repository.CardioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CardioRepositoryImpl : CardioRepository {

    override fun getCardioData(periodDays: Int): Flow<CardioData> = flowOf(
        CardioData(
            paceMin         = 5,
            paceSec         = 22,
            changePercent   = 9,
            vo2Max          = 48,
            distKm          = 42.0f,
            aerobicZoneFrac = 0.78f,
            paceHistory     = listOf(6.2f, 6.1f, 5.95f, 5.85f, 5.75f, 5.65f, 5.55f, 5.45f, 5.35f, 5.22f),
        )
    )
}
