package com.example.presentation.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.FitverseColors

@Composable
fun FitverseScreenTitle(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text       = title.uppercase(),
            fontSize   = 36.sp,
            color      = FitverseColors.TextPrimary,
        )
        if (subtitle != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text  = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = FitverseColors.TextMuted,
            )
        }
    }
}