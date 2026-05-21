package org.fitverse.presentation.expect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation as LottieAnimationView
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import org.fitverse.domain.models.onboarding.LottieAnimation

// ── PlatformLottieAnimation ───────────────────────────────────────────────────

@Composable
actual fun PlatformLottieAnimation(
    animation: LottieAnimation,
    modifier: Modifier,
) {
    val file = when (animation) {
        LottieAnimation.WORKOUT          -> "lottie_lifestyle.json"
        LottieAnimation.NUTRITION        -> "lottie_cooking.json"
        LottieAnimation.AI               -> "lottie_ai_robot.json"
        LottieAnimation.COMMUNITY        -> "lottie_network_friend.json"
        LottieAnimation.REGISTER_SUCCESS -> "lottie_ani_confetti.json"
        LottieAnimation.ACHIEVEMENT      -> "lottie_achievement.json"
        LottieAnimation.LEVEL_UP         -> "lottie_level_up.json"
        LottieAnimation.STREAK           -> "lottie_streak.json"
        LottieAnimation.LOADING          -> "lottie_loading.json"
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(file))
    LottieAnimationView(
        composition = composition,
        iterations  = LottieConstants.IterateForever,
        modifier    = modifier,
    )
}
