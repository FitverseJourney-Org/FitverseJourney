package org.fitverse.project.routes

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
sealed interface NavRoutes : NavKey {

    // ── Raiz ──────────────────────────────────────────────────
    @Serializable data object SplashScreen     : NavRoutes
    @Serializable data object OnboardingScreen : NavRoutes
    @Serializable data object TrialScreen      : NavRoutes
    @Serializable data object LoadingLanguage  : NavRoutes

    // ── Auth ──────────────────────────────────────────────────
    @Serializable data object AuthFlow : NavRoutes {
        @Serializable data object Home          : NavRoutes
        @Serializable data object Login         : NavRoutes
        @Serializable data object Register      : NavRoutes
        @Serializable data object ResetPassword : NavRoutes
    }

    // ── Home ──────────────────────────────────────────────────
    @Serializable data object HomeFlow : NavRoutes {

        // Bottom Bar
        @Serializable data object Dashboard  : NavRoutes
        @Serializable data object Workout    : NavRoutes
        @Serializable data object Community  : NavRoutes
        @Serializable data object Nutrition  : NavRoutes
        @Serializable data object Profile    : NavRoutes

        // Telas internas da Home

        object SubFlow : NavRoutes {
            @Serializable data object AddPost            : NavRoutes
            @Serializable data object Notification : NavRoutes
            @Serializable data object UserLevelUp        : NavRoutes
        }
    }

    // ── Workout ───────────────────────────────────────────────
    @Serializable data object WorkoutFlow : NavRoutes {
        @Serializable data object Main   : NavRoutes
        @Serializable data object WorkoutSession   : NavRoutes
        @Serializable data object WorkoutCompleted : NavRoutes
    }

    // ── Plano de Treino ───────────────────────────────────────
    @Serializable data object PlanWorkoutFlow : NavRoutes {
        @Serializable data object PlanList        : NavRoutes
        @Serializable data object Plan            : NavRoutes
        @Serializable data object Builder         : NavRoutes
        @Serializable data object Exercises       : NavRoutes
        @Serializable data object ExerciseDetails : NavRoutes
        @Serializable data object PlanIA          : NavRoutes
    }

    // ── Tasks ─────────────────────────────────────────────────
    @Serializable data object TasksFlow : NavRoutes {
        @Serializable data object TasksList    : NavRoutes
        @Serializable data object TasksLibrary : NavRoutes
    }

    // ── Telas avulsas ─────────────────────────────────────────
    @Serializable data object Shopping         : NavRoutes
    @Serializable data object WikiFitness      : NavRoutes
    @Serializable data object PlanPayment: NavRoutes
    @Serializable data object Devices          : NavRoutes
    @Serializable data object Friends          : NavRoutes
    @Serializable data object Leaderboards     : NavRoutes
    @Serializable data object Historic         : NavRoutes
    @Serializable data object Progress         : NavRoutes
    @Serializable data object Achievements     : NavRoutes
    @Serializable data object HelpSupport      : NavRoutes
    @Serializable data object ActionsWorkout   : NavRoutes
    @Serializable data object ActionsNutrition : NavRoutes
    @Serializable data object ActionsTasks     : NavRoutes
    @Serializable data object ActionsHistoric  : NavRoutes
}