package org.fitverse.presentation.expect

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.fitverse.domain.models.onboarding.LottieAnimation

// ── PlatformLottieAnimation ───────────────────────────────────────────────────
// Stub: Lottie não tem suporte oficial para KMP/iOS.
// Implementar via interop nativo quando disponível.

@Composable
actual fun PlatformLottieAnimation(
    animation: LottieAnimation,
    modifier: Modifier,
) = Unit
