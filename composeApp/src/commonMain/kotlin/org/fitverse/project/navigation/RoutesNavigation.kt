package org.fitverse.project.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoutes : NavKey {
    @Serializable object SplashScreen : NavKey, NavRoutes
    @Serializable object Onboarding : NavRoutes
    @Serializable object LoginScreen : NavRoutes
    @Serializable object RegisterScreen : NavRoutes
    @Serializable object ForgotPasswordScreen : NavRoutes
    @Serializable object HomeScreen : NavRoutes
    @Serializable object Profile : NavRoutes
}