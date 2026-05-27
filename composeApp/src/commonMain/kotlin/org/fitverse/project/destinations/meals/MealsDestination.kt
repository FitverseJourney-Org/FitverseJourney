package org.fitverse.project.destinations.meals

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.fitverse.presentation.ui.meals.MealsScreen
import org.fitverse.presentation.ui.meals.viewmodel.MealsViewModel

@Composable
fun MealsDestination(
    viewModel: MealsViewModel,
    onBottomSheetOpen: (Boolean) -> Unit = {},
    onNavigateToAddManualFood: (mealId: String, mealName: String) -> Unit = { _, _ -> },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MealsScreen(
        uiState                = uiState,
        onIntent               = viewModel::onIntent,
        isRefreshing           = uiState.isRefreshing,
        onRefresh              = { viewModel.refresh() },
        onBottomSheetOpen      = onBottomSheetOpen,
        onNavigateToManualFood = { mealId, mealName -> onNavigateToAddManualFood(mealId, mealName) },
    )
}
