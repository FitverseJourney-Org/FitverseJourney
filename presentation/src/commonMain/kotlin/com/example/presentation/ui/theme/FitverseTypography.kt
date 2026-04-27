package ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Barlow Condensed (display) + Barlow (body).
 *
 * In CMP, load via `Font("barlow_condensed_black", ...)` after adding the .ttf
 * assets to commonMain/composeResources/font/.
 * For now we fall back to the system default so the project compiles on all targets.
 */
val FitverseTypography = Typography(
    // ── Display / screen titles ───────────────────────────────
    displayLarge = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.Black,
        fontSize    = 34.sp,
        lineHeight  = 36.sp,
        letterSpacing = (-0.5).sp,
        color       = FitverseColors.TextPrimary,
    ),
    displayMedium = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.Black,
        fontSize    = 28.sp,
        lineHeight  = 30.sp,
        letterSpacing = 0.sp,
        color       = FitverseColors.TextPrimary,
    ),

    // ── Headlines ─────────────────────────────────────────────
    headlineLarge = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.ExtraBold,
        fontSize    = 20.sp,
        lineHeight  = 22.sp,
        color       = FitverseColors.TextPrimary,
    ),
    headlineMedium = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.Bold,
        fontSize    = 16.sp,
        lineHeight  = 20.sp,
        color       = FitverseColors.TextPrimary,
    ),

    // ── Body ──────────────────────────────────────────────────
    bodyLarge = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.Normal,
        fontSize    = 15.sp,
        lineHeight  = 22.sp,
        color       = FitverseColors.TextPrimary,
    ),
    bodyMedium = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.Normal,
        fontSize    = 13.sp,
        lineHeight  = 19.sp,
        color       = FitverseColors.TextMuted,
    ),
    bodySmall = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.Normal,
        fontSize    = 12.sp,
        lineHeight  = 17.sp,
        color       = FitverseColors.TextMuted,
    ),

    // ── Label ─────────────────────────────────────────────────
    labelLarge = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.Bold,
        fontSize    = 17.sp,
        letterSpacing = 1.sp,
        color       = FitverseColors.Bg,
    ),
    labelMedium = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.SemiBold,
        fontSize    = 14.sp,
        color       = FitverseColors.TextPrimary,
    ),
    labelSmall = TextStyle(
        fontFamily  = FontFamily.Default,
        fontWeight  = FontWeight.Bold,
        fontSize    = 11.sp,
        letterSpacing = 1.2.sp,
        color       = FitverseColors.TextMuted,
    ),
)
