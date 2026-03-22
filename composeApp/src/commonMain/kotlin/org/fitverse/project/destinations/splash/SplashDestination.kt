package org.fitverse.project.destinations.splash

import androidx.compose.runtime.Composable
import com.example.presentation.screens.ui.splash.AppSplashScreen

@Composable
fun SplashDestination(
    toLogin: () -> Unit,
    toTrial: () -> Unit,
    toHome: () -> Unit,
    toOnboarding: () -> Unit
) {
    AppSplashScreen(
        toLogin = toLogin,
        toTrial = toTrial,
        toHome = toHome,
        toOnboarding = toOnboarding
    )
}