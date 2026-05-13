package org.fitverse.project.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Groups
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.domain.repository.authentication.AuthRepository
import com.example.presentation.theme.FVExtension
import com.example.presentation.theme.FitverseColors
import com.example.presentation.ui.achievements.viewmodel.AchievementsViewModel
import com.example.presentation.ui.friends.viewmodel.FriendsViewModel
import com.example.presentation.ui.historic.viewmodel.HistoricViewModel
import com.example.presentation.ui.leaderboards.viewmodel.LeaderboardsViewModel
import com.example.presentation.ui.progress.viewmodel.ProgressViewModel
import com.example.presentation.ui.wiki.viewmodel.WikiViewModel
import com.example.presentation.widgets.DarkGamifiedDashboardBackground
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.modal_destinations.achievement.AchievementDestination
import org.fitverse.project.destinations.modal_destinations.device.DevicesDestination
import org.fitverse.project.destinations.modal_destinations.friends.FriendsDestination
import org.fitverse.project.destinations.modal_destinations.helpSupport.HelpSupportDestination
import org.fitverse.project.destinations.modal_destinations.historic.HistoricDestination
import org.fitverse.project.destinations.modal_destinations.leaderboards.LeaderboardsDestination
import org.fitverse.project.destinations.modal_destinations.progress.ProgressDestination
import org.fitverse.project.destinations.payments.PlanPaymentDestination
import org.fitverse.project.destinations.modal_destinations.shopping.ShoppingDestination
import org.fitverse.project.destinations.modal_destinations.wiki.WikiFitnessDestination
import org.fitverse.project.routes.NavRoutes
import org.koin.compose.koinInject

@Composable
fun HomeNavigation(
    logout: () -> Unit,
    toLoadingLanguage: () -> Unit
) {
    val rootBackStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    // ── Tabs ──────────────────────────────────────────────────
                    subclass(NavRoutes.HomeFlow.Dashboard::class,  NavRoutes.HomeFlow.Dashboard.serializer())
                    subclass(NavRoutes.HomeFlow.Workout::class,    NavRoutes.HomeFlow.Workout.serializer())
                    subclass(NavRoutes.HomeFlow.Activity::class,   NavRoutes.HomeFlow.Activity.serializer())
                    subclass(NavRoutes.HomeFlow.Community::class,  NavRoutes.HomeFlow.Community.serializer())
                    subclass(NavRoutes.HomeFlow.Nutrition::class,  NavRoutes.HomeFlow.Nutrition.serializer())
                    subclass(NavRoutes.HomeFlow.Profile::class,    NavRoutes.HomeFlow.Profile.serializer())
                    // ── Telas avulsas (drawer) ────────────────────────────────
                    subclass(NavRoutes.WikiFitness::class,         NavRoutes.WikiFitness.serializer())
                    subclass(NavRoutes.Shopping::class,            NavRoutes.Shopping.serializer())
                    subclass(NavRoutes.Historic::class,            NavRoutes.Historic.serializer())
                    subclass(NavRoutes.Progress::class,            NavRoutes.Progress.serializer())
                    subclass(NavRoutes.Achievements::class,        NavRoutes.Achievements.serializer())
                    subclass(NavRoutes.Leaderboards::class,        NavRoutes.Leaderboards.serializer())
                    subclass(NavRoutes.Friends::class,             NavRoutes.Friends.serializer())
                    subclass(NavRoutes.Devices::class,             NavRoutes.Devices.serializer())
                    subclass(NavRoutes.HelpSupport::class,         NavRoutes.HelpSupport.serializer())
                    subclass(NavRoutes.PlanPayment::class,         NavRoutes.PlanPayment.serializer())
                    // ── Sub-navegações ────────────────────────────────────────
                    subclass(NavRoutes.NutritionAddManualFood::class, NavRoutes.NutritionAddManualFood.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow::class,     NavRoutes.PlanWorkoutFlow.serializer())
                    subclass(NavRoutes.TasksFlow::class,           NavRoutes.TasksFlow.serializer())
                }
            }
        },
        NavRoutes.HomeFlow.Dashboard
    )

    val bottomBarItems = listOf(
        NavRoutes.HomeFlow.Dashboard,
        NavRoutes.HomeFlow.Workout,
        NavRoutes.HomeFlow.Activity,
        NavRoutes.HomeFlow.Community,
        NavRoutes.HomeFlow.Nutrition,
    )

    var isMealsSheetOpen by remember { mutableStateOf(false) }
    var isWorkoutFullScreen by remember { mutableStateOf(false) }
    var isDashboardSubScreen by remember { mutableStateOf(false) }
    var isCommunitySubScreen by remember { mutableStateOf(false) }
    var isCommunitySheetOpen by remember { mutableStateOf(false) }
    var isNutritionSubScreen by remember { mutableStateOf(false) }
    val showBottomBar = rootBackStack.lastOrNull() in bottomBarItems &&
        !isMealsSheetOpen &&
        !isWorkoutFullScreen &&
        !isDashboardSubScreen &&
        !isCommunitySubScreen &&
        !isCommunitySheetOpen &&
        !isNutritionSubScreen

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val authRepository = koinInject<AuthRepository>()

    ModalDrawerSheetMainScreen(
        drawerState = drawerState,
        gesturesEnabled = showBottomBar,
        onNavigate = {
            rootBackStack.add(it)
            coroutineScope.launch { drawerState.close() }
        },
        onLogout = {
            coroutineScope.launch {
                authRepository.logout()
                logout()
            }
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()){
                if(showBottomBar) DarkGamifiedDashboardBackground()
                Scaffold(
                    containerColor = if(showBottomBar) Color.Transparent else FVExtension.bg,
                    bottomBar = {
                        AnimatedVisibility(
                            visible = showBottomBar,
                            enter = fadeIn(animationSpec = tween(250, easing = FastOutSlowInEasing)) + expandVertically(animationSpec = tween(250, easing = FastOutSlowInEasing)),
                            exit  = fadeOut(animationSpec = tween(200)) + shrinkVertically(animationSpec = tween(200))
                        ) {
                            FitVerseBottomBar(items = bottomBarItems, backStack = rootBackStack)
                        }
                    }
                ) { paddingValues ->
                    val subScreenModifier = Modifier.fillMaxSize().padding(top = paddingValues.calculateTopPadding())

                    val modifier = Modifier.background(FitverseColors.Bg).padding(paddingValues)
                    NavDisplay(
                        modifier = Modifier,
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
                            // ── Feature navigations (tabs) ────────────────────────
                            entry<NavRoutes.HomeFlow.Dashboard> {
                                DashboardNavigation(
                                    modifier = Modifier.padding(paddingValues),
                                    subScreenModifier = subScreenModifier,
                                    onSubScreenChange = { isDashboardSubScreen = it },
                                    onNavigateToWorkout = {
                                        rootBackStack.removeAll { it in bottomBarItems }
                                        rootBackStack.add(NavRoutes.HomeFlow.Workout)
                                    }
                                )
                            }
                            entry<NavRoutes.HomeFlow.Workout> {
                                WorkoutNavigation(
                                    modifier = Modifier.padding(paddingValues),
                                    subScreenModifier = subScreenModifier,
                                    onFullScreen = { isWorkoutFullScreen = it }
                                )
                            }
                            entry<NavRoutes.HomeFlow.Activity> {
                                org.fitverse.project.destinations.activity.ActivityDestination(
                                    modifier = Modifier.padding(paddingValues),
                                )
                            }
                            entry<NavRoutes.HomeFlow.Community> {
                                CommunityNavigation(
                                    modifier = Modifier.padding(paddingValues),
                                    subScreenModifier = subScreenModifier,
                                    onSubScreenChange  = { isCommunitySubScreen = it },
                                    onSheetStateChange = { isCommunitySheetOpen = it },
                                )
                            }
                            entry<NavRoutes.HomeFlow.Nutrition> {
                                NutritionNavigation(
                                    modifier = Modifier.padding(paddingValues),
                                    subScreenModifier = subScreenModifier,
                                    onSheetStateChange = { isMealsSheetOpen = it },
                                    onSubScreenChange = { isNutritionSubScreen = it }
                                )
                            }
                            entry<NavRoutes.HomeFlow.Profile> {
                                ProfileNavigation(
                                    modifier = modifier,
                                    onBack = { rootBackStack.removeLastOrNull() },
                                )
                            }
                            // ── Telas avulsas (drawer) ────────────────────────────
                            entry<NavRoutes.Historic> {
                                val viewModel = koinInject<HistoricViewModel>()
                                HistoricDestination(
                                    viewModel    = viewModel,
                                    modifier     = modifier,
                                    navigateBack = { rootBackStack.removeLastOrNull() },
                                )
                            }
                            entry<NavRoutes.WikiFitness> {
                                val viewModel = koinInject<WikiViewModel>()
                                WikiFitnessDestination(
                                    modifier = modifier,
                                    viewModel = viewModel,
                                    onBack = { rootBackStack.removeLastOrNull() },
                                    onNavigateToArticle = {}
                                )
                            }
                            entry<NavRoutes.Friends> {
                                val viewModel = koinInject<FriendsViewModel>()
                                FriendsDestination(
                                    modifier = modifier,
                                    viewModel = viewModel,
                                    onBack = { rootBackStack.removeLastOrNull() },
                                    onNavigateToQrScanner = {}
                                )
                            }
                            entry<NavRoutes.Shopping> {
                                ShoppingDestination(
                                    modifier = modifier,
                                    toBack = { rootBackStack.removeLastOrNull() }
                                )
                            }
                            entry<NavRoutes.Progress> {
                                val viewModel = koinInject<ProgressViewModel>()
                                ProgressDestination(
                                    modifier = modifier,
                                    viewmodel = viewModel,
                                    toBack = { rootBackStack.removeLastOrNull() }
                                )
                            }
                            entry<NavRoutes.Achievements> {
                                val viewModel = koinInject<AchievementsViewModel>()
                                AchievementDestination(
                                    viewModel = viewModel,
                                    modifier  = modifier,
                                    toBack    = { rootBackStack.removeLastOrNull() },
                                )
                            }
                            entry<NavRoutes.Leaderboards> {
                                val viewModel = koinInject<LeaderboardsViewModel>()
                                LeaderboardsDestination(
                                    viewModel    = viewModel,
                                    modifier     = modifier,
                                    navigateBack = { rootBackStack.removeLastOrNull() },
                                )
                            }
                            entry<NavRoutes.Devices> {
                                DevicesDestination(
                                    modifier = modifier,
                                    toBack = { rootBackStack.removeLastOrNull() }
                                )
                            }
                            entry<NavRoutes.HelpSupport> {
                                HelpSupportDestination(
                                    modifier = modifier,
                                    toBack = { rootBackStack.removeLastOrNull() }
                                )
                            }
                            entry<NavRoutes.PlanPayment> {
                                PlanPaymentDestination(
                                    modifier = modifier,
                                    toBack = { rootBackStack.removeLastOrNull() }
                                )
                            }
                            // ── Sub-navegações ────────────────────────────────────
                            entry<NavRoutes.PlanWorkoutFlow> {
                                PlanWorkoutNavigation(
                                    modifier = modifier,
                                    toBack = { rootBackStack.removeLastOrNull() }
                                )
                            }
                            entry<NavRoutes.TasksFlow> {
                                TasksNavigation(
                                    modifier = modifier,
                                    toBack = { rootBackStack.removeLastOrNull() }
                                )
                            }
                        }
                    )
                }
            }
        }
    )
}

fun handleHomeBackPress(backStack: MutableList<NavKey>): Boolean {
    return when (backStack.lastOrNull()) {
        NavRoutes.HomeFlow.Dashboard -> false
        NavRoutes.HomeFlow.Workout,
        NavRoutes.HomeFlow.Activity,
        NavRoutes.HomeFlow.Community,
        NavRoutes.HomeFlow.Nutrition -> {
            backStack.clear()
            backStack.add(NavRoutes.HomeFlow.Dashboard)
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
                    NavRoutes.HomeFlow.Activity   -> "ATIVIDADE" to Icons.Outlined.DirectionsRun
                    NavRoutes.HomeFlow.Community  -> "SOCIAL"    to Icons.Outlined.Groups
                    NavRoutes.HomeFlow.Nutrition  -> "NUTRIÇÃO"  to Icons.Outlined.Restaurant
                    else -> return@forEach
                }
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
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
