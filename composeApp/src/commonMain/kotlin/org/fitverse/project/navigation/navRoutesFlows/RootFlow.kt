package org.fitverse.project.navigation.navRoutesFlows

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import org.fitverse.project.navigation.NavRoutes
import org.fitverse.project.navigation.destinations.OnboardingDestination
import org.fitverse.project.navigation.destinations.SplashDestination
import org.fitverse.project.navigation.destinations.TrialDestination

@Composable
fun RootFlow(
    config: SavedStateConfiguration,
    start: NavKey = NavRoutes.SplashScreen,
    goNext: () -> Unit
) {

    val rootBackStack = rememberNavBackStack(config, start)
    NavDisplay(
        backStack = rootBackStack,
        transitionSpec = {
            // Slide in from right when navigating forward
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        },
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSaveableStateHolderNavEntryDecorator(),
            // Then add the view model store decorator
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = { rootBackStack.removeLastOrNull() },
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
                is NavRoutes.SplashScreen -> NavEntry(key) {
                    SplashDestination(
                        onFinish = {
                            rootBackStack.add(NavRoutes.OnboardingScreen)
                        },
                    )
                }
                is NavRoutes.OnboardingScreen -> NavEntry(key = key) {
                    OnboardingDestination(
                        toLogin = { rootBackStack.add(NavRoutes.LoginScreen) },
                        toTrial = { rootBackStack.add(NavRoutes.TrialScreen) }
                    )
                }
                is NavRoutes.TrialScreen -> NavEntry(key = key) {
                    TrialDestination(
                        toLogin = {
                            goNext()
                        }
                    )
                }
                else -> {
                    error("FitVerseNavRoot: rota desconhecida: $key")
                }

            }
        }
    )
}