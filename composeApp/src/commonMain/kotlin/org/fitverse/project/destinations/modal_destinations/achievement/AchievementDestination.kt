package org.fitverse.project.destinations.modal_destinations.achievement

import androidx.compose.runtime.Composable
import com.example.presentation.screens.ui.achievements.AchievementsScreen

@Composable
fun AchievementDestination(toBack: () -> Unit) {
    AchievementsScreen(
        navigateBack = {
            toBack()
        }
    )
}