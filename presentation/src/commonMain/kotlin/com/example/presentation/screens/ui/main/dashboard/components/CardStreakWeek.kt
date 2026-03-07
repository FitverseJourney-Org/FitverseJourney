package com.example.presentation.screens.ui.main.dashboard.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import com.example.presentation.theme.transparent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class StreakState(
    val checkedDays: Set<Int> = emptySet(), // índices 0..6 (domingo..sábado)
    val todayIndex: Int = 3, // por exemplo: quarta
    val consecutiveGoal: Int = 7 // meta para disparar confetti
) {
    fun isChecked(dayIndex: Int) = checkedDays.contains(dayIndex)
    fun consecutiveCount(): Int {
        // calcula sequência mais longa contigua que inclua 'todayIndex' indo para trás
        var count = 0
        for (i in todayIndex downTo 0) {
            if (isChecked(i)) count++ else break
        }
        return count
    }
}

@Composable
fun CardStreakWeek(state: StreakState) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = transparent)
    ) {
        StreakWeek(
            state = state,
            onToggleCheckIn = { /* lógica externa */ },
            size = 48.dp,
        )
    }
}


@Composable
fun StreakWeek(
    state: StreakState,
    onToggleCheckIn: (dayIndex: Int) -> Unit,
    size: Dp = 45.dp
) {
    val scope = rememberCoroutineScope()
    var showConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(state.checkedDays) {
        val consecutive = state.consecutiveCount()
        if (consecutive >= state.consecutiveGoal) {
            showConfetti = true
            scope.launch {
                delay(2500)
                showConfetti = false
            }
        }
    }

    val labels = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until 7) {
            val isToday = i == state.todayIndex
            val checked = state.isChecked(i)

            DayRing(
                modifier = Modifier.weight(1f),
                dayLabel = labels[i],
                sizeAll = size,
                dayNumber = (i + 1).toString(),
                checked = checked,
                isToday = isToday,
                onClick = { onToggleCheckIn(i) }
            )
        }
    }
}

// -----------------------------
// DayRing: círculo com arco de progresso, micro animação e pulso ao check-in
// -----------------------------
@Composable
fun DayRing(
    modifier: Modifier = Modifier,
    dayLabel: String,
    sizeAll: Dp,
    dayNumber: String,
    checked: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()

    val target = if (checked) 1f else 0f
    val progress = animateFloatAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = 520)
    )

    val scaleAnim = remember { Animatable(1f) }

    LaunchedEffect(checked) {
        if (checked) {
            scaleAnim.snapTo(0.92f)
            scaleAnim.animateTo(1.06f, tween(250, easing = FastOutSlowInEasing))
            scaleAnim.animateTo(1f, tween(220, easing = LinearOutSlowInEasing))
        }
    }

    // Gradientes usando sua paleta
    val ringBrush = if (checked) {
        Brush.horizontalGradient(
            listOf(cs.primary, cs.secondary)
        )
    } else {
        Brush.horizontalGradient(
            listOf(
                cs.outline.copy(alpha = 0.4f),
                cs.outline.copy(alpha = 0.2f)
            )
        )
    }

    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(sizeAll)
                    .graphicsLayer {
                        scaleX = scaleAnim.value
                        scaleY = scaleAnim.value
                    }.clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {

                Canvas(modifier = Modifier.fillMaxSize()) {

                    val stroke = size.minDimension * 0.12f
                    val center = this.center
                    val radius = (size.minDimension - stroke) / 2f

                    // fundo do anel
                    drawCircle(
                        color = cs.outline.copy(alpha = 0.15f),
                        radius = radius,
                        center = center,
                        style = Stroke(width = stroke)
                    )

                    // progresso
                    val sweep = 360f * progress.value

                    drawArc(
                        brush = ringBrush,
                        startAngle = -90f,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2f, radius * 2f),
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = dayNumber,
                        color = cs.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = dayLabel,
                color = when {
                    isToday -> cs.primary
                    else -> cs.onSurface.copy(alpha = 0.65f)
                },
                fontWeight = if (isToday) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 12.sp
            )
        }
    }
}