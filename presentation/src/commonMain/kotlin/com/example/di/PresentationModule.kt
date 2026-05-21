package org.fitverse.presentation.di

// ── Auth ──────────────────────────────────────────────────────────────────────
import org.fitverse.presentation.ui.authentication.login.LoginViewModel
import org.fitverse.presentation.ui.authentication.register.RegisterViewModel
import org.fitverse.presentation.ui.authentication.resetPassword.ResetPasswordViewModel
// ── Splash & Onboarding ───────────────────────────────────────────────────────
import org.fitverse.presentation.ui.splash.viewmodel.SplashViewModel
import org.fitverse.presentation.ui.onboarding.viewmodel.OnboardingViewModel
import org.fitverse.presentation.ui.trial.viewmodel.TrialViewModel
// ── Home ──────────────────────────────────────────────────────────────────────
import org.fitverse.presentation.ui.community.viewmodel.CommunityViewModel
import org.fitverse.presentation.ui.community.viewmodel.GroupHomeViewModel
import org.fitverse.presentation.ui.dashboard.viewmodel.DashboardViewModel
import org.fitverse.presentation.ui.notification.NotificationViewModel
import org.fitverse.presentation.ui.workout.viewmodel.WorkoutViewModel
import org.fitverse.presentation.ui.workout.viewmodel.WorkoutSessionViewModel
import org.fitverse.presentation.ui.meals.viewmodel.AddManualFoodViewModel
import org.fitverse.presentation.ui.meals.viewmodel.MealsViewModel
import org.fitverse.presentation.ui.profile.ProfileViewModel
// ── Modal / Features ──────────────────────────────────────────────────────────
import org.fitverse.presentation.ui.workoutPlan.viewmodel.WorkoutPlanBuilderViewModel
import org.fitverse.presentation.ui.workoutPlan.viewmodel.WorkoutPlanViewModel
import org.fitverse.presentation.ui.workoutPlan.viewmodel.WorkoutPlansViewModel
import org.fitverse.presentation.ui.friends.viewmodel.FriendsViewModel
import org.fitverse.presentation.ui.wiki.viewmodel.WikiViewModel
import org.fitverse.presentation.ui.progress.viewmodel.ProgressViewModel
// ── Global ────────────────────────────────────────────────────────────────────
import org.fitverse.presentation.ui.LanguageViewModel
import org.fitverse.presentation.ui.achievements.viewmodel.AchievementsViewModel
import org.fitverse.presentation.ui.historic.viewmodel.HistoricViewModel
import org.fitverse.presentation.ui.leaderboards.viewmodel.LeaderboardsViewModel
import org.fitverse.presentation.ui.shopping.ShoppingViewModel
import org.fitverse.presentation.ui.tasks.viewmodel.TasksViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

// =============================================================================
// Auth Module
// =============================================================================
val authModule = module {
    viewModel<LoginViewModel> {
        LoginViewModel(loginUseCase = get())
    }
    viewModel<RegisterViewModel> {
        RegisterViewModel(registerUseCase = get())
    }
    viewModel<ResetPasswordViewModel> {
        ResetPasswordViewModel(resetPasswordUseCase = get())
    }
}

// =============================================================================
// Splash / Onboarding / Trial Module
// =============================================================================
val onboardingModule = module {
    viewModel<SplashViewModel> {
        SplashViewModel(
            observeIsAuthenticatedUseCase = get(),
            observeOnboardingCompletedUseCase = get(),
            observeTrialCompletedUseCase = get()
        )
    }
    viewModel<OnboardingViewModel> {
        OnboardingViewModel(setOnboardingCompletedUseCase = get())
    }
    viewModel<TrialViewModel> {
        TrialViewModel(
            activatePlan = get(),
            setIsTrialCompletedUseCase = get()
        )
    }
}

// =============================================================================
// Home Module (Bottom Bar)
// =============================================================================
val homeModule = module {
    viewModel<DashboardViewModel> {
        DashboardViewModel(
            observeDailyMissions = get(),
            completeMission      = get(),
            getDailyMissions     = get(),
            observeUserStats     = get(),
            observeStreakWeek    = get(),
        )
    }
    viewModel<NotificationViewModel> {
        NotificationViewModel(
            observeNotifications = get(),
            markRead             = get(),
            markAllRead          = get(),
            deleteNotification   = get(),
        )
    }
    viewModel<WorkoutViewModel> {
        WorkoutViewModel()
    }
    viewModel<WorkoutSessionViewModel> {
        WorkoutSessionViewModel()
    }
    viewModel<MealsViewModel> {
        MealsViewModel(
            observeMealsByDate = get(),
            createMeal         = get(),
            insertMeal         = get(),
            deleteMeal         = get(),
            getDailyMacros     = get(),
            getFoodsByMeal     = get(),
            cleanupOldMeals    = get(),
        )
    }
    viewModel<AddManualFoodViewModel> {
        AddManualFoodViewModel(addFoodToMeal = get())
    }
    viewModel<ProfileViewModel> {
        ProfileViewModel()
    }
    viewModel<CommunityViewModel> {
        CommunityViewModel()
    }
    viewModel<GroupHomeViewModel> {
        GroupHomeViewModel()
    }
}

// =============================================================================
// Features Module (Modal / Telas avulsas)
// =============================================================================
val featuresModule = module {
    viewModel<WorkoutPlanViewModel> {
        WorkoutPlanViewModel()
    }
    viewModel<WorkoutPlanBuilderViewModel> {
        WorkoutPlanBuilderViewModel()
    }
    viewModel<WorkoutPlansViewModel> {
        WorkoutPlansViewModel(useCases = get())
    }
    viewModel<FriendsViewModel> {
        FriendsViewModel(friendsRepository = get())
    }
    viewModel<WikiViewModel> {
        WikiViewModel(
            getWikiArticlesUseCase = get(),
            searchWikiArticlesUseCase = get(),
            toggleBookmarkUseCase = get()
        )
    }
    viewModel<ProgressViewModel> {
        ProgressViewModel(
            getTrainingSplitsUseCase = get(),
            getExercisesByTrainingSplitUseCase = get(),
            getProgressionDataUseCase = get(),
            buildProgressionInsightUseCase = get()
        )
    }
    viewModel<HistoricViewModel>{
        HistoricViewModel(
            observeWorkoutSessions = get(),
            getSessionsByPeriod    = get(),
            deleteWorkoutSession   = get(),
        )
    }
    viewModel<AchievementsViewModel>{
        AchievementsViewModel(achievementDao = get(), authRepository = get())
    }
    viewModel<TasksViewModel>{
        TasksViewModel(
            observeDailyMissions = get(),
            swapMission          = get(),
            catalogDao           = get(),
        )
    }
    viewModel<ShoppingViewModel>{
        ShoppingViewModel()
    }
    viewModel<LeaderboardsViewModel>{
        LeaderboardsViewModel()
    }
}

// =============================================================================
// Global Module
// =============================================================================
val globalModule = module {
    viewModel<LanguageViewModel> {
        LanguageViewModel(
            getAppLanguageUseCase = get(),
            changeAppLanguageUseCase = get(),
            getSystemLocaleUseCase = get()
        )
    }
}

// =============================================================================
// presentationModule — agrega todos os módulos acima
// Usado no Application: startKoin { modules(presentationModule) }
// =============================================================================
val presentationModules = listOf(
    globalModule,
    authModule,
    onboardingModule,
    homeModule,
    featuresModule,
)