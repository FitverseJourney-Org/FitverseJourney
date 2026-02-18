package com.example.expect

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.domain.model.onboarding.OnboardingAnimationTopics

@Composable
expect fun FitnessLottieAnimation(
    animation: OnboardingAnimationTopics,
    modifier: Modifier
)
