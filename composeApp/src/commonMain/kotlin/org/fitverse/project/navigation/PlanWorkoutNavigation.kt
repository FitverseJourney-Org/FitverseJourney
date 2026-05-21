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
import org.fitverse.presentation.ui.workoutPlan.WorkoutPlanBuilderRoute
import org.fitverse.presentation.ui.workoutPlan.WorkoutPlansRoot
import org.fitverse.presentation.ui.workoutPlan.viewmodel.WorkoutPlanBuilderViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.workout.WorkoutPlanExercisesDestination
import org.fitverse.project.destinations.workout.WorkoutPlanExercisesDetailsDestination
import org.fitverse.project.routes.NavRoutes
import org.koin.compose.koinInject

@Composable
fun PlanWorkoutNavigation(
    toBack: () -> Unit,
    modifier: Modifier
) {
    val rootBackStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.PlanWorkoutFlow.Plan::class, NavRoutes.PlanWorkoutFlow.Plan.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow.Builder::class, NavRoutes.PlanWorkoutFlow.Builder.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow.PlanList::class, NavRoutes.PlanWorkoutFlow.PlanList.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow.Exercises::class, NavRoutes.PlanWorkoutFlow.Exercises.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow.ExerciseDetails::class, NavRoutes.PlanWorkoutFlow.ExerciseDetails.serializer())
                }
            }
        },
        NavRoutes.PlanWorkoutFlow.PlanList
    )


    NavDisplay(
        modifier = modifier,
        backStack = rootBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.PlanWorkoutFlow.PlanList> {
                WorkoutPlansRoot(
                    onBack = toBack,
                    onEditPlan = {
                        val planId = it
                        rootBackStack.add(NavRoutes.PlanWorkoutFlow.Builder)
                    },
                    onNewPlan = {
                        rootBackStack.add(NavRoutes.PlanWorkoutFlow.Builder)
                    },
                )
            }
            entry<NavRoutes.PlanWorkoutFlow.Plan> {

            }
            entry<NavRoutes.PlanWorkoutFlow.Builder>{
                val viewmodel = koinInject<WorkoutPlanBuilderViewModel>()
                WorkoutPlanBuilderRoute(
                    viewmodel = viewmodel,
                    onBack = {
                        rootBackStack.removeLastOrNull()
                    },
                    onNavigateToAddExercise = {
                        rootBackStack.add(NavRoutes.PlanWorkoutFlow.Exercises)
                    }
                )
            }
            entry<NavRoutes.PlanWorkoutFlow.Exercises>{
                WorkoutPlanExercisesDestination(
                    onBack = {
                        rootBackStack.removeLastOrNull()
                    },
                    onAddExercise = {
                        rootBackStack.add(NavRoutes.PlanWorkoutFlow.Exercises)
                    },
                    onDetails = {
                        rootBackStack.add(NavRoutes.PlanWorkoutFlow.ExerciseDetails)
                    }
                )
            }
            entry<NavRoutes.PlanWorkoutFlow.ExerciseDetails>{
                WorkoutPlanExercisesDetailsDestination(
                    onBack = {
                        rootBackStack.removeLastOrNull()
                    },
                    onAddExercise = {

                    }
                )
            }
        }
    )
}