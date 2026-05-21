package org.fitverse.project.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import org.fitverse.domain.models.levelUp.LevelUpData
import org.fitverse.presentation.ui.dashboard.viewmodel.DashboardViewModel
import org.fitverse.presentation.ui.notification.NotificationViewModel
import org.fitverse.presentation.widgets.LevelUpScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.dashboad.DashboardDestination
import org.fitverse.project.destinations.dashboad.NotificationDestination
import org.fitverse.project.noTransition
import org.fitverse.project.routes.NavRoutes
import org.fitverse.project.slideFromBottom
import org.fitverse.project.slideFromRight
import org.fitverse.project.slideFromTop
import org.koin.compose.koinInject

@Composable
fun DashboardNavigation(
    modifier: Modifier,
    subScreenModifier: Modifier = Modifier,
    onSubScreenChange: (Boolean) -> Unit = {},
    onNavigateToWorkout: () -> Unit = {}
) {
    val backStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.HomeFlow.Dashboard::class,
                        NavRoutes.HomeFlow.Dashboard.serializer())

                    subclass(NavRoutes.HomeFlow.SubFlow::class,           // ← adicione
                        NavRoutes.HomeFlow.SubFlow.serializer())

                    subclass(NavRoutes.HomeFlow.SubFlow.Notification::class,
                        NavRoutes.HomeFlow.SubFlow.Notification.serializer())

                    subclass(NavRoutes.HomeFlow.SubFlow.UserLevelUp::class,
                        NavRoutes.HomeFlow.SubFlow.UserLevelUp.serializer())
                }
            }
        },
        NavRoutes.HomeFlow.Dashboard
    )

    val isSubScreen = backStack.lastOrNull() != NavRoutes.HomeFlow.Dashboard

    LaunchedEffect(isSubScreen) { onSubScreenChange(isSubScreen) }

    DisposableEffect(Unit) { onDispose { onSubScreenChange(false) } }

    NavDisplay(
        modifier = if (isSubScreen) subScreenModifier else modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            this.slideFromTop<NavRoutes.HomeFlow.SubFlow.Notification>() ?: noTransition()
        },
        popTransitionSpec = {
            this.slideFromTop<NavRoutes.HomeFlow.SubFlow.Notification>() ?: noTransition()
        },
        entryProvider = entryProvider {
            entry<NavRoutes.HomeFlow.Dashboard> {
                val viewModel = koinInject<DashboardViewModel>()
                DashboardDestination(
                    viewModel            = viewModel,
                    toNotification       = { backStack.add(NavRoutes.HomeFlow.SubFlow.Notification) },
                    toEnergy             = { backStack.add(NavRoutes.HomeFlow.SubFlow.UserLevelUp) },
                    onNavigateToWorkout  = onNavigateToWorkout,
                )
            }
            entry<NavRoutes.HomeFlow.SubFlow.Notification> {
                val viewModel = koinInject<NotificationViewModel>()
                NotificationDestination(
                    viewModel   = viewModel,
                    toDashboard = { backStack.removeLastOrNull() },
                )
            }
            entry<NavRoutes.HomeFlow.SubFlow.UserLevelUp> {
                LevelUpScreen(
                    data = LevelUpData(
                        userName  = "Alex",
                        level     = 24,
                        className = "Warrior",
                        xpGained  = 200,
                    ),
                    onContinue = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
