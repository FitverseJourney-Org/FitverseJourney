package com.example.presentation.components.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.example.presentation.theme.AccentGreen

@Composable
fun FloatingShapes(
    modifier: Modifier = Modifier
) {
    val infinite = rememberInfiniteTransition(label = "floating")

    val offsetA by infinite.animateFloat(
        initialValue = -40f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetA"
    )

    val offsetB by infinite.animateFloat(
        initialValue = 30f,
        targetValue = -30f,
        animationSpec = infiniteRepeatable(
            animation = tween(7200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetB"
    )

    Canvas(modifier = modifier) {
        // Blob esquerdo superior
        drawCircle(
            color = AccentGreen.copy(alpha = 0.035f),
            radius = size.minDimension * 0.55f,
            center = Offset(
                x = size.width * 0.15f + offsetA,
                y = size.height * 0.2f
            )
        )

        // Blob direito inferior
        drawCircle(
            color = AccentGreen.copy(alpha = 0.025f),
            radius = size.minDimension * 0.45f,
            center = Offset(
                x = size.width * 0.85f + offsetB,
                y = size.height * 0.75f
            )
        )
    }
}