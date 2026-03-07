package com.example.presentation.screens.ui.main.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.example.domain.model.dashboard.TaskIcon
import com.example.domain.model.dashboard.TaskItem
import com.example.presentation.core.utils.Dashboard.levelFromXp
import com.example.presentation.screens.ui.main.dashboard.components.AvatarCard
import com.example.presentation.screens.ui.main.dashboard.components.CardStreakWeek
import com.example.presentation.screens.ui.main.dashboard.components.DailyTaskItemAvatar
import com.example.presentation.screens.ui.main.dashboard.components.PointsGainCard
import com.example.presentation.screens.ui.main.dashboard.components.StreakCard
import com.example.presentation.screens.ui.main.dashboard.components.StreakState
import com.example.presentation.theme.AccentGreen
import com.example.presentation.theme.BaseGreen
import com.example.presentation.theme.DeepGreen
import com.example.presentation.theme.OnSurfaceText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import androidx.compose.runtime.mutableStateListOf as rememberMutableStateListOf

data class AvatarState(
    val name: String = "Your Avatar",
    var level: Int = 5,
    var xp: Int = 340,
    val xpToNext: Int = 500,
    val health: Int = 0,
    val points: Int = 0,
    val todayPts: Int,
    var stamina: Int = 60,
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
    modifier: Modifier = Modifier,
    username: String = "Athlete",
    avatarInitials: String = "A",
    exit: () -> Unit,
    onNotificationsClick: () -> Unit
){
    // in-memory state (mantive sua lógica)
    val avatarState = remember {
        AvatarState(
            xp = 120,
            level = levelFromXp(120),
            hp = 19,
            stamina = 75,
            consecutiveDays = 3,
            todayPts = 100,
            health = 19,
            points = 100
        )
    }
    val tasks = remember { mutableStateListOf<TaskItem>().apply { addAll(sampleTasksDataV12()) } }

    var levelUpTrigger by remember { mutableStateOf(false) }
    var previousLevel by remember { mutableStateOf(avatarState.level) }
    var streakState by remember {
        mutableStateOf(
            StreakState(
                checkedDays = setOf(1, 2),
                todayIndex = 3,
                consecutiveGoal = 5
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

    // usaremos as cores do MaterialTheme
    val cs = MaterialTheme.colorScheme

    // gradiente de fundo: usando primary e primaryContainer (ou surface -> card)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            HeaderRow(
                username = "username",
                avatarInitials = "A",
                onNotificationsClick = {
                    onNotificationsClick()
                }
            )
        }

        item {
            CardStreakWeek(state = streakState)
        }

        item {
            AvatarCard(state = avatarState) // componente existente — deve usar MaterialTheme internamente
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                var rewardClaimed by remember { mutableStateOf(false) }
                StreakCard(
                    days = avatarState.consecutiveDays,
                    rewardClaimed = rewardClaimed,
                    onClaimReward = {
                        avatarState.xp += 100 // recompensa real
                        rewardClaimed = true
                        avatarState.consecutiveDays = 0
                    },
                    modifier = Modifier.weight(1f)
                )


            }
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
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
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
                            if (avatarState.stamina >= staminaCost) {
                                avatarState.stamina =
                                    (avatarState.stamina - staminaCost).coerceAtLeast(0)
                            } else {
                                avatarState.hp = (avatarState.hp - 10).coerceAtLeast(0)
                                avatarState.stamina = 0
                            }
                            avatarState.xp += current.xp
                            val oldLevel = avatarState.level
                            avatarState.level = levelFromXp(avatarState.xp)
                            if (avatarState.level > oldLevel) {
                                levelUpTrigger = true
                            }
                        } else {
                            avatarState.stamina =
                                (avatarState.stamina + (staminaCost / 2)).coerceAtMost(100)
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
                        avatarState.consecutiveDays = 0
                        avatarState.hp = (avatarState.hp - 10).coerceAtLeast(0)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = cs.surfaceVariant)
                ) {
                    Text("Simulate Missed Day", color = cs.onSurface)
                }

                Button(
                    onClick = {
                        avatarState.stamina = (avatarState.stamina + 30).coerceAtMost(100)
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
fun HeaderRow(username: String, avatarInitials: String, onNotificationsClick: () -> Unit) {
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(cs.secondary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = avatarInitials,
                    color = cs.onSecondary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Welcome back",
                    color = cs.onBackground.copy(alpha = 0.9f),
                    fontSize = 12.sp
                )
                Text(
                    text = username,
                    color = cs.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        IconButton(onClick = onNotificationsClick, shape = RoundedCornerShape(5.dp)) {
            Icon(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = cs.onSurface.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(8.dp),
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notifications",
                tint = cs.onSurface
            )
        }
    }
}



