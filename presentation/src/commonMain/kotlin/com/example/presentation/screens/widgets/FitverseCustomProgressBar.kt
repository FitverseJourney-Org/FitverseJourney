package com.example.presentation.screens.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FitverseCustomProgressBar(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxWidth().height(4.dp)) {
        val width = size.width
        val height = size.height

        // Draw Track (Background)
        drawRoundRect(
            color = color.copy(alpha = 0.2f),
            size = size,
            cornerRadius = CornerRadius(height / 2)
        )

        // Draw Indicator (Progress)
        drawRoundRect(
            color = color,
            size = size.copy(width = width * progress),
            cornerRadius = CornerRadius(height / 2)
        )
    }
}