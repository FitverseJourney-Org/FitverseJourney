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
import org.fitverse.project.destinations.onboading.OnboardingDestination
import org.fitverse.project.destinations.splash.SplashDestination
import org.fitverse.project.destinations.trial.TrialDestination
import org.fitverse.project.routes.NavRoutes
import kotlin.collections.listOf

@Composable
fun FitverseRootNavigation() {
    val rootBackStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.SplashScreen::class, NavRoutes.SplashScreen.serializer())
                    subclass(NavRoutes.OnboardingScreen::class, NavRoutes.OnboardingScreen.serializer())
                    subclass(NavRoutes.TrialScreen::class, NavRoutes.TrialScreen.serializer())
                    subclass(NavRoutes.AuthFlow::class, NavRoutes.AuthFlow.serializer())
                    subclass(NavRoutes.HomeFlow::class, NavRoutes.HomeFlow.serializer())
                    subclass(NavRoutes.WorkoutFlow::class, NavRoutes.WorkoutFlow.serializer())
                }
            }
        },
        NavRoutes.SplashScreen
    )

    NavDisplay(
        modifier = Modifier,
        backStack = rootBackStack,
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
            entry<NavRoutes.SplashScreen>{
                SplashDestination(
                    toLogin = {
                        rootBackStack.add(NavRoutes.AuthFlow)
                    },
                    toTrial = {
                        rootBackStack.add(NavRoutes.TrialScreen)
                    },
                    toHome = {
                        rootBackStack.clear()
                        rootBackStack.add(NavRoutes.HomeFlow)
                    },
                    toOnboarding = {
                        rootBackStack.add(NavRoutes.OnboardingScreen)
                    }
                )
            }
            entry<NavRoutes.OnboardingScreen>{
                OnboardingDestination(
                    toLogin = {

                    },
                    toTrial = {

                    }
                )
            }
            entry<NavRoutes.TrialScreen>{
                TrialDestination(
                    toLogin = {

                    },
                )
            }
            entry<NavRoutes.AuthFlow>{
                AuthNavigation(
                    toHomeFlow = {
                        rootBackStack.clear()
                        rootBackStack.add(NavRoutes.HomeFlow)
                    }
                )
            }
            entry<NavRoutes.HomeFlow>{
                HomeNavigation(
                    onLogout = {
                        rootBackStack.clear()
                        rootBackStack.add(NavRoutes.AuthFlow)
                    }
                )
            }
        }
    )
}