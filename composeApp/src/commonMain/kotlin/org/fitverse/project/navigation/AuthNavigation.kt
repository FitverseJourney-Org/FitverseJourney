package org.fitverse.project.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.auth.LoginDestination
import org.fitverse.project.destinations.auth.RegisterDestination
import org.fitverse.project.destinations.auth.ResetPasswordDestination
import org.fitverse.project.routes.NavRoutes
import kotlin.collections.listOf

@Composable
fun AuthNavigation(
    toHomeFlow: () -> Unit,
    toLoadingLanguage: () -> Unit
) {
    val authBackStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.AuthFlow.Login::class, NavRoutes.AuthFlow.Login.serializer())
                    subclass(NavRoutes.AuthFlow.Register::class, NavRoutes.AuthFlow.Register.serializer())
                    subclass(NavRoutes.AuthFlow.ResetPassword::class, NavRoutes.AuthFlow.ResetPassword.serializer())
                }
            }
        },
        NavRoutes.AuthFlow.Login
    )

    NavDisplay(
        modifier = Modifier,
        backStack = authBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
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
        entryProvider = entryProvider {
            entry<NavRoutes.AuthFlow.Login>{
                LoginDestination(
                    toRegister = {
                        authBackStack.add(NavRoutes.AuthFlow.Register)
                    },
                    toForgotPassword = {
                        authBackStack.add(NavRoutes.AuthFlow.ResetPassword)
                    },
                    toHome = {
                        toHomeFlow()
                    },
                    toLoadingLanguage = {
                        toLoadingLanguage()
                    }
                )
            }
            entry<NavRoutes.AuthFlow.Register>{
                RegisterDestination(
                    onBack = {
                        authBackStack.removeLastOrNull()
                    }
                )
            }
            entry<NavRoutes.AuthFlow.ResetPassword>{
                ResetPasswordDestination(
                    toLogin = {
                        authBackStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}