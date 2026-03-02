package com.example.presentation.screens.ui.setupLanguage.components


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BtnChangeLanguage(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    val colors = MaterialTheme.colorScheme

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(100),
        label = "ButtonScale"
    )

    val shape = RoundedCornerShape(14.dp)

    // Gradiente usando cores do tema
    val gradientBrush = Brush.horizontalGradient(
        listOf(
            colors.primary,
            colors.secondary
        )
    )

    // Glow baseado na primary
    val shadowColor = colors.primary.copy(alpha = 0.35f)

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = if (isPressed) 2.dp else 8.dp,
                shape = shape,
                spotColor = shadowColor,
                ambientColor = shadowColor
            )
            .clip(shape)
            .then(
                if (enabled) {
                    Modifier.background(
                        brush = gradientBrush,
                        shape = shape
                    )
                } else {
                    Modifier.background(
                        color = colors.surfaceVariant.copy(alpha = 0.6f),
                        shape = shape
                    )
                }
            )
    ) {

        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = enabled && !isLoading,
            shape = shape,
            interactionSource = interactionSource,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = colors.onPrimary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = colors.onSurfaceVariant
            ),
            contentPadding = PaddingValues(0.dp)
        ) {

            AnimatedContent(
                targetState = isLoading,
                transitionSpec = {
                    fadeIn(tween(250)) + scaleIn() togetherWith
                            fadeOut(tween(250))
                },
                label = "ButtonContentAnim"
            ) { loading ->

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = colors.onPrimary,
                            strokeWidth = 2.5.dp,
                            trackColor = colors.onPrimary.copy(alpha = 0.3f)
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = "Processando...",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    } else {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}