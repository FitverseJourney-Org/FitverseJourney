package com.example.presentation.ui.historic

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expect.NumberFormatter
import com.example.expect.TimerManager
import com.example.presentation.theme.FVExtension
import com.example.presentation.ui.historic.viewmodel.HistoricEvent
import com.example.presentation.ui.historic.viewmodel.HistoricIntent
import com.example.presentation.ui.historic.viewmodel.HistoricViewModel
import com.example.presentation.widgets.FitverseTopAppBar

// ─── Data ────────────────────────────────────────────────────────────────────

data class ExerciseSet(
    val setNum: Int,
    val reps:   Int,
    val weight: Float,
    val isPR:   Boolean = false
)

data class WorkoutExercise(
    val name:   String,
    val muscle: String,
    val sets:   List<ExerciseSet>
)

data class WorkoutHistory(
    val id:             String,
    val title:          String,
    val dateDisplay:    String,
    val timestamp:      Long,
    val duration:       String,
    val totalVolume:    String,
    val muscleGroups:   List<String>,
    val exerciseCount:  Int                   = 0,
    val xpEarned:       Int                   = 0,
    val hasPR:          Boolean               = false,
    val intensityLevel: Int                   = 3,
    val exercises:      List<WorkoutExercise> = emptyList(),
    val notes:          String                = ""
)

enum class HistoricPeriod(val label: String, val days: Int) {
    WEEK("7D", 7),
    MONTH("30D", 30),
    THREE_MONTHS("3M", 90),
    ALL("TUDO", Int.MAX_VALUE)
}

// ─── Mock Data ────────────────────────────────────────────────────────────────

private val PUSH_EX = listOf(
    WorkoutExercise("Supino Reto", "Peito", listOf(
        ExerciseSet(1, 10, 80f), ExerciseSet(2, 8, 85f), ExerciseSet(3, 6, 90f, true), ExerciseSet(4, 8, 85f)
    )),
    WorkoutExercise("Supino Inclinado", "Peito", listOf(
        ExerciseSet(1, 10, 70f), ExerciseSet(2, 8, 75f), ExerciseSet(3, 8, 75f)
    )),
    WorkoutExercise("Crucifixo", "Peito", listOf(
        ExerciseSet(1, 12, 20f), ExerciseSet(2, 10, 22.5f), ExerciseSet(3, 10, 22.5f)
    )),
    WorkoutExercise("Tríceps Corda", "Tríceps", listOf(
        ExerciseSet(1, 15, 25f), ExerciseSet(2, 12, 27.5f), ExerciseSet(3, 10, 30f, true)
    )),
    WorkoutExercise("Tríceps Testa", "Tríceps", listOf(
        ExerciseSet(1, 12, 30f), ExerciseSet(2, 10, 32.5f), ExerciseSet(3, 8, 35f)
    )),
    WorkoutExercise("Paralelas", "Tríceps", listOf(
        ExerciseSet(1, 12, 0f), ExerciseSet(2, 10, 0f), ExerciseSet(3, 8, 0f)
    )),
)

private val LEG_EX = listOf(
    WorkoutExercise("Agachamento", "Pernas", listOf(
        ExerciseSet(1, 12, 80f), ExerciseSet(2, 10, 100f), ExerciseSet(3, 8, 110f, true), ExerciseSet(4, 8, 110f)
    )),
    WorkoutExercise("Leg Press", "Pernas", listOf(
        ExerciseSet(1, 15, 180f), ExerciseSet(2, 12, 200f), ExerciseSet(3, 10, 220f), ExerciseSet(4, 10, 220f)
    )),
    WorkoutExercise("Extensora", "Pernas", listOf(
        ExerciseSet(1, 15, 50f), ExerciseSet(2, 12, 55f), ExerciseSet(3, 12, 55f)
    )),
    WorkoutExercise("Femoral Deitado", "Glúteos", listOf(
        ExerciseSet(1, 12, 40f), ExerciseSet(2, 10, 45f), ExerciseSet(3, 10, 47.5f)
    )),
    WorkoutExercise("Panturrilha em Pé", "Pernas", listOf(
        ExerciseSet(1, 20, 60f), ExerciseSet(2, 18, 65f), ExerciseSet(3, 15, 70f), ExerciseSet(4, 15, 70f)
    )),
    WorkoutExercise("Cadeira Adutora", "Glúteos", listOf(
        ExerciseSet(1, 15, 50f), ExerciseSet(2, 12, 55f), ExerciseSet(3, 12, 55f)
    )),
    WorkoutExercise("Abdução de Quadril", "Glúteos", listOf(
        ExerciseSet(1, 15, 45f), ExerciseSet(2, 12, 50f), ExerciseSet(3, 12, 50f)
    )),
)

private val PULL_EX = listOf(
    WorkoutExercise("Puxada Frontal", "Costas", listOf(
        ExerciseSet(1, 12, 60f), ExerciseSet(2, 10, 70f), ExerciseSet(3, 8, 75f), ExerciseSet(4, 8, 75f)
    )),
    WorkoutExercise("Remada Curvada", "Costas", listOf(
        ExerciseSet(1, 10, 80f), ExerciseSet(2, 8, 90f), ExerciseSet(3, 8, 90f, true), ExerciseSet(4, 8, 90f)
    )),
    WorkoutExercise("Remada Unilateral", "Costas", listOf(
        ExerciseSet(1, 12, 32.5f), ExerciseSet(2, 10, 37.5f), ExerciseSet(3, 10, 37.5f)
    )),
    WorkoutExercise("Rosca Direta", "Bíceps", listOf(
        ExerciseSet(1, 12, 30f), ExerciseSet(2, 10, 35f), ExerciseSet(3, 8, 40f)
    )),
    WorkoutExercise("Rosca Martelo", "Bíceps", listOf(
        ExerciseSet(1, 12, 22.5f), ExerciseSet(2, 10, 25f), ExerciseSet(3, 10, 25f)
    )),
    WorkoutExercise("Face Pull", "Costas", listOf(
        ExerciseSet(1, 15, 20f), ExerciseSet(2, 15, 22.5f), ExerciseSet(3, 12, 22.5f)
    )),
)

val currentMoment = TimerManager.nowMillis()
val dayMillis     = 86_400_000L

val mockHistory = listOf(
    WorkoutHistory("1",  "Push Day",     "Hoje",        currentMoment,                  "1h 15m", "5.200 kg",  listOf("Peito", "Tríceps"),   6,  320, true,  4, PUSH_EX),
    WorkoutHistory("2",  "Leg Day",      "Ontem",       currentMoment - dayMillis,      "1h 30m", "8.100 kg",  listOf("Pernas", "Glúteos"),  7,  450, false, 5, LEG_EX),
    WorkoutHistory("3",  "Pull Day",     "há 2 dias",   currentMoment - dayMillis * 2,  "1h 10m", "6.300 kg",  listOf("Costas", "Bíceps"),   6,  280, false, 3, PULL_EX),
    WorkoutHistory("4",  "Full Body",    "há 4 dias",   currentMoment - dayMillis * 4,  "2h 00m", "9.500 kg",  listOf("Full Body"),           10, 600, true,  5),
    WorkoutHistory("5",  "Cardio Core",  "há 5 dias",   currentMoment - dayMillis * 5,  "45m",    "1.200 kg",  listOf("Core", "Cardio"),     4,  180, false, 2),
    WorkoutHistory("6",  "Push Day",     "há 7 dias",   currentMoment - dayMillis * 7,  "1h 20m", "5.800 kg",  listOf("Peito", "Ombros"),    7,  340, false, 4, PUSH_EX),
    WorkoutHistory("7",  "Leg Day",      "há 9 dias",   currentMoment - dayMillis * 9,  "1h 35m", "8.400 kg",  listOf("Pernas"),             8,  480, false, 5, LEG_EX),
    WorkoutHistory("8",  "Pull Day",     "há 10 dias",  currentMoment - dayMillis * 10, "1h 05m", "6.100 kg",  listOf("Costas", "Bíceps"),   6,  260, false, 3, PULL_EX),
    WorkoutHistory("9",  "Shoulder Day", "há 12 dias",  currentMoment - dayMillis * 12, "55m",    "3.400 kg",  listOf("Ombros", "Tríceps"),  5,  210, false, 3),
    WorkoutHistory("10", "Full Body",    "há 15 dias",  currentMoment - dayMillis * 15, "2h 10m", "10.200 kg", listOf("Full Body"),           12, 720, true,  5),
)

// ─── Root — wires ViewModel ──────────────────────────────────────────────────

@Composable
fun HistoricRoot(
    viewModel: HistoricViewModel,
    onBack:    () -> Unit,
    modifier:  Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                HistoricEvent.NavigateBack -> onBack()
            }
        }
    }

    HistoricScreen(
        modifier   = modifier,
        onBack     = { viewModel.onIntent(HistoricIntent.NavigateBack) },
        allHistory = uiState.history,
        isPremium  = uiState.isPremium,
    )
}

// ─── Main Screen ─────────────────────────────────────────────────────────────

@Composable
fun HistoricScreen(
    modifier:   Modifier,
    onBack:     () -> Unit,
    allHistory: List<WorkoutHistory> = mockHistory,
    isPremium:  Boolean = false
) {
    var activePeriod    by remember { mutableStateOf(HistoricPeriod.MONTH) }
    var selectedWorkout by remember { mutableStateOf<WorkoutHistory?>(null) }

    AnimatedContent(
        targetState  = selectedWorkout,
        transitionSpec = {
            if (targetState != null) {
                (slideInHorizontally { it } + fadeIn()) togetherWith
                (slideOutHorizontally { -it / 3 } + fadeOut())
            } else {
                (slideInHorizontally { -it / 3 } + fadeIn()) togetherWith
                (slideOutHorizontally { it } + fadeOut())
            }
        },
        label = "historicNav"
    ) { selected ->
        if (selected != null) {
            WorkoutDetailScreen(
                workout  = selected,
                modifier = modifier,
                onBack   = { selectedWorkout = null }
            )
        } else {
            HistoricListContent(
                modifier       = modifier,
                onBack         = onBack,
                allHistory     = allHistory,
                isPremium      = isPremium,
                activePeriod   = activePeriod,
                onPeriodChange = { activePeriod = it },
                onWorkoutClick = { selectedWorkout = it }
            )
        }
    }
}

// ─── List Screen ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoricListContent(
    modifier:       Modifier,
    onBack:         () -> Unit,
    allHistory:     List<WorkoutHistory>,
    isPremium:      Boolean,
    activePeriod:   HistoricPeriod,
    onPeriodChange: (HistoricPeriod) -> Unit,
    onWorkoutClick: (WorkoutHistory) -> Unit
) {
    val filteredData = remember(activePeriod, allHistory) {
        val cutoff = if (activePeriod == HistoricPeriod.ALL) 0L
                     else currentMoment - activePeriod.days.toLong() * dayMillis
        val list = allHistory
            .filter { it.timestamp >= cutoff }
            .sortedByDescending { it.timestamp }
        val totalVolumeRaw  = list.sumOf { parseVolume(it.totalVolume) }
        val totalMinutesRaw = list.sumOf { parseDurationToMinutes(it.duration) }
        object {
            val list          = list
            val totalWorkouts = list.size
            val totalMinutes  = totalMinutesRaw
            val volumeDisplay = formatVolume(totalVolumeRaw)
            val streak        = calculateStreak(allHistory)
            val grouped       = groupBySection(list)
        }
    }

    Scaffold(
        modifier            = modifier,
        containerColor      = FVExtension.bg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FitverseTopAppBar(
                title  = "HISTÓRICO",
                subtitle    = {
                    Text(
                        text = "Seu progresso ao longo do tempo",
                        fontSize = 12.sp,
                        color = FVExtension.textMuted
                    )
                },
                onBack = onBack
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier       = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item { ActivityStrip(allHistory = allHistory) }

            item {
                StatsGrid(
                    workouts = filteredData.totalWorkouts,
                    minutes  = filteredData.totalMinutes,
                    volume   = filteredData.volumeDisplay,
                    streak   = filteredData.streak,
                    modifier = Modifier.padding(horizontal = FVExtension.margin, vertical = 16.dp)
                )
            }

            item {
                PeriodFilterRow(
                    active    = activePeriod,
                    isPremium = isPremium,
                    onSelect  = onPeriodChange,
                    modifier  = Modifier.padding(horizontal = FVExtension.margin)
                )
            }

            item { Spacer(Modifier.height(20.dp)) }

            if (filteredData.list.isEmpty()) {
                item { HistoricEmptyState(modifier = Modifier.padding(top = 48.dp)) }
            } else {
                filteredData.grouped.forEach { (section, sectionItems) ->
                    item(key = "hdr_$section") {
                        SectionLabel(
                            label    = section,
                            count    = sectionItems.size,
                            modifier = Modifier.padding(horizontal = FVExtension.margin, vertical = 8.dp)
                        )
                    }
                    items(sectionItems, key = { it.id }) { workout ->
                        WorkoutListCard(
                            workout  = workout,
                            onClick  = { onWorkoutClick(workout) },
                            modifier = Modifier
                                .padding(horizontal = FVExtension.margin)
                                .padding(bottom = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

// ─── Detail Screen ───────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkoutDetailScreen(
    workout:  WorkoutHistory,
    modifier: Modifier,
    onBack:   () -> Unit
) {
    val intensityColor = intensityColor(workout.intensityLevel)
    val intensityLabel = intensityLabel(workout.intensityLevel)
    val groupedEx      = workout.exercises.groupBy { it.muscle }

    Scaffold(
        modifier            = modifier,
        containerColor      = FVExtension.bg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FitverseTopAppBar(
                title  = workout.title,
                subtitle    = {
                    Text(
                        text = workout.dateDisplay,
                        fontSize = 12.sp,
                        color = FVExtension.text
                    )
                },
                onBack = onBack
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier       = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(bottom = 40.dp, top = 16.dp)
        ) {
            // ── Summary stats ─────────────────────────────────────────
            item {
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = FVExtension.margin, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailStat("VOLUME",    workout.totalVolume, FVExtension.primary,   Modifier.weight(1f))
                    DetailStat("EXERCÍCIOS","${workout.exerciseCount}",         FVExtension.secondary, Modifier.weight(1f))
                    DetailStat("XP",        "+${workout.xpEarned}",             FVExtension.xp,        Modifier.weight(1f))
                    DetailStat("INTENSIDADE", intensityLabel,                   intensityColor, Modifier.weight(1f))
                }
            }

            // ── Muscle groups ─────────────────────────────────────────
            item {
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = FVExtension.margin),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    workout.muscleGroups.forEach { muscle ->
                        val c = muscleGroupColor(muscle)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(FVExtension.radiusPill))
                                .background(c.copy(0.12f))
                                .border(1.dp, c.copy(0.3f), RoundedCornerShape(FVExtension.radiusPill))
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(muscle.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = c, letterSpacing = 0.5.sp)
                        }
                    }
                }
            }

            // ── Intensity bar ─────────────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = FVExtension.margin, vertical = 14.dp)) {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("INTENSIDADE DO TREINO", fontSize = 9.sp, color = FVExtension.textMuted, letterSpacing = 0.8.sp)
                        Text(intensityLabel, fontSize = 9.sp, color = intensityColor, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress   = workout.intensityLevel / 5f,
                        modifier   = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(FVExtension.radiusPill)),
                        color      = intensityColor,
                        trackColor = FVExtension.surface2
                    )
                }
            }

            // ── PR banner ─────────────────────────────────────────────
            if (workout.hasPR) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = FVExtension.margin)
                            .padding(bottom = 14.dp)
                            .clip(RoundedCornerShape(FVExtension.radius))
                            .background(FVExtension.xp.copy(0.08f))
                            .border(1.dp, FVExtension.xp.copy(0.25f), RoundedCornerShape(FVExtension.radius))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("🏆", fontSize = 20.sp)
                        Column {
                            Text(
                                "Recorde Pessoal",
                                fontSize   = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color      = FVExtension.xp
                            )
                            Text(
                                "Novo PR alcançado neste treino!",
                                fontSize = 11.sp,
                                color    = FVExtension.textMuted
                            )
                        }
                    }
                }
            }

            // ── Exercises grouped by muscle ───────────────────────────
            if (groupedEx.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = FVExtension.margin, vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Detalhes dos exercícios\nnão disponíveis",
                            fontSize  = 12.sp,
                            color     = FVExtension.textMuted,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            } else {
                groupedEx.forEach { (muscle, exercises) ->
                    item(key = "muscle_$muscle") {
                        SectionLabel(
                            label    = muscle,
                            count    = exercises.size,
                            modifier = Modifier.padding(horizontal = FVExtension.margin, vertical = 8.dp)
                        )
                    }
                    items(exercises, key = { "${muscle}_${it.name}" }) { exercise ->
                        ExerciseCard(
                            exercise = exercise,
                            modifier = Modifier
                                .padding(horizontal = FVExtension.margin)
                                .padding(bottom = 10.dp)
                        )
                    }
                }
            }

            // ── Notes ─────────────────────────────────────────────────
            if (workout.notes.isNotBlank()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = FVExtension.margin, vertical = 8.dp)
                            .clip(RoundedCornerShape(FVExtension.radius))
                            .background(FVExtension.surface)
                            .border(1.dp, FVExtension.outline, RoundedCornerShape(FVExtension.radius))
                            .padding(14.dp)
                    ) {
                        Text("ANOTAÇÕES", fontSize = 9.sp, color = FVExtension.textMuted, letterSpacing = 1.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text(workout.notes, fontSize = 13.sp, color = FVExtension.text, lineHeight = 20.sp)
                    }
                }
            }
        }
    }
}

// ─── Detail Stat Box ─────────────────────────────────────────────────────────

@Composable
private fun DetailStat(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier            = modifier
            .clip(RoundedCornerShape(FVExtension.radius))
            .background(FVExtension.surface)
            .border(1.dp, FVExtension.outline, RoundedCornerShape(FVExtension.radius))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            value,
            fontSize   = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            color      = color,
            textAlign  = TextAlign.Center,
            maxLines   = 1
        )
        Spacer(Modifier.height(3.dp))
        Text(
            label,
            fontSize      = 7.sp,
            color         = FVExtension.textMuted,
            letterSpacing = 0.5.sp,
            textAlign     = TextAlign.Center
        )
    }
}

// ─── Exercise Card ────────────────────────────────────────────────────────────

@Composable
private fun ExerciseCard(exercise: WorkoutExercise, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FVExtension.radius))
            .background(FVExtension.surface)
            .border(1.dp, FVExtension.outline, RoundedCornerShape(FVExtension.radius))
            .padding(14.dp)
    ) {
        Text(
            exercise.name,
            fontSize   = 13.sp,
            fontWeight = FontWeight.Bold,
            color      = FVExtension.text
        )
        Spacer(Modifier.height(10.dp))

        // ── Table header ─────────────────────────────────────────────
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("SÉRIE", fontSize = 8.sp, color = FVExtension.textMuted, letterSpacing = 0.5.sp, modifier = Modifier.width(52.dp))
            Text("CARGA",  fontSize = 8.sp, color = FVExtension.textMuted, letterSpacing = 0.5.sp, modifier = Modifier.width(72.dp))
            Text("REPS",   fontSize = 8.sp, color = FVExtension.textMuted, letterSpacing = 0.5.sp, modifier = Modifier.weight(1f))
        }

        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).height(0.5.dp).background(FVExtension.outline))

        // ── Set rows ─────────────────────────────────────────────────
        exercise.sets.forEachIndexed { index, set ->
            SetRow(set = set)
            if (index < exercise.sets.lastIndex) {
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun SetRow(set: ExerciseSet) {
    Row(
        modifier          = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "${set.setNum}",
            fontSize   = 12.sp,
            color      = if (set.isPR) FVExtension.xp else FVExtension.textMuted,
            fontWeight = FontWeight.Bold,
            modifier   = Modifier.width(52.dp),
            fontFamily = FontFamily.Monospace
        )
        Text(
            if (set.weight == 0f) "Peso Corp." else "${set.weight.let { if (it == it.toLong().toFloat()) it.toLong().toString() else it.toString() }} kg",
            fontSize   = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color      = if (set.isPR) FVExtension.xp else FVExtension.text,
            modifier   = Modifier.width(72.dp)
        )
        Text(
            "× ${set.reps} reps",
            fontSize = 12.sp,
            color    = if (set.isPR) FVExtension.xp else FVExtension.textMuted,
            modifier = Modifier.weight(1f)
        )
        if (set.isPR) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(FVExtension.xp.copy(0.12f))
                    .border(0.5.dp, FVExtension.xp.copy(0.3f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Text("PR", fontSize = 8.sp, color = FVExtension.xp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ─── Activity Strip ───────────────────────────────────────────────────────────

@Composable
private fun ActivityStrip(allHistory: List<WorkoutHistory>) {
    val workoutDays = remember(allHistory) { allHistory.map { it.timestamp / dayMillis }.toSet() }
    val today       = currentMoment / dayMillis
    val recentCount = workoutDays.count { it >= today - 13 }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = FVExtension.margin, vertical = 16.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text("ÚLTIMOS 14 DIAS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = FVExtension.textMuted, letterSpacing = 1.sp)
            Text(
                "$recentCount ${if (recentCount == 1) "treino" else "treinos"}",
                fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = FVExtension.primary
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            (13 downTo 0).forEach { daysAgo ->
                val dayIndex   = today - daysAgo
                val hasWorkout = dayIndex in workoutDays
                val isToday    = daysAgo == 0
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                hasWorkout && isToday -> FVExtension.primary
                                hasWorkout            -> FVExtension.primary.copy(0.55f)
                                else                  -> FVExtension.surface2
                            }
                        )
                        .then(if (isToday) Modifier.border(1.5.dp, FVExtension.primary, CircleShape) else Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    if (hasWorkout) Text("✓", fontSize = 8.sp, color = FVExtension.bg, fontWeight = FontWeight.Black)
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("14 dias atrás", fontSize = 8.sp, color = FVExtension.textMuted)
            Text("hoje",          fontSize = 8.sp, color = FVExtension.primary, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ─── Stats Grid ───────────────────────────────────────────────────────────────

@Composable
private fun StatsGrid(workouts: Int, minutes: Int, volume: String, streak: Int, modifier: Modifier = Modifier) {
    val hours       = minutes / 60
    val mins        = minutes % 60
    val timeDisplay = when {
        hours > 0 && mins > 0 -> "${hours}h ${mins}m"
        hours > 0             -> "${hours}h"
        else                  -> "${mins}m"
    }
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        StatBox("TREINOS", workouts.toString(), "🏋️", FVExtension.primary,   Modifier.weight(1f))
        StatBox("TEMPO",   timeDisplay,          "⏱️", FVExtension.secondary, Modifier.weight(1f))
        StatBox("VOLUME",  volume,               "📊", FVExtension.xp,        Modifier.weight(1f))
        StatBox("STREAK",  "${streak}d",         "🔥", FVExtension.danger,    Modifier.weight(1f))
    }
}

@Composable
private fun StatBox(label: String, value: String, icon: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier            = modifier
            .clip(RoundedCornerShape(FVExtension.radius))
            .background(FVExtension.surface)
            .border(1.dp, FVExtension.outline, RoundedCornerShape(FVExtension.radius))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, fontSize = 16.sp)
        Spacer(Modifier.height(6.dp))
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = color, textAlign = TextAlign.Center, maxLines = 1)
        Spacer(Modifier.height(2.dp))
        Text(label, fontSize = 8.sp, color = FVExtension.textMuted, letterSpacing = 0.5.sp, textAlign = TextAlign.Center)
    }
}

// ─── Period Filter ────────────────────────────────────────────────────────────

@Composable
private fun PeriodFilterRow(active: HistoricPeriod, isPremium: Boolean, onSelect: (HistoricPeriod) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        HistoricPeriod.values().forEach { period ->
            val isLocked   = !isPremium && period.days > 30
            val isSelected = period == active
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(FVExtension.radiusPill))
                    .background(if (isSelected) FVExtension.primary else FVExtension.surface2)
                    .border(1.dp, if (isSelected) Color.Transparent else FVExtension.outline, RoundedCornerShape(FVExtension.radiusPill))
                    .clickable(enabled = !isLocked) { onSelect(period) }
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (isLocked) Text("🔒", fontSize = 9.sp)
                    Text(
                        period.label,
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = when {
                            isSelected -> FVExtension.bg
                            isLocked   -> FVExtension.textMuted.copy(alpha = 0.4f)
                            else       -> FVExtension.textMuted
                        }
                    )
                }
            }
        }
    }
}

// ─── Section Label ────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(label: String, count: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = FVExtension.textMuted, letterSpacing = 1.sp)
        Box(modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(FVExtension.surface2).padding(horizontal = 6.dp, vertical = 2.dp)) {
            Text("$count", fontSize = 9.sp, color = FVExtension.textMuted, fontWeight = FontWeight.Bold)
        }
        Box(modifier = Modifier.weight(1f).height(0.5.dp).background(FVExtension.outline))
    }
}

// ─── Workout List Card (clean, minimal) ───────────────────────────────────────

@Composable
private fun WorkoutListCard(workout: WorkoutHistory, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val dotColor = muscleGroupColor(workout.muscleGroups.firstOrNull() ?: "")

    Row(
        modifier          = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FVExtension.radius))
            .background(FVExtension.surface)
            .border(1.dp, if (workout.hasPR) FVExtension.xp.copy(0.2f) else FVExtension.outline, RoundedCornerShape(FVExtension.radius))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left accent dot
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(dotColor))
        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Row 1: Title + Duration
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(workout.title, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = FVExtension.text)
                Text(workout.duration, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FVExtension.primary)
            }

            Spacer(Modifier.height(3.dp))

            // Row 2: Date + Volume
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(workout.dateDisplay, fontSize = 11.sp, color = FVExtension.textMuted)
                Text(workout.totalVolume, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FVExtension.text)
            }

            Spacer(Modifier.height(8.dp))

            // Row 3: Muscle chips + PR badge
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                workout.muscleGroups.take(3).forEach { muscle ->
                    val c = muscleGroupColor(muscle)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(FVExtension.radiusPill))
                            .background(c.copy(0.1f))
                            .border(0.5.dp, c.copy(0.25f), RoundedCornerShape(FVExtension.radiusPill))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(muscle.uppercase(), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = c, letterSpacing = 0.3.sp)
                    }
                }
                if (workout.hasPR) {
                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(FVExtension.radiusPill))
                            .background(FVExtension.xp.copy(0.1f))
                            .border(0.5.dp, FVExtension.xp.copy(0.3f), RoundedCornerShape(FVExtension.radiusPill))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("🏆 PR", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = FVExtension.xp)
                    }
                }
            }
        }

        Spacer(Modifier.width(10.dp))
        Text("›", fontSize = 20.sp, color = FVExtension.textMuted.copy(alpha = 0.4f))
    }
}

// ─── Empty State ──────────────────────────────────────────────────────────────

@Composable
private fun HistoricEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier            = modifier.fillMaxWidth().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📭", fontSize = 48.sp)
        Spacer(Modifier.height(16.dp))
        Text("NENHUM TREINO ENCONTRADO", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = FVExtension.text, letterSpacing = 1.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text("Tente um período diferente\nou comece a treinar!", fontSize = 12.sp, color = FVExtension.textMuted, textAlign = TextAlign.Center, lineHeight = 18.sp)
    }
}

// ─── Helpers ─────────────────────────────────────────────────────────────────

private fun muscleGroupColor(group: String): Color = when (group.lowercase().trim()) {
    "peito"                      -> Color(0xFFFF4D1C)
    "costas"                     -> Color(0xFF7C6FFF)
    "pernas", "glúteos"          -> Color(0xFF00C97A)
    "bíceps", "tríceps"          -> Color(0xFFF5C518)
    "ombros"                     -> Color(0xFFFF6B9D)
    "core", "abdômen", "cardio"  -> Color(0xFF00D4FF)
    else                         -> FVExtension.primary
}

private fun intensityColor(level: Int): Color = when (level) {
    1, 2 -> FVExtension.secondary
    3    -> FVExtension.xp
    else -> FVExtension.danger
}

private fun intensityLabel(level: Int): String = when (level) {
    1    -> "Leve"
    2    -> "Moderada"
    3    -> "Média"
    4    -> "Alta"
    else -> "Máxima"
}

private fun calculateStreak(history: List<WorkoutHistory>): Int {
    if (history.isEmpty()) return 0
    val days     = history.map { it.timestamp / dayMillis }.toSet()
    val today    = currentMoment / dayMillis
    var streak   = 0
    var expected = today
    while (expected in days) { streak++; expected-- }
    return streak
}

private fun groupBySection(list: List<WorkoutHistory>): List<Pair<String, List<WorkoutHistory>>> {
    val today = currentMoment / dayMillis
    val order = listOf("Hoje", "Ontem", "Esta Semana", "Semana Passada", "Este Mês", "Últimos 3 Meses", "Mais Antigo")
    return list
        .groupBy { w ->
            val diff = today - (w.timestamp / dayMillis)
            when {
                diff == 0L  -> "Hoje"
                diff == 1L  -> "Ontem"
                diff <= 7L  -> "Esta Semana"
                diff <= 14L -> "Semana Passada"
                diff <= 30L -> "Este Mês"
                diff <= 90L -> "Últimos 3 Meses"
                else        -> "Mais Antigo"
            }
        }
        .entries
        .sortedBy { order.indexOf(it.key) }
        .map { it.key to it.value }
}

private fun formatVolume(raw: Double): String =
    if (raw >= 1000) "${NumberFormatter.formatOneDecimal(raw / 1000)}t" else "${raw.toInt()}kg"

private fun parseVolume(volumeStr: String): Double =
    volumeStr.replace(" kg", "").replace(".", "").toDoubleOrNull() ?: 0.0

private fun parseDurationToMinutes(durationStr: String): Int {
    val hours   = durationStr.substringBefore("h", "0").trim().toIntOrNull() ?: 0
    val minutes = durationStr.substringAfter("h", "0").substringBefore("m", "0").trim().toIntOrNull() ?: 0
    return (hours * 60) + minutes
}
