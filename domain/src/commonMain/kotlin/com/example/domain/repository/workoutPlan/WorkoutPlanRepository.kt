package org.fitverse.domain.repository.workoutPlan

import org.fitverse.domain.models.workoutPlan.WorkoutPlan
import kotlinx.coroutines.flow.Flow

interface WorkoutPlanRepository {
    /** Emits the current list of plans whenever it changes */
    fun getWorkoutPlans(): Flow<List<WorkoutPlan>>

    /** Add a new plan (mock: inserts a predefined plan) */
    suspend fun addWorkoutPlan(plan: WorkoutPlan)

    /** Permanently removes a plan by id */
    suspend fun deleteWorkoutPlan(planId: String)

    /** Sets the given plan as active, deactivating all others */
    suspend fun activateWorkoutPlan(planId: String)

    /** Maximum number of simultaneous plans allowed */
    fun getMaxPlansAllowed(): Int
}