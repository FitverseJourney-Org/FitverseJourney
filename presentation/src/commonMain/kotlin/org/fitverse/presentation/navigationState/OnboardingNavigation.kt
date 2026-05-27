package org.fitverse.presentation.navigationState

sealed class OnboardingNavigation {
    object ToTrial : OnboardingNavigation()
    object ToNewAccount : OnboardingNavigation()
    object ToLogin : OnboardingNavigation()
}