package org.fitverse.project.routes

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoutes : NavKey {
    @Serializable object SplashScreen : NavKey, NavRoutes
    @Serializable object OnboardingScreen : NavRoutes
    @Serializable object TrialScreen : NavKey,NavRoutes

    @Serializable
    data object AuthFlow : NavKey, NavRoutes {
        @Serializable
        data object Login : NavKey, NavRoutes
        @Serializable
        data object Register : NavKey, NavRoutes
        @Serializable
        data object ResetPassword : NavKey, NavRoutes
    }
    @Serializable
    data object HomeFlow : NavKey, NavRoutes {
        @Serializable
        data object Dashboard : NavKey, NavRoutes
        @Serializable
        data object Meals : NavKey, NavRoutes
        @Serializable
        data object Workout : NavKey, NavRoutes
        @Serializable
        data object Profile : NavKey, NavRoutes
        @Serializable object NotificationScreen : NavRoutes
    }
    @Serializable
    data object WorkoutFlow : NavKey, NavRoutes {
        @Serializable
        data object WorkoutSession : NavKey, NavRoutes
    }

    @Serializable
    data object PlanWorkoutFlow : NavKey, NavRoutes {
        @Serializable
        data object PlanList : NavKey, NavRoutes
        @Serializable
        data object PlanBuilder : NavKey, NavRoutes

        @Serializable
        data object PlanExercises : NavKey, NavRoutes

    }

    @Serializable
    data object MealsFlow : NavKey, NavRoutes {
        @Serializable
        data object WorkoutSession : NavKey, NavRoutes
    }






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
}