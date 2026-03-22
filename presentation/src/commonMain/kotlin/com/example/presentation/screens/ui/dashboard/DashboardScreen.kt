package com.example.presentation.screens.ui.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.GeneratingTokens
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.LocalDining
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.SentimentVeryDissatisfied
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.domain.model.dashboard.TaskIcon
import com.example.domain.model.dashboard.TaskItem
import com.example.expect.getHourOfDay
import com.example.presentation.core.utils.Dashboard.levelFromXp
import com.example.presentation.screens.ui.dashboard.components.ContainerLevel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class AvatarState(
    val name: String = "Your Avatar",
    var level: Int = 23,
    var xp: Int = 340,
    val xpToNext: Int = 500,
    val health: Int = 0,
    val points: Int = 0,
    val todayPts: Int,
    var food: Int = 60,
    var consecutiveDays: Int = 0,
    var hp: Int = 0,
)

fun sampleTasksDataV12(): List<TaskItem> = listOf(
    TaskItem(
        id = "t1",
        title = "Morning Stretch",
        description = "5–10 minutes to wake up your body",
        xp = 10,
        iconType = TaskIcon.WORKOUT
    ),
    TaskItem(
        id = "t2",
        title = "Log Water Intake",
        description = "8 cups goal",
        xp = 5,
        iconType = TaskIcon.NUTRITION
    ),
    TaskItem(
        id = "t3",
        title = "30-min Cardio",
        description = "Walk / Run / Bike",
        xp = 30,
        iconType = TaskIcon.RUN
    ),
    TaskItem(
        id = "t4",
        title = "Plan Meals",
        description = "Set protein goals",
        xp = 20,
        iconType = TaskIcon.NUTRITION
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    username: String = "Athlete",
    avatarInitials: String = "A",
    exit: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    var showStreakDialog by remember { mutableStateOf(false) }

    val avatarState = remember {
        AvatarState(
            xp = 30000, level = levelFromXp(30000), hp = 100, food = 75,
            consecutiveDays = 3, todayPts = 100, health = 100, points = 100
        )
    }

    var tasks by remember { mutableStateOf(sampleTasksDataV12()) }

    val onToggleTask: (TaskItem) -> Unit = { taskToToggle ->
        tasks = tasks.map { task ->
            if (task.id == taskToToggle.id) task.copy(completed = !task.completed) else task
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var totalStreakCount by rememberSaveable { mutableStateOf(0) }
    var lastCheckInDate by rememberSaveable { mutableStateOf<String?>(null) }

    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val isTodayChecked = lastCheckInDate == today.toString()

    val streakState = StreakState(
        totalStreakCount = totalStreakCount,
        isTodayChecked = isTodayChecked
    )

    val cs = MaterialTheme.colorScheme

    AnimatedStreakDialog(
        visible = showStreakDialog,
        streakState = streakState,
        onDismiss = { showStreakDialog = false }
    ) {
        CardStreakWeek(
            state = streakState,
            onCheckInClick = {
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

                if (lastCheckInDate == null) {
                    totalStreakCount = 1
                } else {
                    val lastDate = LocalDate.parse(lastCheckInDate!!)
                    val daysBetween = lastDate.daysUntil(now)

                    when {
                        now == lastDate -> { /* Já fez check-in */ }
                        daysBetween == 1 -> { totalStreakCount += 1 }
                        else -> { totalStreakCount = 1 }
                    }
                }
                lastCheckInDate = now.toString()
            },
            onClaimPremium = {
                println("Resgatou prêmio do dia ${streakState.totalStreakCount}!")
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Espaço respirável
    ) {
        item {
            HeaderRow(
                username = username,
                avatarInitials = avatarInitials,
                streakCount = totalStreakCount,
                onNotificationsClick = onNotificationsClick,
                onStreakClick = { showStreakDialog = true }
            )
        }
        item { ContainerLevel(state = avatarState, modifier = Modifier.fillMaxWidth()) }
        item { AvatarCard(state = avatarState) }

        item {
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                text = "Daily Tasks",
                color = cs.onBackground, // Branco Puro do seu tema
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            )
        }

        items(items = tasks, key = { it.id }) { task ->
            DailyTaskItemAvatar(
                task = task,
                isSelected = task.completed,
                onToggle = { onToggleTask(task) },
                onSelect = {
                    tasks = tasks.map { if (it.id == task.id) it.copy(completed = !it.completed) else it }
                }
            )
        }
    }
}

@Composable
fun getGreeting(): String {
    return when (getHourOfDay()) {
        in 5..11 -> "Good morning"    // 05:00 - 11:59
        in 12..17 -> "Good afternoon" // 12:00 - 17:59
        in 18..22 -> "Good evening"   // 18:00 - 22:59
        else -> "Good night"          // 23:00 - 04:59 (Madrugada/Noite tardia)
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedStreakDialog(
    visible: Boolean,
    streakState: StreakState,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var animateTrigger by remember { mutableStateOf(false) }
    val cs = MaterialTheme.colorScheme

    LaunchedEffect(visible) {
        if (visible) animateTrigger = true
    }

    val triggerClose = {
        coroutineScope.launch {
            animateTrigger = false
            delay(200)
            onDismiss()
        }
    }

    if (visible) {
        Dialog(onDismissRequest = { triggerClose() }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = cs.surface), // #2A2E3C
                border = BorderStroke(1.dp, cs.outline) // #3F4458
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    // Título e Ícone
                    Icon(Icons.Rounded.LocalFireDepartment, null, tint = cs.primary, modifier = Modifier.size(60.dp))

                    // Contador com Contraste
                    Text(
                        text = "${streakState.totalStreakCount} Days",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = cs.onBackground
                    )

                    Spacer(Modifier.height(24.dp))
                    content() // O CardStreakWeek entra aqui

                    Spacer(Modifier.height(24.dp))

                    // BOTÃO PRINCIPAL: Neon + Texto Preto (Regra de Ouro)
                    Button(
                        onClick = { triggerClose() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = cs.primary,
                            contentColor = Color.Black // Máximo contraste
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("BORAA TREINAR!", fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}
// Lista de referência global no seu package de UI/Utils
val dayLabels = listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")

data class StreakState(
    val totalStreakCount: Int = 0, // Contador total (ex: 8, 15, 42...)
    val isTodayChecked: Boolean = false,
    val goal: Int = 7
) {
    // Retorna quantos bloquinhos devem estar preenchidos no ciclo atual (1 a 7)
    // Se o total é 8, o progresso no ciclo é 1. Se é 7, o progresso é 7.
    val currentCycleProgress: Int
        get() = if (totalStreakCount == 0) 0 else ((totalStreakCount - 1) % goal) + 1

    // Verifica se um bloquinho específico (0 a 6) deve estar marcado
    fun isBlockChecked(blockIndex: Int): Boolean {
        // blockIndex vai de 0 a 6.
        // Se currentCycleProgress for 3, os blocos 0, 1 e 2 estarão marcados.
        return blockIndex < currentCycleProgress
    }
}

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
                Column {
                    Text("Daily Streak", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                    Text(
                        text = if (isWeekFull) "Goal Reached! 🏆" else "Keep it up for 7 days!",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isWeekFull) cs.tertiary else cs.onSurfaceVariant
                    )
                }

                Row(
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

@Composable
fun HeaderRow(
    username: String,
    avatarInitials: String,
    streakCount: Int,
    onNotificationsClick: () -> Unit,
    onStreakClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val greeting = getGreeting()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar Elevado
            Surface(
                modifier = Modifier.size(54.dp),
                shape = CircleShape,
                color = cs.surfaceVariant, // Fundo mais escuro que a superfície normal
                border = BorderStroke(1.dp, cs.primary.copy(alpha = 0.5f)) // Borda Neon sutil
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = avatarInitials,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = cs.primary // Texto da inicial em Neon
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = greeting,
                    style = MaterialTheme.typography.labelMedium,
                    color = cs.onSurface // Cinza claro
                )
                Text(
                    text = username,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = cs.onBackground // Branco Puro
                )
            }
        }

        // Botões de Ação
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ÍCONE DE STREAK
            Surface(
                onClick = onStreakClick,
                shape = RoundedCornerShape(16.dp),
                color = cs.surface,
                border = BorderStroke(1.dp, cs.outline),
                modifier = Modifier.height(48.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocalFireDepartment,
                        contentDescription = "Streaks",
                        tint = cs.tertiary, // Dourado (XP) do FitverseTheme
                        modifier = Modifier.size(22.dp)
                    )
                    if (streakCount > 0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$streakCount",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = cs.onBackground // Branco para contraste na label
                        )
                    }
                }
            }

            // ÍCONE DE NOTIFICAÇÃO
            Surface(
                onClick = onNotificationsClick,
                shape = RoundedCornerShape(16.dp),
                color = cs.surface,
                border = BorderStroke(1.dp, cs.outline),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.NotificationsNone,
                        contentDescription = "Notifications",
                        tint = cs.onBackground, // Branco
                        modifier = Modifier.size(24.dp)
                    )
                    // Bolinha de Notificação
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .size(8.dp)
                            .background(cs.error, CircleShape) // Danger Red do FitverseTheme
                            .border(1.5.dp, cs.surface, CircleShape) // Borda da mesma cor do card para "recortar"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DailyTaskItemAvatar(
    task: TaskItem,
    isSelected: Boolean,
    onToggle: () -> Unit,
    onSelect: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Animação de cor baseada no estado de completado
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) cs.surfaceVariant else cs.surface,
        label = "containerColor"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggle,
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) cs.primary.copy(alpha = 0.5f) else cs.outline.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Container do Ícone: Neon quando inativo, Sólido quando concluído
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (isSelected) cs.primary else cs.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isSelected) Icons.Rounded.Check else Icons.Rounded.Bolt,
                    contentDescription = null,
                    tint = if (isSelected) Color.Black else cs.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    color = cs.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = task.description,
                    color = cs.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            // Badge de XP Dinâmico
            Text(
                text = "+${task.xp} XP",
                color = if (isSelected) cs.primary else cs.tertiary,
                fontWeight = FontWeight.Black,
                fontSize = 13.sp
            )
        }
    }
}




@Composable
fun AvatarCard(
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
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = cs.surface // #2A2E3C
        ),
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.3f)) // Borda sutil para profundidade
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
        // Avatar Circle com Gradiente do Tema
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(cs.surfaceVariant)
                .border(
                    2.dp,
                    Brush.linearGradient(listOf(cs.primary, cs.secondary)), // Volt -> Roxo
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = cs.onBackground, // Branco
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = state.name,
                color = cs.onBackground, // Branco Puro
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                letterSpacing = (-0.5).sp
            )

            // Badge de Level (Roxo para Força/Progresso)
            Surface(
                color = cs.secondary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(0.5.dp, cs.secondary.copy(alpha = 0.5f)),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "Lvl ${state.level}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    color = cs.secondary, // Roxo Elétrico
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        // Seção de Pontos (XP/Dourado)
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Bolt,
                    contentDescription = null,
                    tint = cs.tertiary, // Dourado (XP)
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${state.points}",
                    color = cs.onBackground,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
            }
            Text(
                text = "FITCOINS",
                color = cs.onSurface.copy(alpha = 0.6f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }

    Spacer(Modifier.height(28.dp))

    // Barras de Status - Minimalismo Puro
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        PremiumStatRow(
            label = "HEALTH",
            value = "${state.health}%",
            progress = healthProgress,
            icon = Icons.Rounded.Favorite,
            color = cs.error // Vermelho Danger
        )

        PremiumStatRow(
            label = "ENERGY",
            value = "${state.food}%",
            progress = foodProgress,
            icon = Icons.Rounded.Bolt,
            color = cs.primary // Neon Volt
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
