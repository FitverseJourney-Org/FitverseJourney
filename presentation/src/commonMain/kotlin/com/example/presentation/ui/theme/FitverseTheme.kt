package ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FitverseDarkColors = darkColorScheme(
    background       = FitverseColors.Bg,
    surface          = FitverseColors.Surface,
    surfaceVariant   = FitverseColors.Surface2,
    primary          = FitverseColors.Accent,
    onPrimary        = FitverseColors.Bg,
    secondary        = FitverseColors.Green,
    onSecondary      = FitverseColors.Bg,
    tertiary         = FitverseColors.Purple,
    onTertiary       = FitverseColors.TextPrimary,
    onBackground     = FitverseColors.TextPrimary,
    onSurface        = FitverseColors.TextPrimary,
    onSurfaceVariant = FitverseColors.TextMuted,
    outline          = FitverseColors.Border,
    outlineVariant   = FitverseColors.Border2,
    error            = FitverseColors.Red,
    onError          = FitverseColors.TextPrimary,
)

@Composable
fun FitverseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FitverseDarkColors,
        typography  = FitverseTypography,
        content     = content,
    )
}
