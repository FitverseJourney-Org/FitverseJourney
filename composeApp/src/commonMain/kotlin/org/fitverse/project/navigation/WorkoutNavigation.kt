package org.fitverse.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.presentation.ui.workout.WorkoutCompletionResult
import com.example.presentation.ui.workout.viewmodel.WorkoutSessionViewModel
import com.example.presentation.ui.workout.viewmodel.WorkoutViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.homepage.workout.WorkoutCompletedDestination
import org.fitverse.project.destinations.homepage.workout.WorkoutDestination
import org.fitverse.project.destinations.homepage.workout.WorkoutSessionDestination
import org.fitverse.project.routes.NavRoutes
import org.koin.compose.koinInject

@Composable
fun WorkoutNavigation(
    onFullScreen: (Boolean) -> Unit = {}
) {
    val backStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.HomeFlow.Workout::class,                NavRoutes.HomeFlow.Workout.serializer())
                    subclass(NavRoutes.WorkoutFlow.WorkoutSession::class,      NavRoutes.WorkoutFlow.WorkoutSession.serializer())
                    subclass(NavRoutes.WorkoutFlow.WorkoutCompleted::class,    NavRoutes.WorkoutFlow.WorkoutCompleted.serializer())
                }
            }
        },
        NavRoutes.HomeFlow.Workout
    )

    var lastWorkoutResult by remember { mutableStateOf<WorkoutCompletionResult?>(null) }

    val fullScreenRoutes = remember {
        setOf<NavKey>(NavRoutes.WorkoutFlow.WorkoutSession, NavRoutes.WorkoutFlow.WorkoutCompleted)
    }
    val isFullScreen = backStack.lastOrNull() in fullScreenRoutes

    LaunchedEffect(isFullScreen) { onFullScreen(isFullScreen) }

    DisposableEffect(Unit) { onDispose { onFullScreen(false) } }

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.HomeFlow.Workout> {
                val viewModel = koinInject<WorkoutViewModel>()
                WorkoutDestination(
                    onStartWorkout = { backStack.add(NavRoutes.WorkoutFlow.WorkoutSession) }
                )
            }
            entry<NavRoutes.WorkoutFlow.WorkoutSession> {
                val viewModel = koinInject<WorkoutSessionViewModel>()
                WorkoutSessionDestination(
                    toCompletedWorkout = { result ->
                        lastWorkoutResult = result
                        backStack.add(NavRoutes.WorkoutFlow.WorkoutCompleted)
                    }
                )
            }
            entry<NavRoutes.WorkoutFlow.WorkoutCompleted> {
                lastWorkoutResult?.let { result ->
                    WorkoutCompletedDestination(
                        result = result,
                        onContinue = {
                            lastWorkoutResult = null
                            backStack.removeAll {
                                it == NavRoutes.WorkoutFlow.WorkoutCompleted ||
                                it == NavRoutes.WorkoutFlow.WorkoutSession
                            }
                        }
                    )
                }
            }
        }
    )
}
