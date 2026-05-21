package org.fitverse.presentation.ui.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.expect.DateFormatter
import org.fitverse.presentation.expect.TimerManager
import org.fitverse.presentation.expect.format
import org.fitverse.presentation.expect.formatWorkoutTime
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.bg_girl_pose
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource

// ============================================================================
// DOMAIN MODELS
// ============================================================================

enum class ExerciseType { REPS, TIME, DISTANCE }

data class Exercise(
    val id: Int,
    val title: String,
    val description: String,
    val targetMuscleGroup: String,
    val durationSeconds: Int = 30,
    val reps: Int = 10,
    val sets: Int = 3,
    val restAfterSeconds: Int = 60,
    val mediaUrl: DrawableResource,
    val videoUrl: String? = null,
    val xp: Int = 10,
    val type: ExerciseType = ExerciseType.REPS,
    val difficulty: String = "Intermediate"
)

data class SetRecord(
    val setNumber: Int,
    val weight: String = "",
    val reps: String = "",
    val durationSeconds: String = "",
    val restSeconds: String = "60",
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)

data class PastWorkoutLog(
    val id: String,
    val date: String,
    val timestamp: Long = TimerManager.nowMillis(),
    val exerciseId: Int,
    val exerciseName: String = "",
    val sets: List<SetRecord>,
    val totalVolume: Float = 0f,
    val personalRecord: Boolean = false
)

data class WorkoutPlan(
    val id: Int,
    val title: String,
    val description: String = "",
    val exercises: List<Exercise>,
    val estimatedDuration: Int = 0,
    val difficulty: String = "Intermediate",
    val category: String = "Strength"
)

// ============================================================================
// DATA SOURCE
// ============================================================================

object WorkoutDataSource {

    fun getMockWorkoutPlan(): WorkoutPlan {
        return WorkoutPlan(
            id = 1,
            title = "Upper Body Strength",
            description = "Complete upper body workout focusing on chest, back, and arms",
            estimatedDuration = 45,
            difficulty = "Intermediate",
            category = "Strength",
            exercises = listOf(
                Exercise(
                    id = 1,
                    title = "Bench Press",
                    description = "Deite no banco plano com as costas apoiadas. Segure a barra na largura dos ombros, desça até o peito controladamente e empurre de volta até a extensão completa dos braços.",
                    targetMuscleGroup = "Peito, Tríceps, Ombros",
                    reps = 10,
                    sets = 4,
                    restAfterSeconds = 90,
                    mediaUrl = Res.drawable.bg_girl_pose,
                    xp = 15,
                    type = ExerciseType.REPS,
                    difficulty = "Intermediate"
                ),
                Exercise(
                    id = 2,
                    title = "Pull-ups",
                    description = "Segure a barra com as mãos mais largas que os ombros, palmas voltadas para frente. Puxe o corpo para cima até o queixo ultrapassar a barra, depois desça de forma controlada.",
                    targetMuscleGroup = "Costas, Bíceps, Core",
                    reps = 8,
                    sets = 4,
                    restAfterSeconds = 90,
                    mediaUrl = Res.drawable.bg_girl_pose,
                    xp = 20,
                    type = ExerciseType.REPS,
                    difficulty = "Advanced"
                ),
                Exercise(
                    id = 3,
                    title = "Prancha",
                    description = "Apoie os antebraços e as pontas dos pés no chão, mantendo o corpo em linha reta da cabeça aos calcanhares. Contraia o abdômen e os glúteos durante todo o exercício.",
                    targetMuscleGroup = "Core, Ombros, Lombar",
                    durationSeconds = 60,
                    sets = 3,
                    restAfterSeconds = 60,
                    mediaUrl = Res.drawable.bg_girl_pose,
                    xp = 10,
                    type = ExerciseType.TIME,
                    difficulty = "Beginner"
                ),
                Exercise(
                    id = 4,
                    title = "Remada com Haltere",
                    description = "Apoie um joelho e a mão do mesmo lado no banco. Com o haltere na mão oposta, puxe em direção ao quadril mantendo o cotovelo próximo ao corpo, depois desça controladamente.",
                    targetMuscleGroup = "Costas, Bíceps",
                    reps = 12,
                    sets = 3,
                    restAfterSeconds = 60,
                    mediaUrl = Res.drawable.bg_girl_pose,
                    xp = 12,
                    type = ExerciseType.REPS,
                    difficulty = "Intermediate"
                )
            )
        )
    }

    fun getMockHistoryForExercise(exerciseId: Int, exerciseName: String): List<PastWorkoutLog> {
        return listOf(
            PastWorkoutLog(
                id = "1",
                date = DateFormatter.getFormattedDate(daysOffset = -1),
                exerciseId = exerciseId,
                exerciseName = exerciseName,
                sets = listOf(
                    SetRecord(1, "70", "12", "", "90", true),
                    SetRecord(2, "70", "10", "", "90", true),
                    SetRecord(3, "75", "8", "", "90", true),
                    SetRecord(4, "75", "8", "", "120", true)
                ),
                totalVolume = 3220f,
                personalRecord = true
            ),
            PastWorkoutLog(
                id = "2",
                date = DateFormatter.getFormattedDate(daysOffset = -2),
                exerciseId = exerciseId,
                exerciseName = exerciseName,
                sets = listOf(
                    SetRecord(1, "65", "12", "", "90", true),
                    SetRecord(2, "65", "10", "", "90", true),
                    SetRecord(3, "70", "8", "", "90", true),
                    SetRecord(4, "70", "6", "", "90", true)
                ),
                totalVolume = 2860f
            )
        )
    }
}

// ============================================================================
// MAIN SCREEN
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSessionScreen(
    modifier: Modifier = Modifier,
    workout: WorkoutPlan = WorkoutDataSource.getMockWorkoutPlan(),
    onFinish: (WorkoutCompletionResult) -> Unit = {},
    onExit: () -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme

    var currentIndex by remember { mutableIntStateOf(0) }
    var globalTimerSeconds by remember { mutableIntStateOf(0) }
    var showExitDialog by remember { mutableStateOf(false) }
    var showInfoSheet by remember { mutableStateOf(false) }

    val currentExercise = workout.exercises[currentIndex]
    val isLastExercise = currentIndex == workout.exercises.lastIndex

    val allExerciseSets = remember {
        mutableStateMapOf<Int, List<SetRecord>>().apply {
            workout.exercises.forEach { this[it.id] = emptyList() }
        }
    }
    val currentSets = allExerciseSets[currentExercise.id] ?: emptyList()

    // Input state — resets with sensible defaults when changing exercise
    var inputWeight by remember(currentIndex) {
        val last = (allExerciseSets[workout.exercises[currentIndex].id] ?: emptyList()).lastOrNull()
        mutableFloatStateOf(last?.weight?.toFloatOrNull() ?: 20f)
    }
    var inputReps by remember(currentIndex) {
        val ex = workout.exercises[currentIndex]
        val last = (allExerciseSets[ex.id] ?: emptyList()).lastOrNull()
        mutableIntStateOf(
            when (ex.type) {
                ExerciseType.TIME -> last?.durationSeconds?.toIntOrNull() ?: ex.durationSeconds
                else -> last?.reps?.toIntOrNull() ?: ex.reps
            }
        )
    }

    LaunchedEffect(Unit) {
        while (true) { delay(1000L); globalTimerSeconds++ }
    }

    if (showExitDialog) {
        ExitConfirmDialog(
            onConfirm = { showExitDialog = false; onExit() },
            onDismiss = { showExitDialog = false }
        )
    }

    if (showInfoSheet) {
        ExerciseInfoSheet(
            exercise = currentExercise,
            onDismiss = { showInfoSheet = false }
        )
    }

    Scaffold(
        containerColor = cs.background,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0, 0, 0, 0),
                navigationIcon = {
                    if (currentIndex == 0) {
                        IconButton(onClick = { showExitDialog = true }) {
                            Icon(Icons.Rounded.Close, "Sair do treino")
                        }
                    } else {
                        IconButton(onClick = { currentIndex-- }) {
                            Icon(Icons.Rounded.ArrowBack, "Exercício anterior")
                        }
                    }
                },
                title = {
                    Column {
                        Text(
                            text = currentExercise.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${currentIndex + 1} de ${workout.exercises.size}",
                            style = MaterialTheme.typography.labelSmall,
                            color = cs.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showInfoSheet = true }) {
                        Icon(Icons.Rounded.Info, "Informações do exercício")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            WorkoutProgressBar(
                currentIndex = currentIndex,
                total = workout.exercises.size,
                isLastExercise = isLastExercise,
                onNext = { currentIndex++ },
                onFinish = {
                    val xp = workout.exercises.sumOf { ex ->
                        if ((allExerciseSets[ex.id] ?: emptyList()).isNotEmpty()) ex.xp else 0
                    }
                    onFinish(
                        WorkoutAnalyzer.analyzeWorkout(
                            workoutPlan = workout,
                            allExerciseSets = allExerciseSets.toMap(),
                            durationSeconds = globalTimerSeconds,
                            totalXp = xp,
                            exerciseHistory = emptyMap()
                        )
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ── Sets list ──────────────────────────────────────────────────
            if (currentSets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Rounded.FitnessCenter,
                            contentDescription = null,
                            modifier = Modifier.size(44.dp),
                            tint = cs.onSurfaceVariant.copy(alpha = 0.35f)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            "Nenhuma série ainda",
                            style = MaterialTheme.typography.bodyMedium,
                            color = cs.onSurfaceVariant.copy(alpha = 0.55f)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Adicione sua primeira série abaixo",
                            style = MaterialTheme.typography.bodySmall,
                            color = cs.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    item {
                        Text(
                            text = "Séries",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = cs.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    itemsIndexed(currentSets) { index, set ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically()
                        ) {
                            SetRow(
                                set = set,
                                exercise = currentExercise,
                                onDelete = {
                                    val updated = currentSets
                                        .toMutableList()
                                        .also { it.removeAt(index) }
                                        .mapIndexed { i, s -> s.copy(setNumber = i + 1) }
                                    allExerciseSets[currentExercise.id] = updated
                                }
                            )
                        }
                        if (index < currentSets.lastIndex) {
                            HorizontalDivider(
                                color = cs.outlineVariant.copy(alpha = 0.4f),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }

            // ── Add set panel ──────────────────────────────────────────────
            AddSetPanel(
                exercise = currentExercise,
                weight = inputWeight,
                reps = inputReps,
                onWeightChange = { inputWeight = it },
                onRepsChange = { inputReps = it },
                onAddSet = {
                    val newSet = when (currentExercise.type) {
                        ExerciseType.REPS -> SetRecord(
                            setNumber = currentSets.size + 1,
                            weight = formatWeight(inputWeight),
                            reps = inputReps.toString(),
                            isCompleted = true,
                            completedAt = TimerManager.nowMillis()
                        )
                        ExerciseType.TIME -> SetRecord(
                            setNumber = currentSets.size + 1,
                            durationSeconds = inputReps.toString(),
                            isCompleted = true,
                            completedAt = TimerManager.nowMillis()
                        )
                        ExerciseType.DISTANCE -> SetRecord(
                            setNumber = currentSets.size + 1,
                            reps = inputReps.toString(),
                            isCompleted = true,
                            completedAt = TimerManager.nowMillis()
                        )
                    }
                    allExerciseSets[currentExercise.id] = currentSets + newSet
                }
            )
        }
    }
}

// ============================================================================
// EXIT DIALOG
// ============================================================================

@Composable
private fun ExitConfirmDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Rounded.Warning, null) },
        title = { Text("Sair do treino?") },
        text = { Text("Seu progresso não será salvo. Deseja sair?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) { Text("Sair") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

// ============================================================================
// EXERCISE INFO SHEET
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseInfoSheet(exercise: Exercise, onDismiss: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = cs.surface
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        exercise.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                    Spacer(Modifier.width(12.dp))
                    Surface(
                        color = when (exercise.difficulty) {
                            "Beginner" -> cs.tertiaryContainer
                            "Advanced" -> cs.errorContainer
                            else -> cs.secondaryContainer
                        },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = exercise.difficulty,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = when (exercise.difficulty) {
                                "Beginner" -> cs.onTertiaryContainer
                                "Advanced" -> cs.onErrorContainer
                                else -> cs.onSecondaryContainer
                            }
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                val ref = when (exercise.type) {
                    ExerciseType.REPS -> "${exercise.sets} séries × ${exercise.reps} reps"
                    ExerciseType.TIME -> "${exercise.sets} séries × ${formatWorkoutTime(exercise.durationSeconds)}"
                    ExerciseType.DISTANCE -> "${exercise.sets} séries"
                }
                Text(
                    ref,
                    style = MaterialTheme.typography.bodySmall,
                    color = cs.onSurfaceVariant
                )
            }

            // Divider
            item { HorizontalDivider(color = cs.outlineVariant) }

            // Execução
            item {
                InfoSection(
                    emoji = "▶",
                    title = "Como Executar",
                    content = exercise.description
                )
            }

            item { HorizontalDivider(color = cs.outlineVariant) }

            // Músculos
            item {
                InfoSection(
                    emoji = "💪",
                    title = "Músculos Trabalhados",
                    content = exercise.targetMuscleGroup
                )
            }

            item { HorizontalDivider(color = cs.outlineVariant) }

            // Dica
            item {
                val tip = when (exercise.type) {
                    ExerciseType.REPS ->
                        "Priorize a execução limpa antes de aumentar a carga. Controle a descida para maximizar o trabalho muscular."
                    ExerciseType.TIME ->
                        "Respire de forma constante e mantenha o abdômen contraído durante todo o tempo de duração."
                    ExerciseType.DISTANCE ->
                        "Comece em ritmo moderado e aumente gradualmente para evitar fadiga prematura."
                }
                Surface(
                    color = cs.primaryContainer.copy(alpha = 0.45f),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("💡", fontSize = 18.sp)
                        Spacer(Modifier.width(12.dp))
                        Text(
                            tip,
                            style = MaterialTheme.typography.bodyMedium,
                            color = cs.onPrimaryContainer,
                            lineHeight = 22.sp
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun InfoSection(emoji: String, title: String, content: String) {
    val cs = MaterialTheme.colorScheme
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 16.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = cs.onSurface
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            content,
            style = MaterialTheme.typography.bodyMedium,
            color = cs.onSurfaceVariant,
            lineHeight = 22.sp
        )
    }
}

// ============================================================================
// SET ROW — compact history line
// ============================================================================

@Composable
private fun SetRow(
    set: SetRecord,
    exercise: Exercise,
    onDelete: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Set label
        Text(
            text = "Série ${set.setNumber}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = cs.onSurface,
            modifier = Modifier.width(72.dp)
        )

        // Values
        val valueText = when (exercise.type) {
            ExerciseType.REPS -> buildString {
                if (set.weight.isNotBlank()) append("${set.weight} kg")
                if (set.weight.isNotBlank() && set.reps.isNotBlank()) append("  ·  ")
                if (set.reps.isNotBlank()) append("${set.reps} reps")
            }
            ExerciseType.TIME -> if (set.durationSeconds.isNotBlank())
                "${set.durationSeconds}s" else "—"
            ExerciseType.DISTANCE -> if (set.reps.isNotBlank())
                "${set.reps} m" else "—"
        }
        Text(
            text = valueText,
            style = MaterialTheme.typography.bodyMedium,
            color = cs.onSurface,
            modifier = Modifier.weight(1f)
        )

        // Delete
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                Icons.Rounded.Delete,
                contentDescription = "Remover série",
                modifier = Modifier.size(18.dp),
                tint = cs.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

// ============================================================================
// ADD SET PANEL — weight/reps steppers + confirm button
// ============================================================================

@Composable
private fun AddSetPanel(
    exercise: Exercise,
    weight: Float,
    reps: Int,
    onWeightChange: (Float) -> Unit,
    onRepsChange: (Int) -> Unit,
    onAddSet: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        color = cs.surfaceColorAtElevation(3.dp),
        tonalElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                "Adicionar Série",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = cs.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            when (exercise.type) {
                ExerciseType.REPS -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StepperField(
                            label = "Peso",
                            displayValue = "${formatWeight(weight)} kg",
                            onDecrement = { onWeightChange((weight - 2.5f).coerceAtLeast(0f)) },
                            onIncrement = { onWeightChange(weight + 2.5f) },
                            modifier = Modifier.weight(1f)
                        )
                        StepperField(
                            label = "Reps",
                            displayValue = "$reps reps",
                            onDecrement = { onRepsChange((reps - 1).coerceAtLeast(1)) },
                            onIncrement = { onRepsChange(reps + 1) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                ExerciseType.TIME -> {
                    StepperField(
                        label = "Duração",
                        displayValue = "${reps}s",
                        onDecrement = { onRepsChange((reps - 5).coerceAtLeast(5)) },
                        onIncrement = { onRepsChange(reps + 5) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                ExerciseType.DISTANCE -> {
                    StepperField(
                        label = "Distância",
                        displayValue = "$reps m",
                        onDecrement = { onRepsChange((reps - 10).coerceAtLeast(10)) },
                        onIncrement = { onRepsChange(reps + 10) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            Button(
                onClick = onAddSet,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Adicionar Série",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun StepperField(
    label: String,
    displayValue: String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    Column(modifier = modifier) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = cs.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = cs.outlineVariant,
                    shape = RoundedCornerShape(12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onDecrement,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Rounded.Remove,
                    contentDescription = "Diminuir",
                    modifier = Modifier.size(18.dp),
                    tint = cs.onSurface
                )
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayValue,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurface,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                onClick = onIncrement,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Aumentar",
                    modifier = Modifier.size(18.dp),
                    tint = cs.onSurface
                )
            }
        }
    }
}

// ============================================================================
// BOTTOM PROGRESS BAR — exercise navigation
// ============================================================================

@Composable
private fun WorkoutProgressBar(
    currentIndex: Int,
    total: Int,
    isLastExercise: Boolean,
    onNext: () -> Unit,
    onFinish: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        color = cs.background,
        shadowElevation = 12.dp,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Exercise dots indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExerciseDots(currentIndex = currentIndex, total = total)
            }

            // Action button
            if (isLastExercise) {
                Button(
                    onClick = onFinish,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Finalizar Treino",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            } else {
                OutlinedButton(
                    onClick = onNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        "Próximo Exercício",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ExerciseDots(currentIndex: Int, total: Int) {
    val cs = MaterialTheme.colorScheme
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(total) { index ->
            val isActive = index == currentIndex
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .width(if (isActive) 20.dp else 8.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (isActive) cs.primary
                        else cs.onSurface.copy(alpha = 0.15f)
                    )
            )
        }
    }
}

// ============================================================================
// UTILITIES
// ============================================================================

private fun formatWeight(weight: Float): String =
    if (weight % 1f == 0f) weight.toInt().toString() else "%.1f".format(weight)
