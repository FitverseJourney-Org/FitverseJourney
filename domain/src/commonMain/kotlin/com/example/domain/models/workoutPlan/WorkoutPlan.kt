package org.fitverse.domain.models.workoutPlan


data class WorkoutPlan(
    val id: String,
    val name: String,
    val isActive: Boolean,
    val creationType: PlanCreationType,
    val daysPerWeek: Int,
    val trainingDays: List<DayOfWeek>,
    val restDays: List<DayOfWeek>,
    val exercisesCount: Int,
    val weeksCount: Int?,
    val scheduleDays: List<ScheduleDay>
)

enum class PlanCreationType { MANUAL, AI }

enum class DayOfWeek(
    val fullName: String,
    val shortLabel: String
) {
    MON(fullName = "Segunda-feira", shortLabel = "S"),
    TUE(fullName = "Terça-feira",   shortLabel = "T"),
    WED(fullName = "Quarta-feira",  shortLabel = "Q"),
    THU(fullName = "Quinta-feira",  shortLabel = "Q"),
    FRI(fullName = "Sexta-feira",   shortLabel = "S"),
    SAT(fullName = "Sábado",        shortLabel = "S"),
    SUN(fullName = "Domingo",       shortLabel = "D");
}

