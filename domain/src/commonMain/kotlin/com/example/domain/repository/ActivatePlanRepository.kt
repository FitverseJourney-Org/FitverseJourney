package com.example.domain.repository

import com.example.domain.models.plan.PlanId


interface ActivatePlanRepository {
    suspend fun activatePlan(planId:  PlanId): Result<Unit>
}