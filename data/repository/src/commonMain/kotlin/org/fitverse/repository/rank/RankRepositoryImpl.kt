package org.fitverse.data.repository.rank

import org.fitverse.domain.models.progression.rank.RankDomain
import org.fitverse.domain.repository.RankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RankRepositoryImpl : RankRepository {

    override fun getRankInfo(): Flow<RankDomain> = flowOf(
        RankDomain(
            rank       = "PRATA",
            currentXp  = 8_548,
            nextRank   = "OURO",
            xpToNext   = 6_468,
        )
    )
}
