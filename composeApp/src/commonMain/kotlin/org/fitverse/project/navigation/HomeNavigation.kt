package org.fitverse.project.navigation

import HistoricDestination
import LeaderboardsDestination
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.AchievementDestination
import org.fitverse.project.destinations.AddPostDestination
import org.fitverse.project.destinations.CommunityDestination
import org.fitverse.project.destinations.DashboardDestination
import org.fitverse.project.destinations.DevicesDestination
import org.fitverse.project.destinations.MealsDestination
import org.fitverse.project.destinations.NotificationDestination
import org.fitverse.project.destinations.ProfileDestination
import org.fitverse.project.destinations.modal.FriendsModalDestination
import org.fitverse.project.destinations.ProgressDestination
import org.fitverse.project.destinations.workout.WorkoutDestination
import org.fitverse.project.routes.NavRoutes

@Composable
fun HomeNavigation() {
    val rootBackStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.HomeFlow.Dashboard::class, NavRoutes.HomeFlow.Dashboard.serializer())
                    subclass(NavRoutes.HomeFlow.Workout::class, NavRoutes.HomeFlow.Workout.serializer())
                    subclass(NavRoutes.HomeFlow.Meals::class, NavRoutes.HomeFlow.Meals.serializer())
                    subclass(NavRoutes.HomeFlow.Profile::class, NavRoutes.HomeFlow.Profile.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow::class, NavRoutes.PlanWorkoutFlow.serializer())
                    subclass(NavRoutes.TasksFlow::class, NavRoutes.TasksFlow.serializer())
                }
            }
        },
        NavRoutes.HomeFlow.Dashboard
    )

    val bottomBarItems = listOf(
        NavRoutes.HomeFlow.Dashboard,
        NavRoutes.HomeFlow.Workout,
        NavRoutes.HomeFlow.Community,
        NavRoutes.HomeFlow.Meals,
        NavRoutes.HomeFlow.Profile
    )


    val currentKey = rootBackStack.lastOrNull()
    val isMainScreen = when (currentKey) {
        is NavRoutes.HomeFlow.Dashboard,
        is NavRoutes.HomeFlow.Workout,
        is NavRoutes.HomeFlow.Community,
        is NavRoutes.HomeFlow.Meals,
        is NavRoutes.HomeFlow.Profile -> true
        else -> false
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    ModalDrawerSheetMainScreen(
        drawerState = if(isMainScreen) drawerState else rememberDrawerState(initialValue = DrawerValue.Closed),
        onNavigate = {
            rootBackStack.add(it)
        },
        gesturesEnabled = isMainScreen,
        content = {
            Scaffold(
                modifier = Modifier,
                bottomBar = {
                    val currentKey = rootBackStack.lastOrNull()

                    val showBottomBar = when (currentKey) {
                        is NavRoutes.HomeFlow.Dashboard,
                        is NavRoutes.HomeFlow.Workout,
                        is NavRoutes.HomeFlow.Community,
                        is NavRoutes.HomeFlow.Meals,
                        is NavRoutes.HomeFlow.Profile -> true
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
                        FitVerseBottomBar(
                            items = bottomBarItems,
                            backStack = rootBackStack
                        )
                    }
                }
            ){
                NavDisplay(
                    modifier = Modifier.padding(it),
                    backStack = rootBackStack,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    onBack = {
                        val consumed = handleHomeBackPress(rootBackStack)
                        if (!consumed) {
                            rootBackStack.removeLastOrNull()
                        }

                    },
                    entryProvider = entryProvider {
                        entry<NavRoutes.HomeFlow.Dashboard> {
                            DashboardDestination(
                                toNotification = {
                                    rootBackStack.add(NavRoutes.HomeFlow.NotificationScreen)
                                }
                            )
                        }
                        entry<NavRoutes.HomeFlow.Workout> {
                            WorkoutDestination(
                                toWorkoutSession = {}
                            )
                        }
                        entry<NavRoutes.HomeFlow.Community> {
                            CommunityDestination(
                                toAddPost = {
                                    rootBackStack.add(NavRoutes.HomeFlow.AddPost)
                                }
                            )
                        }
                        entry<NavRoutes.HomeFlow.AddPost> {
                            AddPostDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.HomeFlow.Meals> {
                            MealsDestination(
                                toAddMeal = {}
                            )
                        }
                        entry<NavRoutes.HomeFlow.Profile> {
                            ProfileDestination(
                                toPlans = {}
                            )
                        }
                        entry<NavRoutes.HomeFlow.NotificationScreen> {
                            NotificationDestination(
                                toDashboard = {
                                    rootBackStack.clear()
                                    rootBackStack.add(NavRoutes.HomeFlow.Dashboard)
                                }
                            )
                        }
                        entry<NavRoutes.PlanWorkoutFlow> {
                            PlanWorkoutNavigation(
                                toBack = {
                                    rootBackStack.removeLastOrNull()
                                }
                            )
                        }
                        entry<NavRoutes.TasksFlow> {
                            TasksNavigation(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Friends>{
                            FriendsModalDestination(
                                navigateBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Leaderboards>{
                            LeaderboardsDestination(
                                navigateBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Historic>{
                            HistoricDestination(
                                navigateBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Progress>{
                            ProgressDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Achievements>{
                            AchievementDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Devices>{
                            DevicesDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                    }
                )
            }
        }
    )
}


@Composable
fun FitVerseBottomBar(items: List<NavKey>, backStack: NavBackStack<NavKey>) {
    val currentDestination = backStack.lastOrNull()

    NavigationBar {
        items.forEach { item ->
            val label = when (item) {
                is NavRoutes.HomeFlow.Dashboard -> "Dashboard"
                is NavRoutes.HomeFlow.Workout -> "Workout"
                is NavRoutes.HomeFlow.Community -> "Community"
                is NavRoutes.HomeFlow.Meals -> "Meals"
                is NavRoutes.HomeFlow.Profile -> "Profile"
                else -> ""
            }

            val icon = when (item) {
                is NavRoutes.HomeFlow.Dashboard -> Icons.Default.Home
                is NavRoutes.HomeFlow.Workout -> Icons.Default.FitnessCenter
                is NavRoutes.HomeFlow.Community -> Icons.Default.Groups
                is NavRoutes.HomeFlow.Meals -> Icons.Default.Restaurant
                is NavRoutes.HomeFlow.Profile -> Icons.Default.Person
                else -> Icons.Default.Home
            }

            val isSelected = currentDestination == item

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        backStack.add(item)
                    }
                },
                icon = { Icon(imageVector = icon, contentDescription = label) },
                label = { Text(text = label) }
            )
        }
    }
}

fun handleHomeBackPress(homeBackStack: MutableList<NavKey>): Boolean {
    val current = homeBackStack.lastOrNull() ?: return false

    return when (current) {
        is NavRoutes.HomeFlow.Dashboard -> {
            false
        }

        is NavRoutes.HomeFlow.Workout,
        is NavRoutes.HomeFlow.Meals,
        is NavRoutes.HomeFlow.Community,
        is NavRoutes.HomeFlow.Profile -> {
            homeBackStack.clear()
            homeBackStack.add(NavRoutes.HomeFlow.Dashboard)
            true
        }

        else -> {
            homeBackStack.removeLastOrNull()
            true
        }
    }
}