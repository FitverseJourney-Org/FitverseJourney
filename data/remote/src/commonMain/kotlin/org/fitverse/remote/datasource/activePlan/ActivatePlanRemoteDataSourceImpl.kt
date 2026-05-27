package org.fitverse.data.remote.datasource.activePlan

import org.fitverse.domain.models.PlanId

class ActivatePlanRemoteDataSourceImpl : ActivatePlanRemoteDataSource {

    override suspend fun activatePlan(planId: PlanId): Result<Unit> =
        runCatching {
            return Result.success(Unit)
        }
}