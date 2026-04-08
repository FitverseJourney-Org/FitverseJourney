package com.example.presentation.screens.ui.workout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expect.formatWorkoutTime
import kotlinx.coroutines.delay

// 1. Enums e Data Classes de Apoio
enum class ExerciseType {
    REPS, TIME
}

data class Exercise(
    val id: Int,
    val title: String,
    val description: String = "",
    val durationSeconds: Int = 30,
    val reps: Int = 10,
    val sets: Int = 3,
    val restAfterSeconds: Int = 60,
    val animationAsset: String? = null,
    val xp: Int = 10,
    val type: ExerciseType = ExerciseType.REPS
)

data class PastWorkoutLog(
    val date: String,
    val sets: List<SetRecord>
)


data class WorkoutPlan(
    val id: Int,
    val title: String,
    val exercises: List<Exercise>
)

// --- Estado auxiliar para rastrear o input do usuário por série ---
// Estrutura baseada no seu código
data class SetRecord(
    val setNumber: Int,
    val weight: String,
    val reps: String,
    val restSeconds: String,
    val isCompleted: Boolean
)
@Composable
fun HistoryLogView(
    history: List<PastWorkoutLog>,
    exerciseType: ExerciseType,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = history,
            key = { it.date } // Idealmente use um ID único do log
        ) { log ->
            HistoryCard(log, exerciseType)
        }
    }
}

@Composable
fun HistoryCard(log: PastWorkoutLog, exerciseType: ExerciseType) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        // Trocamos LazyColumn por Column normal
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = log.date,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Cabeçalho da tabela
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Série", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall)
                Text(
                    text = if (exerciseType == ExerciseType.REPS) "Peso / Reps" else "Duração",
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.labelSmall
                )
                Text("Descanso", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall)
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Iteramos sobre os sets usando um loop simples
            log.sets.forEach { set ->
                SetRow(set = set, exerciseType = exerciseType)
            }
        }
    }
}
@Composable
fun SetRow(set: SetRecord, exerciseType: ExerciseType) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Número da Série
        Text(
            text = "${set.setNumber}ª",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )

        // Dados de Performance (Peso x Reps ou Tempo)
        val performanceText = if (exerciseType == ExerciseType.REPS) {
            "${set.weight}kg x ${set.reps}"
        } else {
            // Formata os segundos salvos no log (ex: 90 -> "01:30")
            formatWorkoutTime(set.reps.toIntOrNull() ?: 0)
        }

        Text(
            text = performanceText,
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )

        // Descanso
        Text(text = formatWorkoutTime(set.restSeconds.toIntOrNull() ?: 0))
    }
}
// Mock para simular o banco de dados retornando treinos anteriores
fun getMockHistoryForExercise(exerciseId: Int): List<PastWorkoutLog> {
    return listOf(
        PastWorkoutLog(
            date = "Oct 12, 2023",
            sets = listOf(
                // Adicionando o valor de descanso (ex: "60") antes do boolean isCompleted
                SetRecord(
                    1,
                    "20",
                    "12",
                    "60",
                    true
                ),
                SetRecord(
                    2,
                    "20",
                    "10",
                    "60",
                    true
                ),
                SetRecord(
                    3,
                    "22.5",
                    "8",
                    "90",
                    true
                )
            )
        ),
        PastWorkoutLog(
            date = "Oct 05, 2023",
            sets = listOf(
                SetRecord(
                    1,
                    "15",
                    "12",
                    "60",
                    true
                ),
                SetRecord(
                    2,
                    "17.5",
                    "10",
                    "60",
                    true
                ),
                SetRecord(
                    3,
                    "20",
                    "8",
                    "60",
                    true
                )
            )
        ),
        PastWorkoutLog(
            date = "Oct 05, 2023",
            sets = listOf(
                SetRecord(
                    1,
                    "15",
                    "12",
                    "60",
                    true
                ),
                SetRecord(
                    2,
                    "17.5",
                    "10",
                    "60",
                    true
                ),
                SetRecord(
                    3,
                    "20",
                    "8",
                    "60",
                    true
                )
            )
        ),
        PastWorkoutLog(
            date = "Oct 05, 2023",
            sets = listOf(
                SetRecord(
                    1,
                    "15",
                    "12",
                    "60",
                    true
                ),
                SetRecord(
                    2,
                    "17.5",
                    "10",
                    "60",
                    true
                ),
                SetRecord(
                    3,
                    "20",
                    "8",
                    "60",
                    true
                )
            )
        ),
        PastWorkoutLog(
            date = "Oct 05, 2023",
            sets = listOf(
                SetRecord(
                    1,
                    "15",
                    "12",
                    "60",
                    true
                ),
                SetRecord(
                    2,
                    "17.5",
                    "10",
                    "60",
                    true
                ),
                SetRecord(
                    3,
                    "20",
                    "8",
                    "60",
                    true
                )
            )
        ),
        PastWorkoutLog(
            date = "Oct 05, 2023",
            sets = listOf(
                SetRecord(
                    1,
                    "15",
                    "12",
                    "60",
                    true
                ),
                SetRecord(
                    2,
                    "17.5",
                    "10",
                    "60",
                    true
                ),
                SetRecord(
                    3,
                    "20",
                    "8",
                    "60",
                    true
                )
            )
        ),
        PastWorkoutLog(
            date = "Oct 05, 2023",
            sets = listOf(
                SetRecord(
                    1,
                    "15",
                    "12",
                    "60",
                    true
                ),
                SetRecord(
                    2,
                    "17.5",
                    "10",
                    "60",
                    true
                ),
                SetRecord(
                    3,
                    "20",
                    "8",
                    "60",
                    true
                )
            )
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSessionScreen(
    modifier: Modifier = Modifier,
    workout: WorkoutPlan,
    onFinish: (resultXp: Int) -> Unit,
) {
    val cs = MaterialTheme.colorScheme

    // Estados
    var globalTimerSeconds by remember { mutableIntStateOf(0) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var totalXp by remember { mutableIntStateOf(0) }
    var restTimerSeconds by remember { mutableIntStateOf(0) }

    val currentExerciseData = workout.exercises[currentIndex]
    var selectedTab by remember(currentIndex) { mutableIntStateOf(0) }
    val tabs = listOf("Track", "History")

    val completedExercises = remember {
        mutableStateMapOf<Int, Boolean>().apply {
            workout.exercises.forEach { this[it.id] = false }
        }
    }

    val isCurrentExerciseDone = completedExercises[currentExerciseData.id] == true
    val areAllExercisesDone = workout.exercises.all { completedExercises[it.id] == true }

    val allExerciseSets = remember {
        mutableStateMapOf<Int, List<SetRecord>>().apply {
            workout.exercises.forEach { ex ->
                this[ex.id] = List(ex.sets) { index ->
                    SetRecord(
                        setNumber = index + 1,
                        weight = "", // Inicializa vazio para o usuário preencher
                        reps = if (ex.type == ExerciseType.REPS) {
                            ex.reps.toString()
                        } else {
                            ex.durationSeconds.toString()
                        },
                        restSeconds = ex.restAfterSeconds.toString(), // Nome corrigido para bater com o data class
                        isCompleted = false
                    )
                }
            }
        }
    }

    var currentSets = allExerciseSets[currentExerciseData.id] ?: emptyList()

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            globalTimerSeconds++
        }
    }

    LaunchedEffect(restTimerSeconds) {
        if (restTimerSeconds > 0) {
            delay(1000L)
            restTimerSeconds--
        }
    }

    Scaffold(
        containerColor = cs.background,
        topBar = {
            // Usamos uma Column para empilhar a TopAppBar e o Header de forma fixa
            Column(modifier = Modifier.background(cs.background)) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = workout.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                // Usando sua nova função expect de tempo aqui também!
                                text = "⏱ ${formatWorkoutTime(globalTimerSeconds)}",
                                style = MaterialTheme.typography.labelLarge,
                                color = cs.primary
                            )
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = { onFinish(totalXp) },
                            enabled = areAllExercisesDone
                        ) {
                            Text(
                                "Finish",
                                fontWeight = FontWeight.Bold,
                                color = if (areAllExercisesDone) cs.primary else cs.onSurface.copy(alpha = 0.3f)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = cs.background)
                )

                // O Header agora faz parte da TopBar (fixo no topo)
                CurrentExerciseHeader(
                    currentExercise = currentExerciseData,
                    currentIndex = currentIndex,
                    total = workout.exercises.size
                )

                HorizontalDivider(color = cs.outlineVariant, thickness = 0.5.dp)
            }
        },
        bottomBar = {
            WorkoutBottomBar(
                isCurrentDone = isCurrentExerciseDone,
                areAllDone = areAllExercisesDone,
                isLastExercise = currentIndex == workout.exercises.lastIndex,
                onAction = {
                    when {
                        !isCurrentExerciseDone -> {
                            completedExercises[currentExerciseData.id] = true
                            totalXp += currentExerciseData.xp
                            restTimerSeconds = 0
                            if (currentIndex < workout.exercises.lastIndex) {
                                currentIndex++
                            } else if (!areAllExercisesDone) {
                                val firstUndone = workout.exercises.indexOfFirst { completedExercises[it.id] != true }
                                if (firstUndone != -1) currentIndex = firstUndone
                            }
                        }
                        areAllExercisesDone -> onFinish(totalXp)
                        currentIndex < workout.exercises.lastIndex -> currentIndex++
                        else -> {
                            val firstUndone = workout.exercises.indexOfFirst { completedExercises[it.id] != true }
                            if (firstUndone != -1) currentIndex = firstUndone
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp) // Respiro maior entre as seções
        ) {

            item {
                AnimatedVisibility(
                    visible = restTimerSeconds > 0,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    RestTimerCard(
                        timeRemaining = restTimerSeconds,
                        totalRestTime = currentExerciseData.restAfterSeconds,
                        onAddTime = { restTimerSeconds += 30 },
                        onSkip = { restTimerSeconds = 0 }
                    )
                }
            }

            item {
                Column {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        divider = { HorizontalDivider(color = cs.outlineVariant) },
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = cs.primary
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
                                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                        color = if (selectedTab == index) cs.primary else cs.onSurfaceVariant
                                    )
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    AnimatedContent(
                        targetState = selectedTab,
                        transitionSpec = {
                            fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                        }, label = "TabTransition"
                    ) { tab ->
                        when (tab) {
                            0 -> SetsTable(
                                sets = currentSets,
                                onSetChange = { updatedSets ->
                                    currentSets = updatedSets
                                    allExerciseSets[currentExerciseData.id] = updatedSets
                                },
                                onSetComplete = { completedSet ->
                                    if (completedSet.isCompleted && completedSet.setNumber < currentSets.size) {
                                        restTimerSeconds = currentExerciseData.restAfterSeconds
                                    }
                                    // Valida se todos os sets estão completos para marcar exercício como pronto
                                    if (currentSets.all { it.isCompleted }) {
                                        completedExercises[currentExerciseData.id] = true
                                    }
                                },
                                onAddSet = {
                                    val lastSet = currentSets.lastOrNull()
                                    val newSet = SetRecord(
                                        setNumber = currentSets.size + 1,
                                        reps = currentExerciseData.reps.toString(),
                                        weight = lastSet?.weight ?: "",
                                        restSeconds = lastSet?.restSeconds ?: currentExerciseData.restAfterSeconds.toString(),
                                        isCompleted = false
                                    )

                                    // Atualiza apenas o mapa, o Compose cuidará do resto
                                    allExerciseSets[currentExerciseData.id] = currentSets + newSet
                                }
                            )
                            1 -> HistoryLogView(
                                history = getMockHistoryForExercise(currentExerciseData.id),
                                exerciseType = currentExerciseData.type
                            )
                        }
                    }
                }
            }

            item {
                if (selectedTab == 0) {
                    Text("Workout Plan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
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
}

@Composable
fun HistoryLogView(history: List<PastWorkoutLog>, exerciseType: ExerciseType) {
    TODO("Not yet implemented")
}

// Assuming a basic Exercise data class for context

@Composable
fun CurrentExerciseHeader(
    currentExercise: Exercise,
    currentIndex: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Exercício ${currentIndex + 1} de $total",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            // Badge de XP - Valorizando a gamificação do app
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(
                    text = "+${currentExercise.xp} XP",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Trocado .name por .title
        Text(
            text = currentExercise.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Trocado .targetMuscleGroup por .description
        if (currentExercise.description.isNotBlank()) {
            Text(
                text = currentExercise.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Barra de progresso do treino
        LinearProgressIndicator(
            progress = { (currentIndex + 1) / total.toFloat() },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round // Deixa as bordas da barra arredondadas
        )
    }
}

@Composable
fun RestTimerCard(
    timeRemaining: Int,
    totalRestTime: Int,
    onAddTime: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Cálculo do progresso para o indicador visual
    val progress = if (totalRestTime > 0) timeRemaining.toFloat() / totalRestTime else 0f

    // Agora utilizamos a função expect que delega para o Android ou iOS nativo
    val formattedTime = formatWorkoutTime(timeRemaining)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Descanso", // Traduzido para manter a consistência do app
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(140.dp)
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 8.dp,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )

                Text(
                    text = formattedTime, // Usando a string vinda do expect/actual
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                OutlinedButton(
                    onClick = onAddTime,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("+30s")
                }

                Button(
                    onClick = onSkip,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Pular")
                }
            }
        }
    }
}

// --- TABELA DE SÉRIES (A que estava cortada) ---
@Composable
fun SetsTable(
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
            .padding(8.dp)
    ) {
        // Headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("SET", modifier = Modifier.weight(0.5f), style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant, textAlign = TextAlign.Center)
            Text("KG", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant, textAlign = TextAlign.Center)
            Text("REPS", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant, textAlign = TextAlign.Center)
            Text("DONE", modifier = Modifier.weight(0.6f), style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant, textAlign = TextAlign.Center)
        }

        // Sets Rows
        sets.forEachIndexed { index, setRecord ->
            SetTableRow(
                setRecord = setRecord,
                onWeightChange = { newWeight ->
                    val updated = sets.toMutableList().apply { this[index] = setRecord.copy(weight = newWeight) }
                    onSetChange(updated)
                },
                onRepsChange = { newReps ->
                    val updated = sets.toMutableList().apply { this[index] = setRecord.copy(reps = newReps) }
                    onSetChange(updated)
                },
                onToggleDone = {
                    val updatedSet = setRecord.copy(isCompleted = !setRecord.isCompleted)
                    val updated = sets.toMutableList().apply { this[index] = updatedSet }
                    onSetChange(updated)
                    if (updatedSet.isCompleted) onSetComplete(updatedSet)
                }
            )
            if (index < sets.lastIndex) {
                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(16.dp))

        // Add Set Button
        TextButton(
            onClick = onAddSet,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Add Set")
            Spacer(Modifier.width(8.dp))
            Text("Add Set", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SetTableRow(
    setRecord: SetRecord,
    onWeightChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onToggleDone: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current

    // Animações para quando a série for concluída
    val backgroundColor by animateColorAsState(
        targetValue = if (setRecord.isCompleted) cs.primaryContainer.copy(alpha = 0.5f) else Color.Transparent,
        label = "row_bg_color"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Set Indicator
        Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.Center) {
            Text(
                text = setRecord.setNumber.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (setRecord.isCompleted) cs.primary else cs.onSurfaceVariant
            )
        }

        // Weight Input
        Box(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
            WorkoutInputCell(
                value = setRecord.weight,
                onValueChange = onWeightChange,
                placeholder = "-",
                isDone = setRecord.isCompleted,
                focusManager = focusManager
            )
        }

        // Reps Input
        Box(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) {
            WorkoutInputCell(
                value = setRecord.reps,
                onValueChange = onRepsChange,
                placeholder = "-",
                isDone = setRecord.isCompleted,
                focusManager = focusManager
            )
        }

        // Done Checkbox/Button
        Box(modifier = Modifier.weight(0.6f), contentAlignment = Alignment.Center) {
            val checkColor by animateColorAsState(
                targetValue = if (setRecord.isCompleted) cs.primary else cs.surfaceVariant,
                label = "check_color"
            )
            val iconTint by animateColorAsState(
                targetValue = if (setRecord.isCompleted) cs.onPrimary else cs.onSurfaceVariant,
                label = "icon_color"
            )

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(checkColor)
                    .clickable {
                        focusManager.clearFocus() // Tira o foco do teclado ao marcar
                        onToggleDone()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Check, contentDescription = "Done", tint = iconTint, modifier = Modifier.size(20.dp))
            }
        }
    }
}

// Célula de input altamente customizada para parecer com um app Nativo iOS / Premium
@Composable
fun WorkoutInputCell(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isDone: Boolean,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    val cs = MaterialTheme.colorScheme

    BasicTextField(
        value = value,
        onValueChange = { if (it.length <= 5) onValueChange(it) }, // Limita tamanho
        textStyle = TextStyle(
            color = if (isDone) cs.onSurface.copy(alpha = 0.6f) else cs.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        enabled = !isDone, // Bloqueia edição se estiver concluído
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(cs.surfaceVariant.copy(alpha = if (isDone) 0.3f else 0.8f))
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                if (value.isEmpty()) {
                    Text(placeholder, color = cs.onSurfaceVariant.copy(alpha = 0.5f))
                }
                innerTextField()
            }
        }
    )
}

// --- Preview de Exercícios na parte inferior ---
@Composable
fun SmallExercisePreview(ex: Exercise, isSelected: Boolean, isDone: Boolean, onClick: () -> Unit) {
    val cs = MaterialTheme.colorScheme

    Card(
        onClick = onClick,
        modifier = Modifier
            .width(140.dp)
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) cs.primaryContainer else cs.surfaceColorAtElevation(2.dp)
        ),
        border = if (isSelected) BorderStroke(2.dp, cs.primary) else null,
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Text(
                    text = ex.title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) cs.onPrimaryContainer else cs.onSurface,
                    maxLines = 2
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${ex.sets} sets",
                    style = MaterialTheme.typography.bodySmall,
                    color = cs.onSurfaceVariant
                )
            }
            if (isDone) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = cs.primary,
                    modifier = Modifier.align(Alignment.BottomEnd).size(20.dp)
                )
            }
        }
    }
}

// --- Bottom Bar Dinâmica Abstraída ---
@Composable
fun WorkoutBottomBar(
    isCurrentDone: Boolean,
    areAllDone: Boolean,
    isLastExercise: Boolean,
    onAction: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val buttonText = when {
        !isCurrentDone -> "Finish Exercise"
        areAllDone -> "Complete Workout"
        isLastExercise -> "Review Pending"
        else -> "Next Exercise"
    }

    Surface(
        color = cs.background,
        shadowElevation = 16.dp // Elevação para destacar do scroll
    ) {
        Button(
            onClick = onAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isCurrentDone && !areAllDone && isLastExercise) cs.error else cs.primary
            )
        ) {
            Text(buttonText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}