package com.example.presentation.navigationState

sealed class OnboardingNavigation {
    object ToTrial : OnboardingNavigation()
    object ToLogin : OnboardingNavigation()
}