package org.fitverse.domain.usecase.progression.rank

import org.fitverse.domain.models.progression.rank.RankDomain
import org.fitverse.domain.repository.RankRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetRankInfoUseCase(
    private val rankRepository: RankRepository,
) {
    operator fun invoke(): Flow<Result<RankDomain>> =
        rankRepository.getRankInfo()
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
}
