package org.fitverse.presentation.ui.workout

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
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.theme.FVTypography
import org.fitverse.presentation.ui.dashboard.components.SectionHeader
import org.fitverse.presentation.ui.workout.components.WorkoutMainCardy

// ── Data ─────────────────────────────────────────────────────────────────────

data class RecommendedWorkout(
    val icon: ImageVector,
    val iconColor: Color,
    val name: String,
    val difficulty: String,
    val difficultyColor: Color,
    val duration: String,
    val sets: String,
    val reward: String,
)

val categories = listOf("Força", "Cardio", "Hipertrofia", "HIIT")

val recommendedWorkouts = listOf(
    RecommendedWorkout(
        icon           = Icons.Rounded.FitnessCenter,
        iconColor      = FitColors.Accent,
        name           = "PEITO & TRÍCEPS",
        difficulty     = "Intermediário",
        difficultyColor= FitColors.Amber,
        duration       = "45 min",
        sets           = "18 séries",
        reward         = "+150 PTS"
    ),
    RecommendedWorkout(
        icon           = Icons.Rounded.FitnessCenter,
        iconColor      = FitColors.Blue,
        name           = "COSTAS & BÍCEPS",
        difficulty     = "Intermediário",
        difficultyColor= FitColors.Amber,
        duration       = "50 min",
        sets           = "20 séries",
        reward         = "+160 PTS"
    ),
    RecommendedWorkout(
        icon           = Icons.Rounded.DirectionsRun,
        iconColor      = FitColors.Orange,
        name           = "PERNAS COMPLETO",
        difficulty     = "Avançado",
        difficultyColor= FitColors.Red,
        duration       = "60 min",
        sets           = "24 séries",
        reward         = "+200 PTS"
    ),
)

// ── Screen ───────────────────────────────────────────────────────────────────

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
            if (workoutCompletedToday) WorkoutDoneCard()
            else WorkoutMainCardy(onStart = onStartWorkout)
        }
        item { SectionHeader("RECOMENDADOS") }
        item {
            CategoryFilter(
                selected = selectedCategory,
                onSelect = { selectedCategory = it }
            )
        }
        items(recommendedWorkouts) { workout ->
            WorkoutCard(workout = workout)
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun WorkoutHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text  = "SEGUNDA-FEIRA",
            style = FVTypography.overlineLarge,
            color = FitColors.TextMuted,
        )
        Text(
            text  = "TREINO DO DIA",
            style = FVTypography.displayLarge,
            color = FitColors.TextPrimary,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FitChip("LEVEL 23", FitColors.Purple)
            FitChip("+150 PTS", FitColors.AccentDim, textColor = FitColors.Accent)
        }
        Spacer(Modifier.height(8.dp))
        WeekPlanDots()
    }
}

@Composable
private fun WeekPlanDots() {
    // label / done / isToday — mock: Mon–Wed done, Thu = today
    val days = listOf(
        Triple("S", true,  false),
        Triple("T", true,  false),
        Triple("Q", true,  false),
        Triple("Q", false, true),
        Triple("S", false, false),
        Triple("S", false, false),
        Triple("D", false, false),
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        days.forEach { (label, done, isToday) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text  = label,
                    style = FVTypography.bodyLarge.copy(
                        fontWeight = if (isToday) FontWeight.Black else FontWeight.Normal,
                    ),
                    color = when {
                        isToday -> FitColors.Accent
                        done    -> FitColors.TextMuted
                        else    -> FitColors.TextDisabled
                    },
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                done    -> FitColors.Accent
                                isToday -> FitColors.AccentDim
                                else    -> FitColors.Surface2
                            }
                        )
                        .then(
                            if (isToday) Modifier.border(1.dp, FitColors.Accent.copy(alpha = 0.6f), CircleShape)
                            else Modifier
                        )
                )
            }
        }
    }
}

// ── Chip ─────────────────────────────────────────────────────────────────────

@Composable
fun FitChip(
    text: String,
    color: Color,
    textColor: Color = FitColors.Bg
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(color)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = text, style = FVTypography.labelLarge, color = textColor)
    }
}

// ── Main card (workout not done today) ────────────────────────────────────────

@Composable
private fun WorkoutMainCard(onStart: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .border(1.dp, Color(0xFF2a2a35), RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cs.surface)
    ) {
        Column {
            // Accent gradient top strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                FitColors.Accent.copy(alpha = 0.6f),
                                FitColors.Accent,
                                FitColors.Accent.copy(alpha = 0.6f),
                                Color.Transparent,
                            )
                        )
                    )
            )

            Column(modifier = Modifier.padding(20.dp)) {
                // Title row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text  = "HYPERTROPHY A",
                            style = FVTypography.headlineMedium,
                            color = FitColors.TextPrimary,
                        )
                        Text(
                            text  = "Fase 2 · Semana 3",
                            style = FVTypography.bodyMedium,
                            color = FitColors.TextMuted,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(FitColors.Accent)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text  = "42:15",
                            style = FVTypography.monoMedium.copy(fontWeight = FontWeight.Black),
                            color = FitColors.Bg,
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Plan progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text  = "Progresso do plano",
                        style = FVTypography.labelMedium,
                        color = FitColors.TextMuted,
                    )
                    Text(
                        text  = "37%",
                        style = FVTypography.monoSmall.copy(fontWeight = FontWeight.Black),
                        color = FitColors.Accent,
                    )
                }
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { 0.37f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = FitColors.Accent,
                    trackColor = FitColors.Surface2,
                )

                Spacer(Modifier.height(20.dp))

                // Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WorkoutStat(Icons.Rounded.FitnessCenter, "18",   "Séries", FitColors.Accent)
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(36.dp)
                            .background(Color(0xFF2a2a35))
                    )
                    WorkoutStat(Icons.Rounded.Bolt,          "8.4k", "Volume", FitColors.Blue)
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(36.dp)
                            .background(Color(0xFF2a2a35))
                    )
                    WorkoutStat(Icons.Rounded.Favorite,      "134",  "BPM",    FitColors.Red)
                }

                Spacer(Modifier.height(20.dp))

                // CTA
                Button(
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FitColors.Accent)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        tint = FitColors.Bg,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text  = "INICIAR TREINO",
                        style = FVTypography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = FitColors.Bg,
                    )
                }
            }
        }
    }
}

// ── Done card (workout completed today) ───────────────────────────────────────

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
            .border(1.dp, FitColors.Green.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        FitColors.Green.copy(alpha = 0.10f),
                        FitColors.Green.copy(alpha = 0.03f),
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Completion icon with glow
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(FitColors.Green.copy(alpha = glowAlpha), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(FitColors.GreenDim, CircleShape)
                        .border(1.5.dp, FitColors.Green.copy(alpha = 0.55f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = FitColors.Green,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text  = "TREINO CONCLUÍDO",
                style = FVTypography.overlineLarge.copy(letterSpacing = 2.sp),
                color = FitColors.Green,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text      = workoutName,
                style     = FVTypography.headlineLarge,
                color     = FitColors.TextPrimary,
                textAlign = TextAlign.Center,
            )
            Text(
                text  = phase,
                style = FVTypography.bodyMedium,
                color = FitColors.TextMuted,
            )

            Spacer(Modifier.height(24.dp))

            // Metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DoneStat(icon = Icons.Rounded.Timer,         value = duration,     label = "Duração")
                DoneStatDivider()
                DoneStat(icon = Icons.Rounded.FitnessCenter, value = sets,         label = "Séries")
                DoneStatDivider()
                DoneStat(icon = Icons.Rounded.EmojiEvents,   value = pointsEarned, label = "Pontos")
            }

            Spacer(Modifier.height(24.dp))

            // CTAs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onViewSummary,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, FitColors.Green.copy(alpha = 0.45f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = FitColors.Green)
                ) {
                    Text(
                        text  = "VER RESUMO",
                        style = FVTypography.labelLarge,
                    )
                }

                Button(
                    onClick = onNextWorkout,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FitColors.Green)
                ) {
                    Text(
                        text  = "PRÓXIMO",
                        style = FVTypography.labelLarge.copy(fontWeight = FontWeight.Black),
                        color = Color(0xFF0A0A0F),
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
            tint = FitColors.Green.copy(alpha = 0.8f),
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text  = value,
            style = FVTypography.headlineMedium,
            color = FitColors.TextPrimary,
        )
        Text(
            text  = label,
            style = FVTypography.labelMedium,
            color = FitColors.TextMuted,
        )
    }
}

@Composable
private fun DoneStatDivider() {
    Box(
        modifier = Modifier
            .height(36.dp)
            .width(1.dp)
            .background(FitColors.Green.copy(alpha = 0.15f))
    )
}

// ── Category filter ───────────────────────────────────────────────────────────

@Composable
private fun CategoryFilter(selected: Int, onSelect: (Int) -> Unit) {
    val cs = MaterialTheme.colorScheme
    LazyRow(modifier = Modifier.padding(bottom = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed(categories) { index, cat ->
            val isSelected = index == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(if (isSelected) FitColors.Accent else cs.surface)
                    .then(
                        if (isSelected) Modifier
                        else Modifier.border(1.dp, Color(0xFF2a2a35), RoundedCornerShape(50.dp))
                    )
                    .clickable { onSelect(index) }
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Text(
                    text  = cat,
                    style = FVTypography.bodyMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    ),
                    color = if (isSelected) FitColors.Bg else FitColors.TextMuted,
                )
            }
        }
    }
}

// ── Recommended workout card ──────────────────────────────────────────────────

@Composable
fun WorkoutCard(
    workout: RecommendedWorkout,
    onClick: () -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(
            width = 1.dp, Color(0xFF2a2a35)
        ),
        color = cs.surface.copy(alpha = .5f),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        content = {
            Row(modifier = Modifier.fillMaxSize().padding(8.dp), verticalAlignment = Alignment.CenterVertically){
                // Icon box
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(workout.iconColor.copy(alpha = 0.10f))
                        .border(1.dp, workout.iconColor.copy(alpha = 0.25f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = workout.icon,
                        contentDescription = null,
                        tint = workout.iconColor,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        text  = workout.name,
                        style = FVTypography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = FitColors.TextPrimary,
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(workout.difficultyColor)
                        )
                        Text(
                            text  = "${workout.difficulty} · ${workout.duration} · ${workout.sets}",
                            style = FVTypography.labelMedium,
                            color = FitColors.TextMuted,
                        )
                    }
                }

                Spacer(Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FitColors.AccentDim)
                        .border(1.dp, FitColors.Accent.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Text(
                        text  = workout.reward,
                        style = FVTypography.monoSmall.copy(fontWeight = FontWeight.Black),
                        color = FitColors.Accent,
                    )
                }

                Spacer(Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = FitColors.TextDisabled,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    )
}

// ── Shared stat composable ────────────────────────────────────────────────────
@Composable
fun WorkoutStat(
    icon: ImageVector,
    value: String,
    label: String,
    iconColor: Color = FitColors.TextMuted
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor.copy(alpha = 0.75f),
            modifier = Modifier.size(14.dp)
        )
        Text(text = value, style = FVTypography.monoLarge, color = FitColors.TextPrimary)
        Text(text = label, style = FVTypography.labelMedium, color = FitColors.TextMuted)
    }
}
