package com.example.presentation.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.FitverseColors

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text      = text.uppercase(),
        style     = MaterialTheme.typography.labelSmall,
        color     = FitverseColors.TextMuted,
        letterSpacing = 1.2.sp,
        modifier  = modifier.padding(top = 20.dp, bottom = 10.dp),
    )
}