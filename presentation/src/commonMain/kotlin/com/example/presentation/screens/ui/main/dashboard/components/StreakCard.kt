package com.example.presentation.screens.ui.main.dashboard.components

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

    val progress = (days.coerceAtMost(goal)) / goal.toFloat()
    val isReady = days >= goal && !rewardClaimed

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = cs.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(cs.surfaceVariant, cs.surface)
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocalFireDepartment,
                    contentDescription = null,
                    tint = cs.primary
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "Weekly Streak",
                    fontWeight = FontWeight.SemiBold,
                    color = cs.onSurface
                )
            }

            // Dias visuais
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(goal) { index ->
                    val active = index < days

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (active) cs.primary
                                else cs.outline.copy(alpha = 0.25f)
                            )
                    )
                }
            }

            // Texto principal
            Text(
                text = "$days / $goal days",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (isReady) cs.secondary else cs.primary
            )

            // Mensagem dinâmica
            Text(
                text = when {
                    rewardClaimed -> "Reward collected. Start a new streak."
                    isReady -> "You reached the goal. Claim your reward."
                    else -> "Stay consistent. ${goal - days} days remaining."
                },
                fontSize = 13.sp,
                color = cs.onSurface.copy(alpha = 0.7f)
            )

            // Botão de recompensa
            Button(
                onClick = onClaimReward,
                enabled = isReady,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isReady) cs.secondary else cs.surface,
                    contentColor = if (isReady) cs.onSecondary else cs.onSurface.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = if (rewardClaimed) "Reward Collected"
                    else "Claim Reward",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}