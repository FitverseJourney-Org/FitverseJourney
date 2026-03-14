package com.example.presentation.screens.ui.main.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.dashboard.TaskIcon
import com.example.domain.model.dashboard.TaskItem
import com.example.expect.getHourOfDay
import com.example.presentation.core.utils.Dashboard.levelFromXp
import com.example.presentation.screens.ui.main.dashboard.components.AvatarCard
import com.example.presentation.screens.ui.main.dashboard.components.CardStreakWeek
import com.example.presentation.screens.ui.main.dashboard.components.ContainerLevel
import com.example.presentation.screens.ui.main.dashboard.components.DailyTaskItemAvatar
import com.example.presentation.screens.ui.main.dashboard.components.PointsGainCard
import com.example.presentation.screens.ui.main.dashboard.components.StreakState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class AvatarState(
    val name: String = "Your Avatar",
    var level: Int = 5,
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
    val avatarState = remember {
        AvatarState(
            xp = 120,
            level = levelFromXp(120),
            hp = 100,
            food = 75,
            consecutiveDays = 3, // Começa com 3
            todayPts = 100,
            health = 100,
            points = 100
        )
    }
    val tasks = remember { mutableStateListOf<TaskItem>().apply { addAll(sampleTasksDataV12()) } }

    var levelUpTrigger by remember { mutableStateOf(false) }
    var previousLevel by remember { mutableStateOf(avatarState.level) }

    // Inicializamos o StreakState já com os dias marcados, simulando os 3 dias do avatarState
    var streakState by remember {
        mutableStateOf(
            StreakState(
                // Simula preenchimento retroativo baseado no consecutiveDays atual para manter a UI consistente
                checkedDays = (0 until avatarState.consecutiveDays).toSet()
            )
        )
    }

    val scope = rememberCoroutineScope()
    val becameLevelUp = remember { mutableStateOf(false) }

    LaunchedEffect(avatarState.level) {
        if (avatarState.level > previousLevel) {
            levelUpTrigger = true
            becameLevelUp.value = true
            previousLevel = avatarState.level
            scope.launch {
                delay(1400)
                levelUpTrigger = false
                becameLevelUp.value = false
            }
        }
    }

    val cs = MaterialTheme.colorScheme

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            HeaderRow(
                username = username, // Usando o parâmetro da função
                avatarInitials = avatarInitials, // Usando o parâmetro da função
                onNotificationsClick = onNotificationsClick
            )
        }

        item {
            CardStreakWeek(
                state = streakState,
                onCheckInClick = { index ->
                    // Lógica de check-in normal que você já tem
                    val updatedDays = streakState.checkedDays.toMutableSet()
                    if (!updatedDays.contains(index)) {
                        updatedDays.add(index)
                        if (index == streakState.todayIndex) {
                            avatarState.consecutiveDays += 1
                        }
                    }
                    streakState = streakState.copy(checkedDays = updatedDays)
                },
                onClaimPremium = {
                    // Lógica de RESGATE
                    avatarState.xp += 200 // Adiciona os 200 pontos
                    avatarState.level = levelFromXp(avatarState.xp) // Atualiza nível se necessário

                    // Resetamos a semana para um novo ciclo de 7 dias
                    streakState = streakState.copy(checkedDays = emptySet())
                    avatarState.consecutiveDays = 0

                    // Dica: Você pode disparar um som ou efeito de confetti aqui!
                }
            )
        }

        item {
            ContainerLevel(
                state = avatarState,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            AvatarCard(state = avatarState)
        }

        item {
            PointsGainCard(
                todayPts = tasks.filter { it.completed }.sumOf { it.xp },
                totalPts = avatarState.todayPts,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text(
                text = "Daily Tasks",
                color = cs.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 6.dp, bottom = 4.dp)
            )
        }

        items(tasks, key = { it.id }) { task ->
            DailyTaskItemAvatar(
                task = task,
                avatarState = avatarState,
                onToggle = { t ->
                    val index = tasks.indexOfFirst { it.id == t.id }
                    if (index >= 0) {
                        val current = tasks[index]
                        val staminaCost = (current.xp / 2).coerceIn(5, 30)

                        if (!current.completed) {
                            if (avatarState.food >= staminaCost) {
                                avatarState.food =
                                    (avatarState.food - staminaCost).coerceAtLeast(0)
                            } else {
                                avatarState.hp = (avatarState.hp - 10).coerceAtLeast(0)
                                avatarState.food = 0
                            }
                            avatarState.xp += current.xp
                            val oldLevel = avatarState.level
                            avatarState.level = levelFromXp(avatarState.xp)
                            if (avatarState.level > oldLevel) {
                                levelUpTrigger = true
                            }
                        } else {
                            avatarState.food =
                                (avatarState.food + (staminaCost / 2)).coerceAtMost(100)
                            avatarState.xp = maxOf(0, avatarState.xp - current.xp)
                            avatarState.level = levelFromXp(avatarState.xp)
                        }

                        tasks[index] = current.copy(completed = !current.completed)
                    }
                }
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        // Sincroniza a perda do dia nos dois componentes
                        avatarState.consecutiveDays = 0
                        streakState = streakState.copy(checkedDays = emptySet())

                        avatarState.hp = (avatarState.hp - 10).coerceAtLeast(0)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = cs.surfaceVariant)
                ) {
                    Text("Simulate Missed Day", color = cs.onSurface)
                }

                Button(
                    onClick = {
                        avatarState.food = (avatarState.food + 30).coerceAtMost(100)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = cs.primary)
                ) {
                    Text("Rest +30 Stamina", color = cs.onPrimary)
                }
            }
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

@Composable
fun HeaderRow(
    username: String,
    avatarInitials: String,
    onNotificationsClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Chamando a lógica KMP pura
    val greeting = getGreeting()


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar com gradiente e profundidade
            Surface(
                modifier = Modifier.size(54.dp),
                shape = CircleShape,
                color = cs.secondaryContainer,
                border = BorderStroke(
                    width = 2.dp,
                    brush = Brush.linearGradient(listOf(cs.primary, cs.secondary))
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = avatarInitials,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = cs.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = greeting,
                    style = MaterialTheme.typography.labelMedium,
                    color = cs.onSurfaceVariant.copy(alpha = 0.7f)
                )
                Text(
                    text = username,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = cs.onSurface
                )
            }
        }

        // Botão de notificação profissional
        Surface(
            onClick = onNotificationsClick,
            shape = RoundedCornerShape(16.dp),
            color = cs.surface,
            border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f)),
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.NotificationsNone,
                    contentDescription = null,
                    tint = cs.onSurface,
                    modifier = Modifier.size(24.dp)
                )
                // Badge de notificação sutil
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(8.dp)
                        .background(cs.error, CircleShape)
                        .border(1.5.dp, cs.surface, CircleShape)
                )
            }
        }
    }
}


