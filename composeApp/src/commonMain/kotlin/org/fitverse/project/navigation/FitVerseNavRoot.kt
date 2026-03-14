package org.fitverse.project.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.presentation.screens.ui.main.nutrition.AddManualMealScreen
import com.example.presentation.screens.ui.main.nutrition.MealPeriod
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.navigation.destinations.DashboardDestination
import org.fitverse.project.navigation.destinations.NotificationDestination
import org.fitverse.project.navigation.destinations.modal.AchievementsModalDestination
import org.fitverse.project.navigation.destinations.modal.DevicesModalDestination
import org.fitverse.project.navigation.destinations.modal.HelpSupportModalDestination
import org.fitverse.project.navigation.destinations.nutrition.NutritionDestination
import org.fitverse.project.navigation.destinations.PlanDestination
import org.fitverse.project.navigation.destinations.ProfileDestination
import org.fitverse.project.navigation.destinations.modal.FriendsModalDestination
import org.fitverse.project.navigation.destinations.modal.HistoricModalDestination
import org.fitverse.project.navigation.destinations.modal.LeaderboardsModalDestination
import org.fitverse.project.navigation.destinations.modal.MealsPlanDestination
import org.fitverse.project.navigation.destinations.modal.TasksModalDestination
import org.fitverse.project.navigation.destinations.modal.WorkoutCreateDestination
import org.fitverse.project.navigation.destinations.workout.WorkoutDestination
import org.fitverse.project.navigation.destinations.workout.WorkoutSessionDestination
import org.fitverse.project.navigation.navRoutesFlows.HomeFlow

@Composable
fun FitVerseNavRoot() {

    val config = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(NavRoutes.SplashScreen::class, NavRoutes.SplashScreen.serializer())
                subclass(NavRoutes.OnboardingScreen::class, NavRoutes.OnboardingScreen.serializer())
                subclass(NavRoutes.LoginScreen::class, NavRoutes.LoginScreen.serializer())
                subclass(NavRoutes.RegisterScreen::class, NavRoutes.RegisterScreen.serializer())
                subclass(NavRoutes.ForgotPasswordScreen::class, NavRoutes.ForgotPasswordScreen.serializer())
                subclass(NavRoutes.DashboardScreen::class, NavRoutes.DashboardScreen.serializer())
                subclass(NavRoutes.ProfileScreen::class, NavRoutes.ProfileScreen.serializer())
                subclass(NavRoutes.WorkoutScreen::class, NavRoutes.WorkoutScreen.serializer())
                subclass(NavRoutes.NutritionScreen::class, NavRoutes.NutritionScreen.serializer())
                // adicione subclasses adicionais conforme necessário
            }
        }
    }

    var isAuthenticated by remember { mutableStateOf(false) }
    var goNext by remember { mutableStateOf(false) }


//    if (!isAuthenticated) {
//        if(!goNext){
//            RootFlow(config = config, goNext = {goNext = !goNext})
//        }else {
//            AuthFlow(config = config, onAuthSuccess = { isAuthenticated = true })
//        }
//    } else {
//
//    }
    HomeFlow(config = config, onSignOut = { isAuthenticated = false })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentFlowApp(
    config: SavedStateConfiguration,
    start: NavKey = NavRoutes.DashboardScreen,
    homeBackStack: MutableList<NavKey> = rememberNavBackStack(config, start),
    tabs: List<NavKey>,
) {
    val cs = MaterialTheme.colorScheme
    Scaffold(

        bottomBar = {

            val currentKey = homeBackStack.lastOrNull()

            val showBottomBar = when (currentKey) {
                is NavRoutes.DashboardScreen,
                is NavRoutes.WorkoutScreen,
                is NavRoutes.NutritionScreen,
                is NavRoutes.ProfileScreen -> true
                else -> false
            }

            AnimatedVisibility(
                visible = showBottomBar,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = 200
                    )
                )
            ) {
                NavigationBar(
                    containerColor = cs.surfaceVariant,
                    tonalElevation = 6.dp
                ) {
                    tabs.forEach { tabKey ->

                        val selected = currentKey?.let { it::class == tabKey::class } == true

                        val (label, iconVector) = when (tabKey) {
                            is NavRoutes.DashboardScreen -> "Dashboard" to Icons.Default.Home
                            is NavRoutes.WorkoutScreen -> "Workout" to Icons.Default.FitnessCenter
                            is NavRoutes.NutritionScreen -> "Nutrition" to Icons.Default.LocalDining
                            is NavRoutes.ProfileScreen -> "Profile" to Icons.Default.Person
                            else -> "" to Icons.Default.Home
                        }

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    homeBackStack.add(tabKey)
                                }
                            },
                            icon = {
                                Icon(iconVector, label)
                            },
                            label = {
                                Text(label)
                            }
                        )
                    }
                }
            }
        }
    ) {
        NavDisplay(
            modifier = Modifier.padding(it),
            onBack = {
                val consumed = handleHomeBackPress(homeBackStack)
                println("homeBackStack onBack: $consumed")
                if (!consumed) {
                    homeBackStack.removeLastOrNull()
                }
            },
            backStack = homeBackStack,
            transitionSpec = {
                EnterTransition.None togetherWith ExitTransition.None
            },
            popTransitionSpec = {
                // Slide in from left when navigating back
                slideInHorizontally(initialOffsetX = { -it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { it })
            },
            predictivePopTransitionSpec = {
                // Slide in from left when navigating back
                slideInHorizontally(initialOffsetX = { -it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { it })
            },
            entryProvider = { key ->
                when (key) {
                    // home bottom
                    is NavRoutes.DashboardScreen -> NavEntry(key) {
                        DashboardDestination(
                            navigateToNotification = { homeBackStack.add(NavRoutes.NotificationScreen) }
                        )
                    }
                    is NavRoutes.WorkoutScreen -> NavEntry(key) {
                        WorkoutDestination(
                            navigateToWorkoutSession = { homeBackStack.add(NavRoutes.WorkoutSession) }

                        )
                    }
                    is NavRoutes.NutritionScreen -> NavEntry(key) {
                        NutritionDestination(
                            navigateToAddMeal = { period ->
                                homeBackStack.add(NavRoutes.AddMeal(period.name))
                            }
                        )
                    }
                    is NavRoutes.ProfileScreen -> NavEntry(key) {
                        ProfileDestination(
                            navigateToPlans = { homeBackStack.add(NavRoutes.PlanScreen) },
                            onSignOut = {

                            }
                        )
                    }


                    is NavRoutes.NotificationScreen -> NavEntry(key) {
                        NotificationDestination(
                            navigateToDashboard = { homeBackStack.removeLastOrNull()})
                    }
                    is NavRoutes.PlanScreen -> NavEntry(key) {
                        PlanDestination(
                            navigateToProfile = {
                                homeBackStack.removeLastOrNull()
                            }
                        )
                    }
                    is NavRoutes.WorkoutSession -> NavEntry(key) {
                        WorkoutSessionDestination(
                            navigateToWorkoutSession = { homeBackStack.removeLastOrNull() }
                        )
                    }
                    is NavRoutes.AddMeal -> NavEntry(key = key) { key ->
                        // 1. Recupera o período passado via argumento (ex: "BREAKFAST")

                        AddManualMealScreen(
                            initialPeriod = key,
                            onNavigateBack = {

                            },
                            onSaveMeal = { newMeal ->

                            }
                        )
                    }

                    // modal screens
                    is NavRoutes.ActionsWorkout -> NavEntry(key) {
                        WorkoutCreateDestination(
                            navigateBack = { homeBackStack.removeLastOrNull() }
                        )
                    }
                    is NavRoutes.ActionsNutrition -> NavEntry(key) {
                        MealsPlanDestination(
                            navigateBack = { homeBackStack.removeLastOrNull() }
                        )
                    }
                    is NavRoutes.ActionsTasks -> NavEntry(key) {
                        TasksModalDestination(
                            navigateBack = { homeBackStack.removeLastOrNull() }
                        )
                    }
                    is NavRoutes.ActionsFriends -> NavEntry(key) {
                        FriendsModalDestination(
                            navigateBack = { homeBackStack.removeLastOrNull() }
                        )
                    }
                    is NavRoutes.ActionsLeaderboards -> NavEntry(key) {
                        LeaderboardsModalDestination(
                            navigateBack = { homeBackStack.removeLastOrNull() }
                        )
                    }
                    is NavRoutes.ActionsHistoric -> NavEntry(key) {
                        HistoricModalDestination(
                            navigateBack = { homeBackStack.removeLastOrNull() }
                        )
                    }
                    is NavRoutes.ActionsAchievements -> NavEntry(key) {
                        AchievementsModalDestination(
                            navigateBack = { homeBackStack.removeLastOrNull() }
                        )
                    }

                    // preferences modal
                    is NavRoutes.PreferencesDevicesConnect -> NavEntry(key) {
                        DevicesModalDestination(
                            navigateBack = { homeBackStack.removeLastOrNull() }
                        )
                    }
                    is NavRoutes.PreferencesHelpSupport -> NavEntry(key) {
                        HelpSupportModalDestination()
                    }
                    else -> error("HomeFlow: rota desconhecida: $key")
                }
            }
        )
    }
}






fun handleHomeBackPress(homeBackStack: MutableList<NavKey>): Boolean {
    val current = homeBackStack.lastOrNull() ?: return false

    return when (current) {
        is NavRoutes.DashboardScreen -> {
            false
        }

        is NavRoutes.WorkoutScreen,
        is NavRoutes.NutritionScreen,
        is NavRoutes.ProfileScreen -> {
            homeBackStack.clear()
            homeBackStack.add(NavRoutes.DashboardScreen)
            true
        }

        else -> {
            homeBackStack.removeLastOrNull()
            true
        }
    }
}