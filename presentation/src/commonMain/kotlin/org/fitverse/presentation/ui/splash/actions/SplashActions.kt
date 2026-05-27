package org.fitverse.presentation.ui.splash.actions

sealed class SplashActions {
    object NavigateToOnboarding: SplashActions()
    object NavigateToHome : SplashActions()
    object NavigateToLogin : SplashActions()
    object NavigateToTrial : SplashActions()
}