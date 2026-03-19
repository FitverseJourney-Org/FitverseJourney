package com.example.presentation.screens.ui.dashboard.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoodBad
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.GeneratingTokens
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.RestaurantMenu
import androidx.compose.material.icons.rounded.SentimentVeryDissatisfied
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.dashboard.AvatarState

@Composable
fun AvatarCard(
    state: com.example.presentation.screens.ui.dashboard.AvatarState,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val isAlive = state.health > 0

    // Animações mais responsivas com FastOutSlowIn
    val healthProgress by animateFloatAsState(targetValue = state.health / 100f, animationSpec = tween(1000, easing = FastOutSlowInEasing), label = "health")
    val foodProgress by animateFloatAsState(targetValue = state.food / 100f, animationSpec = tween(1000, easing = FastOutSlowInEasing), label = "food")

    // Superfície com base mais escura (surfaceVariant)
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surfaceVariant.copy(alpha = 0.7f), // Tom mais fechado
        border = BorderStroke(
            width = 1.dp,
            color = if (isAlive) cs.outline.copy(alpha = 0.2f) else cs.error.copy(alpha = 0.4f)
        ),
        tonalElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = if (isAlive) {
                            // Gradiente bem sutil e escuro
                            listOf(cs.primary.copy(alpha = 0.08f), cs.surface.copy(alpha = 0.1f))
                        } else {
                            listOf(cs.error.copy(alpha = 0.15f), Color.Transparent)
                        }
                    )
                )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                if (isAlive) {
                    _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.AvatarAlive(
                        state = state,
                        healthProgress = healthProgress,
                        foodProgress = foodProgress
                    )
                } else {
                    _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.AvatarDead(
                        onCreateNew = {},
                        onRestore = {})
                }
            }
        }
    }
}

@Composable
fun AvatarAlive(
    state: com.example.presentation.screens.ui.dashboard.AvatarState,
    healthProgress: Float,
    foodProgress: Float
) {
    val cs = MaterialTheme.colorScheme

    // Layout principal: Identidade do Avatar
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Foto do Avatar - Estilo Profile Pro
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(cs.onSurface.copy(alpha = 0.08f))
                .border(1.dp, cs.onSurface.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = cs.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        // Info: Nome e Status Rápido
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = state.name,
                color = cs.onSurface,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                letterSpacing = (-0.5).sp
            )

            // Indicador de "Em Missão" ou Status Ativo (Opcional)
            Text(
                text = "Online agora",
                color = Color(0xFF4CAF50), // Verde discreto
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Moedas/Pontos - Estilo Minimalista
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.GeneratingTokens,
                    contentDescription = null,
                    tint = Color(0xFFFFB300), // Tom Dourado Dark
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "12,450",
                    color = cs.onSurface,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
            }
            Text(
                text = "FitCoins",
                color = cs.onSurfaceVariant,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    Spacer(Modifier.height(28.dp))

    // Seção de Status de Sobrevivência
    // Cores em tons sóbrios (Dark Mode friendly)
    val healthColor = Color(0xFFCF6679) // Vermelho suave/desaturado
    val foodColor = Color(0xFFFFB74D)   // Laranja suave/desaturado

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.PremiumStatRow(
            label = "Health",
            value = "${state.health}%",
            progress = healthProgress,
            icon = Icons.Rounded.Favorite,
            color = healthColor
        )

        _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.PremiumStatRow(
            label = "Food",
            value = "${state.food}%",
            progress = foodProgress,
            icon = Icons.Rounded.RestaurantMenu,
            color = foodColor
        )
    }
}

@Composable
fun PremiumStatRow(
    label: String,
    value: String,
    progress: Float,
    icon: ImageVector,
    color: Color
) {
    val cs = MaterialTheme.colorScheme

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
                Text(label, color = cs.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Text(value, color = cs.onSurface, fontSize = 12.sp, fontWeight = FontWeight.Black)
        }

        Spacer(Modifier.height(6.dp))

        // Barra de progresso com design arredondado
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.2f), // Fundo da barra com a mesma cor, mas transparente
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
fun AvatarDead(
    onCreateNew: () -> Unit,
    onRestore: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    val pulse = rememberInfiniteTransition(label = "pulse")
    val iconScale by pulse.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(animation = tween(1000, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "scale"
    )

    Column {
        // Tag de Alerta
        Surface(
            color = cs.errorContainer,
            shape = RoundedCornerShape(8.dp),
            contentColor = cs.onErrorContainer
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                Icon(Icons.Rounded.Warning, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("BLOQUEADO", fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            // Ícone Pulsante
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(cs.error.copy(alpha = 0.15f))
                    .graphicsLayer {
                        scaleX = iconScale
                        scaleY = iconScale
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.SentimentVeryDissatisfied,
                    contentDescription = null,
                    tint = cs.error,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Avatar Derrotado",
                    color = cs.onSurface,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Crie um novo avatar ou restaure o atual para continuar ganhando recompensas.",
                    color = cs.onSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botões de Ação
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onCreateNew,
                modifier = Modifier.weight(1f).height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = cs.primary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Criar Novo", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onRestore,
                modifier = Modifier.weight(1f).height(52.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, cs.primary.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = cs.primary)
            ) {
                Text("Restaurar", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dica
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Rounded.Info, contentDescription = null, tint = cs.onSurfaceVariant, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Dica: você pode restaurar usando seus pontos.",
                color = cs.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

