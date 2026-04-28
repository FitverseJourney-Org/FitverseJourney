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
import com.example.domain.models.workout.workout_plan.WorkoutPlanItem
import com.example.domain.models.workout.workout_plan.WorkoutScreenState
import com.example.presentation.screens.ui.planWorkout.WorkoutAiPlanGenerationDestination
import com.example.presentation.screens.ui.planWorkout.WorkoutPlanListScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.homepage.workout.WorkoutPlanBuilderDestination
import org.fitverse.project.destinations.homepage.workout.WorkoutPlanExercisesDestination
import org.fitverse.project.destinations.homepage.workout.WorkoutPlanDestination
import org.fitverse.project.destinations.homepage.workout.WorkoutPlanExercisesDetailsDestination
import org.fitverse.project.routes.NavRoutes

@Composable
fun PlanWorkoutNavigation(
    toBack: () -> Unit
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
        modifier = Modifier,
        backStack = rootBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<NavRoutes.PlanWorkoutFlow.PlanList> {
                WorkoutPlanListScreen(
                    state = WorkoutScreenState(
                        objective = "Hipertrofia Muscular",
                        experienceLevel = "Intermediário",
                        workoutsPerWeek = 5,
                        completedThisWeek = 3,
                        availablePlans = listOf(
                            WorkoutPlanItem(
                                id = "1",
                                title = "Treino de Bateria",
                                frequency = "A cada 2 semanas",
                                intensity = "Alta",
                                isActive = false,
                                progress = 54f
                            ),
                            WorkoutPlanItem(
                                id = "2",
                                title = "Treino de Força",
                                frequency = "A cada semana",
                                intensity = "Moderada",
                                isActive = false,
                                progress = 0f
                            )
                        ),
                        activePlan = null
                    ),
                    onBack = { toBack() },
                    onSelectedPlan = { rootBackStack.add(NavRoutes.PlanWorkoutFlow.Plan) },
                    onNavigateToManualCreation = {
                        // Rota para criação manual
                        rootBackStack.add(NavRoutes.PlanWorkoutFlow.Builder)
                    },
                    onNavigateToAiCreation = {
                        // Nova rota para a IA
                        rootBackStack.add(NavRoutes.PlanWorkoutFlow.PlanIA)
                    },
                    onActivatePlan = {
                        // Lógica para ativar um plano
                    }
                )
            }
            entry<NavRoutes.PlanWorkoutFlow.Plan> {
                WorkoutPlanDestination(
                    onBack = {
                        rootBackStack.removeLastOrNull()
                    },
                    toNewWorkout = {
                        rootBackStack.add(NavRoutes.PlanWorkoutFlow.Builder)
                    }
                )
            }
            entry<NavRoutes.PlanWorkoutFlow.Builder>{
                WorkoutPlanBuilderDestination(
                    onBack = {
                        rootBackStack.removeLastOrNull()
                    },
                    toAddExercises = {
                        rootBackStack.add(NavRoutes.PlanWorkoutFlow.Exercises)
                    },
                    savePlan = {
                        rootBackStack.removeLastOrNull()
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
            entry<NavRoutes.PlanWorkoutFlow.PlanIA> {
                WorkoutAiPlanGenerationDestination(
                    onBack = { rootBackStack.removeLastOrNull() },
                    onPlanAccepted = {
                        // Quando o usuário aceita o plano, você pode levá-lo para a lista
                        // ou direto para os detalhes do plano gerado.
                        rootBackStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}