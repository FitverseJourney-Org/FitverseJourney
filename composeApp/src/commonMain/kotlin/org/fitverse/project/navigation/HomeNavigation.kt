package org.fitverse.project.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.presentation.screens.ui.community.CommunityScreen
import com.example.presentation.screens.ui.meals.MealsScreen
import com.example.presentation.screens.ui.profile.ProfileScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.homepage.dashboad.DashboardDestination
import org.fitverse.project.destinations.homepage.meals.MealsDestination
import org.fitverse.project.destinations.homepage.profile.ProfileDestination
import org.fitverse.project.destinations.homepage.workout.WorkoutDestination
import org.fitverse.project.routes.FitRoutes
import org.fitverse.project.routes.FitverseColors

@Composable
fun HomeNavigation() {
    val rootBackStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(FitRoutes.HomeFlowMenu.Dashboard::class, FitRoutes.HomeFlowMenu.Dashboard.serializer())
                    subclass(FitRoutes.HomeFlowMenu.Workout::class,   FitRoutes.HomeFlowMenu.Workout.serializer())
                    subclass(FitRoutes.HomeFlowMenu.Community::class, FitRoutes.HomeFlowMenu.Community.serializer())
                    subclass(FitRoutes.HomeFlowMenu.Nutrition::class, FitRoutes.HomeFlowMenu.Nutrition.serializer())
                    subclass(FitRoutes.HomeFlowMenu.Profile::class,   FitRoutes.HomeFlowMenu.Profile.serializer())
                }
            }
        },
        FitRoutes.HomeFlowMenu.Dashboard
    )

    val bottomBarItems = listOf(
        FitRoutes.HomeFlowMenu.Dashboard,
        FitRoutes.HomeFlowMenu.Workout,
        FitRoutes.HomeFlowMenu.Community,
        FitRoutes.HomeFlowMenu.Nutrition,
        FitRoutes.HomeFlowMenu.Profile,
    )

    val showBottomBar = rootBackStack.lastOrNull() is FitRoutes.HomeFlowMenu

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

                entry<FitRoutes.HomeFlowMenu.Dashboard>   { DashboardDestination(
                    toNotification = { rootBackStack.add(FitRoutes.HomeFlowMenu.Profile) }
                ) }
                entry<FitRoutes.HomeFlowMenu.Workout>   { WorkoutDestination() }
                entry<FitRoutes.HomeFlowMenu.Community> { CommunityScreen() }
                entry<FitRoutes.HomeFlowMenu.Nutrition> { MealsDestination() }
                entry<FitRoutes.HomeFlowMenu.Profile>   { ProfileDestination() }
            }
        )
    }
}

fun handleHomeBackPress(backStack: MutableList<NavKey>): Boolean {
    return when (backStack.lastOrNull()) {
        is FitRoutes.HomeFlowMenu.Dashboard -> false
        is FitRoutes.HomeFlowMenu.Workout,
        is FitRoutes.HomeFlowMenu.Community,
        is FitRoutes.HomeFlowMenu.Nutrition,
        is FitRoutes.HomeFlowMenu.Profile -> {
            backStack.clear()
            backStack.add(FitRoutes.HomeFlowMenu.Workout)
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
                    is FitRoutes.HomeFlowMenu.Dashboard -> "DASHBOARD" to Icons.Outlined.Dashboard
                    is FitRoutes.HomeFlowMenu.Workout   -> "TREINO"   to Icons.Outlined.FitnessCenter
                    is FitRoutes.HomeFlowMenu.Community -> "SOCIAL"   to Icons.Outlined.Groups
                    is FitRoutes.HomeFlowMenu.Nutrition -> "NUTRIÇÃO" to Icons.Outlined.Restaurant
                    is FitRoutes.HomeFlowMenu.Profile   -> "PERFIL"   to Icons.Outlined.Person
                    else -> return@forEach
                }
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            backStack.add(item)
                        }
                    },
                    icon = { Icon(icon, label, Modifier.size(22.dp)) },
                    label = {
                        Text(label, fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            letterSpacing = 0.8.sp)
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
