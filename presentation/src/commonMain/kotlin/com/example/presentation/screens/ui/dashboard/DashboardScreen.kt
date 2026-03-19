package com.example.presentation.screens.ui.dashboard

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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.presentation.screens.ui.dashboard.components.AvatarCard
import com.example.presentation.screens.ui.dashboard.components.CardStreakWeek
import com.example.presentation.screens.ui.dashboard.components.ContainerLevel
import com.example.presentation.screens.ui.dashboard.components.DailyTaskItemAvatar
import com.example.presentation.screens.ui.dashboard.components.PointsGainCard
import com.example.presentation.screens.ui.dashboard.components.StreakState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

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
    val tasks = remember { mutableStateListOf<TaskItem>().apply { addAll(_root_ide_package_.com.example.presentation.screens.ui.dashboard.sampleTasksDataV12()) } }

    var levelUpTrigger by remember { mutableStateOf(false) }
    var previousLevel by remember { mutableStateOf(avatarState.level) }

    // Inicializamos o StreakState já com os dias marcados, simulando os 3 dias do avatarState
    var totalStreakCount by rememberSaveable { mutableStateOf(0) }
    var lastCheckInDate by rememberSaveable { mutableStateOf<String?>(null) } // Formato "yyyy-MM-dd"

    // 2. Cálculo do estado atual
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val isTodayChecked = lastCheckInDate == today.toString()

    // 3. Criamos o StreakState para passar para o componente visual
    val streakState =
        _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.StreakState(
            totalStreakCount = totalStreakCount,
            isTodayChecked = isTodayChecked
        )

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
            _root_ide_package_.com.example.presentation.screens.ui.dashboard.HeaderRow(
                username = username, // Usando o parâmetro da função
                avatarInitials = avatarInitials, // Usando o parâmetro da função
                onNotificationsClick = onNotificationsClick
            )
        }

        item {
            _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.CardStreakWeek(
                state = streakState,
                onCheckInClick = {
                    val now =
                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

                    if (lastCheckInDate == null) {
                        // Primeiro check-in da história
                        totalStreakCount = 1
                    } else {
                        val lastDate = LocalDate.parse(lastCheckInDate!!)
                        val daysBetween = now.daysUntil(lastDate) // Comparação de datas

                        when {
                            now == lastDate -> { /* Já fez check-in hoje, não faz nada */
                            }

                            daysBetween == -1 -> {
                                // Ontem foi o último check-in, mantém o streak vivo!
                                totalStreakCount += 1
                            }

                            else -> {
                                // Perdeu um dia ou mais, resetou o streak para 1
                                totalStreakCount = 1
                            }
                        }
                    }
                    lastCheckInDate = now.toString()
                },
                onClaimPremium = {
                    // Lógica de resgate: Opcional resetar ou apenas dar os pontos
                    println("Resgatou prêmio do dia ${streakState.totalStreakCount}!")
                }
            )
        }

        item {
            _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.ContainerLevel(
                state = avatarState,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.AvatarCard(
                state = avatarState
            )
        }

        item {
            Text(
                modifier = Modifier.padding(top = 6.dp, bottom = 4.dp),
                text = "Daily Tasks",
                color = cs.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(tasks, key = { it.id }) { task ->
            _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.DailyTaskItemAvatar(
                task = task,
                onSelect = { },
                isSelected = task.completed,
                onToggle = {}
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

@Composable
fun HeaderRow(
    username: String,
    avatarInitials: String,
    onNotificationsClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Chamando a lógica KMP pura
    val greeting = _root_ide_package_.com.example.presentation.screens.ui.dashboard.getGreeting()


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
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


