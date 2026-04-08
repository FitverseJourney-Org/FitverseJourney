package com.example.presentation.screens.ui.dashboard

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.domain.model.dashboard.TaskIcon
import com.example.domain.model.dashboard.TaskItem
import com.example.expect.getHourOfDay
import com.example.presentation.utils.Dashboard.levelFromXp
import com.example.presentation.screens.ui.dashboard.components.ContainerLevel
import com.example.presentation.screens.widgets.FitVerseSpacer
import com.example.presentation.screens.widgets.FitverseAvatarCard
import com.example.presentation.screens.widgets.FitverseIconNotifications
import com.example.presentation.screens.widgets.FitverseIconStreak
import com.example.presentation.screens.widgets.FitverseProfileImage
import com.example.presentation.screens.widgets.FitverseTaskItem
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
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
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
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ContainerLevel(state = avatarState, modifier = Modifier.fillMaxWidth())
                FitverseAvatarCard(state = avatarState)
                FitVerseSpacer(vertical = true, value = 16.dp)
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SectionTitle("CONDIÇÃO FÍSICA")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    HydrationTrackerCard(
                        modifier = Modifier.weight(1f)
                    )
                    StepsTrackerCard(
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SectionTitle("MISSÕES DIÁRIAS")
                tasks.forEach { task ->
                    FitverseTaskItem(
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
    }
}
@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
        fontSize = 13.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@Composable
fun FitverseMetricCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    currentValue: String,
    goalValue: String,
    unit: String,
    progress: Float,
    accentColor: Color
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface.copy(alpha = 0.7f), // Glassmorphism padrão
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier.background(
                Brush.radialGradient(
                    colors = listOf(accentColor.copy(alpha = 0.15f), Color.Transparent),
                    center = Offset(0f, 0f),
                    radius = 400f
                )
            )
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Header (Ícone + Título)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = title.uppercase(),
                            color = accentColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Valores
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = currentValue,
                            color = cs.onSurface,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = " / $goalValue $unit",
                            color = cs.onSurfaceVariant,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun HydrationTrackerCard(
    modifier: Modifier = Modifier,
    currentLiters: Float = 2.4f,
    goalLiters: Float = 3.5f
) {
    FitverseMetricCard(
        modifier = modifier,
        title = "Hidratação",
        icon = Icons.Rounded.WaterDrop,
        currentValue = currentLiters.toString(),
        goalValue = goalLiters.toString(),
        unit = "L",
        progress = (currentLiters / goalLiters).coerceIn(0f, 1f),
        accentColor = MaterialTheme.colorScheme.secondary // Azul/Ciano (dependendo do seu tema)
    )
}
@Composable
fun StepsTrackerCard(
    modifier: Modifier = Modifier,
    currentSteps: Int = 6500,
    goalSteps: Int = 10000
) {
    FitverseMetricCard(
        modifier = modifier,
        title = "Passos",
        icon = Icons.Rounded.DirectionsRun, // ou DirectionsWalk
        currentValue = currentSteps.toString(), // Em um app real, formatar para "6.500"
        goalValue = (goalSteps / 1000).toString(), // Exibe "10 k" para ficar mais limpo
        unit = "k",
        progress = (currentSteps.toFloat() / goalSteps).coerceIn(0f, 1f),
        accentColor = MaterialTheme.colorScheme.primary
    )
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

data class StreakState(
    val totalStreakCount: Int = 0,
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
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar Elevado
            FitverseProfileImage(
                avatarInitials = avatarInitials,
            )
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
            FitverseIconStreak(
                streakCount = streakCount,
                onStreakClick = onStreakClick
            )
            FitverseIconNotifications(
                onNotificationsClick = onNotificationsClick
            )
        }
    }
}