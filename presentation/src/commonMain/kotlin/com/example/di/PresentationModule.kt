package com.example.di

// ── Auth ──────────────────────────────────────────────────────────────────────
import com.example.presentation.screens.ui.authentication.login.LoginViewModel
import com.example.presentation.screens.ui.authentication.register.RegisterViewModel
import com.example.presentation.screens.ui.authentication.resetPassword.ResetPasswordViewModel
// ── Splash & Onboarding ───────────────────────────────────────────────────────
import com.example.presentation.screens.ui.splash.viewmodel.SplashViewModel
import com.example.presentation.screens.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.presentation.screens.ui.trial.viewmodel.TrialViewModel
// ── Home ──────────────────────────────────────────────────────────────────────
import com.example.presentation.screens.ui.dashboard.viewmodel.DashboardViewModel
import com.example.presentation.screens.ui.workout.viewmodel.WorkoutViewModel
import com.example.presentation.screens.ui.workout.viewmodel.WorkoutSessionViewModel
import com.example.presentation.screens.ui.meals.viewmodel.NutritionViewModel
import com.example.presentation.screens.ui.profile.ProfileViewModel
// ── Modal / Features ──────────────────────────────────────────────────────────
import com.example.presentation.screens.ui.planWorkout.viewmodel.WorkoutPlanViewModel
import com.example.presentation.screens.ui.friends.viewmodel.FriendsViewModel
import com.example.presentation.screens.ui.wiki.viewmodel.WikiViewModel
import com.example.presentation.screens.ui.progress.viewmodel.ProgressViewModel
// ── Global ────────────────────────────────────────────────────────────────────
import com.example.presentation.screens.ui.LanguageViewModel
import com.example.presentation.screens.ui.achievements.viewmodel.AchievementsViewModel
import com.example.presentation.screens.ui.historic.viewmodel.HistoricViewModel
import com.example.presentation.screens.ui.leaderboards.viewmodel.LeaderboardsViewModel
import com.example.presentation.screens.ui.shopping.ShoppingViewModel
import com.example.presentation.screens.ui.tasks.viewmodel.TasksViewModel
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
            observeOnboardingCompletedUseCase = get()
        )
    }
    viewModel<OnboardingViewModel> {
        OnboardingViewModel(setOnboardingCompletedUseCase = get())
    }
    viewModel<TrialViewModel> {
        TrialViewModel(activatePlan = get())
    }
}

// =============================================================================
// Home Module (Bottom Bar)
// =============================================================================
val homeModule = module {
    viewModel<DashboardViewModel> {
        DashboardViewModel()
    }
    viewModel<WorkoutViewModel> {
        WorkoutViewModel()
    }
    viewModel<WorkoutSessionViewModel> {
        WorkoutSessionViewModel()
    }
    viewModel<NutritionViewModel> {
        NutritionViewModel()
    }
    viewModel<ProfileViewModel> {
        ProfileViewModel()
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
        HistoricViewModel()
    }
    viewModel<AchievementsViewModel>{
        AchievementsViewModel()
    }
    viewModel<TasksViewModel>{
        TasksViewModel()
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