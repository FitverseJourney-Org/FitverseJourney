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
            onToggleCheckIn = { index ->
                // simples lógica: toggle check
//                state = if (state.isChecked(index)) {
//                    state.copy(checkedDays = state.checkedDays - index)
//                } else {
//                    state.copy(checkedDays = state.checkedDays + index)
//                }
            },
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

    // quando entra na meta disparamos confetti
    LaunchedEffect(state.checkedDays) {
        val consecutive = state.consecutiveCount()
        if (consecutive >= state.consecutiveGoal) {
            showConfetti = true
            // esconde depois de um tempo
            scope.launch {
                delay(2500)
                showConfetti = false
            }
        }
    }

    val labels = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ){
        for(i in 0 until 7){
            val isToday = i == state.todayIndex
            val checked = state.isChecked(i)
           DayRing(
                modifier = Modifier.weight(1f),
                dayLabel = labels[i],
                sizeAll = size,
                dayNumber = (i + 1).toString(), // substitua por data real se quiser
                checked = checked,
                isToday = isToday,
                onClick = {
                    // prevenir múltiplos cliques durante animação é tratado internamente
                    onToggleCheckIn(i)
                }
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
    val scope = rememberCoroutineScope()
    // animação do arco (de 0f -> 1f se checked)
    val target = if (checked) 1f else 0f
    val progress = animateFloatAsState(targetValue = target, animationSpec = tween(durationMillis = 520))

    // pulse quando checado agora
    val scaleAnim = remember { Animatable(1f) }
    LaunchedEffect(checked) {
        if (checked) {
            scaleAnim.snapTo(0.92f)
            scaleAnim.animateTo(1.06f, animationSpec = tween(250, easing = FastOutSlowInEasing))
            scaleAnim.animateTo(1f, animationSpec = tween(220, easing = LinearOutSlowInEasing))
        }
    }

    val ringColor = if (checked) Brush.horizontalGradient(listOf(Color(0xFF7BF2A6), Color(0xFF3FAE6A)))
    else Brush.horizontalGradient(listOf(Color(0xFF2D7F3B), Color(0xFF1B4728)))

    Box(modifier=modifier){
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
        ){
            Box(
                modifier = Modifier.size(sizeAll).graphicsLayer {
                    scaleX = scaleAnim.value
                    scaleY = scaleAnim.value
                }.clickable { onClick() },
                contentAlignment = Alignment.Center,
            ) {
                // canvas para desenho do anel
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = size.minDimension * 0.12f
                    val center = this.center
                    val radius = (size.minDimension - stroke) / 2f

                    // fundo do anel
                    drawCircle(
                        color = Color.White.copy(alpha = 0.06f),
                        radius = radius,
                        center = center,
                        style = Stroke(width = stroke)
                    )

                    // arco de progresso
                    val sweep = 360f * progress.value
                    drawArc(
                        brush = ringColor,
                        startAngle = -90f,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2f, radius * 2f),
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                }

                // conteúdo interno (numero + label)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = dayNumber, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            Text(
                text = dayLabel,
                color = if(isToday) Color.White else Color.White.copy(alpha = 0.7f),
                fontWeight = if(isToday) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp
            )
        }

    }
}