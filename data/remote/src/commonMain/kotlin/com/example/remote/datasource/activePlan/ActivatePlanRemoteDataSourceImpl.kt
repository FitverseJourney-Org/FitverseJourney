package com.example.remote.datasource.activePlan

import com.example.domain.models.activePlan.PlanId
import com.example.remote.api.PlanApi
import com.example.remote.dto.activePlans.ActivatePlanRequest

class ActivatePlanRemoteDataSourceImpl(
    private val api: PlanApi,
) : ActivatePlanRemoteDataSource {

    override suspend fun activatePlan(planId: PlanId): Result<Unit> =
        runCatching {
            val request = ActivatePlanRequest(planId = planId.name.lowercase())
            val response = api.activatePlan(request)

            if (response.isSuccess) {
                error("Falha ao ativar plano: HTTP $response")
            }
        }
}