package org.fitverse.presentation.ui.meals.viewmodel

import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.DailyMacros
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.FoodItemRecord
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.MealEntryRecord

data class MealsUiState(
    val meals: List<MealEntryRecord> = emptyList(),
    val foodsByMeal: Map<String, List<FoodItemRecord>> = emptyMap(),
    val dailyMacros: DailyMacros? = null,
    val selectedDate: String = "",
    val isLoading: Boolean = true,
)

sealed interface MealsIntent {
    data class CreateMeal(val name: String) : MealsIntent
    data class AddMeal(val record: MealEntryRecord) : MealsIntent
    data class DeleteMeal(val mealId: String) : MealsIntent
    data class SelectDate(val date: String) : MealsIntent
}

sealed interface MealsEvent
