package org.fitverse.project.destinations.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.components.background.PremiumGamifiedBackground
import com.example.presentation.screens.ui.main.workout.WorkoutScreenV2

@Composable
fun WorkoutDestination(
    toWorkoutSession: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()){
        PremiumGamifiedBackground()
        WorkoutScreenV2(
            onStart = {
                toWorkoutSession()
            }
        )

    }
}