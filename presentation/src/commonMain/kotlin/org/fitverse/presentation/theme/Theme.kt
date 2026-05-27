package org.fitverse.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Typography
import androidx.compose.ui.unit.dp


private val AppTypography = Typography(
    // ── Display ──────────────────────────────────────────────────────────────
    displayLarge  = FVTypography.displayLarge,
    displayMedium = FVTypography.displayMedium,
    displaySmall  = FVTypography.headlineLarge,
    // ── Headline ─────────────────────────────────────────────────────────────
    headlineLarge  = FVTypography.headlineLarge,
    headlineMedium = FVTypography.headlineMedium,
    headlineSmall  = FVTypography.headlineSmall,
    // ── Title ─────────────────────────────────────────────────────────────────
    titleLarge  = FVTypography.titleLarge,
    titleMedium = FVTypography.titleMedium,
    titleSmall  = FVTypography.titleSmall,
    // ── Body ──────────────────────────────────────────────────────────────────
    bodyLarge  = FVTypography.bodyLarge,
    bodyMedium = FVTypography.bodyMedium,
    bodySmall  = FVTypography.bodySmall,
    // ── Label ─────────────────────────────────────────────────────────────────
    labelLarge  = FVTypography.labelLarge,
    labelMedium = FVTypography.labelMedium,
    labelSmall  = FVTypography.labelSmall,
)

@Composable
fun FitVerseJourneyTheme(content: @Composable () -> Unit) {
    val darkColorScheme = darkColorScheme(
        // Primary — Neon Lime
        primary            = FitColors.Accent,
        onPrimary          = FitColors.Bg,
        primaryContainer   = FitColors.AccentContainer,
        onPrimaryContainer = FitColors.TextPrimary,

        // Secondary — Neon Aqua
        secondary          = FitColors.Secondary,
        onSecondary        = FitColors.Bg,
        secondaryContainer = FitColors.Secondary.copy(alpha = 0.15f),
        onSecondaryContainer = FitColors.TextPrimary,

        // Tertiary — Green (success / active)
        tertiary           = FitColors.Green,
        onTertiary         = FitColors.Bg,
        tertiaryContainer  = FitColors.GreenDim,
        onTertiaryContainer = FitColors.TextPrimary,

        // Backgrounds & Surfaces
        background         = FitColors.Bg,
        onBackground       = FitColors.TextPrimary,
        surface            = FitColors.Surface,
        onSurface          = FitColors.TextPrimary,
        surfaceVariant     = FitColors.Surface2,
        onSurfaceVariant   = FitColors.TextMuted,

        // Borders & Errors
        outline            = FitColors.Outline,
        error              = FitColors.Red,
        onError            = FitColors.TextPrimary,
    )

    MaterialTheme(
        colorScheme = darkColorScheme,
        shapes = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(28.dp) // Curvatura visível nos cards de plano
        ),
        typography = AppTypography,
        content = content
    )
}

