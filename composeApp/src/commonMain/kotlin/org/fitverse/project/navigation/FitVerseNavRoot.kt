package org.fitverse.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.presentation.screens.AppSplashScreen
import com.example.presentation.screens.ui.authentication.login.LoginScreen
import com.example.presentation.screens.ui.authentication.login.LoginViewModel
import com.example.presentation.screens.ui.onboarding.OnboardingScreen
import com.example.presentation.screens.ui.onboarding.OnboardingViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.navigation.destinations.OnboardingDestination
import org.fitverse.project.navigation.destinations.SplashDestination
import org.koin.compose.koinInject

@Composable
fun FitVerseNavRoot() {

    val config = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(NavRoutes.SplashScreen::class, NavRoutes.SplashScreen.serializer())
                subclass(NavRoutes.Onboarding::class, NavRoutes.Onboarding.serializer())
                subclass(NavRoutes.LoginScreen::class, NavRoutes.LoginScreen.serializer())
                subclass(NavRoutes.RegisterScreen::class, NavRoutes.RegisterScreen.serializer())
                subclass(NavRoutes.ForgotPasswordScreen::class, NavRoutes.ForgotPasswordScreen.serializer())
                subclass(NavRoutes.HomeScreen::class, NavRoutes.HomeScreen.serializer())
                subclass(NavRoutes.Profile::class, NavRoutes.Profile.serializer())
            }
        }

    }

    val navBackStack = rememberNavBackStack(config, NavRoutes.SplashScreen)

    NavDisplay(
        backStack = navBackStack,
        entryProvider = { key ->
            when(key) {
                is NavRoutes.SplashScreen -> NavEntry(key) {
                    SplashDestination(
                        onFinish = { navBackStack.add(NavRoutes.Onboarding) }
                    )
                }
                is NavRoutes.Onboarding -> NavEntry(key) {
                    OnboardingDestination(
                        onFinish = { navBackStack.add(NavRoutes.LoginScreen) },
                        nextPage = { navBackStack.add(NavRoutes.LoginScreen) },
                        skipToLastPage = { navBackStack.add(NavRoutes.LoginScreen) }
                    )
                }
                is NavRoutes.LoginScreen -> NavEntry(key){
                    LoginDestination(
                        navigateToRegister = { navBackStack.add(NavRoutes.RegisterScreen) },
                        navigateToForgotPassword = { navBackStack.add(NavRoutes.ForgotPasswordScreen) },
                        navigateToHome = { navBackStack.add(NavRoutes.HomeScreen) }
                    )
                }
                else -> error("erro na mensagem")
            }
        }
    )
}

@Composable
fun LoginDestination(
    navigateToRegister: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    navigateToHome: () -> Unit
) {
    val viewmodel = koinInject<LoginViewModel>()
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(true){

    }
    LoginScreen(
        state = state,
    )
}


