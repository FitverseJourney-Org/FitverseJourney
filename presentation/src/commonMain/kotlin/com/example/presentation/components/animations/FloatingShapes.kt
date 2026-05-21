package org.fitverse.presentation.components.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.lerp

@Composable
fun FloatingShapes(
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val infinite = rememberInfiniteTransition()
    val offsetA by infinite.animateFloat(
        initialValue = -40f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val offsetB by infinite.animateFloat(
        initialValue = 30f,
        targetValue = -30f,
        animationSpec = infiniteRepeatable(
            animation = tween(7200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = modifier) {
        // cor base: mistura primary + secondary (Accent) para profundidade
        val blobColorA = lerp(
            colors.primary,
            colors.secondary,
            0.35f
        ).copy(alpha = 0.035f)

        val blobColorB = colors.primary.copy(alpha = 0.02f)

        // Blob esquerdo superior
        drawCircle(
            color = blobColorA,
            radius = size.minDimension * 0.55f,
            center = Offset(
                x = size.width * 0.15f + offsetA,
                y = size.height * 0.2f
            )
        )

        // Blob direito inferior
        drawCircle(
            color = blobColorB,
            radius = size.minDimension * 0.45f,
            center = Offset(
                x = size.width * 0.85f + offsetB,
                y = size.height * 0.75f
            )
        )
    }
}