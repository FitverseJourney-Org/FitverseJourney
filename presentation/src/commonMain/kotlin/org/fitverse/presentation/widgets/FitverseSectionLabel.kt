package org.fitverse.presentation.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.theme.FVTypography

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text     = text.uppercase(),
        style    = FVTypography.overline,
        color    = FitColors.TextMuted,
        modifier = modifier.padding(top = 20.dp, bottom = 10.dp),
    )
}