package org.fitverse.domain.repository

import org.fitverse.domain.models.progression.cardio.CardioData
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de domínio para acesso às métricas cardiovasculares do usuário.
 */
interface CardioRepository {

    /**
     * Emite as métricas de cardio agregadas para os últimos [periodDays] dias.
     *
     * @param periodDays Janela de tempo em dias.
     */
    fun getCardioData(periodDays: Int): Flow<CardioData>
}
