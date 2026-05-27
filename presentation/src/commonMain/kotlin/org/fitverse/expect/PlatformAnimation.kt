package org.fitverse.presentation.expect

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.fitverse.domain.models.onboarding.LottieAnimation

// ─────────────────────────────────────────────────────────────────────────────
// ANIMATIONS — Lottie por tópico de onboarding/gamificação.
// actual: androidMain → Lottie (airbnb) com arquivo JSON em assets/
//         iosMain     → stub vazio (Lottie não suportado no KMP/iOS)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Exibe a animação Lottie correspondente ao [animation] solicitado.
 *
 * ```kotlin
 * PlatformLottieAnimation(
 *     animation = LottieAnimation.WORKOUT,
 *     modifier  = Modifier.size(240.dp),
 * )
 * ```
 */
@Composable
expect fun PlatformLottieAnimation(
    animation: LottieAnimation,
    modifier: Modifier = Modifier,
)
