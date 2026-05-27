package org.fitverse.project.destinations.workout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.fitverse.presentation.ui.workout.RecommendedWorkout
import org.fitverse.presentation.ui.workout.WorkoutDetailScreen
import org.fitverse.presentation.ui.workout.WorkoutScreen
import org.fitverse.presentation.ui.workout.viewmodel.WorkoutViewModel
import org.fitverse.presentation.widgets.DarkGamifiedDashboardBackground

@Composable
fun WorkoutDestination(
    viewModel: WorkoutViewModel,
    onStartWorkout: () -> Unit,
    workoutCompletedToday: Boolean = false,
    onDetailOpen: (Boolean) -> Unit = {},
) {
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    var selectedWorkout by remember { mutableStateOf<RecommendedWorkout?>(null) }

    LaunchedEffect(selectedWorkout) { onDetailOpen(selectedWorkout != null) }
    DisposableEffect(Unit) { onDispose { onDetailOpen(false) } }

    val defaultColor  = MaterialTheme.colorScheme.primary
    val targetColor   = selectedWorkout?.iconColor ?: defaultColor

    val accentColor by animateColorAsState(
        targetValue   = targetColor,
        animationSpec = tween(durationMillis = 600),
        label         = "workout_accent",
    )

    Box(modifier = Modifier.fillMaxSize()) {
        DarkGamifiedDashboardBackground(accentColor = accentColor)

        AnimatedContent(
            targetState  = selectedWorkout,
            transitionSpec = {
                if (targetState != null) {
                    (slideInVertically { it } + fadeIn(initialAlpha = 0.3f)) togetherWith
                    fadeOut(targetAlpha = 0f)
                } else {
                    fadeIn(initialAlpha = 0.3f) togetherWith
                    (slideOutVertically { it } + fadeOut(targetAlpha = 0f))
                }
            },
            label = "workout_nav",
        ) { workout ->
            if (workout != null) {
                WorkoutDetailScreen(
                    workout        = workout,
                    onBack         = { selectedWorkout = null },
                    onStartWorkout = onStartWorkout,
                )
            } else {
                WorkoutScreen(
                    onStartWorkout        = onStartWorkout,
                    workoutCompletedToday = workoutCompletedToday,
                    isRefreshing          = isRefreshing,
                    onRefresh             = { viewModel.refresh() },
                    onWorkoutTapped       = { selectedWorkout = it },
                )
            }
        }
    }
}
