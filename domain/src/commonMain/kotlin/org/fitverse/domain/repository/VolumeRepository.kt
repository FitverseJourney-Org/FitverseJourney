package org.fitverse.domain.repository

import org.fitverse.domain.models.progression.volume.VolumeData
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de domínio para acesso aos dados de volume de treino.
 */
interface VolumeRepository {

    /**
     * Emite o volume total e o heatmap de intensidade para os últimos [periodDays] dias.
     *
     * @param periodDays Janela de tempo em dias (ex.: 7, 30, 90, [Int.MAX_VALUE] para tudo).
     */
    fun getVolumeData(periodDays: Int): Flow<VolumeData>
}
