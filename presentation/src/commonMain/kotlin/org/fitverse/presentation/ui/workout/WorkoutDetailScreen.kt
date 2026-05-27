package org.fitverse.presentation.ui.workout

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

// ── Entry point ───────────────────────────────────────────────────────────────

@Composable
fun WorkoutDetailScreen(
    workout: RecommendedWorkout,
    onBack: () -> Unit,
    onStartWorkout: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier            = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor      = Color.Transparent,
    ) { _ ->
        LazyColumn(
            modifier       = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 52.dp),
        ) {
            item { WorkoutDetailHero(workout = workout, onBack = onBack) }
            item { Spacer(Modifier.height(20.dp)) }
            item { WorkoutInfoCard(workout = workout, modifier = Modifier.padding(horizontal = 20.dp)) }
            item { Spacer(Modifier.height(16.dp)) }
            item { WorkoutExerciseList(accent = workout.iconColor, modifier = Modifier.padding(horizontal = 20.dp)) }
            item { Spacer(Modifier.height(24.dp)) }
            item { WorkoutStartButton(accent = workout.iconColor, onClick = onStartWorkout, modifier = Modifier.padding(horizontal = 20.dp)) }
        }
    }
}

// ── Hero ──────────────────────────────────────────────────────────────────────

@Composable
private fun WorkoutDetailHero(workout: RecommendedWorkout, onBack: () -> Unit) {
    val accent = workout.iconColor

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 28.dp),
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(FitColors.Surface2)
                .clickable { onBack() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Voltar",
                tint     = FitColors.TextPrimary,
                modifier = Modifier.size(18.dp),
            )
        }

        Spacer(Modifier.height(26.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(accent.copy(alpha = 0.14f))
                    .border(1.dp, accent.copy(alpha = 0.30f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    workout.icon,
                    contentDescription = null,
                    tint     = accent,
                    modifier = Modifier.size(28.dp),
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text          = workout.name,
                    color         = FitColors.TextPrimary,
                    fontSize      = 22.sp,
                    fontWeight    = FontWeight.Black,
                    letterSpacing = 0.6.sp,
                )
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(workout.difficultyColor)
                    )
                    Text(
                        text       = workout.difficulty,
                        color      = workout.difficultyColor,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            DetailHeroStat(value = workout.duration, label = "DURAÇÃO",    valueColor = accent, modifier = Modifier.weight(1f))
            DetailHeroDivider()
            DetailHeroStat(value = workout.sets,     label = "SÉRIES",     modifier = Modifier.weight(1f))
            DetailHeroDivider()
            DetailHeroStat(value = workout.reward,   label = "RECOMPENSA", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun DetailHeroStat(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    valueColor: Color = FitColors.TextPrimary,
) {
    Column(
        modifier            = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text          = value,
            color         = valueColor,
            fontSize      = 18.sp,
            fontWeight    = FontWeight.Black,
            letterSpacing = (-0.5).sp,
            textAlign     = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text          = label,
            color         = FitColors.TextDisabled,
            fontSize      = 9.sp,
            fontWeight    = FontWeight.Bold,
            letterSpacing = 1.sp,
            textAlign     = TextAlign.Center,
        )
    }
}

@Composable
private fun DetailHeroDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(FitColors.Outline)
    )
}

// ── Info card ─────────────────────────────────────────────────────────────────

@Composable
private fun WorkoutInfoCard(workout: RecommendedWorkout, modifier: Modifier = Modifier) {
    val cs     = MaterialTheme.colorScheme
    val accent = workout.iconColor

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cs.surface)
            .border(1.dp, accent.copy(alpha = 0.30f), RoundedCornerShape(20.dp)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            accent.copy(0.5f),
                            accent.copy(0.85f),
                            accent.copy(0.5f),
                            Color.Transparent,
                        )
                    )
                )
        )
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            InfoChip(icon = Icons.Rounded.Timer,        value = workout.duration, color = accent,           modifier = Modifier.weight(1f))
            InfoChip(icon = Icons.Rounded.FitnessCenter, value = workout.sets,    color = FitColors.Purple,  modifier = Modifier.weight(1f))
            InfoChip(icon = Icons.Rounded.EmojiEvents,   value = workout.reward,  color = FitColors.Amber,   modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun InfoChip(
    icon: ImageVector,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.10f))
            .border(1.dp, color.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
        Text(
            text  = value,
            color = FitColors.TextPrimary,
            style = FVTypography.labelLarge.copy(fontWeight = FontWeight.Bold),
        )
    }
}

// ── Exercise list ─────────────────────────────────────────────────────────────

private val mockExercises = listOf(
    "Supino Reto"       to "4 séries × 12 reps",
    "Supino Inclinado"  to "3 séries × 10 reps",
    "Crossover"         to "3 séries × 15 reps",
    "Tríceps Pulley"    to "4 séries × 12 reps",
    "Tríceps Testa"     to "3 séries × 12 reps",
)

@Composable
private fun WorkoutExerciseList(accent: Color, modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme

    Column(modifier = modifier) {
        Text(
            text          = "EXERCÍCIOS",
            color         = FitColors.TextMuted,
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Bold,
            letterSpacing = 1.2.sp,
        )
        Spacer(Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(cs.surface)
                .border(1.dp, FitColors.Outline, RoundedCornerShape(20.dp)),
        ) {
            mockExercises.forEachIndexed { index, (name, sets) ->
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(accent.copy(alpha = 0.10f))
                            .border(1.dp, accent.copy(alpha = 0.25f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text       = "${index + 1}",
                            color      = accent,
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.Black,
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text  = name,
                            color = FitColors.TextPrimary,
                            style = FVTypography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        )
                        Text(
                            text  = sets,
                            color = FitColors.TextMuted,
                            style = FVTypography.labelMedium,
                        )
                    }
                    Icon(
                        Icons.Rounded.ChevronRight,
                        contentDescription = null,
                        tint     = FitColors.TextDisabled,
                        modifier = Modifier.size(16.dp),
                    )
                }
                if (index < mockExercises.lastIndex) {
                    HorizontalDivider(
                        modifier  = Modifier.padding(start = 66.dp),
                        thickness = 0.5.dp,
                        color     = FitColors.Outline,
                    )
                }
            }
        }
    }
}

// ── Start button ──────────────────────────────────────────────────────────────

@Composable
private fun WorkoutStartButton(
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick  = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape    = RoundedCornerShape(14.dp),
        colors   = ButtonDefaults.buttonColors(containerColor = accent),
    ) {
        Icon(Icons.Rounded.PlayArrow, null, tint = Color(0xFF0A0A0F), modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(
            text  = "INICIAR TREINO",
            style = FVTypography.labelLarge.copy(fontWeight = FontWeight.Black),
            color = Color(0xFF0A0A0F),
        )
    }
}
