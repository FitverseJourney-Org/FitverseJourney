package com.example.presentation.screens.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun FitVerseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFF6366F1), // Roxo Elétrico (Primary)
    contentColor: Color = Color.White,         // Texto e Loader em Branco
    isLoading: Boolean = false,
    enabled: () -> Boolean = {true}, // Idiomático do Compose (Boolean ao invés de lambda para botões)
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // 1. Feedback tátil elegante: Em vez de um bloco 3D, o botão "afunda" suavemente (Scale)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "ButtonScale"
    )

    // 2. Transição de opacidade suave para o estado desabilitado
    val alpha by animateFloatAsState(
        targetValue = if (enabled()) 1f else 0.5f,
        label = "ButtonAlpha"
    )

    Surface(
        onClick = { if (!isLoading) onClick() },
        enabled = enabled(),
        interactionSource = interactionSource,
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp) // Altura padrão Material 3 para toque confortável
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
    ) {
        Box(contentAlignment = Alignment.Center) {
            // 3. Transição suave entre o Texto e o Loading
            AnimatedContent(
                targetState = isLoading,
                label = "LoadingTransition"
            ) { loading ->
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(26.dp),
                        color = contentColor,
                        strokeWidth = 2.5.dp,
                        strokeCap = StrokeCap.Round // Bordas arredondadas no loader = Premium
                    )
                } else {
                    Text(
                        text = text, // Sem uppercase forçado para um tom de voz mais agradável
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold, // SemiBold é forte, mas elegante
                            letterSpacing = 0.5.sp // Espaçamento mais natural
                        )
                    )
                }
            }
        }
    }
}