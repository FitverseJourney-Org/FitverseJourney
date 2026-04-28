package com.example.domain.usecase.activatePlan

import com.example.domain.models.activePlan.PlanId
import com.example.domain.repository.ActivatePlanRepository

class ActivatePlanUseCase(
    private val activatePlanRepository: ActivatePlanRepository
){
    suspend operator fun invoke(planId: PlanId): Result<Unit> {
        return activatePlanRepository.activatePlan(planId)
    }
}