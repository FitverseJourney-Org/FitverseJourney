package org.fitverse.project.destinations.meals

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.ui.meals.AddManualFoodScreen
import com.example.presentation.widgets.DarkGamifiedDashboardBackground

@Composable
fun AddManualFoodDestination(
    mealName: String,
    onBack: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        DarkGamifiedDashboardBackground()
        AddManualFoodScreen(mealName = mealName, onBack = onBack)
    }
}
