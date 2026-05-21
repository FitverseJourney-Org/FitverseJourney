package org.fitverse.project.destinations.meals

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import org.fitverse.presentation.ui.meals.AddManualFoodScreen
import org.fitverse.presentation.ui.meals.viewmodel.AddManualFoodEvent
import org.fitverse.presentation.ui.meals.viewmodel.AddManualFoodIntent
import org.fitverse.presentation.ui.meals.viewmodel.AddManualFoodViewModel
import org.fitverse.presentation.widgets.DarkGamifiedDashboardBackground

@Composable
fun AddManualFoodDestination(
    mealId: String,
    mealName: String,
    viewModel: AddManualFoodViewModel,
    onBack: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AddManualFoodEvent.FoodSaved  -> onBack()
                is AddManualFoodEvent.ShowError  -> { /* TODO: snackbar */ }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        DarkGamifiedDashboardBackground()
        AddManualFoodScreen(
            mealName = mealName,
            onSave   = { name, portion, unit, kcal, protein, carbs, fat ->
                viewModel.onIntent(
                    AddManualFoodIntent.SaveFood(
                        mealId  = mealId,
                        name    = name,
                        portion = portion,
                        unit    = unit,
                        kcal    = kcal,
                        protein = protein,
                        carbs   = carbs,
                        fat     = fat,
                    )
                )
            },
            onBack = onBack,
        )
    }
}
