package org.fitverse.presentation.ui.workoutPlan

enum class DayOfWeek(val shortLabel: String, val fullName: String) {
    MON("SEG", "Segunda"),
    TUE("TER", "Terça"),
    WED("QUA", "Quarta"),
    THU("QUI", "Quinta"),
    FRI("SEX", "Sexta"),
    SAT("SÁB", "Sábado"),
    SUN("DOM", "Domingo");

    companion object {
        val ordered: List<DayOfWeek> = entries.toList()
    }
}
