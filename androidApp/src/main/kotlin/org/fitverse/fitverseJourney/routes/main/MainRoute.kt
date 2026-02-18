package org.fitverse.fitverseJourney.routes.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.domain.model.bottomNavBar.BottomNavItem
import com.example.presentation.core.utils.shouldShowBottomBar
import com.example.presentation.screens.main.nutrition.NutritionScreenV3
import com.example.presentation.screens.main.plans.AppPlansScreen
import com.example.presentation.screens.main.profile.ProfileScreenV2
import com.example.presentation.screens.main.workout.Exercise
import com.example.presentation.screens.main.workout.WorkoutPlan
import com.example.presentation.screens.main.workout.WorkoutSessionScreen
import com.example.presentation.theme.AccentGreen
import com.example.presentation.theme.BaseGreen
import com.example.presentation.theme.DeepGreen
import com.example.presentation.theme.backgroundBrush
import org.fitverse.fitverseJourney.navigation.NavGraph
import org.fitverse.fitverseJourney.navigation.NavGraphMain
import org.fitverse.fitverseJourney.navigation.Routes
import org.fitverse.project.presentation.ui.components.ModalDrawerSheetMainScreen


@Composable
fun MainScreen() {
    val navController: NavHostController = rememberNavController()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
//    val isInMain = currentRoute?.startsWith("main/") == true
    val isShow = shouldShowBottomBar(currentRoute)
    ModalDrawerSheetMainScreen(
        drawerState = drawerState,
        content = {
            Scaffold(
                bottomBar = {
                    if (isShow) {
                        MainBottomBar(navController)
                    }
                }
            ) { padding ->
                val modifier = Modifier.background(brush =
                    Brush.verticalGradient(
                        colors = arrayListOf(BaseGreen,
                            DeepGreen)
                    )
                ).padding(padding)
                NavHost(
                    navController = navController,
                    route = "main",
                    startDestination = NavGraphMain.DASHBOARD,
                    enterTransition = {
                        EnterTransition.None
                    },
                    exitTransition = {
                        ExitTransition.None
                    },
                    popEnterTransition = {
                        EnterTransition.None
                    },
                    popExitTransition = {
                        ExitTransition.None
                    }
                ) {
                    dashboardNavGraph(
                        modifier = modifier,
                        navController = navController
                    )
                    composable(
                        route="main/workout",
                    ) {
                        WorkoutRoute(
                            modifier = modifier,
                            onExit = { navController.popBackStack() },
                            onStart = {
                                navController.navigate(Routes.WorkoutSession.route)
                            }
                        )
                    }
                    composable(
                        route="main/nutrition",
                    ) {
                        NutritionScreenV3(modifier = modifier)
                    }
                    composable(
                        route="main/profile",
                    ) {
                        ProfileScreenV2(
                            modifier = modifier,
                            isPremium = true,
                            onChoosePackage = {
                                navController.navigate(Routes.Plans.route)
                            },
                            goToPlans = {
                                navController.navigate(Routes.Plans.route)
                            }
                        )
                    }
                }
            }
        }
    )
}

fun NavGraphBuilder.plansNavGraph(
    modifier: Modifier,
    navController: NavHostController
) {
    navigation(
        route = NavGraphMain.PLANS,
        startDestination = Routes.Plans.route
    ){
        composable(route = Routes.Plans.route){
            AppPlansScreen(modifier = modifier)
        }
    }
}

fun NavGraphBuilder.dashboardNavGraph(
    modifier: Modifier,
    navController: NavHostController
) {
    navigation(
        route = NavGraphMain.DASHBOARD,
        startDestination = "main/dashboard"
    ) {
        composable(route ="main/dashboard") {
            DashboardRoute(
                modifier = modifier,
                onExit = { navController.popBackStack() },
                navigateToNotification = {
                    navController.navigate("main/dashboard/notification")
                }
            )
        }
        // rota de notification (filha de dashboard)
        composable(route ="main/dashboard/notification") {
            NotificationRoute(
                modifier = modifier,
                onExit = { navController.popBackStack() },
            )
        }
    }
}

fun NavGraphBuilder.workoutNavGraph(
    modifier: Modifier,
    navController: NavHostController
) {
    navigation(
        route = NavGraphMain.WORKOUT,
        startDestination = Routes.Workout.route
    ) {
        composable(route = Routes.Workout.route) {
            WorkoutRoute(
                modifier = modifier,
                onExit = { navController.popBackStack() },
                onStart = {
                    navController.navigate(Routes.WorkoutSession.route)
                }
            )
        }

        composable(route = Routes.WorkoutSession.route) {
            val workoutPlan = WorkoutPlan(
                id = 0,
                title = "Upper Body Strength",
                exercises = listOf(
                    Exercise(id = 0, title = "Bench Press", sets = 3, reps = 10),
                    Exercise(id = 1, title = "Incline Bench Press", sets = 3, reps = 10),
                    Exercise(id = 2, title = "Chest Fly", sets = 3, reps = 13),
                    Exercise(id = 3, title = "Shoulder Press", sets = 3, reps = 15)
                )
            )
            WorkoutSessionScreen(
                modifier = modifier,
                workout = workoutPlan,
                onFinish = { /* implementar se necessário */ },
            )
        }
    }
}

fun NavGraphBuilder.nutritionNavGraph(
    modifier: Modifier,
    navController: NavHostController
) {
    navigation(
        route = NavGraphMain.NUTRITION,
        startDestination = Routes.Nutrition.route
    ) {
        composable(route = Routes.Nutrition.route) {
            NutritionScreenV3(modifier = modifier)
        }
    }
}

fun NavGraphBuilder.profileNavGraph(
    modifier: Modifier,
    navController: NavHostController
) {
    navigation(
        route = NavGraphMain.PROFILE,
        startDestination = Routes.Profile.route
    ) {
        composable(route = Routes.Profile.route) {
            ProfileScreenV2(
                modifier = modifier,
                isPremium = true,
                onChoosePackage = {
                    navController.navigate(Routes.Plans.route)
                },
                goToPlans = {
                    navController.navigate(Routes.Plans.route)
                }
            )
        }

        // se Profile tiver rota com argumento, registre também aqui:
        // composable(route = Routes.Profile.routeWithArg) { backStackEntry -> ... }
    }
}




@Composable
fun MainBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Só exibe se a rota atual pertence ao grafo principal
    if (currentRoute == null || !currentRoute.startsWith(Routes.MainRoot.route)) return

    val items = BottomNavItem.items

    NavigationBar(containerColor = DeepGreen) {
        items.forEach { item ->
            val selected = currentRoute == item.route || currentRoute.startsWith(item.route + "/")

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = {
                    Text(
                        text = item.label,
                        color = if (selected) AccentGreen else Color.White.copy(alpha = 0.6f)
                    )
                }
            )
        }
    }
}
