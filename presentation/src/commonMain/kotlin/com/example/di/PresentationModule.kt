package com.example.di

import com.example.presentation.screens.ui.LanguageViewModel
import com.example.presentation.screens.ui.authentication.login.viewmodel.LoginViewModel
import com.example.presentation.screens.ui.authentication.register.viewmodel.RegisterViewModel
import com.example.presentation.screens.ui.authentication.resetPassword.viewmodel.ResetPasswordViewModel
import com.example.presentation.screens.ui.community.viewmodel.CommunityViewModel
import com.example.presentation.screens.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.presentation.screens.ui.splash.viewmodel.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel<LoginViewModel> {
        LoginViewModel(
            loginUseCase = get(),
        )
    }
    viewModel<RegisterViewModel> { RegisterViewModel(registerUseCase = get()) }
    viewModel<ResetPasswordViewModel> { ResetPasswordViewModel(resetPasswordUseCase = get()) }
    viewModel<OnboardingViewModel> {
        OnboardingViewModel(
            setOnboardingCompletedUseCase = get()
        )
    }
    viewModel<CommunityViewModel>{ CommunityViewModel() }
    viewModel<SplashViewModel>{
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
}