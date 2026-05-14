package com.example.presentation.ui.meals.viewmodel

import com.example.domain.repository.dbLocal.sqldelight.nutrition.DailyMacros
import com.example.domain.repository.dbLocal.sqldelight.nutrition.MealEntryRecord

data class MealsUiState(
    val meals: List<MealEntryRecord> = emptyList(),
    val dailyMacros: DailyMacros? = null,
    val selectedDate: String = "",
    val isLoading: Boolean = true,
)

sealed interface MealsIntent {
    data class AddMeal(val record: MealEntryRecord) : MealsIntent
    data class DeleteMeal(val mealId: String) : MealsIntent
    data class SelectDate(val date: String) : MealsIntent
}

sealed interface MealsEvent
