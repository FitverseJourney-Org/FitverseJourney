package com.example.presentation.screens.setupLanguage.components


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

// Definição de Cores do Tema (Verde Escuro Profissional)
val DarkGreenPrimary = Color(0xFF1B5E20)
val DarkGreenSecondary = Color(0xFF2E7D32)
val GreenShadow = Color(0xFF003300)

@Composable
fun BtnChangeLanguage(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    // Cores padrão para o tema verde escuro se nada for passado
    brush: Brush? = Brush.horizontalGradient(listOf(
        DarkGreenPrimary,
        DarkGreenSecondary
    )),
    backgroundColor: Color? = null,
    contentColor: Color = Color.White,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    // 1. Controle de Animação de Clique
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Escala: diminui para 0.97 quando pressionado, volta para 1.0 quando solta
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "ButtonScale"
    )

    // 2. Definição do Shape e Sombra
    val shape = RoundedCornerShape(12.dp) // Curvas mais modernas que 5.dp
    val shadowColor = if (enabled) _root_ide_package_.com.example.presentation.screens.setupLanguage.components.GreenShadow else Color.Gray

    Box(
        modifier = modifier
            .scale(scale) // Aplica a animação de clique
            // Sombra colorida para efeito "Glow"
            .shadow(
                elevation = if (isPressed) 2.dp else 8.dp, // Sombra diminui ao clicar (efeito de afundar)
                shape = shape,
                spotColor = shadowColor,
                ambientColor = shadowColor
            )
            .clip(shape)
            // Lógica de fundo: Brush > Cor Sólida > Cinza (Desabilitado)
            .then(
                if (enabled) {
                    if (brush != null) Modifier.background(brush)
                    else Modifier.background(backgroundColor ?: _root_ide_package_.com.example.presentation.screens.setupLanguage.components.DarkGreenPrimary)
                } else {
                    Modifier.background(Color.Gray.copy(alpha = 0.5f))
                }
            )
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().height(45.dp), // Altura padrão ergonômica
            enabled = enabled && !isLoading,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, // O fundo é controlado pelo Box pai
                contentColor = contentColor,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = contentColor.copy(alpha = 0.6f)
            ),
            interactionSource = interactionSource,
            contentPadding = PaddingValues(0.dp) // Remove padding padrão para controle total
        ) {
            // 3. Animação de Transição de Conteúdo (Texto <-> Loading)
            AnimatedContent(
                targetState = isLoading,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) + scaleIn() togetherWith
                            fadeOut(animationSpec = tween(300))
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
                            color = contentColor,
                            strokeWidth = 2.5.dp,
                            trackColor = contentColor.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Processando...", // Texto de loading mais profissional
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                // fontFamily = mPlusRoundedFont // Descomente se tiver a fonte
                            )
                        )
                    } else {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }
            }
        }
    }
}