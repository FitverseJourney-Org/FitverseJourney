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
import org.fitverse.project.destinations.workout.PlanWorkoutBuilderDestination
import org.fitverse.project.destinations.workout.PlanWorkoutExercisesDestination
import org.fitverse.project.destinations.workout.PlanWorkoutListDestination
import org.fitverse.project.routes.NavRoutes

@Composable
fun PlanWorkoutNavigation(
    toBack: () -> Unit
) {
    val rootBackStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.PlanWorkoutFlow.PlanList::class, NavRoutes.PlanWorkoutFlow.PlanList.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow.PlanBuilder::class, NavRoutes.PlanWorkoutFlow.PlanBuilder.serializer())
                }
            }
        },
        NavRoutes.PlanWorkoutFlow.PlanList
    )


    NavDisplay(
        modifier = Modifier,
        backStack = rootBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.PlanWorkoutFlow.PlanList> {
                PlanWorkoutListDestination(
                    toBack = {
                        toBack()
                    },
                    toNewWorkout = {
                        rootBackStack.add(NavRoutes.PlanWorkoutFlow.PlanBuilder)
                    }
                )
            }
            entry<NavRoutes.PlanWorkoutFlow.PlanBuilder>{
                PlanWorkoutBuilderDestination(
                    onBack = {
                        rootBackStack.removeLastOrNull()
                    },
                    toAddExercises = {
                        rootBackStack.add(NavRoutes.PlanWorkoutFlow.PlanExercises)
                    }
                )
            }
            entry<NavRoutes.PlanWorkoutFlow.PlanExercises>{
                PlanWorkoutExercisesDestination(
                    onBack = {
                        rootBackStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}