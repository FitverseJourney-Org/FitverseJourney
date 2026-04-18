package com.example.presentation.screens.ui.workout

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.Calendar
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.expect.DateTimeManager
import com.example.expect.PlatformDate
import com.example.expect.PlatformDateFormatter
import com.example.expect.formatWorkoutTime
import com.example.presentation.screens.widgets.FitVerseSpacer
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.bg_girl_pose
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.Resource
import org.jetbrains.compose.resources.painterResource

// ============================================================================
// DOMAIN MODELS - Estrutura preparada para arquitetura limpa
// ============================================================================

enum class ExerciseType {
    REPS,      // Exercícios baseados em repetições
    TIME,      // Exercícios baseados em tempo
    DISTANCE   // Exercícios baseados em distância (futuro)
}

/**
 * Modelo de exercício individual
 * Em produção, isso viria do Repository/Database
 */
data class Exercise(
    val id: Int,
    val title: String,
    val description: String,
    val targetMuscleGroup: String,
    val durationSeconds: Int = 30,
    val reps: Int = 10,
    val sets: Int = 3,
    val restAfterSeconds: Int = 60,
    val mediaUrl: DrawableResource,  // URL para GIF/imagem do exercício
    val videoUrl: String? = null,   // URL para vídeo demonstrativo
    val xp: Int = 10,
    val type: ExerciseType = ExerciseType.REPS,
    val difficulty: String = "Intermediate" // Beginner, Intermediate, Advanced
)

/**
 * Registro de uma série individual realizada
 */
data class SetRecord(
    val setNumber: Int,
    val weight: String = "",
    val reps: String = "",
    val durationSeconds: String = "",
    val restSeconds: String = "60",
    val isCompleted: Boolean = false,
    val completedAt: Long? = null // Timestamp para analytics
)

/**
 * Log histórico de um treino completo
 */
data class PastWorkoutLog(
    val id: String,
    val date: String,
    val timestamp: Long = DateTimeManager.nowMillis(),
    val exerciseId: Int,
    val exerciseName: String = "",
    val sets: List<SetRecord>,
    val totalVolume: Float = 0f, // kg × reps total
    val personalRecord: Boolean = false
)

/**
 * Plano de treino contendo múltiplos exercícios
 */
data class WorkoutPlan(
    val id: Int,
    val title: String,
    val description: String = "",
    val exercises: List<Exercise>,
    val estimatedDuration: Int = 0, // em minutos
    val difficulty: String = "Intermediate",
    val category: String = "Strength" // Strength, Cardio, Flexibility, etc.
)

// ============================================================================
// DATA SOURCE - Simulação de dados (preparado para Repository pattern)
// ============================================================================

/**
 * Em produção, isso seria um Repository com Room Database + Remote API
 */
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
                    description = "Compound chest exercise",
                    targetMuscleGroup = "Chest, Triceps, Shoulders",
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
                    description = "Back and biceps compound movement",
                    targetMuscleGroup = "Back, Biceps, Core",
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
                    title = "Plank Hold",
                    description = "Core stability exercise",
                    targetMuscleGroup = "Core, Shoulders",
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
                    title = "Dumbbell Rows",
                    description = "Unilateral back exercise",
                    targetMuscleGroup = "Back, Biceps",
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
                date = PlatformDateFormatter.getFormattedDate(daysOffset = -1),
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
                date = PlatformDateFormatter.getFormattedDate(daysOffset = -2),
                exerciseId = exerciseId,
                exerciseName = exerciseName,
                sets = listOf(
                    SetRecord(1, "65", "12", "", "90", true),
                    SetRecord(2, "65", "10", "", "90", true),
                    SetRecord(3, "70", "8", "", "90", true),
                    SetRecord(4, "70", "6", "", "90", true)
                ),
                totalVolume = 2860f
            ),
            PastWorkoutLog(
                id = "3",
                date = PlatformDateFormatter.getFormattedDate(daysOffset = -3),
                exerciseId = exerciseId,
                exerciseName = exerciseName,
                sets = listOf(
                    SetRecord(1, "60", "12", "", "90", true),
                    SetRecord(2, "60", "12", "", "90", true),
                    SetRecord(3, "65", "10", "", "90", true)
                ),
                totalVolume = 2090f
            )
        )
    }
}

// ============================================================================
// MAIN WORKOUT SCREEN
// ============================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSessionScreen(
    modifier: Modifier = Modifier,
    workout: WorkoutPlan = WorkoutDataSource.getMockWorkoutPlan(),
    onFinish: (resultXp: Int) -> Unit = {},
    onExit: () -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme

    // ========== State Management ==========
    var globalTimerSeconds by remember { mutableIntStateOf(0) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var totalXp by remember { mutableIntStateOf(0) }
    var restTimerSeconds by remember { mutableIntStateOf(0) }
    var showExerciseMedia by remember { mutableStateOf(false) }
    var selectedTab by remember(currentIndex) { mutableIntStateOf(0) }
    var showExitDialog by remember { mutableStateOf(false) }

    val currentExercise = workout.exercises[currentIndex]
    println("Exercise: $currentExercise")
    val tabs = listOf("Track", "History", "Info")

    // Tracking de exercícios completados
    val completedExercises = remember {
        mutableStateMapOf<Int, Boolean>().apply {
            workout.exercises.forEach { this[it.id] = false }
        }
    }

    // Estado de todas as séries de todos os exercícios
    val allExerciseSets = remember {
        mutableStateMapOf<Int, List<SetRecord>>().apply {
            workout.exercises.forEach { ex ->
                this[ex.id] = List(ex.sets) { index ->
                    SetRecord(
                        setNumber = index + 1,
                        weight = "",
                        reps = if (ex.type == ExerciseType.REPS) ex.reps.toString() else "",
                        durationSeconds = if (ex.type == ExerciseType.TIME) ex.durationSeconds.toString() else "",
                        restSeconds = ex.restAfterSeconds.toString(),
                        isCompleted = false
                    )
                }
            }
        }
    }

    val currentSets by remember(currentIndex) {
        derivedStateOf { allExerciseSets[currentExercise.id] ?: emptyList() }
    }

    val isCurrentExerciseDone = completedExercises[currentExercise.id] == true
    val areAllExercisesDone = workout.exercises.all { completedExercises[it.id] == true }
    val completedCount = completedExercises.count { it.value }

    // ========== Effects ==========

    // Timer global do treino
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            globalTimerSeconds++
        }
    }

    // Timer de descanso
    LaunchedEffect(restTimerSeconds) {
        if (restTimerSeconds > 0) {
            delay(1000L)
            restTimerSeconds--
        }
    }

    // ========== Exit Dialog ==========
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            icon = { Icon(Icons.Rounded.Warning, contentDescription = null) },
            title = { Text("Exit Workout?") },
            text = { Text("Your progress will be lost. Are you sure you want to exit?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        onExit()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = cs.error)
                ) {
                    Text("Exit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // ========== UI ==========
    Scaffold(
        containerColor = cs.background,
        topBar = {
            Column(
                modifier = Modifier
            ){
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = workout.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Timer global
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Rounded.Timer,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = cs.primary
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = formatWorkoutTime(globalTimerSeconds),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = cs.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                // XP acumulado
                                Surface(
                                    color = cs.tertiaryContainer,
                                    shape = MaterialTheme.shapes.extraSmall
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "⚡",
                                            fontSize = 10.sp
                                        )
                                        Text(
                                            text = "$totalXp XP",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = cs.onTertiaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { showExitDialog = true }) {
                            Icon(Icons.Rounded.Close, "Exit workout")
                        }
                    },
                    actions = {
                        // Botão de finalizar (só ativa quando tudo estiver completo)
                        AnimatedVisibility(visible = areAllExercisesDone) {
                            TextButton(
                                onClick = { onFinish(totalXp) },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = cs.primary
                                )
                            ) {
                                Icon(
                                    Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text("Finish", fontWeight = FontWeight.Bold)
                            }
                        }
                    },
                    windowInsets = WindowInsets(0,0,0,0),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )

                // Header do exercício atual
                CurrentExerciseHeader(
                    currentExercise = currentExercise,
                    currentIndex = currentIndex,
                    total = workout.exercises.size,
                    completedCount = completedCount,
                    onShowMedia = { showExerciseMedia = true }
                )
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    divider = { HorizontalDivider(color = cs.outlineVariant) },
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = cs.primary,
                            height = 3.dp
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == index) {
                                        FontWeight.Bold
                                    } else {
                                        FontWeight.Medium
                                    },
                                    color = if (selectedTab == index) {
                                        cs.primary
                                    } else {
                                        cs.onSurfaceVariant
                                    }
                                )
                            }
                        )
                    }
                }

            }
        },
        bottomBar = {
            WorkoutBottomBar(
                isCurrentDone = isCurrentExerciseDone,
                areAllDone = areAllExercisesDone,
                isLastExercise = currentIndex == workout.exercises.lastIndex,
                currentIndex = currentIndex,
                totalExercises = workout.exercises.size,
                onAction = {
                    when {
                        !isCurrentExerciseDone -> {
                            // Marca exercício como completo
                            completedExercises[currentExercise.id] = true
                            totalXp += currentExercise.xp
                            restTimerSeconds = 0

                            // Avança para próximo não completado
                            if (currentIndex < workout.exercises.lastIndex) {
                                currentIndex++
                            }
                        }
                        areAllExercisesDone -> onFinish(totalXp)
                        currentIndex < workout.exercises.lastIndex -> currentIndex++
                        else -> {
                            // Volta para o primeiro não completado
                            val firstUndone = workout.exercises.indexOfFirst {
                                completedExercises[it.id] != true
                            }
                            if (firstUndone != -1) currentIndex = firstUndone
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Timer de descanso (aparece quando ativo)
                item {
                    FitVerseSpacer(vertical = true, value = 16.dp)
                    AnimatedVisibility(
                        visible = restTimerSeconds > 0,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        RestTimerCard(
                            timeRemaining = restTimerSeconds,
                            totalRestTime = currentExercise.restAfterSeconds,
                            onAddTime = { restTimerSeconds += 30 },
                            onSkip = { restTimerSeconds = 0 }
                        )
                    }
                }

                // Tabs e conteúdo principal
                item {
                    Column(modifier = Modifier) {
                        Spacer(Modifier.height(20.dp))
                        // Conteúdo das tabs com animação
                        AnimatedContent(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            targetState = selectedTab,
                            transitionSpec = {
                                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                            },
                            label = "TabTransition"
                        ) { tab ->
                            when (tab) {
                                0 -> {
                                    // Tab de Tracking
                                    SetsTable(
                                        exercise = currentExercise,
                                        sets = currentSets,
                                        onSetChange = { updatedSets ->
                                            allExerciseSets[currentExercise.id] = updatedSets
                                        },
                                        onSetComplete = { completedSet ->
                                            // Inicia timer de descanso se não for a última série
                                            if (completedSet.isCompleted &&
                                                completedSet.setNumber < currentSets.size
                                            ) {
                                                restTimerSeconds = currentExercise.restAfterSeconds
                                            }

                                            // Auto-completa exercício se todas as séries estiverem done
                                            if (currentSets.all { it.isCompleted }) {
                                                completedExercises[currentExercise.id] = true
                                                totalXp += currentExercise.xp
                                            }
                                        },
                                        onAddSet = {
                                            val lastSet = currentSets.lastOrNull()
                                            val newSet = SetRecord(
                                                setNumber = currentSets.size + 1,
                                                reps = currentExercise.reps.toString(),
                                                weight = lastSet?.weight ?: "",
                                                durationSeconds = if (currentExercise.type == ExerciseType.TIME) {
                                                    currentExercise.durationSeconds.toString()
                                                } else "",
                                                restSeconds = lastSet?.restSeconds
                                                    ?: currentExercise.restAfterSeconds.toString(),
                                                isCompleted = false
                                            )
                                            allExerciseSets[currentExercise.id] = currentSets + newSet
                                        }
                                    )
                                }
                                1 -> {
                                    // Tab de Histórico
                                    HistoryLogView(
                                        history = WorkoutDataSource.getMockHistoryForExercise(
                                            currentExercise.id,
                                            currentExercise.title
                                        ),
                                        exerciseType = currentExercise.type
                                    )
                                }
                                2 -> {
                                    // Tab de Info do exercício
                                    ExerciseInfoView(exercise = currentExercise)
                                }
                            }
                        }
                    }
                }

                // Preview de exercícios (só aparece na tab de Track)
                if (selectedTab == 0) {
                    item {
                        Column(modifier = Modifier) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Workout Plan",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "$completedCount/${workout.exercises.size} completed",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = cs.onSurfaceVariant
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                itemsIndexed(workout.exercises) { index, ex ->
                                    SmallExercisePreview(
                                        ex = ex,
                                        isSelected = index == currentIndex,
                                        isDone = completedExercises[ex.id] == true,
                                        onClick = { currentIndex = index }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Media viewer overlay
            if (showExerciseMedia) {
                ExerciseMediaViewer(
                    exercise = currentExercise,
                    onDismiss = { showExerciseMedia = false }
                )
            }
        }
    }
}

// ============================================================================
// EXERCISE HEADER COMPONENT
// ============================================================================

@Composable
fun CurrentExerciseHeader(
    currentExercise: Exercise,
    currentIndex: Int,
    total: Int,
    completedCount: Int,
    onShowMedia: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme



    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Exercise ${currentIndex + 1} of $total",
                    style = MaterialTheme.typography.labelLarge,
                    color = cs.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.width(8.dp))

                // Difficulty badge
                Surface(
                    color = when (currentExercise.difficulty) {
                        "Beginner" -> cs.tertiaryContainer
                        "Advanced" -> cs.errorContainer
                        else -> cs.secondaryContainer
                    },
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        text = currentExercise.difficulty,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // XP Badge
            Surface(
                color = cs.primaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "+${currentExercise.xp} XP",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = cs.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Título e descrição
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = currentExercise.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurface
                )

                if (currentExercise.targetMuscleGroup != null) {
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.FitnessCenter,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = cs.onSurfaceVariant
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = currentExercise.targetMuscleGroup,
                            style = MaterialTheme.typography.bodyMedium,
                            color = cs.onSurfaceVariant
                        )
                    }
                }
            }

            // Botão para ver mídia do exercício
            if (currentExercise.mediaUrl != null) {
                IconButton(
                    onClick = onShowMedia,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(cs.primaryContainer)
                ) {
                    Icon(
                        Icons.Rounded.PlayCircle,
                        contentDescription = "View exercise",
                        tint = cs.onPrimaryContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Barra de progresso do workout completo
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Workout Progress",
                    style = MaterialTheme.typography.labelSmall,
                    color = cs.onSurfaceVariant
                )
                Text(
                    "$completedCount/$total exercises",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = cs.primary
                )
            }

            Spacer(Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { (currentIndex + 1) / total.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = cs.primary,
                trackColor = cs.surfaceVariant,
                strokeCap = StrokeCap.Round
            )
        }
    }
}

// ============================================================================
// REST TIMER CARD
// ============================================================================

@Composable
fun RestTimerCard(
    timeRemaining: Int,
    totalRestTime: Int,
    onAddTime: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val progress = if (totalRestTime > 0) timeRemaining.toFloat() / totalRestTime else 0f
    val formattedTime = formatWorkoutTime(timeRemaining)

    // Animação de pulsação para chamar atenção
    val scale by animateFloatAsState(
        targetValue = if (timeRemaining > 0) 1f else 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = cs.tertiaryContainer
        ),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Rounded.HotelClass,
                    contentDescription = null,
                    tint = cs.onTertiaryContainer,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Rest Time",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = cs.onTertiaryContainer
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Circular progress timer
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 10.dp,
                    color = cs.primary,
                    trackColor = cs.onTertiaryContainer.copy(alpha = 0.15f),
                    strokeCap = StrokeCap.Round
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formattedTime,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = cs.onTertiaryContainer
                    )
                    Text(
                        text = "remaining",
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onTertiaryContainer.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onAddTime,
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.5.dp, cs.onTertiaryContainer),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = cs.onTertiaryContainer
                    )
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("30s", fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onSkip,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cs.primary
                    )
                ) {
                    Icon(
                        Icons.Rounded.SkipNext,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Skip", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// ============================================================================
// SETS TABLE - Tabela de séries com inputs
// ============================================================================

@Composable
fun SetsTable(
    exercise: Exercise,
    sets: List<SetRecord>,
    onSetChange: (List<SetRecord>) -> Unit,
    onSetComplete: (SetRecord) -> Unit,
    onAddSet: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cs.surfaceColorAtElevation(1.dp))
            .padding(16.dp)
    ) {
        // Header da tabela
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "SET",
                modifier = Modifier.weight(0.6f),
                style = MaterialTheme.typography.labelMedium,
                color = cs.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            when (exercise.type) {
                ExerciseType.REPS -> {
                    Text(
                        "WEIGHT",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelMedium,
                        color = cs.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        "REPS",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelMedium,
                        color = cs.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                ExerciseType.TIME -> {
                    Text(
                        "DURATION",
                        modifier = Modifier.weight(2f),
                        style = MaterialTheme.typography.labelMedium,
                        color = cs.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                else -> {}
            }

            Box(
                modifier = Modifier.weight(0.7f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = cs.onSurfaceVariant
                )
            }
        }

        // Linhas das séries
        sets.forEachIndexed { index, setRecord ->
            SetTableRow(
                exercise = exercise,
                setRecord = setRecord,
                onWeightChange = { newWeight ->
                    val updated = sets.toMutableList().apply {
                        this[index] = setRecord.copy(weight = newWeight)
                    }
                    onSetChange(updated)
                },
                onRepsChange = { newReps ->
                    val updated = sets.toMutableList().apply {
                        this[index] = setRecord.copy(reps = newReps)
                    }
                    onSetChange(updated)
                },
                onDurationChange = { newDuration ->
                    val updated = sets.toMutableList().apply {
                        this[index] = setRecord.copy(durationSeconds = newDuration)
                    }
                    onSetChange(updated)
                },
                onToggleDone = {
                    val updatedSet = setRecord.copy(
                        isCompleted = !setRecord.isCompleted,
                        completedAt = if (!setRecord.isCompleted) {
                            DateTimeManager.nowMillis()
                        } else null
                    )
                    val updated = sets.toMutableList().apply {
                        this[index] = updatedSet
                    }
                    onSetChange(updated)
                    if (updatedSet.isCompleted) onSetComplete(updatedSet)
                }
            )

            if (index < sets.lastIndex) {
                Spacer(Modifier.height(12.dp))
            }
        }

        Spacer(Modifier.height(20.dp))

        // Botão adicionar série
        OutlinedButton(
            onClick = onAddSet,
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.5.dp, cs.primary.copy(alpha = 0.5f))
        ) {
            Icon(
                Icons.Rounded.Add,
                contentDescription = null,
                tint = cs.primary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Add Set",
                fontWeight = FontWeight.Bold,
                color = cs.primary
            )
        }
    }
}

@Composable
fun SetTableRow(
    exercise: Exercise,
    setRecord: SetRecord,
    onWeightChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onDurationChange: (String) -> Unit,
    onToggleDone: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current

    // Animações
    val backgroundColor by animateColorAsState(
        targetValue = if (setRecord.isCompleted) {
            cs.primaryContainer.copy(alpha = 0.4f)
        } else {
            Color.Transparent
        },
        label = "row_bg"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Número da série
        Box(
            modifier = Modifier.weight(0.6f),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = if (setRecord.isCompleted) cs.primary else cs.surfaceVariant,
                shape = CircleShape,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "${setRecord.setNumber}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (setRecord.isCompleted) cs.onPrimary else cs.onSurfaceVariant
                    )
                }
            }
        }

        // Inputs baseados no tipo de exercício
        when (exercise.type) {
            ExerciseType.REPS -> {
                // Input de peso
                Box(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                    WorkoutInputCell(
                        value = setRecord.weight,
                        onValueChange = onWeightChange,
                        placeholder = "kg",
                        isDone = setRecord.isCompleted,
                        focusManager = focusManager,
                        suffix = "kg"
                    )
                }

                // Input de reps
                Box(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
                    WorkoutInputCell(
                        value = setRecord.reps,
                        onValueChange = onRepsChange,
                        placeholder = "reps",
                        isDone = setRecord.isCompleted,
                        focusManager = focusManager
                    )
                }
            }
            ExerciseType.TIME -> {
                // Input de duração
                Box(modifier = Modifier.weight(2f).padding(horizontal = 4.dp)) {
                    WorkoutInputCell(
                        value = setRecord.durationSeconds,
                        onValueChange = onDurationChange,
                        placeholder = "seconds",
                        isDone = setRecord.isCompleted,
                        focusManager = focusManager,
                        suffix = "s"
                    )
                }
            }
            else -> {}
        }

        // Botão de completar
        Box(
            modifier = Modifier.weight(0.7f),
            contentAlignment = Alignment.Center
        ) {
            val checkColor by animateColorAsState(
                targetValue = if (setRecord.isCompleted) cs.primary else cs.surfaceVariant,
                label = "check_color"
            )
            val iconTint by animateColorAsState(
                targetValue = if (setRecord.isCompleted) cs.onPrimary else cs.onSurfaceVariant,
                label = "icon_tint"
            )

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(checkColor)
                    .clickable {
                        focusManager.clearFocus()
                        onToggleDone()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = "Mark as done",
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun WorkoutInputCell(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isDone: Boolean,
    focusManager: androidx.compose.ui.focus.FocusManager,
    suffix: String = ""
) {
    val cs = MaterialTheme.colorScheme

    BasicTextField(
        value = value,
        onValueChange = {
            // Limita tamanho e permite apenas números e ponto decimal
            if (it.length <= 6 && it.all { char -> char.isDigit() || char == '.' }) {
                onValueChange(it)
            }
        },
        textStyle = TextStyle(
            color = if (isDone) cs.onSurface.copy(alpha = 0.5f) else cs.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        enabled = !isDone,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isDone) {
                            cs.surfaceVariant.copy(alpha = 0.3f)
                        } else {
                            cs.surfaceVariant
                        }
                    )
                    .padding(vertical = 14.dp, horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                if (value.isEmpty()) {
                    Text(
                        placeholder,
                        color = cs.onSurfaceVariant.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        innerTextField()
                        if (suffix.isNotEmpty() && value.isNotEmpty()) {
                            Spacer(Modifier.width(2.dp))
                            Text(
                                suffix,
                                fontSize = 12.sp,
                                color = cs.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    )
}

// ============================================================================
// HISTORY LOG VIEW - Mostra treinos anteriores
// ============================================================================

@Composable
fun HistoryLogView(
    history: List<PastWorkoutLog>,
    exerciseType: ExerciseType,
    modifier: Modifier = Modifier
) {
    if (history.isEmpty()) {
        // Estado vazio
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Rounded.History,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    "No History Yet",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "Complete this exercise to see your progress",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            history.forEach { log ->
                HistoryCard(log = log, exerciseType = exerciseType)
            }
        }
    }
}

@Composable
fun HistoryCard(
    log: PastWorkoutLog,
    exerciseType: ExerciseType
) {
    val cs = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cs.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = MaterialTheme.shapes.medium,
        border = if (log.personalRecord) {
            BorderStroke(2.dp, cs.primary)
        } else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header do log
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = cs.primary
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = log.date,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = cs.primary
                    )
                }

                if (log.personalRecord) {
                    Surface(
                        color = cs.primaryContainer,
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🏆", fontSize = 10.sp)
                            Spacer(Modifier.width(2.dp))
                            Text(
                                "PR",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = cs.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Tabela de sets
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Set",
                    modifier = Modifier.weight(0.6f),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurfaceVariant
                )
                Text(
                    text = if (exerciseType == ExerciseType.REPS) "Weight × Reps" else "Duration",
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurfaceVariant
                )
                Text(
                    "Rest",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurfaceVariant,
                    textAlign = TextAlign.End
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 0.5.dp,
                color = cs.outlineVariant
            )

            // Sets individuais
            log.sets.forEach { set ->
                SetHistoryRow(set = set, exerciseType = exerciseType)
            }

            // Volume total (para exercícios de peso)
            if (exerciseType == ExerciseType.REPS && log.totalVolume > 0) {
                Spacer(Modifier.height(8.dp))
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = cs.outlineVariant
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Total Volume",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = cs.onSurfaceVariant
                    )
                    Text(
                        "${log.totalVolume.toInt()} kg",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = cs.primary
                    )
                }
            }
        }
    }
}

@Composable
fun SetHistoryRow(
    set: SetRecord,
    exerciseType: ExerciseType
) {
    val cs = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Número da série
        Surface(
            color = cs.surfaceVariant,
            shape = CircleShape,
            modifier = Modifier.size(28.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "${set.setNumber}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        // Performance data
        val performanceText = if (exerciseType == ExerciseType.REPS) {
            "${set.weight} kg × ${set.reps}"
        } else {
            formatWorkoutTime(set.durationSeconds.toIntOrNull() ?: 0)
        }

        Text(
            text = performanceText,
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = cs.onSurface
        )

        // Rest time
        Text(
            text = formatWorkoutTime(set.restSeconds.toIntOrNull() ?: 0),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = cs.onSurfaceVariant,
            textAlign = TextAlign.End
        )
    }
}

// ============================================================================
// EXERCISE INFO VIEW - Informações detalhadas do exercício
// ============================================================================

@Composable
fun ExerciseInfoView(
    exercise: Exercise,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cs.surfaceColorAtElevation(1.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Descrição
        if (exercise.description != null) {
            InfoSection(
                icon = Icons.Rounded.Description,
                title = "Description",
                content = exercise.description
            )
        }

        // Músculos alvo
        if (exercise.targetMuscleGroup != null) {
            InfoSection(
                icon = Icons.Rounded.FitnessCenter,
                title = "Target Muscles",
                content = exercise.targetMuscleGroup
            )
        }

        // Detalhes do exercício
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoBox(
                icon = Icons.Rounded.Refresh,
                label = "Sets",
                value = "${exercise.sets}",
                modifier = Modifier.weight(1f)
            )

            InfoBox(
                icon = if (exercise.type == ExerciseType.REPS) {
                    Icons.Rounded.Numbers
                } else {
                    Icons.Rounded.Timer
                },
                label = if (exercise.type == ExerciseType.REPS) "Reps" else "Time",
                value = if (exercise.type == ExerciseType.REPS) {
                    "${exercise.reps}"
                } else {
                    "${exercise.durationSeconds}s"
                },
                modifier = Modifier.weight(1f)
            )

            InfoBox(
                icon = Icons.Rounded.HotelClass,
                label = "Rest",
                value = "${exercise.restAfterSeconds}s",
                modifier = Modifier.weight(1f)
            )
        }

        // Tips (placeholder - poderia vir do backend)
        Surface(
            color = cs.primaryContainer.copy(alpha = 0.5f),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.Lightbulb,
                    contentDescription = null,
                    tint = cs.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(
                        "Pro Tip",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = cs.onPrimaryContainer
                    )
                    Text(
                        "Focus on form over speed. Control the movement both ways.",
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoSection(
    icon: ImageVector,
    title: String,
    content: String
) {
    val cs = MaterialTheme.colorScheme

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = cs.primary
            )
            Spacer(Modifier.width(6.dp))
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = cs.onSurface
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            content,
            style = MaterialTheme.typography.bodyMedium,
            color = cs.onSurfaceVariant,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun InfoBox(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = modifier,
        color = cs.secondaryContainer.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = cs.onSecondaryContainer
            )
            Spacer(Modifier.height(6.dp))
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = cs.onSecondaryContainer
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = cs.onSecondaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

// ============================================================================
// EXERCISE MEDIA VIEWER - Visualizador de GIF/Imagem
// ============================================================================

@Composable
fun ExerciseMediaViewer(
    exercise: Exercise,
    onDismiss: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable(enabled = false) { }, // Previne dismiss ao clicar no conteúdo
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        exercise.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (exercise.targetMuscleGroup != null) {
                        Text(
                            exercise.targetMuscleGroup,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Media content
            Card(
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = cs.surface
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    contentAlignment = Alignment.Center
                ) {
                    if (exercise.mediaUrl != null) {
                        // Usando Coil para carregar imagens/GIFs
                        // Em produção, você usaria: AsyncImage do Coil
                        Image(
                            painter = painterResource(resource = exercise.mediaUrl),
                            contentDescription = exercise.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        // Placeholder quando não há mídia
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Rounded.FitnessCenter,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = cs.onSurfaceVariant.copy(alpha = 0.3f)
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "No media available",
                                style = MaterialTheme.typography.bodyMedium,
                                color = cs.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Instruções de navegação
            Text(
                "Tap anywhere to close",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

// ============================================================================
// SMALL EXERCISE PREVIEW - Card pequeno para navegação
// ============================================================================

@Composable
fun SmallExercisePreview(
    ex: Exercise,
    isSelected: Boolean,
    isDone: Boolean,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Animação de escala quando selecionado
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        label = "scale"
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .width(150.dp)
            .height(90.dp)
            .scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> cs.primaryContainer
                isDone -> cs.tertiaryContainer.copy(alpha = 0.5f)
                else -> cs.surfaceColorAtElevation(2.dp)
            }
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, cs.primary)
        } else null,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Text(
                    text = ex.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        isSelected -> cs.onPrimaryContainer
                        isDone -> cs.onTertiaryContainer
                        else -> cs.onSurface
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${ex.sets} sets • ${if (ex.type == ExerciseType.REPS) "${ex.reps} reps" else "${ex.durationSeconds}s"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = cs.onSurfaceVariant
                )
            }

            // Indicador de conclusão
            if (isDone) {
                Surface(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    color = cs.primary,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = "Completed",
                        tint = cs.onPrimary,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(20.dp)
                    )
                }
            }
        }
    }
}

// ============================================================================
// WORKOUT BOTTOM BAR - Navegação e ações principais
// ============================================================================

@Composable
fun WorkoutBottomBar(
    isCurrentDone: Boolean,
    areAllDone: Boolean,
    isLastExercise: Boolean,
    currentIndex: Int,
    totalExercises: Int,
    onAction: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    val (buttonText, buttonIcon, buttonColor) = when {
        !isCurrentDone -> Triple(
            "Mark Exercise Complete",
            Icons.Rounded.CheckCircle,
            cs.primary
        )
        areAllDone -> Triple(
            "Complete Workout 🎉",
            Icons.Rounded.EmojiEvents,
            cs.tertiary
        )
        isLastExercise -> Triple(
            "Review Pending Exercises",
            Icons.Rounded.ArrowBack,
            cs.secondary
        )
        else -> Triple(
            "Next Exercise",
            Icons.Rounded.ArrowForward,
            cs.primary
        )
    }

    Box(
        modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                content = {
                    if (!areAllDone) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Progress",
                                style = MaterialTheme.typography.labelMedium,
                                color = cs.onSurfaceVariant
                            )
                            Text(
                                "${currentIndex + 1} / $totalExercises exercises",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = cs.primary
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { (currentIndex + 1) / totalExercises.toFloat() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = cs.primary,
                            trackColor = cs.surfaceVariant,
                            strokeCap = StrokeCap.Round
                        )

                        Spacer(Modifier.height(12.dp))
                    }

                    // Action button
                    Button(
                        onClick = onAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        Icon(
                            buttonIcon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            buttonText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    )
}