package com.example.presentation.screens.ui.main.dashboard.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
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
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expect.getDayOfWeek
import com.example.presentation.theme.transparent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// Lista de referência global no seu package de UI/Utils
val dayLabels = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

data class StreakState(
    val checkedDays: Set<Int> = emptySet(),
    val consecutiveGoal: Int = 7
) {
    // Pegamos o índice baseado na String que vem da plataforma
    val todayIndex: Int = dayLabels.indexOf(getDayOfWeek()).coerceAtLeast(0)

    fun isChecked(dayIndex: Int) = checkedDays.contains(dayIndex)

    fun consecutiveCount(): Int {
        var count = 0
        // Lógica de ofensiva para trás a partir do índice detectado
        for (i in todayIndex downTo 0) {
            if (isChecked(i)) count++ else break
        }
        return count
    }
}

@Composable
fun CardStreakWeek(
    state: StreakState,
    onCheckInClick: (Int) -> Unit,
    onClaimPremium: () -> Unit // Novo callback para resgate
) {
    val cs = MaterialTheme.colorScheme
    val streak = state.consecutiveCount()
    val isTodayChecked = state.isChecked(state.todayIndex)

    // Lógica principal: Semana completa?
    val isWeekFull = streak >= 7

    // O botão fica habilitado se: não fez check-in HOJE OU se já completou a semana (para resgatar)
    val buttonEnabled = !isTodayChecked || isWeekFull

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, if (isWeekFull) cs.tertiary.copy(alpha = 0.5f) else cs.outline.copy(alpha = 0.1f)),
        tonalElevation = if (isWeekFull) 8.dp else 2.dp // Brilha mais quando pronto para resgate
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cabeçalho (Simplificado para o exemplo)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Weekly Streak", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                    Text(
                        text = if (isWeekFull) "Goal Reached! 🏆" else "Complete 7 days for +200 pts",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isWeekFull) cs.tertiary else cs.onSurfaceVariant
                    )
                }

                // Badge de Progresso
                Surface(
                    color = if (isWeekFull) cs.tertiary else cs.primary,
                    shape = CircleShape
                ) {
                    Text(
                        text = "$streak/7",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        color = if (isWeekFull) cs.onTertiary else cs.onPrimary,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            StreakWeek(state = state, onToggleCheckIn = onCheckInClick)
            Spacer(Modifier.height(24.dp))

            // BOTÃO DINÂMICO (Check-in ou Resgate)
            Button(
                onClick = {
                    if (isWeekFull) onClaimPremium() else onCheckInClick(state.todayIndex)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = buttonEnabled,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isWeekFull) cs.tertiary else cs.primary,
                    contentColor = if (isWeekFull) cs.onTertiary else cs.onPrimary,
                    disabledContainerColor = cs.outline.copy(alpha = 0.12f)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isWeekFull) Icons.Rounded.Check else Icons.Rounded.LocalFireDepartment,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = when {
                            isWeekFull -> "CLAIM PREMIUM +200 PTS"
                            isTodayChecked -> "TODAY COMPLETED"
                            else -> "DAILY CHECK-IN"
                        },
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StreakWeek(
    state: StreakState,
    onToggleCheckIn: (Int) -> Unit
) {
    // Invocamos a função expect diretamente
    val currentDayString = getDayOfWeek()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        dayLabels.forEachIndexed { index, label ->
            // Comparação direta de String conforme solicitado
            val isToday = label == currentDayString

            DayRing(
                dayLabel = label.take(1), // Exibe apenas "S", "M", "T" na UI para economizar espaço
                dayNumber = (index + 1).toString(),
                checked = state.isChecked(index),
                isToday = isToday,
                onClick = { onToggleCheckIn(index) }
            )
        }
    }
}
// -----------------------------
// DayRing: círculo com arco de progresso, micro animação e pulso ao check-in
// -----------------------------
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