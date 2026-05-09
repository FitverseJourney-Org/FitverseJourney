package com.example.presentation.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import com.example.presentation.widgets.FitVerseSpacer
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
    onEnergyClick: () -> Unit
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
                listOfStreakDay = listOfStreakDay
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
    listOfStreakDay: List<StreakDay>
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HomeHeader(
                greeting = getGreeting(),
                userName = username,
                onEnergyClick = onEnergyClick,
                onNotificationClick = onNotificationsClick,
            )
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PlayerProfileCard()
                FitVerseSpacer(vertical = true, value = 16.dp)
            }
        }
        item {
            DailyStreakCard(
                currentStreak = totalStreakCount,
                days = listOfStreakDay
            )
        }
        item {
            Column(modifier = Modifier.fillMaxSize()) {
                SectionHeader("Condição Física")

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

                Spacer(Modifier.height(32.dp))

                SectionHeader("Missões Diárias", actionText = "Ver todas")

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    defaultDailyMissions.forEach { mission ->
                        MissionCard(
                            title = mission.title,
                            description = mission.description,
                            xp = mission.xp,
                            icon = mission.type.icon,
                            iconColor = mission.type.color,
                            isCompleted = mission.isCompleted,
                            onClaim = { println("XP ganho: ${mission.xp}") }
                        )
                    }
                }
            }
        }
    }
}
