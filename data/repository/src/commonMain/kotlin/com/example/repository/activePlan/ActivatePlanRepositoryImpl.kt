package com.example.repository.activePlan

import com.example.domain.models.activePlan.PlanId
import com.example.domain.repository.ActivatePlanRepository
import com.example.remote.datasource.activePlan.ActivatePlanRemoteDataSource

class ActivatePlanRepositoryImpl(
    private val remoteDataSource: ActivatePlanRemoteDataSource,
) : ActivatePlanRepository {

    override suspend fun activatePlan(planId: PlanId): Result<Unit> {
        return remoteDataSource.activatePlan(planId)
    }
}