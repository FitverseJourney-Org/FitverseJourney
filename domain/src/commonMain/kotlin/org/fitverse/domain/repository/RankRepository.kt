package org.fitverse.domain.repository

import org.fitverse.domain.models.progression.rank.RankDomain
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de domínio para acesso aos dados de gamificação do usuário.
 *
 * A implementação concreta vive na camada de dados e é injetada via DI.
 * Reemite automaticamente quando XP ou rank do usuário mudar.
 */
interface RankRepository {

    /**
     * Emite o estado atual de rank e XP do usuário.
     * Reemite sempre que um treino ganha XP ou o usuário sobe de nível.
     */
    fun getRankInfo(): Flow<RankDomain>
}
