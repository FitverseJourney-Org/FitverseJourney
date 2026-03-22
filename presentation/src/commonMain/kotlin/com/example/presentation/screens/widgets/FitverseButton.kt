package com.example.presentation.screens.widgets

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FitVerseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    // Cores padrão ajustadas para contraste em fundo branco
    topColor: Color = Color(0xFFF7F8FA),    // Cinza Superfície (FitverseTheme.Surface)
    edgeColor: Color = Color(0xFFE2E8F0),   // Cinza Borda/Sombra (FitverseTheme.Outline)
    textColor: Color = Color(0xFF6366F1),   // Roxo Elétrico para o texto (Destaque)
    isLoading: Boolean = false,
    enabled: Boolean = true,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.5.sp
    )
) {
    val depth = 6.dp
    val pressedDepth = 2.dp
    val cornerRadius = 16.dp

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val yOffset by animateDpAsState(
        targetValue = if (isPressed) (depth - pressedDepth) else 0.dp,
        label = "ButtonAnimation"
    )

    Box(
        modifier = modifier
            .padding(bottom = depth)
            .alpha(if (enabled) 1f else 0.5f) // Feedback visual de desabilitado
    ) {
        // 1. Base (A "Sombra" 3D em cinza mais escuro)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = depth)
                .background(
                    color = if (enabled) edgeColor else Color(0xFFD1D5DB),
                    shape = RoundedCornerShape(cornerRadius)
                )
        )

        // 2. Face do Botão (Onde o usuário clica)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .offset(y = yOffset)
                .background(
                    color = if (enabled) topColor else Color(0xFFF3F4F6),
                    shape = RoundedCornerShape(cornerRadius)
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { if (!isLoading && enabled) onClick() },
                    enabled = enabled
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = textColor,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = text.uppercase(), // Uppercase combina com o estilo ExtraBold
                    color = if (enabled) textColor else Color(0xFF9CA3AF),
                    style = textStyle
                )
            }
        }
    }
}