package com.example.presentation.screens.widgets

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.SentimentVeryDissatisfied
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.dashboard.AvatarState
import com.example.presentation.theme.DarkGamifiedColors

@Composable
fun FitverseAvatarCard(
    state: AvatarState,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    val isAlive = state.health > 0
    val healthProgress by animateFloatAsState(targetValue = state.health / 100f, label = "hp")
    val foodProgress by animateFloatAsState(targetValue = state.food / 100f, label = "food")
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            // SurfaceVariant (#16171D) com alpha para profundidade
            containerColor = cs.surface.copy(alpha = 0.7f),
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            if (isAlive) {
                AvatarAlive(state, healthProgress, foodProgress)
            } else {
                AvatarDead(onCreateNew = {}, onRestore = {})
            }
        }
    }
}


@Composable
fun AvatarAlive(
    state: AvatarState,
    healthProgress: Float,
    foodProgress: Float
) {
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar Circle com Gradiente Linear (Roxo -> Azul)
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(cs.surface)
                .border(
                    2.dp,
                    Brush.linearGradient(listOf(cs.primary, cs.secondary)),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = cs.onSurface,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = state.name.uppercase(),
                color = cs.onSurface,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                letterSpacing = 0.5.sp
            )

            // Badge de Level (Azul Secondary para Informação Técnica)
            Surface(
                color = cs.secondary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(0.5.dp, cs.secondary.copy(alpha = 0.4f)),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "LVL ${state.level}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    color = cs.secondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Seção de Fitcoins (Dourado/Amarelo via Custom ou OnSurface)
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Bolt,
                    contentDescription = null,
                    tint = Color(0xFFFFD700), // Dourado clássico de recompensas
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${state.points}",
                    color = cs.onSurface,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
            }
            Text(
                text = "FITCOINS",
                color = cs.onSurfaceVariant,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }

    Spacer(Modifier.height(28.dp))

    // Barras de Status
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        PremiumStatRow(
            label = "HEALTH",
            value = "${state.health}%",
            progress = healthProgress,
            icon = Icons.Rounded.Favorite,
            color = cs.tertiary // Verde Neon para Saúde
        )

        PremiumStatRow(
            label = "ENERGY",
            value = "${state.food}%",
            progress = foodProgress,
            icon = Icons.Rounded.Bolt,
            color = cs.secondary // Azul Elétrico para Energia
        )
    }
}

@Composable
fun AvatarDead(onCreateNew: () -> Unit, onRestore: () -> Unit) {
    val cs = MaterialTheme.colorScheme

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
    ) {
        // Ícone de alerta em vermelho sutil
        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = cs.error.copy(alpha = 0.1f),
            border = BorderStroke(2.dp, cs.error.copy(alpha = 0.3f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Rounded.SentimentVeryDissatisfied,
                    null,
                    tint = cs.error,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "AVATAR ABATIDO",
            color = cs.onBackground,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp,
            letterSpacing = 1.sp
        )

        Text(
            "Sua jornada parou. Recupere suas energias para continuar.",
            color = cs.onSurface,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        Spacer(Modifier.height(28.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Botão Neon Volt - Foco Principal
            Button(
                onClick = onCreateNew,
                modifier = Modifier.weight(1f).height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cs.primary,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("RECOMEÇAR", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            }

            // Botão Outlined Roxo - Secundário
            OutlinedButton(
                onClick = onRestore,
                modifier = Modifier.weight(1f).height(52.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, cs.secondary)
            ) {
                Text(
                    "RESTAURAR",
                    color = cs.secondary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
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
                Icon(icon, null, tint = color, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    label,
                    color = cs.onSurface,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }
            Text(
                value,
                color = cs.onBackground,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(Modifier.height(8.dp))

        // Progress com track quase invisível para foco total na cor ativa
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = color,
            trackColor = color.copy(alpha = 0.08f),
            strokeCap = StrokeCap.Round
        )
    }
}