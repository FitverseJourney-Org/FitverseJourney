package org.fitverse.domain.di

// ── Auth ──────────────────────────────────────────────────────────────────────
import org.fitverse.domain.usecase.login.LoginUseCase
import org.fitverse.domain.usecase.register.RegisterUseCase
import org.fitverse.domain.usecase.reset.ResetPasswordUseCase
// ── DataStore — Authentication ────────────────────────────────────────────────
import org.fitverse.domain.usecase.db.datastore.authentication.ObserveIsAuthenticatedUseCase
import org.fitverse.domain.usecase.db.datastore.authentication.SetIsAuthenticatedUseCase
// ── DataStore — Onboarding ────────────────────────────────────────────────────
import org.fitverse.domain.usecase.db.datastore.onboarding.ObserveOnboardingCompletedUseCase
import org.fitverse.domain.usecase.db.datastore.onboarding.SetOnboardingCompletedUseCase
// ── DataStore — Trial ─────────────────────────────────────────────────────────
import org.fitverse.domain.usecase.db.datastore.trial.ObserveIsTrialCompletedUseCase
import org.fitverse.domain.usecase.db.datastore.trial.SetIsTrialCompletedUseCase
// ── DataStore — Language ──────────────────────────────────────────────────────
import org.fitverse.domain.usecase.db.datastore.language.ChangeAppLanguageUseCase
import org.fitverse.domain.usecase.db.datastore.language.GetAppLanguageUseCase
import org.fitverse.domain.usecase.db.datastore.language.GetCurrentLanguageUseCase
import org.fitverse.domain.usecase.db.datastore.language.GetSystemLocaleUseCase
// ── Progression ───────────────────────────────────────────────────────────────
import org.fitverse.domain.usecase.progression.BuildProgressionInsightUseCase
import org.fitverse.domain.usecase.progression.GetExercisesByTrainingSplitUseCase
import org.fitverse.domain.usecase.progression.GetProgressionDataUseCase
import org.fitverse.domain.usecase.progression.GetTrainingSplitsUseCase
import org.fitverse.domain.usecase.progression.rank.GetRankInfoUseCase
import org.fitverse.domain.usecase.progression.volume.GetVolumeDataUseCase
import org.fitverse.domain.usecase.progression.calorias.GetCaloriasDataUseCase
import org.fitverse.domain.usecase.progression.cardio.GetCardioDataUseCase
import org.fitverse.domain.usecase.progression.consistencia.GetConsistenciaDataUseCase
// ── Wiki ──────────────────────────────────────────────────────────────────────
import org.fitverse.domain.usecase.wiki.GetWikiArticlesUseCase
import org.fitverse.domain.usecase.wiki.SearchWikiArticlesUseCase
import org.fitverse.domain.usecase.wiki.ToggleBookmarkUseCase
// ── Plan ──────────────────────────────────────────────────────────────────────
import org.fitverse.domain.usecase.activatePlan.ActivatePlanUseCase
// ── WorkoutPlan ───────────────────────────────────────────────────────────────
import org.fitverse.domain.usecase.workoutPlan.ActivateWorkoutPlanUseCase
import org.fitverse.domain.usecase.workoutPlan.AddWorkoutPlanUseCase
import org.fitverse.domain.usecase.workoutPlan.DeleteWorkoutPlanUseCase
import org.fitverse.domain.usecase.workoutPlan.GetWorkoutPlansUseCase
import org.fitverse.domain.usecase.workoutPlan.WorkoutPlanUseCases
// ── Missions ──────────────────────────────────────────────────────────────────
import org.fitverse.domain.usecase.missions.CompleteMissionUseCase
import org.fitverse.domain.usecase.missions.GetDailyMissionsUseCase
import org.fitverse.domain.usecase.missions.InsertDailyMissionsUseCase
import org.fitverse.domain.usecase.missions.ObserveDailyMissionsUseCase
import org.fitverse.domain.usecase.missions.SwapMissionUseCase
// ── Stats ─────────────────────────────────────────────────────────────────────
import org.fitverse.domain.usecase.stats.AddXpUseCase
import org.fitverse.domain.usecase.stats.IncrementWaterUseCase
import org.fitverse.domain.usecase.stats.ObserveUserStatsUseCase
import org.fitverse.domain.usecase.stats.UpdateStepsUseCase
import org.fitverse.domain.usecase.stats.UpsertUserStatsUseCase
// ── Streak ────────────────────────────────────────────────────────────────────
import org.fitverse.domain.usecase.streak.CheckInTodayUseCase
import org.fitverse.domain.usecase.streak.ObserveStreakWeekUseCase
// ── Notifications ─────────────────────────────────────────────────────────────
import org.fitverse.domain.usecase.notifications.DeleteNotificationUseCase
import org.fitverse.domain.usecase.notifications.MarkAllNotificationsReadUseCase
import org.fitverse.domain.usecase.notifications.MarkNotificationReadUseCase
import org.fitverse.domain.usecase.notifications.ObserveNotificationsUseCase
// ── Meals ─────────────────────────────────────────────────────────────────────
import org.fitverse.domain.usecase.meals.AddFoodToMealUseCase
import org.fitverse.domain.usecase.meals.CleanupOldMealsUseCase
import org.fitverse.domain.usecase.meals.CreateMealUseCase
import org.fitverse.domain.usecase.meals.DeleteMealUseCase
import org.fitverse.domain.usecase.meals.GetDailyMacrosUseCase
import org.fitverse.domain.usecase.meals.GetFoodsByMealUseCase
import org.fitverse.domain.usecase.meals.InsertMealUseCase
import org.fitverse.domain.usecase.meals.ObserveMealsByDateUseCase
// ── Workout ───────────────────────────────────────────────────────────────────
import org.fitverse.domain.usecase.workout.DeleteWorkoutSessionUseCase
import org.fitverse.domain.usecase.workout.GetRecentWorkoutSessionsUseCase
import org.fitverse.domain.usecase.workout.GetSessionsByPeriodUseCase
import org.fitverse.domain.usecase.workout.ObserveWorkoutSessionsUseCase
// ── Achievements ──────────────────────────────────────────────────────────────
import org.fitverse.domain.usecase.achievements.InsertAchievementsUseCase
import org.fitverse.domain.usecase.achievements.ObserveAchievementsUseCase
import org.fitverse.domain.usecase.achievements.UnlockAchievementUseCase
import org.fitverse.domain.usecase.achievements.UpdateAchievementProgressUseCase
import org.koin.dsl.module

val AuthUseCase = module {
    factory {
        LoginUseCase(
            authRepository            = get(),
            userRepository            = get(),
            appAuthenticateRepository = get(),
        )
    }
    factory {
        RegisterUseCase(
            authRepository            = get(),
            userRepository            = get(),
            appAuthenticateRepository = get(),
        )
    }
    factory { ResetPasswordUseCase(authRepository = get()) }
}

val datastoreUseCase = module {
    factory { ObserveIsAuthenticatedUseCase(repository = get()) }
    factory { SetIsAuthenticatedUseCase(repository = get()) }
    factory { ObserveOnboardingCompletedUseCase(repository = get()) }
    factory { SetOnboardingCompletedUseCase(repository = get()) }
    factory { ObserveIsTrialCompletedUseCase(repository = get()) }
    factory { SetIsTrialCompletedUseCase(repository = get()) }
    factory { GetAppLanguageUseCase(get()) }
    factory { GetSystemLocaleUseCase(get()) }
    factory { ChangeAppLanguageUseCase(get()) }
    factory { GetCurrentLanguageUseCase(get()) }
}

val progressionUseCase = module {
    factory { GetTrainingSplitsUseCase(exerciseRepository = get()) }
    factory { GetExercisesByTrainingSplitUseCase(exerciseRepository = get()) }
    factory { GetProgressionDataUseCase(progressionRepository = get()) }
    factory { BuildProgressionInsightUseCase() }
    factory { GetRankInfoUseCase(rankRepository = get()) }
    factory { GetVolumeDataUseCase(volumeRepository = get()) }
    factory { GetCaloriasDataUseCase(caloriasRepository = get()) }
    factory { GetCardioDataUseCase(cardioRepository = get()) }
    factory { GetConsistenciaDataUseCase(consistenciaRepository = get()) }
}

val wikiUseCase = module {
    factory { GetWikiArticlesUseCase(get()) }
    factory { SearchWikiArticlesUseCase(get()) }
    factory { ToggleBookmarkUseCase(get()) }
}

val planUseCase = module {
    factory { ActivatePlanUseCase(get()) }
}

val workoutPlanUseCase = module {
    factory { GetWorkoutPlansUseCase(repository = get()) }
    factory { AddWorkoutPlanUseCase(repository = get()) }
    factory { DeleteWorkoutPlanUseCase(repository = get()) }
    factory { ActivateWorkoutPlanUseCase(repository = get()) }
    factory {
        WorkoutPlanUseCases(
            getWorkoutPlans  = get(),
            addPlan          = get(),
            deleteWorkoutPlan = get(),
            activatePlan     = get(),
        )
    }
}

val missionsUseCase = module {
    factory { ObserveDailyMissionsUseCase(dao = get(), authRepository = get()) }
    factory { CompleteMissionUseCase(dao = get(), authRepository = get()) }
    factory { InsertDailyMissionsUseCase(dao = get()) }
    factory { GetDailyMissionsUseCase(catalogDao = get(), missionDao = get(), authRepository = get()) }
    factory { SwapMissionUseCase(missionDao = get(), catalogDao = get(), authRepository = get()) }
}

val statsUseCase = module {
    factory { ObserveUserStatsUseCase(dao = get(), authRepository = get()) }
    factory { AddXpUseCase(dao = get(), authRepository = get()) }
    factory { IncrementWaterUseCase(dao = get(), authRepository = get()) }
    factory { UpdateStepsUseCase(dao = get(), authRepository = get()) }
    factory { UpsertUserStatsUseCase(dao = get()) }
}

val streakUseCase = module {
    factory { ObserveStreakWeekUseCase(dao = get(), authRepository = get()) }
    factory { CheckInTodayUseCase(dao = get(), authRepository = get()) }
}

val notificationsUseCase = module {
    factory { ObserveNotificationsUseCase(dao = get(), authRepository = get()) }
    factory { MarkNotificationReadUseCase(dao = get(), authRepository = get()) }
    factory { MarkAllNotificationsReadUseCase(dao = get(), authRepository = get()) }
    factory { DeleteNotificationUseCase(dao = get(), authRepository = get()) }
}

val mealsUseCase = module {
    factory { ObserveMealsByDateUseCase(dao = get(), authRepository = get()) }
    factory { CreateMealUseCase(dao = get(), authRepository = get()) }
    factory { InsertMealUseCase(dao = get()) }
    factory { DeleteMealUseCase(dao = get(), authRepository = get()) }
    factory { GetDailyMacrosUseCase(dao = get(), authRepository = get()) }
    factory { AddFoodToMealUseCase(foodDao = get(), mealDao = get(), authRepository = get()) }
    factory { GetFoodsByMealUseCase(foodDao = get()) }
    factory { CleanupOldMealsUseCase(mealDao = get(), foodDao = get(), authRepository = get()) }
}

val workoutUseCase = module {
    factory { ObserveWorkoutSessionsUseCase(dao = get(), authRepository = get()) }
    factory { GetRecentWorkoutSessionsUseCase(dao = get(), authRepository = get()) }
    factory { GetSessionsByPeriodUseCase(dao = get(), authRepository = get()) }
    factory { DeleteWorkoutSessionUseCase(sessionDao = get(), setDao = get(), authRepository = get()) }
}

val achievementsUseCase = module {
    factory { ObserveAchievementsUseCase(dao = get(), authRepository = get()) }
    factory { InsertAchievementsUseCase(dao = get()) }
    factory { UpdateAchievementProgressUseCase(dao = get(), authRepository = get()) }
    factory { UnlockAchievementUseCase(dao = get(), authRepository = get()) }
}

val domainModule = listOf(
    AuthUseCase,
    datastoreUseCase,
    progressionUseCase,
    wikiUseCase,
    planUseCase,
    workoutPlanUseCase,
    missionsUseCase,
    statsUseCase,
    streakUseCase,
    notificationsUseCase,
    mealsUseCase,
    workoutUseCase,
    achievementsUseCase,
)
