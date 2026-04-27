package org.fitverse.project.destinations.homepage.meals


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.ui.meals.MealsScreen
import org.fitverse.project.destinations.homepage.dashboad.DarkGamifiedDashboardBackground

@Composable
fun MealsDestination() {
    Box(modifier = Modifier.fillMaxSize()){
        DarkGamifiedDashboardBackground()
        MealsScreen()
    }
}