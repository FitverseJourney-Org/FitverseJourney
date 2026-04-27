package com.example.remote.api

import com.example.remote.dto.activePlans.ActivatePlanRequest

interface PlanApi {
    suspend fun activatePlan(request: ActivatePlanRequest) : Result<Unit>
}