package com.example.presentation.ui.authentication.register.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SelectionChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    val borderColor by animateColorAsState(
        targetValue = if (selected) colors.primary else colors.outline,
        label       = "chipBorder",
    )
    val bgColor by animateColorAsState(
        targetValue = if (selected) colors.primaryContainer else colors.surfaceVariant,
        label       = "chipBg",
    )

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(bgColor)
            .border(1.dp, borderColor, MaterialTheme.shapes.large)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text  = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if (selected) colors.onPrimaryContainer else colors.onSurface,
            ),
        )
    }
}