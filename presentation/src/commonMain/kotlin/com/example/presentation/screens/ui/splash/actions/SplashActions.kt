package com.example.presentation.screens.ui.splash.actions

import com.example.domain.model.local.language.Language

sealed class SplashActions {
    object NavigateToOnboarding: SplashActions()
    object NavigateToHome : SplashActions()
    object NavigateToLogin : SplashActions()
    object NavigateToTrial : SplashActions()
}