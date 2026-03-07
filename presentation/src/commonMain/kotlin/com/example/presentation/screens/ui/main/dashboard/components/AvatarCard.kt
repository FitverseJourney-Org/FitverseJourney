package com.example.presentation.screens.ui.main.dashboard.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.main.dashboard.AvatarState

@Composable
fun AvatarCard(
    state: AvatarState,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    val xpProgress by animateFloatAsState(
        targetValue = state.xp / state.xpToNext.toFloat(),
        animationSpec = tween(600)
    )

    val healthProgress by animateFloatAsState(
        targetValue = state.health / 100f,
        animationSpec = tween(600)
    )

    val staminaProgress by animateFloatAsState(
        targetValue = state.stamina / 100f,
        animationSpec = tween(600)
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cs.surfaceVariant
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 20.dp
            )
        ) {
            if (state.health > 0) {
                AvatarAlive(
                    state = state,
                    xpProgress = xpProgress,
                    healthProgress = healthProgress,
                    staminaProgress = staminaProgress
                )
            } else {
                AvatarDead(
                    onCreateNew = {},
                    onRestore = {}
                )
            }
        }
    }
}

@Composable
fun AvatarAlive(
    state: AvatarState,
    xpProgress: Float,
    healthProgress: Float,
    staminaProgress: Float
) {
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(cs.primary.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = cs.primary,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column {
            Text(
                text = state.name,
                color = cs.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = "Level ${state.level}",
                color = cs.secondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.weight(1f))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = cs.primary.copy(alpha = 0.12f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(6.dp))

                Column(horizontalAlignment = Alignment.End) {

                    Text(
                        text = "12.000",
                        color = cs.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "points",
                        color = cs.onSurface.copy(alpha = 0.6f),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }

    Spacer(Modifier.height(20.dp))

    // XP — usa primary
    StatRow(
        label = "XP",
        value = "${state.xp} / ${state.xpToNext}",
        progress = xpProgress,
        icon = Icons.Default.Star,
        color = cs.primary
    )

    Spacer(Modifier.height(12.dp))

    // Health — usa error
    StatRow(
        label = "Health",
        value = "${state.health}%",
        progress = healthProgress,
        icon = Icons.Default.Favorite,
        color = cs.error
    )

    Spacer(Modifier.height(12.dp))

    // Stamina — usa secondary
    StatRow(
        label = "Stamina",
        value = "${state.stamina}%",
        progress = staminaProgress,
        icon = Icons.Default.Bolt,
        color = cs.secondary
    )
}

@Composable
fun AvatarDead(
    onCreateNew: () -> Unit,
    onRestore: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    val pulse = rememberInfiniteTransition()
    val iconScale by pulse.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(modifier = Modifier.padding(4.dp)) {

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(cs.error.copy(alpha = 0.15f))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Bloqueado",
                color = cs.error,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(cs.surface.copy(alpha = 0.4f))
                    .graphicsLayer {
                        scaleX = iconScale
                        scaleY = iconScale
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MoodBad,
                    contentDescription = null,
                    tint = cs.error,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Seu avatar foi derrotado",
                    color = cs.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Para voltar a ganhar recompensas e completar tarefas, crie um novo avatar ou tente restaurá-lo.",
                    color = cs.onSurface.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    maxLines = 3
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onCreateNew,
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cs.primary,
                    contentColor = cs.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Criar novo Avatar", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onRestore,
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = cs.secondary
                )
            ) {
                Text("Tentar restaurar")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Dica: você pode restaurar usando seus pontos.",
            color = cs.onSurface.copy(alpha = 0.6f),
            fontSize = 12.sp
        )
    }
}
