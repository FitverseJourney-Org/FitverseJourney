package org.fitverse.project.navigation.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.presentation.screens.ui.onboarding.OnboardingScreen
import com.example.presentation.screens.ui.onboarding.OnboardingViewModel
import org.koin.compose.koinInject

@Composable
fun OnboardingDestination(
    onFinish: () -> Unit,
    nextPage: () -> Unit,
    skipToLastPage: () -> Unit
) {
    val viewmodel = koinInject<OnboardingViewModel>()
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(true){

    }

    OnboardingScreen(
        state = state,
        onFinish = onFinish,
        nextPage = nextPage,
        skipToLastPage = skipToLastPage
    )
}