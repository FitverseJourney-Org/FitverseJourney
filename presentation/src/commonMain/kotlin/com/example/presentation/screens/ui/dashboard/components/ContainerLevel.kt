package com.example.presentation.screens.ui.dashboard.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.dashboard.AvatarState

// Shape customizado para o Hexágono do Nível
class HexagonShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        return Outline.Generic(
            Path().apply {
                val w = size.width
                val h = size.height
                moveTo(w * 0.5f, 0f)
                lineTo(w, h * 0.25f)
                lineTo(w, h * 0.75f)
                lineTo(w * 0.5f, h)
                lineTo(0f, h * 0.75f)
                lineTo(0f, h * 0.25f)
                close()
            }
        )
    }
}

@Composable
fun ContainerLevel(
    state: com.example.presentation.screens.ui.dashboard.AvatarState,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val xpColor = Color(0xFF42A5F5) // Azul claro vibrante baseado na imagem
    val cardBackground = cs.surfaceVariant.copy(alpha = 0.5f)

    val xpProgress by animateFloatAsState(
        targetValue = state.xp / state.xpToNext.toFloat(),
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "xp"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cardBackground,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header: Ícone e Título
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Star, // Substitua pelo seu ícone de diamante se possuir
                    contentDescription = null,
                    tint = xpColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Níveis",
                    color = cs.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.height(20.dp))

            // Corpo: Hexágono e Barra de Progresso
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hexágono de Nível
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(_root_ide_package_.com.example.presentation.screens.ui.dashboard.components.HexagonShape())
                        .background(cs.background.copy(alpha = 0.5f))
                        .background(xpColor.copy(alpha = 0.1f)), // Fundo levemente azulado
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(_root_ide_package_.com.example.presentation.screens.ui.dashboard.components.HexagonShape())
                            .background(cs.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${state.level}",
                            color = cs.onSurface,
                            fontWeight = FontWeight.Black,
                            fontSize = 24.sp
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                // Informações de XP e Barra
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "${state.xp} / ${state.xpToNext} XP",
                            color = cs.onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Nível ${state.level + 1}",
                            color = cs.onSurfaceVariant,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // Barra Estilo Pílula Espessa
                    Box(
                        modifier = Modifier
                            .background(xpColor.copy(alpha = 0.2f))
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(50))
                    ){
                        Box(
                            modifier = Modifier
                                .background(xpColor)
                                .fillMaxWidth(xpProgress)
                                .height(14.dp)
                                .clip(RoundedCornerShape(50)),
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Footer: XP Total e Ações
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // XP Total
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Star, null, tint = xpColor, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "${state.xp}",
                            color = cs.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Text(
                        text = "XP Total",
                        color = cs.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }

                // Botão de Resgate Customizado (Altura fixa 52.dp e Cor parametrizada)
                _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.ClaimRewardButton(
                    text = "Recompensa",
                    buttonColor = cs.surface.copy(alpha = 0.5f), // Cor de estado desativado
                    textColor = cs.onSurfaceVariant.copy(alpha = 0.5f)
                )

                // Botão de Ajuda (?)
                IconButton(
                    onClick = { /* Ação de ajuda */ },
                    modifier = Modifier
                        .size(40.dp)
                        .background(cs.surface.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        Icons.Rounded.HelpOutline,
                        contentDescription = "Ajuda",
                        tint = cs.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// Botão extraído seguindo parâmetros de altura e cor customizável
@Composable
fun ClaimRewardButton(
    text: String,
    buttonColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(52.dp), // Altura fixa aplicada
        shape = RoundedCornerShape(12.dp),
        color = buttonColor
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}