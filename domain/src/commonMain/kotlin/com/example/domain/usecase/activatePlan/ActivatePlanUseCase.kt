package org.fitverse.domain.usecase.activatePlan

import org.fitverse.domain.models.PlanId
import org.fitverse.domain.repository.ActivatePlanRepository

class ActivatePlanUseCase(
    private val activatePlanRepository: ActivatePlanRepository
){
    suspend operator fun invoke(planId: PlanId): Result<Unit> {
        return activatePlanRepository.activatePlan(planId)
    }
}