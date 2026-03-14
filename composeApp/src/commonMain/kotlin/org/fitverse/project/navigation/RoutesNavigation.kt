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
    @Serializable object WorkoutSession : NavRoutes
    @Serializable object ActionsWorkout : NavRoutes
    @Serializable object ActionsNutrition : NavRoutes
    @Serializable object ActionsTasks : NavRoutes
    @Serializable object ActionsFriends : NavRoutes
    @Serializable object ActionsLeaderboards : NavRoutes
    @Serializable object ActionsHistoric : NavRoutes
    @Serializable object ActionsAchievements : NavRoutes
    @Serializable object PreferencesDevicesConnect : NavRoutes
    @Serializable object PreferencesHelpSupport : NavRoutes
    data class AddMeal(val period: String) : NavRoutes
}