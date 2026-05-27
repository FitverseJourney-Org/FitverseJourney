package org.fitverse.project.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import org.fitverse.presentation.ui.LoadingLanguageScreen
import org.fitverse.presentation.ui.onboarding.viewmodel.OnboardingViewModel
import org.fitverse.presentation.ui.splash.viewmodel.SplashViewModel
import org.fitverse.presentation.ui.trial.viewmodel.TrialViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.onboading.OnboardingDestination
import org.fitverse.project.destinations.splash.SplashDestination
import org.fitverse.project.destinations.trial.TrialDestination
import org.fitverse.project.routes.NavRoutes
import org.koin.compose.koinInject


@Composable
fun FitverseRootNavigation(
) {
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
                    subclass(NavRoutes.LoadingLanguage::class, NavRoutes.LoadingLanguage.serializer())
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
            slideInHorizontally(initialOffsetX = { -it }) togetherWith slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith slideOutHorizontally(targetOffsetX = { it })
        },
        entryProvider = entryProvider {
            entry<NavRoutes.SplashScreen>{
                val viewmodel = koinInject<SplashViewModel>()

                SplashDestination(
                    viewmodel = viewmodel,
                    toLogin = {
                        rootBackStack.clear()
                        rootBackStack.add(NavRoutes.AuthFlow)
                    },
                    toTrial = {
                        rootBackStack.clear()
                        rootBackStack.add(NavRoutes.TrialScreen)
                    },
                    toHome = {
                        rootBackStack.clear()
                        rootBackStack.add(NavRoutes.HomeFlow)
                    },
                    toOnboarding = {
                        rootBackStack.clear()
                        rootBackStack.add(NavRoutes.OnboardingScreen)
                    },
                )
            }
            entry<NavRoutes.OnboardingScreen>{
                val viewmodel = koinInject<OnboardingViewModel>()
                val state by viewmodel.state.collectAsStateWithLifecycle()

                OnboardingDestination(
                    state = state,
                    viewmodel = viewmodel,
                    toTrial = {
                        viewmodel.emitToTrial()
                        rootBackStack.add(NavRoutes.TrialScreen)
                    },
                    toNewAccount = {
                        viewmodel.emitToNewAccount()
                        rootBackStack.add(NavRoutes.AuthFlow)
                    },
                    toLogin = {
                        viewmodel.emitToLogin()
                        rootBackStack.add(NavRoutes.AuthFlow)
                    }
                )
            }
            entry<NavRoutes.TrialScreen>{
                val viewModel = koinInject<TrialViewModel>()

                TrialDestination(
                    viewModel             = viewModel,
                    onNavigateToDashboard = {
                        rootBackStack.clear()
                        rootBackStack.add(NavRoutes.HomeFlow)
                    },
                    onNavigateToLogin = {
                        // If HomeFlow is already in the stack the user opened the trial
                        // from inside the app — just pop back instead of logging out.
                        if (rootBackStack.any { it == NavRoutes.HomeFlow }) {
                            rootBackStack.removeLastOrNull()
                        } else {
                            rootBackStack.clear()
                            rootBackStack.add(NavRoutes.AuthFlow)
                        }
                    },
                )
            }
            entry<NavRoutes.AuthFlow>{
                AuthNavigation(
                    toHomeFlow = {
                        rootBackStack.clear()
                        rootBackStack.add(NavRoutes.HomeFlow)
                    },
                    toLoadingLanguage = {
                        rootBackStack.add(NavRoutes.LoadingLanguage)
                    }
                )
            }
            entry<NavRoutes.HomeFlow>{
                HomeNavigation(
                    logout = {
                        rootBackStack.clear()
                        rootBackStack.add(NavRoutes.AuthFlow)
                    },
                    toLoadingLanguage = {
                        rootBackStack.add(NavRoutes.LoadingLanguage)
                    },
                    toTrial = {
                        rootBackStack.add(NavRoutes.TrialScreen)
                    },
                )
            }
            entry<NavRoutes.LoadingLanguage>{
                LoadingLanguageScreen(
                    toBack = {
                        rootBackStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}