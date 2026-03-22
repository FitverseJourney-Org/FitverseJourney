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
        data object Community : NavKey, NavRoutes

        @Serializable
        data object AddPost : NavKey, NavRoutes

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
        data object List : NavKey, NavRoutes
        @Serializable
        data object Builder : NavKey, NavRoutes

        @Serializable
        data object Exercises : NavKey, NavRoutes

    }

    @Serializable
    data object TasksFlow : NavKey, NavRoutes {
        @Serializable
        data object TasksList : NavKey, NavRoutes
        @Serializable
        data object TasksLibrary : NavKey, NavRoutes
    }

    @Serializable
    data object PlanMealsFlow : NavKey, NavRoutes {
        @Serializable
        data object PlanList : NavKey, NavRoutes
        @Serializable
        data object PlanBuilder : NavKey, NavRoutes

        @Serializable
        data object ListMeals : NavKey, NavRoutes

    }






    @Serializable object PlanPaymentScreen : NavRoutes
    @Serializable object Devices : NavRoutes
    @Serializable object WorkoutSession : NavRoutes
    @Serializable object ActionsWorkout : NavRoutes
    @Serializable object ActionsNutrition : NavRoutes
    @Serializable object ActionsTasks : NavRoutes
    @Serializable object Friends : NavRoutes
    @Serializable object Leaderboards : NavRoutes
    @Serializable object Historic : NavRoutes
    @Serializable object Progress : NavRoutes
    @Serializable object ActionsHistoric : NavRoutes
    @Serializable object Achievements : NavRoutes
    @Serializable object HelpSupport : NavRoutes
}