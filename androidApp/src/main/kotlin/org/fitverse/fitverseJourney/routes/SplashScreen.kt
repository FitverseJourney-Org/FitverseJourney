package org.fitverse.fitverseJourney.routes

import androidx.compose.runtime.Composable
import com.example.presentation.screens.ui.splash.AppSplashScreen

@Composable
fun SplashRoute(
    onFinish: () -> Unit
) {
    AppSplashScreen(
        onFinish = onFinish
    )
}