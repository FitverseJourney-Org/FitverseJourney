package org.fitverse.project.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoutes : NavKey {
    @Serializable object SplashScreen : NavKey, NavRoutes
    @Serializable object OnboardingScreen : NavRoutes
    @Serializable object TrialScreen : NavKey,NavRoutes
    @Serializable object LoginScreen : NavRoutes
    @Serializable object RegisterScreen : NavRoutes
    @Serializable object ForgotPasswordScreen : NavRoutes
    @Serializable object DashboardScreen : NavRoutes
    @Serializable object NutritionScreen : NavRoutes
    @Serializable object WorkoutScreen : NavRoutes
    @Serializable object ProfileScreen : NavRoutes
    @Serializable object NotificationScreen : NavRoutes
    @Serializable object PlanScreen : NavRoutes
    @Serializable object Devices : NavRoutes

}