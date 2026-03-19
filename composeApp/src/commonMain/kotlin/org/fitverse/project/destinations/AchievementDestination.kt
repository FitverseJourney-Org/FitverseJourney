package org.fitverse.project.destinations

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