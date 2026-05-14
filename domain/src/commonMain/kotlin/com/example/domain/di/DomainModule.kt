package com.example.domain.di

// ── Auth ──────────────────────────────────────────────────────────────────────
import com.example.domain.usecase.login.LoginUseCase
import com.example.domain.usecase.register.RegisterUseCase
import com.example.domain.usecase.reset.ResetPasswordUseCase
// ── DataStore — Authentication ────────────────────────────────────────────────
import com.example.domain.usecase.db.datastore.authentication.ObserveIsAuthenticatedUseCase
import com.example.domain.usecase.db.datastore.authentication.SetIsAuthenticatedUseCase
// ── DataStore — Onboarding ────────────────────────────────────────────────────
import com.example.domain.usecase.db.datastore.onboarding.ObserveOnboardingCompletedUseCase
import com.example.domain.usecase.db.datastore.onboarding.SetOnboardingCompletedUseCase
// ── DataStore — Trial ─────────────────────────────────────────────────────────
import com.example.domain.usecase.db.datastore.trial.ObserveIsTrialCompletedUseCase
import com.example.domain.usecase.db.datastore.trial.SetIsTrialCompletedUseCase
// ── DataStore — Language ──────────────────────────────────────────────────────
import com.example.domain.usecase.db.datastore.language.ChangeAppLanguageUseCase
import com.example.domain.usecase.db.datastore.language.GetAppLanguageUseCase
import com.example.domain.usecase.db.datastore.language.GetCurrentLanguageUseCase
import com.example.domain.usecase.db.datastore.language.GetSystemLocaleUseCase
// ── Progression ───────────────────────────────────────────────────────────────
import com.example.domain.usecase.progression.BuildProgressionInsightUseCase
import com.example.domain.usecase.progression.GetExercisesByTrainingSplitUseCase
import com.example.domain.usecase.progression.GetProgressionDataUseCase
import com.example.domain.usecase.progression.GetTrainingSplitsUseCase
// ── Wiki ──────────────────────────────────────────────────────────────────────
import com.example.domain.usecase.wiki.GetWikiArticlesUseCase
import com.example.domain.usecase.wiki.SearchWikiArticlesUseCase
import com.example.domain.usecase.wiki.ToggleBookmarkUseCase
// ── Plan ──────────────────────────────────────────────────────────────────────
import com.example.domain.usecase.activatePlan.ActivatePlanUseCase
// ── Missions ──────────────────────────────────────────────────────────────────
import com.example.domain.usecase.missions.CompleteMissionUseCase
import com.example.domain.usecase.missions.GetDailyMissionsUseCase
import com.example.domain.usecase.missions.InsertDailyMissionsUseCase
import com.example.domain.usecase.missions.ObserveDailyMissionsUseCase
import com.example.domain.usecase.missions.SwapMissionUseCase
// ── Stats ─────────────────────────────────────────────────────────────────────
import com.example.domain.usecase.stats.AddXpUseCase
import com.example.domain.usecase.stats.IncrementWaterUseCase
import com.example.domain.usecase.stats.ObserveUserStatsUseCase
import com.example.domain.usecase.stats.UpdateStepsUseCase
import com.example.domain.usecase.stats.UpsertUserStatsUseCase
// ── Streak ────────────────────────────────────────────────────────────────────
import com.example.domain.usecase.streak.CheckInTodayUseCase
import com.example.domain.usecase.streak.ObserveStreakWeekUseCase
// ── Notifications ─────────────────────────────────────────────────────────────
import com.example.domain.usecase.notifications.DeleteNotificationUseCase
import com.example.domain.usecase.notifications.MarkAllNotificationsReadUseCase
import com.example.domain.usecase.notifications.MarkNotificationReadUseCase
import com.example.domain.usecase.notifications.ObserveNotificationsUseCase
// ── Meals ─────────────────────────────────────────────────────────────────────
import com.example.domain.usecase.meals.DeleteMealUseCase
import com.example.domain.usecase.meals.GetDailyMacrosUseCase
import com.example.domain.usecase.meals.InsertMealUseCase
import com.example.domain.usecase.meals.ObserveMealsByDateUseCase
// ── Workout ───────────────────────────────────────────────────────────────────
import com.example.domain.usecase.workout.DeleteWorkoutSessionUseCase
import com.example.domain.usecase.workout.GetRecentWorkoutSessionsUseCase
import com.example.domain.usecase.workout.GetSessionsByPeriodUseCase
import com.example.domain.usecase.workout.ObserveWorkoutSessionsUseCase
// ── Achievements ──────────────────────────────────────────────────────────────
import com.example.domain.usecase.achievements.InsertAchievementsUseCase
import com.example.domain.usecase.achievements.ObserveAchievementsUseCase
import com.example.domain.usecase.achievements.UnlockAchievementUseCase
import com.example.domain.usecase.achievements.UpdateAchievementProgressUseCase
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
}

val wikiUseCase = module {
    factory { GetWikiArticlesUseCase(get()) }
    factory { SearchWikiArticlesUseCase(get()) }
    factory { ToggleBookmarkUseCase(get()) }
}

val planUseCase = module {
    factory { ActivatePlanUseCase(get()) }
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
    factory { InsertMealUseCase(dao = get()) }
    factory { DeleteMealUseCase(dao = get(), authRepository = get()) }
    factory { GetDailyMacrosUseCase(dao = get(), authRepository = get()) }
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
    missionsUseCase,
    statsUseCase,
    streakUseCase,
    notificationsUseCase,
    mealsUseCase,
    workoutUseCase,
    achievementsUseCase,
)
