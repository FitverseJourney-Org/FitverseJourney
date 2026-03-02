package com.example.di

import com.example.presentation.screens.ui.authentication.login.LoginViewModel
import com.example.presentation.screens.ui.authentication.register.RegisterViewModel
import com.example.presentation.screens.ui.authentication.resetPassword.ResetPasswordViewModel
import com.example.presentation.screens.ui.onboarding.OnboardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel<LoginViewModel> { LoginViewModel(loginUseCase = get()) }
    viewModel<RegisterViewModel> { RegisterViewModel(registerUseCase = get()) }
    viewModel<ResetPasswordViewModel> { ResetPasswordViewModel(resetPasswordUseCase = get()) }
    viewModel<OnboardingViewModel> { OnboardingViewModel() }
}