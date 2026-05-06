package org.fitverse.project.destinations.homepage.meals


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.ui.meals.MealsScreen
import com.example.presentation.widgets.DarkGamifiedDashboardBackground

@Composable
fun MealsDestination(onBottomSheetOpen: (Boolean) -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize()){
        DarkGamifiedDashboardBackground()
        MealsScreen(onBottomSheetOpen = onBottomSheetOpen)
    }
}