package com.example.remote.datasource.activePlan

import com.example.domain.models.activePlan.PlanId

class ActivatePlanRemoteDataSourceImpl : ActivatePlanRemoteDataSource {

    override suspend fun activatePlan(planId: PlanId): Result<Unit> =
        runCatching {
            return Result.success(Unit)
        }
}