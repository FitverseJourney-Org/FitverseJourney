package org.fitverse.data.repository.activePlan

import org.fitverse.domain.models.PlanId
import org.fitverse.domain.repository.ActivatePlanRepository
import org.fitverse.data.remote.datasource.activePlan.ActivatePlanRemoteDataSource

class ActivatePlanRepositoryImpl(
    private val remoteDataSource: ActivatePlanRemoteDataSource,
) : ActivatePlanRepository {

    override suspend fun activatePlan(planId: PlanId): Result<Unit> {
        return remoteDataSource.activatePlan(planId)
    }
}