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
    topColor: Color = Color(0xFF55C6BA),
    edgeColor: Color = Color(0xFF297980),
    textColor: Color = Color(0xFF3A9F96),
    isLoading: Boolean = false,
    enabled: Boolean = true,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.5.sp
    )
) {
    // Definições fixas solicitadas
    val buttonHeight = 52.dp
    val depth = 8.dp
    val pressedDepth = 2.dp
    val cornerRadius = 16.dp

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val yOffset by animateDpAsState(
        targetValue = if (isPressed) (depth - pressedDepth) else 0.dp,
        label = "ButtonAnimation"
    )

    // O Modifier externo define o tamanho total (ex: fillMaxWidth)
    Box(
        modifier = modifier
            .padding(bottom = depth) // Espaço para a base 3D
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                enabled = enabled
            )
    ) {
        // 1. Base (Sombra/Borda 3D)
        // matchParentSize garante que a base tenha EXATAMENTE a mesma largura da face
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = depth)
                .background(edgeColor, RoundedCornerShape(cornerRadius))
        )

        // 2. Face do Botão
        Box(
            modifier = Modifier
                .fillMaxWidth() // Faz com que a face preencha o modifier do pai
                .height(buttonHeight)
                .offset(y = yOffset)
                .background(topColor, RoundedCornerShape(cornerRadius)),
            contentAlignment = Alignment.Center
        ) {
            if(isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = textColor,
                    strokeWidth = 2.dp
                )
            }else {
                Text(
                    text = text,
                    color = textColor,
                    style = textStyle
                )
            }
        }
    }
}