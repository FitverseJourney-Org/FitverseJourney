package com.example.presentation.screens.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.onboarding.OnboardingAnimationTopics
import com.example.expect.FitnessLottieAnimation
import kotlinx.coroutines.delay

@Composable
fun AppSplashScreen(
    toLogin: () -> Unit,
    toTrial: () -> Unit,
    toHome: () -> Unit,
    toOnboarding: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    LaunchedEffect(Unit) {
        delay(1500)
        toLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        colors.background,
                        colors.surface
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Glow sutil atrás da animação
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                colors.primary.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                FitnessLottieAnimation(
                    animation = OnboardingAnimationTopics.WORKOUT,
                    modifier = Modifier.size(180.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "FitVerse",
                color = colors.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
        }
    }
}
