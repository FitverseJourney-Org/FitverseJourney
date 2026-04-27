package com.example.domain.repository

import com.example.domain.models.activePlan.PlanId


interface ActivatePlanRepository {
    suspend fun activatePlan(planId:  PlanId): Result<Unit>
}