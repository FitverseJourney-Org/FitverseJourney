package org.fitverse.project.navigation

import HistoricDestination
import LeaderboardsDestination
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.presentation.screens.ui.achievements.viewmodel.AchievementsViewModel
import com.example.presentation.screens.ui.community.CommunityScreen
import com.example.presentation.screens.ui.friends.viewmodel.FriendsViewModel
import com.example.presentation.screens.ui.historic.viewmodel.HistoricViewModel
import com.example.presentation.screens.ui.meals.viewmodel.NutritionViewModel
import com.example.presentation.screens.ui.notification.NotificationViewModel
import com.example.presentation.screens.ui.progress.viewmodel.ProgressViewModel
import com.example.presentation.screens.ui.wiki.viewmodel.WikiViewModel
import com.example.presentation.screens.ui.workout.viewmodel.WorkoutSessionViewModel
import com.example.presentation.screens.ui.workout.viewmodel.WorkoutViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.homepage.community.AddPostDestination
import org.fitverse.project.destinations.homepage.dashboad.DashboardDestination
import org.fitverse.project.destinations.homepage.dashboad.NotificationDestination
import org.fitverse.project.destinations.homepage.meals.MealsDestination
import org.fitverse.project.destinations.homepage.profile.ProfileDestination
import org.fitverse.project.destinations.homepage.workout.WorkoutDestination
import org.fitverse.project.destinations.homepage.workout.WorkoutSessionDestination
import org.fitverse.project.destinations.modal_destinations.achievement.AchievementDestination
import org.fitverse.project.destinations.modal_destinations.device.DevicesDestination
import org.fitverse.project.destinations.modal_destinations.friends.FriendsDestination
import org.fitverse.project.destinations.modal_destinations.helpSupport.HelpSupportDestination
import org.fitverse.project.destinations.modal_destinations.progress.ProgressDestination
import org.fitverse.project.destinations.modal_destinations.tasks.TasksDestination
import org.fitverse.project.destinations.payments.PlanPaymentDestination
import org.fitverse.project.destinations.shopping.ShoppingDestination
import org.fitverse.project.destinations.wiki.WikiFitnessDestination
import org.fitverse.project.routes.FitverseColors
import org.fitverse.project.routes.NavRoutes
import org.koin.compose.koinInject

@Composable
fun HomeNavigation() {
    val rootBackStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    // ── Home Flow ─────────────────────────────────────────
                    subclass(NavRoutes.HomeFlow.Dashboard::class,              NavRoutes.HomeFlow.Dashboard.serializer())
                    subclass(NavRoutes.HomeFlow.Workout::class,                NavRoutes.HomeFlow.Workout.serializer())
                    subclass(NavRoutes.HomeFlow.Community::class,              NavRoutes.HomeFlow.Community.serializer())
                    subclass(NavRoutes.HomeFlow.Nutrition::class,              NavRoutes.HomeFlow.Nutrition.serializer())
                    subclass(NavRoutes.HomeFlow.Profile::class,                NavRoutes.HomeFlow.Profile.serializer())
                    subclass(NavRoutes.HomeFlow.AddPost::class,                NavRoutes.HomeFlow.AddPost.serializer())
                    subclass(NavRoutes.HomeFlow.NotificationScreen::class,     NavRoutes.HomeFlow.NotificationScreen.serializer())
                    // ── Workout Flow ──────────────────────────────────────
                    subclass(NavRoutes.WorkoutFlow.WorkoutSession::class,      NavRoutes.WorkoutFlow.WorkoutSession.serializer())
                    subclass(NavRoutes.WorkoutFlow.WorkoutCompleted::class,    NavRoutes.WorkoutFlow.WorkoutCompleted.serializer())
                    // ── Plan Workout Flow ─────────────────────────────────
                    subclass(NavRoutes.PlanWorkoutFlow::class,                 NavRoutes.PlanWorkoutFlow.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow.PlanList::class,        NavRoutes.PlanWorkoutFlow.PlanList.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow.Plan::class,            NavRoutes.PlanWorkoutFlow.Plan.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow.Builder::class,         NavRoutes.PlanWorkoutFlow.Builder.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow.Exercises::class,       NavRoutes.PlanWorkoutFlow.Exercises.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow.ExerciseDetails::class, NavRoutes.PlanWorkoutFlow.ExerciseDetails.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow.PlanIA::class,          NavRoutes.PlanWorkoutFlow.PlanIA.serializer())
                    // ── Tasks Flow ────────────────────────────────────────
                    subclass(NavRoutes.TasksFlow::class,                       NavRoutes.TasksFlow.serializer())
                    subclass(NavRoutes.TasksFlow.TasksList::class,             NavRoutes.TasksFlow.TasksList.serializer())
                    subclass(NavRoutes.TasksFlow.TasksLibrary::class,          NavRoutes.TasksFlow.TasksLibrary.serializer())
                    // ── Telas avulsas ─────────────────────────────────────
                    subclass(NavRoutes.WikiFitness::class,                     NavRoutes.WikiFitness.serializer())
                    subclass(NavRoutes.Shopping::class,                        NavRoutes.Shopping.serializer())
                    subclass(NavRoutes.Historic::class,                        NavRoutes.Historic.serializer())
                    subclass(NavRoutes.Progress::class,                        NavRoutes.Progress.serializer())
                    subclass(NavRoutes.Achievements::class,                    NavRoutes.Achievements.serializer())
                    subclass(NavRoutes.Leaderboards::class,                    NavRoutes.Leaderboards.serializer())
                    subclass(NavRoutes.Friends::class,                         NavRoutes.Friends.serializer())
                    subclass(NavRoutes.Devices::class,                         NavRoutes.Devices.serializer())
                    subclass(NavRoutes.HelpSupport::class,                     NavRoutes.HelpSupport.serializer())
                    subclass(NavRoutes.PlanPayment::class,                     NavRoutes.PlanPayment.serializer())
                }
            }
        },
        NavRoutes.HomeFlow.Dashboard
    )

    val bottomBarItems = listOf(
        NavRoutes.HomeFlow.Dashboard,
        NavRoutes.HomeFlow.Workout,
        NavRoutes.HomeFlow.Community,
        NavRoutes.HomeFlow.Nutrition,
        NavRoutes.HomeFlow.Profile,
    )

    val showBottomBar = rootBackStack.lastOrNull() in bottomBarItems

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalDrawerSheetMainScreen(
        drawerState = drawerState,
        gesturesEnabled = rootBackStack.lastOrNull() in bottomBarItems,
        onNavigate = {
            rootBackStack.add(it)
            coroutineScope.launch { drawerState.close() }
        },
        onLogout = {
            rootBackStack.clear()
        },
        content = {
            Scaffold(
                containerColor = FitverseColors.BgDark,
                bottomBar = {
                    AnimatedVisibility(
                        visible = showBottomBar,
                        enter = fadeIn(animationSpec = tween(250, easing = FastOutSlowInEasing)),
                        exit  = fadeOut(animationSpec = tween(200))
                    ) {
                        FitVerseBottomBar(items = bottomBarItems, backStack = rootBackStack)
                    }
                }
            ) { innerPadding ->
                NavDisplay(
                    modifier = Modifier.padding(innerPadding),
                    backStack = rootBackStack,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    onBack = {
                        val consumed = handleHomeBackPress(rootBackStack)
                        if (!consumed) rootBackStack.removeLastOrNull()
                    },
                    entryProvider = entryProvider {

                        entry<NavRoutes.HomeFlow.Dashboard> {
                            DashboardDestination(
                                toNotification = { rootBackStack.add(NavRoutes.HomeFlow.NotificationScreen) }
                            )
                        }
                        entry<NavRoutes.HomeFlow.Workout> {
                            val viewModel = koinInject<WorkoutViewModel>()
                            WorkoutDestination(
                                //viewModel = viewModel
                            )
                        }
                        entry<NavRoutes.HomeFlow.Community> {
                            CommunityScreen()
                        }
                        entry<NavRoutes.HomeFlow.Nutrition> {
                            val viewModel = koinInject<NutritionViewModel>()
                            MealsDestination(
                                //viewModel = viewModel
                            )
                        }
                        entry<NavRoutes.HomeFlow.Profile> {
                            ProfileDestination()
                        }
                        entry<NavRoutes.HomeFlow.AddPost> {
                            AddPostDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.HomeFlow.NotificationScreen> {
                            val viewModel = koinInject<NotificationViewModel>()
                            NotificationDestination(
                                //viewModel = viewModel,
                                toDashboard = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.WorkoutFlow.WorkoutSession> {
                            val viewModel = koinInject<WorkoutSessionViewModel>()
                            WorkoutSessionDestination(
                                //viewModel = viewModel,
                                toCompletedWorkout = { rootBackStack.add(NavRoutes.WorkoutFlow.WorkoutCompleted) }
                            )
                        }
                        entry<NavRoutes.Historic> {
                            val viewModel = koinInject<HistoricViewModel>()
                            HistoricDestination(
                                //viewModel = viewModel,
                                navigateBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.WikiFitness> {
                            val viewModel = koinInject<WikiViewModel>()
                            WikiFitnessDestination(
                                viewModel = viewModel,
                                onBack = { rootBackStack.removeLastOrNull() },
                                onNavigateToArticle = {}
                            )
                        }
                        entry<NavRoutes.Friends> {
                            val viewModel = koinInject<FriendsViewModel>()
                            FriendsDestination(
                                viewModel = viewModel,
                                onBack = { rootBackStack.removeLastOrNull() },
                                onNavigateToQrScanner = {}
                            )
                        }
                        entry<NavRoutes.Shopping> {
                            ShoppingDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Progress> {
                            val viewModel = koinInject<ProgressViewModel>()
                            ProgressDestination(
                                viewmodel = viewModel,
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Achievements> {
                            val viewModel = koinInject<AchievementsViewModel>()
                            AchievementDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Leaderboards> {
                            val viewModel = koinInject<AchievementsViewModel>()
                            LeaderboardsDestination(
                                navigateBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Devices> {
                            DevicesDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.HelpSupport> {
                            HelpSupportDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.PlanPayment> {
                            PlanPaymentDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.PlanWorkoutFlow> {
                            PlanWorkoutNavigation(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.TasksFlow> {
                            TasksDestination(
                                toBack = { rootBackStack.removeLastOrNull() },
                                toLibrary = { rootBackStack.add(NavRoutes.TasksFlow.TasksLibrary) },
                            )
                        }
                    }
                )
            }
        }
    )
}




fun handleHomeBackPress(backStack: MutableList<NavKey>): Boolean {
    return when (backStack.lastOrNull()) {
        NavRoutes.HomeFlow.Dashboard -> false
        NavRoutes.HomeFlow.Workout,
        NavRoutes.HomeFlow.Community,
        NavRoutes.HomeFlow.Nutrition,
        NavRoutes.HomeFlow.Profile -> {
            backStack.clear()
            backStack.add(NavRoutes.HomeFlow.Dashboard) // ✅ volta para Dashboard
            true
        }
        else -> { backStack.removeLastOrNull(); true }
    }
}

@Composable
fun FitVerseBottomBar(items: List<NavKey>, backStack: MutableList<NavKey>) {
    val currentDestination = backStack.lastOrNull()
    val haptic = LocalHapticFeedback.current

    Surface(
        color = Color(0xFF05050a).copy(alpha = 0.95f),
        border = BorderStroke(
            width = 0.5.dp,
            brush = Brush.horizontalGradient(
                listOf(Color.Transparent, FitverseColors.Accent.copy(alpha = 0.25f), Color.Transparent)
            )
        ),
        shadowElevation = 20.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier.navigationBarsPadding().height(72.dp)
        ) {
            items.forEach { item ->
                val isSelected = currentDestination == item
                val (label, icon) = when (item) {
                    NavRoutes.HomeFlow.Dashboard  -> "DASHBOARD" to Icons.Outlined.Dashboard
                    NavRoutes.HomeFlow.Workout    -> "TREINO"    to Icons.Outlined.FitnessCenter
                    NavRoutes.HomeFlow.Community  -> "SOCIAL"    to Icons.Outlined.Groups
                    NavRoutes.HomeFlow.Nutrition  -> "NUTRIÇÃO"  to Icons.Outlined.Restaurant
                    NavRoutes.HomeFlow.Profile    -> "PERFIL"    to Icons.Outlined.Person
                    else -> return@forEach
                }
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            // ✅ Substitui em vez de empilhar
                            backStack.removeAll { it in items }
                            backStack.add(item)
                        }
                    },
                    icon = { Icon(icon, label, Modifier.size(22.dp)) },
                    label = {
                        Text(
                            label,
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor   = FitverseColors.Accent,
                        selectedTextColor   = FitverseColors.Accent,
                        indicatorColor      = FitverseColors.Accent.copy(alpha = 0.12f),
                        unselectedIconColor = FitverseColors.TextMuted.copy(alpha = 0.5f),
                        unselectedTextColor = FitverseColors.TextMuted.copy(alpha = 0.5f)
                    )
                )
            }
        }
    }
}