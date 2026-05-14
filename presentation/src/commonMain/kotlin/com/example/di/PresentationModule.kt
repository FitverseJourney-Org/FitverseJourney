package com.example.di

// ── Auth ──────────────────────────────────────────────────────────────────────
import com.example.presentation.ui.authentication.login.LoginViewModel
import com.example.presentation.ui.authentication.register.RegisterViewModel
import com.example.presentation.ui.authentication.resetPassword.ResetPasswordViewModel
// ── Splash & Onboarding ───────────────────────────────────────────────────────
import com.example.presentation.ui.splash.viewmodel.SplashViewModel
import com.example.presentation.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.presentation.ui.trial.viewmodel.TrialViewModel
// ── Home ──────────────────────────────────────────────────────────────────────
import com.example.presentation.ui.community.viewmodel.CommunityViewModel
import com.example.presentation.ui.dashboard.viewmodel.DashboardViewModel
import com.example.presentation.ui.notification.NotificationViewModel
import com.example.presentation.ui.workout.viewmodel.WorkoutViewModel
import com.example.presentation.ui.workout.viewmodel.WorkoutSessionViewModel
import com.example.presentation.ui.meals.viewmodel.MealsViewModel
import com.example.presentation.ui.profile.ProfileViewModel
// ── Modal / Features ──────────────────────────────────────────────────────────
import com.example.presentation.ui.planWorkout.viewmodel.WorkoutPlanViewModel
import com.example.presentation.ui.friends.viewmodel.FriendsViewModel
import com.example.presentation.ui.wiki.viewmodel.WikiViewModel
import com.example.presentation.ui.progress.viewmodel.ProgressViewModel
// ── Global ────────────────────────────────────────────────────────────────────
import com.example.presentation.ui.LanguageViewModel
import com.example.presentation.ui.achievements.viewmodel.AchievementsViewModel
import com.example.presentation.ui.historic.viewmodel.HistoricViewModel
import com.example.presentation.ui.leaderboards.viewmodel.LeaderboardsViewModel
import com.example.presentation.ui.shopping.ShoppingViewModel
import com.example.presentation.ui.tasks.viewmodel.TasksViewModel
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
            insertMeal         = get(),
            deleteMeal         = get(),
            getDailyMacros     = get(),
        )
    }
    viewModel<ProfileViewModel> {
        ProfileViewModel()
    }
    viewModel<CommunityViewModel> {
        CommunityViewModel()
    }
}

// =============================================================================
// Features Module (Modal / Telas avulsas)
// =============================================================================
val featuresModule = module {
    viewModel<WorkoutPlanViewModel> {
        WorkoutPlanViewModel()
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
        TasksViewModel(observeDailyMissions = get())
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