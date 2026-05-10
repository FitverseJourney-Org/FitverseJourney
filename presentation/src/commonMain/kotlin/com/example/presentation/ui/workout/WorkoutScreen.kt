package com.example.presentation.ui.workout

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.FitverseColors
import com.example.presentation.ui.dashboard.components.SectionHeader

val categories = listOf("Força", "Cardio", "Hipertrofia", "HIIT")

val recommendedWorkouts = listOf(
    Triple("💪", "PEITO & TRÍCEPS", "Intermediário · 45 min · 18 séries" to "+150 PTS"),
    Triple("🏋️", "COSTAS & BÍCEPS", "Intermediário · 50 min · 20 séries" to "+160 PTS"),
    Triple("🦵", "PERNAS COMPLETO",  "Avançado · 60 min · 24 séries"      to "+200 PTS"),
)

@Composable
fun WorkoutScreen(
    onStartWorkout: () -> Unit = {},
    workoutCompletedToday: Boolean = false
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = Color.Transparent,
        content = {
            ContentWorkoutScreen(
                onStartWorkout = onStartWorkout,
                workoutCompletedToday = workoutCompletedToday
            )
        }
    )
}

@Composable
fun ContentWorkoutScreen(
    onStartWorkout: () -> Unit = {},
    workoutCompletedToday: Boolean = false
) {
    var selectedCategory by remember { mutableStateOf(0) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { WorkoutHeader() }

        item {
            if (workoutCompletedToday) {
                WorkoutDoneCard()
            } else {
                WorkoutMainCard(onStart = onStartWorkout)
            }
        }

        item {
            CategoryFilter(
                selected = selectedCategory,
                onSelect = { selectedCategory = it }
            )
        }

        item { SectionHeader("RECOMENDADOS") }

        items(recommendedWorkouts) { (emoji, name, detail) ->
            WorkoutCard(
                emoji = emoji,
                name = name,
                subtitle = detail.first,
                reward = detail.second
            )
        }
    }
}

// ── Sub-composables ───────────────────────────────────────────────────────────

@Composable
private fun WorkoutHeader() {
    Column {
        Text(
            text = "SEGUNDA-FEIRA",
            color = FitverseColors.TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.5.sp
        )
        Text(
            text = "TREINO DO DIA",
            color = FitverseColors.TextPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FitChip("LEVEL 23", FitverseColors.Purple)
            FitChip("+150 PTS", FitverseColors.AccentDim, textColor = FitverseColors.Accent)
        }
    }
}

@Composable
fun FitChip(
    text: String,
    color: Color,
    textColor: Color = FitverseColors.Bg
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(color)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun WorkoutMainCard(onStart: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .border(1.dp, Color(0xFF2a2a35), RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cs.surface)
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "HYPERTROPHY A",
                    color = FitverseColors.TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FitverseColors.Accent)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("42:15", color = FitverseColors.Bg, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text("Fase 2 · Semana 3", color = FitverseColors.TextMuted, fontSize = 13.sp)
            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                WorkoutStat("18",   "Séries")
                WorkoutStat("8.4k", "Volume")
                WorkoutStat("134",  "BPM")
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitverseColors.Accent)
            ) {
                Text(
                    "▶  INICIAR TREINO",
                    color = FitverseColors.Bg,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun WorkoutDoneCard(
    workoutName: String = "HYPERTROPHY A",
    phase: String = "Fase 2 · Semana 3",
    duration: String = "42:15",
    sets: String = "18",
    pointsEarned: String = "+150 PTS",
    onViewSummary: () -> Unit = {},
    onNextWorkout: () -> Unit = {}
) {
    val pulse = rememberInfiniteTransition(label = "done_glow")
    val glowAlpha by pulse.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, FitverseColors.Green.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        FitverseColors.Green.copy(alpha = 0.10f),
                        FitverseColors.Green.copy(alpha = 0.03f),
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Ícone de conclusão ────────────────────────────────────────
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(FitverseColors.Green.copy(alpha = glowAlpha), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(FitverseColors.GreenDim, CircleShape)
                        .border(1.5.dp, FitverseColors.Green.copy(alpha = 0.55f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = FitverseColors.Green,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Título ────────────────────────────────────────────────────
            Text(
                text = "TREINO CONCLUÍDO",
                color = FitverseColors.Green,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = workoutName,
                color = FitverseColors.TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = phase,
                color = FitverseColors.TextMuted,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(24.dp))

            // ── Métricas ──────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DoneStat(icon = Icons.Rounded.Timer,        value = duration,      label = "Duração")
                DoneStatDivider()
                DoneStat(icon = Icons.Rounded.FitnessCenter, value = sets,         label = "Séries")
                DoneStatDivider()
                DoneStat(icon = Icons.Rounded.EmojiEvents,  value = pointsEarned,  label = "Pontos")
            }

            Spacer(Modifier.height(24.dp))

            // ── CTAs ──────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onViewSummary,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, FitverseColors.Green.copy(alpha = 0.45f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = FitverseColors.Green)
                ) {
                    Text(
                        text = "VER RESUMO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Button(
                    onClick = onNextWorkout,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FitverseColors.Green)
                ) {
                    Text(
                        text = "PRÓXIMO",
                        color = Color(0xFF0A0A0F),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DoneStat(icon: ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = FitverseColors.Green.copy(alpha = 0.8f),
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = value,
            color = FitverseColors.TextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = label,
            color = FitverseColors.TextMuted,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun DoneStatDivider() {
    Box(
        modifier = Modifier
            .height(36.dp)
            .width(1.dp)
            .background(FitverseColors.Green.copy(alpha = 0.15f))
    )
}

@Composable
private fun CategoryFilter(selected: Int, onSelect: (Int) -> Unit) {
    val cs = MaterialTheme.colorScheme
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed(categories) { index, cat ->
            val isSelected = index == selected
            Box(
                modifier = Modifier
                    .border(1.dp, Color(0xFF2a2a35), RoundedCornerShape(50.dp))
                    .clip(RoundedCornerShape(50.dp))
                    .background(if (isSelected) FitverseColors.Accent else cs.surface)
                    .clickable { onSelect(index) }
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Text(
                    cat,
                    color = if (isSelected) FitverseColors.Bg else FitverseColors.TextMuted,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun WorkoutCard(
    emoji: String,
    name: String,
    subtitle: String,
    reward: String,
    onClick: () -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .border(1.dp, Color(0xFF2a2a35), RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(cs.surface.copy(alpha = .5f))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(FitverseColors.Surface2),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = 24.sp)
        }

        Spacer(Modifier.width(14.dp))

        Column(Modifier.weight(1f)) {
            Text(name,     color = FitverseColors.TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = FitverseColors.TextMuted,   fontSize = 12.sp)
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(FitverseColors.AccentDim.copy(alpha = 0.2f))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(reward, color = FitverseColors.Accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.width(8.dp))
        Text("›", color = FitverseColors.TextMuted, fontSize = 18.sp)
    }
}

@Composable
fun WorkoutStat(value: String, label: String) {
    Column {
        Text(value, color = FitverseColors.TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(label, color = FitverseColors.TextMuted,   fontSize = 12.sp)
    }
}
