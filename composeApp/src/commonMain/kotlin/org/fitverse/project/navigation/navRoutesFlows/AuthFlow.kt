package org.fitverse.project.navigation.navRoutesFlows

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import org.fitverse.project.navigation.NavRoutes
import org.fitverse.project.navigation.destinations.LoginDestination
import org.fitverse.project.navigation.destinations.RegisterDestination
import org.fitverse.project.navigation.destinations.ResetPasswordDestination

@Composable
fun AuthFlow(
    config: SavedStateConfiguration,
    start: NavKey = NavRoutes.LoginScreen,
    onAuthSuccess: () -> Unit
) {
    val authBackStack = rememberNavBackStack(config, start)

    NavDisplay(
        backStack = authBackStack,
        onBack = {
            authBackStack.removeLastOrNull()
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            // Slide in from right when navigating forward
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
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
                is NavRoutes.LoginScreen -> NavEntry(key = key) {
                    LoginDestination(
                        navigateToRegister = { authBackStack.add(NavRoutes.RegisterScreen) },
                        navigateToForgotPassword = { authBackStack.add(NavRoutes.ForgotPasswordScreen) },
                        onLoginSuccess = { onAuthSuccess() },
                        navigateToHome = { onAuthSuccess() }
                    )
                }
                is NavRoutes.RegisterScreen -> NavEntry(key = key) {
                    RegisterDestination(
                        navigateToLogin = {
                            authBackStack.removeLastOrNull()
                        }
                    )
                }
                is NavRoutes.ForgotPasswordScreen -> NavEntry(key = key) {
                    ResetPasswordDestination(
                        navigateToLogin = {
                            authBackStack.removeLastOrNull()
                        }
                    )
                }
                else -> error("AuthFlow: rota desconhecida: $key")
            }
        }
    )
}