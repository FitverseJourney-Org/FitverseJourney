package com.example.di

import com.example.presentation.screens.ui.LanguageViewModel
import com.example.presentation.screens.ui.authentication.login.LoginViewModel
import com.example.presentation.screens.ui.authentication.register.RegisterViewModel
import com.example.presentation.screens.ui.authentication.resetPassword.ResetPasswordViewModel
import com.example.presentation.screens.ui.friends.viewmodel.FriendsViewModel
import com.example.presentation.screens.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.presentation.screens.ui.progress.ProgressViewModel
import com.example.presentation.screens.ui.splash.viewmodel.SplashViewModel
import com.example.presentation.screens.ui.trial.viewmodel.TrialViewModel
import com.example.presentation.screens.ui.wiki.viewmodel.WikiViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel<LoginViewModel> {
        LoginViewModel(
            loginUseCase = get(),
        )
    }
    viewModel<RegisterViewModel> {
        RegisterViewModel(
            registerUseCase = get(),
        )
    }

    viewModel<ResetPasswordViewModel> { ResetPasswordViewModel(resetPasswordUseCase = get()) }
    viewModel<OnboardingViewModel> {
        OnboardingViewModel(
            setOnboardingCompletedUseCase = get()
        )
    }
    //viewModel<CommunityViewModel>{ CommunityViewModel() }
    viewModel<SplashViewModel> {
        SplashViewModel(
            observeIsAuthenticatedUseCase = get(),
            observeOnboardingCompletedUseCase = get()
        )
    }

    viewModel {
        LanguageViewModel(
            appLanguageRepository = get(),
            getAppLanguageUseCase = get(),
            changeAppLanguageUseCase = get(),
            getLocaleLanguageAppUseCase = get()
        )
    }

    viewModel {
        FriendsViewModel(
            friendsRepository = get()
        )
    }
    viewModel {
        WikiViewModel(
            getWikiArticlesUseCase = get(),
            searchWikiArticlesUseCase = get(),
            toggleBookmarkUseCase = get(),
        )
    }
    viewModel {
        ProgressViewModel(
            getTrainingSplitsUseCase = get(),
            getExercisesByTrainingSplitUseCase = get(),
            getProgressionDataUseCase = get(),
            buildProgressionInsightUseCase = get()
        )
    }

    viewModel {
        TrialViewModel(
            activatePlan = get()
        )
    }
}