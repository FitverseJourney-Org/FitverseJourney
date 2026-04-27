package com.example.remote.datasource.activePlan

import com.example.domain.models.activePlan.PlanId

interface ActivatePlanRemoteDataSource {
    suspend fun activatePlan(planId: PlanId): Result<Unit>
}