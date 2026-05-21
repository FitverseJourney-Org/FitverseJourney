package org.fitverse.presentation.ui.meals.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.FoodItemRecord
import org.fitverse.domain.usecase.meals.AddFoodToMealUseCase
import org.fitverse.presentation.expect.DateFormatter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface AddManualFoodIntent {
    data class SaveFood(
        val mealId: String,
        val name: String,
        val portion: Double,
        val unit: String,
        val kcal: Int,
        val protein: Double,
        val carbs: Double,
        val fat: Double,
    ) : AddManualFoodIntent
}

sealed interface AddManualFoodEvent {
    data object FoodSaved : AddManualFoodEvent
    data class ShowError(val message: String) : AddManualFoodEvent
}

class AddManualFoodViewModel(
    private val addFoodToMeal: AddFoodToMealUseCase,
) : ViewModel() {

    private val _events = Channel<AddManualFoodEvent>()
    val events = _events.receiveAsFlow()

    fun onIntent(intent: AddManualFoodIntent) {
        when (intent) {
            is AddManualFoodIntent.SaveFood -> saveFood(intent)
        }
    }

    private fun saveFood(intent: AddManualFoodIntent.SaveFood) {
        viewModelScope.launch {
            val now = DateFormatter.getCurrentTimeMillis()
            val food = FoodItemRecord(
                id      = "${intent.mealId}_food_$now",
                mealId  = intent.mealId,
                name    = intent.name.trim(),
                portion = intent.portion,
                unit    = intent.unit,
                kcal    = intent.kcal,
                protein = intent.protein,
                carbs   = intent.carbs,
                fat     = intent.fat,
            )
            addFoodToMeal(food)
                .onSuccess { _events.send(AddManualFoodEvent.FoodSaved) }
                .onFailure { _events.send(AddManualFoodEvent.ShowError(it.message ?: "Erro ao salvar")) }
        }
    }
}
