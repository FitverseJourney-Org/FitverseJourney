package com.example.expect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.domain.models.onboarding.OnboardingAnimationTopics

@Composable
actual fun FitnessLottieAnimation(
    animation: OnboardingAnimationTopics,
    modifier: Modifier
) {
    val file = when (animation) {
        OnboardingAnimationTopics.WORKOUT -> "lifestyle.json"
        OnboardingAnimationTopics.NUTRITION -> "cooking.json"
        OnboardingAnimationTopics.AI -> "ai_robot.json"
        OnboardingAnimationTopics.COMMUNITY -> "network_friend.json"
        OnboardingAnimationTopics.REGISTER_SUCCESS -> "ani_confetti.json"
    }
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(file)
    )

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
    )

}