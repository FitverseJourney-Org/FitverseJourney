package com.example.presentation.screens.ui.dashboard

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.dashboard.components.AnimatedStreakDialog
import com.example.presentation.screens.ui.dashboard.components.CardStreakWeek
import com.example.presentation.screens.ui.dashboard.components.HomeHeader
import com.example.presentation.screens.ui.dashboard.components.MetricCard
import com.example.presentation.screens.ui.dashboard.components.MissionCard
import com.example.presentation.screens.ui.dashboard.components.PlayerProfileCard
import com.example.presentation.screens.ui.dashboard.components.SectionHeader
import com.example.presentation.screens.ui.dashboard.util.StreakState
import com.example.presentation.screens.ui.dashboard.util.getGreeting
import com.example.presentation.screens.widgets.DailyStreakCard
import com.example.presentation.screens.widgets.FitVerseSpacer
import com.example.presentation.screens.widgets.StreakDay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    username: String = "Athlete",
    avatarInitials: String = "A",
    exit: () -> Unit,
    onNotificationsClick: () -> Unit
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
    Box(
        modifier = Modifier.fillMaxSize(),
        content = {
            DarkGamifiedDashboardBackground()
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets(0,0,0,0),
                containerColor = Color.Transparent,
                content = {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                        contentPadding = PaddingValues(top = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp) // Espaço respirável
                    ) {
                        item {
                            HomeHeader(
                                greeting = getGreeting(),
                                userName = username,
                                onEnergyClick = {  },
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
                                days = listOf(
                                    StreakDay("S", isCompleted = true),
                                    StreakDay("T", isCompleted = true),
                                    StreakDay("Q", isCompleted = true),
                                    StreakDay("Q", isCompleted = true),
                                    StreakDay("S", isCompleted = true),
                                    StreakDay("S", isCompleted = true),
                                    StreakDay("D", isCompleted = true)
                                )
                            )
                        }
                        item {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                SectionHeader("Condição Física")

                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    MetricCard(
                                        title = "Passos", value = "6.5k", target = "10k",
                                        subtitle = "65% da meta", icon = Icons.Default.DirectionsRun,
                                        accentColor = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.weight(1f)
                                    )
                                    MetricCard(
                                        title = "Água", value = "2.4", target = "3.5",
                                        subtitle = "Boa hidratação", icon = Icons.Default.Water,
                                        accentColor = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(Modifier.height(32.dp))

                                SectionHeader("Missões Diárias", actionText = "Ver todas")

                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    MissionCard("Morning Stretch", "5–10 min • Aqueça seu corpo", 10) {
                                        Text(
                                            text = "\uD83E\uDD57\n"
                                        )
                                    }
                                    MissionCard("Registrar Hidratação", "8 copos • Meta de água", null, isCompleted = true) {
                                        Text(
                                            text = "\uD83E\uDD57\n",
                                            fontSize = 24.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    MissionCard("Cardio 30 min", "Caminhar • Correr • Bike", 30) {
                                        Text(
                                            text = "\uD83E\uDD57\n",
                                            fontSize = 24.sp
                                        )
                                    }
                                    MissionCard("Cardio 30 min", "Caminhar • Correr • Bike", 30) {
                                        Text(
                                            text = "\uD83E\uDD57\n",
                                            fontSize = 24.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    )

}
@Composable
fun DarkGamifiedDashboardBackground(
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val infinite = rememberInfiniteTransition(label = "TriadTransition")

    // Animações independentes para cada zona (Paralaxe de brilho)
    val topPulse by infinite.animateFloat(
        initialValue = 0.6f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(12000), RepeatMode.Reverse), label = "top"
    )
    val midShift by infinite.animateFloat(
        initialValue = -50f, targetValue = 50f,
        animationSpec = infiniteRepeatable(tween(15000), RepeatMode.Reverse), label = "mid"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 0. Fundo OLED Absoluto
        drawRect(color = colors.background)

        // 1. TOPO: Aura de Identidade (Roxo Primary)
        // Foca no status do Avatar e XP
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(colors.primary.copy(alpha = 0.12f * topPulse), Color.Transparent),
                center = Offset(w * 0.5f, h * 0.05f),
                radius = w * 0.8f
            ),
            radius = w * 0.8f,
            center = Offset(w * 0.5f, h * 0.05f)
        )

        // 2. MEIO: Vortex de Atividade (Azul Secondary)
        // Cria profundidade atrás dos widgets principais
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(colors.secondary.copy(alpha = 0.06f), Color.Transparent),
                center = Offset(w / 2f + midShift, h * 0.45f),
                radius = w * 0.7f
            ),
            radius = w * 0.7f,
            center = Offset(w / 2f + midShift, h * 0.45f),
            blendMode = BlendMode.Screen
        )

        // 3. BOTTOM: Névoa de Estabilidade (PrimaryContainer/Deep Purple)
        // Dá suporte visual para a NavigationBar transparente
        val footerPath = Path().apply {
            val footerY = h * 0.88f
            moveTo(0f, footerY)
            cubicTo(
                w * 0.3f, footerY - 30f,
                w * 0.7f, footerY + 30f,
                w, footerY
            )
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(
            path = footerPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    colors.primaryContainer.copy(alpha = 0.15f),
                    colors.background
                )
            )
        )

        // 4. DETALHE SÊNIOR: Micro-Glow de Acabamento (Verde Tertiary)
        // Um pequeno "flare" de saúde que aparece e desaparece no canto
        drawCircle(
            color = colors.tertiary.copy(alpha = 0.03f),
            radius = w * 0.4f,
            center = Offset(w * 0.9f, h * 0.25f)
        )
    }
}