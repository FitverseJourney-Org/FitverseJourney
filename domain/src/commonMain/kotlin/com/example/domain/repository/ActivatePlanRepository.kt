package org.fitverse.domain.repository

import org.fitverse.domain.models.PlanId


interface ActivatePlanRepository {
    suspend fun activatePlan(planId:  PlanId): Result<Unit>
}