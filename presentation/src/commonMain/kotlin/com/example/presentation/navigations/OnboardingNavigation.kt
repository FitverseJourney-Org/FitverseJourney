package com.example.presentation.navigations

sealed class OnboardingNavigation {
    object ToTrial : OnboardingNavigation()
    object ToLogin : OnboardingNavigation()
}