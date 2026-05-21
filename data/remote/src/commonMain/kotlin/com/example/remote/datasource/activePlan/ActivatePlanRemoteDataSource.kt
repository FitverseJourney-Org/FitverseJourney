package org.fitverse.data.remote.datasource.activePlan

import org.fitverse.domain.models.PlanId

interface ActivatePlanRemoteDataSource {
    suspend fun activatePlan(planId: PlanId): Result<Unit>
}