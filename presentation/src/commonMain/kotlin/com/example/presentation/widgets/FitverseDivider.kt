package com.example.presentation.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.FitverseColors


// ── Horizontal divider with label ─────────────────────────────────────────────

@Composable
fun FitverseDivider(label: String, modifier: Modifier = Modifier) {
    Row(
        modifier            = modifier.fillMaxWidth(),
        verticalAlignment   = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        HorizontalDivider(
            modifier  = Modifier.weight(1f),
            color     = FitverseColors.Border,
            thickness = 1.dp,
        )
        Text(
            text     = label.uppercase(),
            modifier = Modifier.padding(horizontal = 12.dp),
            style    = MaterialTheme.typography.labelSmall,
            color    = FitverseColors.TextMuted,
            letterSpacing = 0.8.sp,
        )
        HorizontalDivider(
            modifier  = Modifier.weight(1f),
            color     = FitverseColors.Border,
            thickness = 1.dp,
        )
    }
}