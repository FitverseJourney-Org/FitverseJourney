package org.fitverse.data.repository

import org.fitverse.domain.models.workoutPlan.DayOfWeek
import org.fitverse.domain.models.workoutPlan.PlanCreationType
import org.fitverse.domain.models.workoutPlan.ScheduleDay
import org.fitverse.domain.models.workoutPlan.WorkoutPlan
import org.fitverse.domain.repository.workoutPlan.WorkoutPlanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WorkoutPlanRepositoryImpl : WorkoutPlanRepository {

    private val _plans = MutableStateFlow(initialMockPlans())

    override fun getWorkoutPlans(): Flow<List<WorkoutPlan>> = _plans.asStateFlow()

    override suspend fun addWorkoutPlan(plan: WorkoutPlan) {
        if (_plans.value.size < MAX_PLANS) {
            _plans.value = _plans.value + plan
        }
    }

    override suspend fun deleteWorkoutPlan(planId: String) {
        _plans.value = _plans.value.filter { it.id != planId }
    }

    override suspend fun activateWorkoutPlan(planId: String) {
        _plans.value = _plans.value.map { plan ->
            plan.copy(isActive = plan.id == planId)
        }
    }

    override fun getMaxPlansAllowed(): Int = MAX_PLANS

    companion object {
        const val MAX_PLANS = 2

        private val fullWeekSchedule = listOf(
            ScheduleDay("S", isTrainingDay = true),
            ScheduleDay("T", isTrainingDay = true),
            ScheduleDay("Q", isTrainingDay = false), // rest
            ScheduleDay("Q", isTrainingDay = true),
            ScheduleDay("S", isTrainingDay = true),
            ScheduleDay("S", isTrainingDay = false), // rest
            ScheduleDay("D", isTrainingDay = false)  // rest
        )

        /** Mock plan to simulate "Criar com IA" */
        val mockAIPlan = WorkoutPlan(
            id = "2",
            name = "CUTTING — IA",
            isActive = false,
            creationType = PlanCreationType.AI,
            daysPerWeek = 4,
            trainingDays = listOf(DayOfWeek.MON, DayOfWeek.TUE, DayOfWeek.THU, DayOfWeek.FRI),
            restDays = listOf(DayOfWeek.WED, DayOfWeek.SAT, DayOfWeek.SUN),
            exercisesCount = 8,
            weeksCount = 12,
            scheduleDays = fullWeekSchedule
        )

        /** Mock plan to simulate "Criar Manualmente" */
        val mockManualPlan = WorkoutPlan(
            id = "3",
            name = "HIPERTROFIA MAX",
            isActive = false,
            creationType = PlanCreationType.MANUAL,
            daysPerWeek = 5,
            trainingDays = listOf(DayOfWeek.MON, DayOfWeek.TUE, DayOfWeek.WED, DayOfWeek.FRI, DayOfWeek.SAT),
            restDays = listOf(DayOfWeek.THU, DayOfWeek.SUN),
            exercisesCount = 15,
            weeksCount = 8,
            scheduleDays = listOf(
                ScheduleDay("S", true),
                ScheduleDay("T", true),
                ScheduleDay("Q", true),
                ScheduleDay("Q", false),
                ScheduleDay("S", true),
                ScheduleDay("S", true),
                ScheduleDay("D", false)
            )
        )

        private fun initialMockPlans() = emptyList<WorkoutPlan>()
    }
}
