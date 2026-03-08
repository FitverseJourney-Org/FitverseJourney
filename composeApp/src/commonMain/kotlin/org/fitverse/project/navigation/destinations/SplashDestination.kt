package org.fitverse.project.navigation.destinations

import androidx.compose.runtime.Composable
import com.example.presentation.screens.ui.splash.AppSplashScreen

@Composable
fun SplashDestination(
    onFinish: () -> Unit
) {
    AppSplashScreen(
        onFinish = onFinish
    )
}