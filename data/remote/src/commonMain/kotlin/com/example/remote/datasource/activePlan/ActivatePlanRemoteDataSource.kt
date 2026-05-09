package com.example.remote.datasource.activePlan

import com.example.domain.models.plan.PlanId

interface ActivatePlanRemoteDataSource {
    suspend fun activatePlan(planId: PlanId): Result<Unit>
}