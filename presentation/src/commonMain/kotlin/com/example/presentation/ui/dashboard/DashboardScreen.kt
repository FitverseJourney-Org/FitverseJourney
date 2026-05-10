package com.example.presentation.ui.dashboard

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.FitverseColors
import com.example.presentation.ui.dashboard.DailyMission.Companion.defaultDailyMissions
import com.example.presentation.ui.dashboard.MissionType
import com.example.presentation.ui.dashboard.components.AnimatedStreakDialog
import com.example.presentation.ui.dashboard.components.CardStreakWeek
import com.example.presentation.ui.dashboard.components.HomeHeader
import com.example.presentation.ui.dashboard.components.MetricCard
import com.example.presentation.ui.dashboard.components.MissionCard
import com.example.presentation.ui.dashboard.components.PlayerProfileCard
import com.example.presentation.ui.dashboard.components.SectionHeader
import com.example.presentation.ui.dashboard.util.StreakState
import com.example.presentation.ui.dashboard.util.getGreeting
import com.example.presentation.widgets.DailyStreakCard
import com.example.presentation.widgets.StreakDay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
    username: String = "Athlete",
    avatarInitials: String = "A",
    exit: () -> Unit,
    onNotificationsClick: () -> Unit,
    onEnergyClick: () -> Unit,
    onNavigateToWorkout: () -> Unit = {}
) {
    var showStreakDialog by remember { mutableStateOf(false) }
    var totalStreakCount by rememberSaveable { mutableStateOf(0) }
    var lastCheckInDate by rememberSaveable { mutableStateOf<String?>(null) }

    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val isTodayChecked = lastCheckInDate == today.toString()

    val streakState = StreakState(
        totalStreakCount = totalStreakCount,
        isTodayChecked = isTodayChecked
    )

    val listOfStreakDay = listOf(
        StreakDay("S", isCompleted = true),
        StreakDay("T", isCompleted = true),
        StreakDay("Q", isCompleted = true),
        StreakDay("Q", isCompleted = true),
        StreakDay("S", isCompleted = true),
        StreakDay("S", isCompleted = true),
        StreakDay("D", isCompleted = true)
    )

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
                        now == lastDate -> {}
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = Color.Transparent,
        content = {
            ContentDashboardScreen(
                modifier = Modifier.padding(it),
                username = username,
                avatarInitials = avatarInitials,
                exit = exit,
                onNotificationsClick = onNotificationsClick,
                onEnergyClick = onEnergyClick,
                onStreakClick = { showStreakDialog = true },
                totalStreakCount = totalStreakCount,
                listOfStreakDay = listOfStreakDay,
                onNavigateToWorkout = onNavigateToWorkout
            )
        }
    )
}

@Composable
fun ContentDashboardScreen(
    modifier: Modifier,
    username: String,
    avatarInitials: String,
    exit: () -> Unit,
    onNotificationsClick: () -> Unit,
    onEnergyClick: () -> Unit,
    onStreakClick: () -> Unit,
    totalStreakCount: Int,
    listOfStreakDay: List<StreakDay>,
    onNavigateToWorkout: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            HomeHeader(
                greeting = getGreeting(),
                userName = username,
                onEnergyClick = onEnergyClick,
                onNotificationClick = onNotificationsClick,
            )
        }

        item { PlayerProfileCard() }

        item { TodayWorkoutBanner(onStart = onNavigateToWorkout) }

        item {
            DailyStreakCard(
                currentStreak = totalStreakCount,
                days = listOfStreakDay
            )
        }

        item { SectionHeader("Condição Física") }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    title = "Passos",
                    value = "6.5k",
                    target = "10k",
                    subtitle = "65% da meta",
                    icon = Icons.Default.DirectionsRun,
                    accentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Água",
                    value = "2.4",
                    target = "3.5",
                    subtitle = "Boa hidratação",
                    icon = Icons.Default.Water,
                    accentColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item { SectionHeader("Missões Diárias") }

        items(defaultDailyMissions) { mission ->
            MissionCard(
                title = mission.title,
                description = mission.description,
                xp = mission.xp,
                icon = mission.type.icon,
                iconColor = mission.type.color,
                isCompleted = mission.isCompleted,
                isChallengeType = mission.type == MissionType.CHALLENGE,
                onClaim = { println("XP ganho: ${mission.xp}") }
            )
        }
    }
}

// ── Today's workout quick-access ──────────────────────────────────────────────

@Composable
private fun TodayWorkoutBanner(onStart: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .border(1.dp, Color(0xFF2a2a35), RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cs.surface)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                FitverseColors.Accent.copy(alpha = 0.6f),
                                FitverseColors.Accent,
                                FitverseColors.Accent.copy(alpha = 0.6f),
                                Color.Transparent,
                            )
                        )
                    )
            )
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "TREINO DE HOJE",
                        color = FitverseColors.TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "HYPERTROPHY A",
                        color = FitverseColors.TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Fase 2 · 18 séries · 45 min",
                        color = FitverseColors.TextMuted,
                        fontSize = 12.sp
                    )
                }
                Spacer(Modifier.width(12.dp))
                Button(
                    onClick = onStart,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FitverseColors.Accent),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(
                        "TREINO",
                        color = FitverseColors.Bg,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        tint = FitverseColors.Bg,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
