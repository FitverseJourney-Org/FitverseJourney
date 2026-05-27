package org.fitverse.domain.usecase.workoutPlan

import org.fitverse.domain.models.workoutPlan.WorkoutPlan
import org.fitverse.domain.repository.workoutPlan.WorkoutPlanRepository
import kotlinx.coroutines.flow.Flow

class GetWorkoutPlansUseCase(private val repository: WorkoutPlanRepository) {
    operator fun invoke(): Flow<List<WorkoutPlan>> = repository.getWorkoutPlans()
}

class AddWorkoutPlanUseCase(private val repository: WorkoutPlanRepository) {
    suspend operator fun invoke(plan: WorkoutPlan) = repository.addWorkoutPlan(plan)
}

class DeleteWorkoutPlanUseCase(private val repository: WorkoutPlanRepository) {
    suspend operator fun invoke(planId: String) = repository.deleteWorkoutPlan(planId)
}

class ActivateWorkoutPlanUseCase(private val repository: WorkoutPlanRepository) {
    suspend operator fun invoke(planId: String) = repository.activateWorkoutPlan(planId)
}

/** Aggregates all workout plan use cases for DI convenience */
data class WorkoutPlanUseCases(
    val getWorkoutPlans: GetWorkoutPlansUseCase,
    val addPlan: AddWorkoutPlanUseCase,
    val deleteWorkoutPlan: DeleteWorkoutPlanUseCase,
    val activatePlan: ActivateWorkoutPlanUseCase
)
