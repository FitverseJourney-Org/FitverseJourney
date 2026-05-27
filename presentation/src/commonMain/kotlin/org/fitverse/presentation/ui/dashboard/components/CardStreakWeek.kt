package org.fitverse.presentation.ui.dashboard.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.fitverse.presentation.ui.dashboard.util.StreakState


@Composable
fun CardStreakWeek(
    state: StreakState,
    onCheckInClick: () -> Unit,
    onClaimPremium: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val isWeekFull = state.currentCycleProgress == 7 && state.isTodayChecked
    val buttonEnabled = !state.isTodayChecked || isWeekFull

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, if (isWeekFull) cs.tertiary.copy(alpha = 0.5f) else cs.outline.copy(alpha = 0.1f)),
        tonalElevation = if (isWeekFull) 8.dp else 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ){
                    Text("Daily Streak", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                    Text(
                        text = if (isWeekFull) "Goal Reached! 🏆" else "Keep it up for 7 days!",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isWeekFull) cs.tertiary else cs.onSurfaceVariant
                    )
                }

                Row(
                    modifier = Modifier.height(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    Surface(
                        color = if (isWeekFull) cs.tertiary else cs.primary,
                        shape = CircleShape
                    ) {
                        // Aqui mostramos o total real (ex: 8 days)
                        Text(
                            text = "${state.totalStreakCount} Days",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            color = if (isWeekFull) cs.onTertiary else cs.onPrimary,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black
                        )
                    }
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

            Spacer(Modifier.height(20.dp))

            // Passamos o progresso do ciclo para a UI
            StreakCycleRow(
                state = state
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { if (isWeekFull) onClaimPremium() else onCheckInClick() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = buttonEnabled,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isWeekFull) cs.tertiary else cs.primary,
                    disabledContainerColor = cs.outline.copy(alpha = 0.12f)
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isWeekFull) Icons.Rounded.Check else Icons.Rounded.LocalFireDepartment,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = when {
                            isWeekFull -> "CLAIM REWARD"
                            state.isTodayChecked -> "TOMORROW FOR DAY ${state.totalStreakCount + 1}"
                            else -> "CHECK-IN DAY ${state.totalStreakCount + 1}"
                        },
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
fun StreakCycleRow(state: StreakState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Sempre renderiza 7 blocos
        repeat(7) { index ->
            // O número exibido dentro do círculo (ex: no dia 8, mostra 8, 9, 10...)
            // Calculamos o base do ciclo (0, 7, 14...) e somamos o índice atual + 1
            val baseCycle = ((state.totalStreakCount.coerceAtLeast(1) - 1) / 7) * 7
            val displayDayNumber = baseCycle + index + 1

            DayRing(
                dayLabel = "D$displayDayNumber", // Ex: D1, D8, D15...
                dayNumber = displayDayNumber.toString(),
                checked = state.isBlockChecked(index),
                isToday = !state.isTodayChecked && index == state.currentCycleProgress,
                onClick = { /* Ação disparada pelo botão principal */ }
            )
        }
    }
}

@Composable
fun DayRing(
    dayLabel: String,
    dayNumber: String,
    checked: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Animações
    val progress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "progress"
    )

    val scale by animateFloatAsState(
        targetValue = if (checked) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.clickable(
            onClick = onClick,
            interactionSource = remember { MutableInteractionSource() },
            indication = null // Limpo para não poluir
        )
    ) {
        Text(
            text = dayLabel,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isToday) FontWeight.Black else FontWeight.Medium,
            color = if (isToday) cs.primary else cs.onSurfaceVariant.copy(alpha = 0.6f)
        )

        Box(
            modifier = Modifier
                .size(42.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 3.dp.toPx()
                val radius = (size.minDimension - strokeWidth) / 2

                // Background do anel (trilho)
                drawCircle(
                    color = if (isToday) cs.primary.copy(alpha = 0.1f) else cs.outline.copy(alpha = 0.1f),
                    radius = radius,
                    style = Stroke(width = strokeWidth)
                )

                // Arco de progresso (Streak)
                if (progress > 0f) {
                    drawArc(
                        brush = Brush.sweepGradient(listOf(cs.primary, cs.secondary, cs.primary)),
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
            }

            // Conteúdo Interno: Ícone se estiver feito, número se não
            Crossfade(targetState = checked, label = "icon_fade") { isChecked ->
                if (isChecked) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        tint = cs.primary,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(
                        text = dayNumber,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isToday) cs.primary else cs.onSurfaceVariant
                    )
                }
            }
        }

        // Indicador de "Hoje" sutil abaixo do anel
        if (isToday) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(cs.primary)
            )
        } else {
            Spacer(Modifier.height(4.dp))
        }
    }
}