package org.fitverse.project.destinations.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.navigation3.runtime.NavKey
import com.example.presentation.screens.ui.modal.achievements.AchievementsScreen

@Composable
fun TasksModalDestination(
    navigateBack: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(cs.surface, cs.background)))){
        AchievementsScreen(
            navigateBack = navigateBack
        )
    }
}