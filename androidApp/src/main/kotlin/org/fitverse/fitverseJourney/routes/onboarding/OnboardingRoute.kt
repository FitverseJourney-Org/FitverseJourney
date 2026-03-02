package org.fitverse.fitverseJourney.routes.onboarding

import androidx.compose.runtime.Composable
import com.example.presentation.screens.ui.onboarding.OnboardingScreen
import com.example.presentation.states.onboarding.OnboardingState

@Composable
fun OnboardingRoute(
    state : OnboardingState,
    onFinish: () -> Unit,
    nextPage: () -> Unit,
    skipToLastPage: () -> Unit,
) {


    OnboardingScreen(
        state = state,
        onFinish = onFinish,
        nextPage = nextPage,
        skipToLastPage = skipToLastPage,
    )
}