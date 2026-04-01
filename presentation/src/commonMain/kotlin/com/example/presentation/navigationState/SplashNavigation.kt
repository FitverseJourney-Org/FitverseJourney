package com.example.presentation.navigationState

sealed class SplashNavigation {
    object ToTrial : SplashNavigation()
    object ToHome : SplashNavigation()
    object ToOnboarding : SplashNavigation()
    object ToAuth : SplashNavigation()
}