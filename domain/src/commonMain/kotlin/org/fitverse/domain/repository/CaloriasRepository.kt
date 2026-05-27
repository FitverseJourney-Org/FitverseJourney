package org.fitverse.domain.repository

import org.fitverse.domain.models.progression.calorias.CaloriasData
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de domínio para acesso ao histórico calórico do usuário.
 */
interface CaloriasRepository {

    /**
     * Emite os dados de consumo calórico para os últimos [periodDays] dias.
     *
     * @param periodDays Janela de tempo em dias.
     */
    fun getCaloriasData(periodDays: Int): Flow<CaloriasData>
}
