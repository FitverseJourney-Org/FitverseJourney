package org.fitverse.domain.repository

import org.fitverse.domain.models.progression.consistencia.ConsistenciaData
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de domínio para acesso aos dados de consistência e streak do usuário.
 */
interface ConsistenciaRepository {

    /**
     * Emite o streak atual, o recorde e o grid das últimas 5 semanas.
     * Não depende de filtro de período — sempre retorna as últimas 5 semanas.
     */
    fun getConsistenciaData(): Flow<ConsistenciaData>
}
