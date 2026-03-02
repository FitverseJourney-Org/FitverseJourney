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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.presentation.screens.ui.main.nutrition.TextPrimary
import com.example.presentation.screens.ui.main.nutrition.TextSecondary
import com.example.presentation.theme.AccentGreen
import com.example.presentation.theme.CardBgDefaultColor
import com.example.presentation.theme.HealthRed
import com.example.presentation.theme.StaminaYellow

@Composable
fun AvatarCard(
    state: AvatarState,
    modifier: Modifier = Modifier
) {
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
        colors = CardDefaults.cardColors(containerColor = CardBgDefaultColor),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(
            start = 10.dp,
            end = 20.dp,
            top = 10.dp,
            bottom = 20.dp
        )) {
            if(state.health > 0){
                AvatarAlive(
                    state = state,
                    xpProgress = xpProgress,
                    healthProgress = healthProgress,
                    staminaProgress = staminaProgress
                )
            }else {
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
){
    // 🔰 TOPO — Avatar + Nome + Level
    Row(verticalAlignment = Alignment.CenterVertically) {

        // Avatar visual
        Box(
            modifier = Modifier.size(64.dp).clip(CircleShape).background(AccentGreen.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = AccentGreen,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column {
            Text(
                text = state.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = "Level ${state.level}",
                color = AccentGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

    Spacer(Modifier.height(20.dp))

    // ⭐ XP
    StatRow(
        label = "XP",
        value = "${state.xp} / ${state.xpToNext}",
        progress = xpProgress,
        icon = Icons.Default.Star,
        color = AccentGreen
    )

    Spacer(Modifier.height(12.dp))

    // ❤️ VIDA
    StatRow(
        label = "Health",
        value = "${state.health}%",
        progress = healthProgress,
        icon = Icons.Default.Favorite,
        color = HealthRed
    )

    Spacer(Modifier.height(12.dp))

    // ⚡ STAMINA
    StatRow(
        label = "Stamina",
        value = "${state.stamina}%",
        progress = staminaProgress,
        icon = Icons.Default.Bolt,
        color = StaminaYellow
    )
}

@Composable
fun AvatarDead(
    onCreateNew: () -> Unit,
    onRestore: () -> Unit
) {
    // animação sutil de pulso
    val pulse = rememberInfiniteTransition()
    val iconScale by pulse.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier.padding(16.dp)
    ){
        // top row: icon + title + small badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(AccentGreen.copy(alpha = 0.12f))
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Text(text = "Bloqueado", color = AccentGreen, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.03f))
                        .graphicsLayer { scaleX = iconScale; scaleY = iconScale },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MoodBad,
                        contentDescription = "avatar morto",
                        tint = Color(0xFFFF8A65), // warning tint
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column{
                    Text(
                        text = "Seu avatar foi derrotado",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Para voltar a ganhar recompensas e completar tarefas, crie um novo avatar ou tente restaurá-lo.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        maxLines = 3
                    )
                }
            }

            // small hint badge

        }

        Spacer(modifier = Modifier.height(12.dp))

        // consequences (compact list)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)) {
                SmallConsequenceLine(
                    label = "Sem recompensas",
                    sub = "As tarefas não gerarão pontos"
                )
                Spacer(Modifier.height(6.dp))
                SmallConsequenceLine(
                    label = "Sem progresso diário",
                    sub = "Streaks e XP pausados"
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))

        // actions: primary (create new) + secondary (restore)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onCreateNew,
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen, contentColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Criar novo Avatar", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = onRestore,
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentGreen)
            ) {
                Text("Tentar restaurar")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // tip pequeno e discreto
        Text(
            text = "Dica: você pode restaurar usando seus pontos.",
            color = TextSecondary.copy(alpha = 0.9f),
            fontSize = 12.sp
        )
    }
}
