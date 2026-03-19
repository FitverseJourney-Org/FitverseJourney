package com.example.presentation.screens.ui.dashboard.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.AccentGreen
import com.example.presentation.theme.OnSurfaceText
import com.example.presentation.theme.SurfaceGreen

@Composable
fun StreakCard(
    days: Int,
    goal: Int = 7,
    rewardClaimed: Boolean,
    onClaimReward: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val isReady = days >= goal && !rewardClaimed

    // Animação da borda para chamar atenção quando a recompensa estiver pronta
    val borderColor by animateColorAsState(
        targetValue = if (isReady) cs.secondary.copy(alpha = 0.5f) else cs.outline.copy(alpha = 0.1f),
        animationSpec = tween(800)
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        border = BorderStroke(if (isReady) 2.dp else 1.dp, borderColor),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = if (isReady) listOf(cs.secondary.copy(alpha = 0.05f), Color.Transparent)
                        else listOf(cs.primary.copy(alpha = 0.05f), Color.Transparent)
                    )
                )
                .padding(20.dp)
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(cs.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.LocalFireDepartment, contentDescription = null, tint = cs.primary, modifier = Modifier.size(24.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Weekly Streak", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = cs.onSurface)
                    Text(
                        text = if (rewardClaimed) "Reward collected" else if (isReady) "Goal reached!" else "${goal - days} days left",
                        fontSize = 13.sp,
                        color = if (isReady && !rewardClaimed) cs.secondary else cs.onSurfaceVariant
                    )
                }
                Text("$days / $goal", fontWeight = FontWeight.Black, fontSize = 20.sp, color = cs.primary)
            }

            Spacer(Modifier.height(20.dp))

            // Trilho de Dias Modernizado
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                repeat(goal) { index ->
                    val active = index < days
                    val isToday = index == days - 1

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when {
                                    active -> cs.primary
                                    else -> cs.onSurface.copy(alpha = 0.08f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Um pequeno detalhe para o dia atual que foi completado
                        if (isToday) {
                            Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(cs.onPrimary))
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Botão de Ação
            Button(
                onClick = onClaimReward,
                enabled = isReady,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cs.secondary,
                    contentColor = cs.onSecondary,
                    disabledContainerColor = cs.surfaceVariant,
                    disabledContentColor = cs.onSurfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Icon(
                    imageVector = if (rewardClaimed) Icons.Rounded.Check else Icons.Rounded.Star,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (rewardClaimed) "Reward Collected" else "Claim Reward",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}