package org.fitverse.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.homepage.workout.PlanWorkoutBuilderDestination
import org.fitverse.project.destinations.homepage.workout.PlanWorkoutExercisesDestination
import org.fitverse.project.destinations.homepage.workout.PlanWorkoutListDestination
import org.fitverse.project.routes.NavRoutes

@Composable
fun PlanWorkoutNavigation(
    toBack: () -> Unit
) {
    val rootBackStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.PlanWorkoutFlow.List::class, NavRoutes.PlanWorkoutFlow.List.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow.Builder::class, NavRoutes.PlanWorkoutFlow.Builder.serializer())
                }
            }
        },
        NavRoutes.PlanWorkoutFlow.List
    )


    NavDisplay(
        modifier = Modifier,
        backStack = rootBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.PlanWorkoutFlow.List> {
                PlanWorkoutListDestination(
                    toBack = {
                        toBack()
                    },
                    toNewWorkout = {
                        rootBackStack.add(NavRoutes.PlanWorkoutFlow.Builder)
                    }
                )
            }
            entry<NavRoutes.PlanWorkoutFlow.Builder>{
                PlanWorkoutBuilderDestination(
                    onBack = {
                        rootBackStack.removeLastOrNull()
                    },
                    toAddExercises = {
                        rootBackStack.add(NavRoutes.PlanWorkoutFlow.Exercises)
                    }
                )
            }
            entry<NavRoutes.PlanWorkoutFlow.Exercises>{
                PlanWorkoutExercisesDestination(
                    onBack = {
                        rootBackStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}