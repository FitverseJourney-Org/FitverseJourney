package org.fitverse.fitverseJourney.routes.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.screens.main.workout.WorkoutScreenV2

@Composable
fun WorkoutRoute(
    modifier: Modifier,
    onExit: () -> Unit,
    onStart: () -> Unit,
) {
    WorkoutScreenV2(
        modifier = modifier,
        onStart = onStart
    )
}