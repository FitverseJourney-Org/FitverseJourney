package com.example.presentation.screens.ui.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.workout.format
import com.example.presentation.screens.widgets.FitVerseSpacer
import kotlinx.coroutines.delay
import kotlin.text.ifEmpty

// --- Modelos de Dados (Mantenha os seus originais) ---

// Modelo para o Histórico de Treinos Passados
data class PastWorkoutLog(
    val date: String,
    val sets: List<com.example.presentation.screens.ui.workout.SetRecord>
)

enum class ExerciseType { TIMED, REPS }

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
    val type: com.example.presentation.screens.ui.workout.ExerciseType = _root_ide_package_.com.example.presentation.screens.ui.workout.ExerciseType.REPS
)

data class WorkoutPlan(
    val id: Int,
    val title: String,
    val exercises: List<com.example.presentation.screens.ui.workout.Exercise>
)

// --- Estado auxiliar para rastrear o input do usuário por série ---
data class SetRecord(
    val setNumber: Int,
    var weight: String = "",
    var reps: String = "",
    var restTime: String = "", // Novo campo
    var isCompleted: Boolean = false
)

// Mock para simular o banco de dados retornando treinos anteriores
fun getMockHistoryForExercise(exerciseId: Int): List<com.example.presentation.screens.ui.workout.PastWorkoutLog> {
    return listOf(
        _root_ide_package_.com.example.presentation.screens.ui.workout.PastWorkoutLog(
            date = "Oct 12, 2023",
            sets = listOf(
                // Adicionando o valor de descanso (ex: "60") antes do boolean isCompleted
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    1,
                    "20",
                    "12",
                    "60",
                    true
                ),
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    2,
                    "20",
                    "10",
                    "60",
                    true
                ),
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    3,
                    "22.5",
                    "8",
                    "90",
                    true
                )
            )
        ),
        _root_ide_package_.com.example.presentation.screens.ui.workout.PastWorkoutLog(
            date = "Oct 05, 2023",
            sets = listOf(
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    1,
                    "15",
                    "12",
                    "60",
                    true
                ),
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    2,
                    "17.5",
                    "10",
                    "60",
                    true
                ),
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    3,
                    "20",
                    "8",
                    "60",
                    true
                )
            )
        ),
        _root_ide_package_.com.example.presentation.screens.ui.workout.PastWorkoutLog(
            date = "Oct 05, 2023",
            sets = listOf(
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    1,
                    "15",
                    "12",
                    "60",
                    true
                ),
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    2,
                    "17.5",
                    "10",
                    "60",
                    true
                ),
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    3,
                    "20",
                    "8",
                    "60",
                    true
                )
            )
        ),
        _root_ide_package_.com.example.presentation.screens.ui.workout.PastWorkoutLog(
            date = "Oct 05, 2023",
            sets = listOf(
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    1,
                    "15",
                    "12",
                    "60",
                    true
                ),
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    2,
                    "17.5",
                    "10",
                    "60",
                    true
                ),
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    3,
                    "20",
                    "8",
                    "60",
                    true
                )
            )
        ),
        _root_ide_package_.com.example.presentation.screens.ui.workout.PastWorkoutLog(
            date = "Oct 05, 2023",
            sets = listOf(
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    1,
                    "15",
                    "12",
                    "60",
                    true
                ),
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    2,
                    "17.5",
                    "10",
                    "60",
                    true
                ),
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    3,
                    "20",
                    "8",
                    "60",
                    true
                )
            )
        ),
        _root_ide_package_.com.example.presentation.screens.ui.workout.PastWorkoutLog(
            date = "Oct 05, 2023",
            sets = listOf(
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    1,
                    "15",
                    "12",
                    "60",
                    true
                ),
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    2,
                    "17.5",
                    "10",
                    "60",
                    true
                ),
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    3,
                    "20",
                    "8",
                    "60",
                    true
                )
            )
        ),
        _root_ide_package_.com.example.presentation.screens.ui.workout.PastWorkoutLog(
            date = "Oct 05, 2023",
            sets = listOf(
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    1,
                    "15",
                    "12",
                    "60",
                    true
                ),
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                    2,
                    "17.5",
                    "10",
                    "60",
                    true
                ),
                _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
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
    workout: com.example.presentation.screens.ui.workout.WorkoutPlan,
    changeExercise: (com.example.presentation.screens.ui.workout.Exercise) -> Unit, // Mantido caso você use externamente
    currentExercise: () -> com.example.presentation.screens.ui.workout.Exercise,    // Mantido caso você use externamente
    onFinish: (resultXp: Int) -> Unit,
) {
    val cs = MaterialTheme.colorScheme

    // Estados principais
    var globalTimerSeconds by remember { mutableIntStateOf(0) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var totalXp by remember { mutableIntStateOf(0) }
    var restTimerSeconds by remember { mutableIntStateOf(0) }

    val currentExerciseData = workout.exercises[currentIndex]
    var selectedTab by remember(currentIndex) { mutableIntStateOf(0) }
    val tabs = listOf("Track", "History")

    // NOVO: Rastreia quais exercícios foram marcados como "Done"
    val completedExercises = remember {
        mutableStateMapOf<Int, Boolean>().apply {
            workout.exercises.forEach { ex ->
                this[ex.id] = false
            }
        }
    }

    // Variáveis auxiliares para saber o status do treino
    val isCurrentExerciseDone = completedExercises[currentExerciseData.id] == true
    val areAllExercisesDone = workout.exercises.all { completedExercises[it.id] == true }

    // Dicionário que guarda o estado de cada série
    val allExerciseSets = remember {
        mutableStateMapOf<Int, List<com.example.presentation.screens.ui.workout.SetRecord>>().apply {
            workout.exercises.forEach { ex ->
                this[ex.id] = List(ex.sets) {
                    _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                        setNumber = it + 1,
                        reps = if (ex.type == _root_ide_package_.com.example.presentation.screens.ui.workout.ExerciseType.REPS) ex.reps.toString() else ex.durationSeconds.toString(),
                        restTime = ex.restAfterSeconds.toString()
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
            TopAppBar(
                title = {
                    Column {
                        Text(workout.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            _root_ide_package_.com.example.presentation.screens.ui.workout.formatTime(
                                globalTimerSeconds
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = cs.primary
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { onFinish(totalXp) },
                        enabled = areAllExercisesDone // NOVO: Só habilita se todos estiverem prontos
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
        },
        bottomBar = {
            // NOVO: Lógica dinâmica para o texto e ação do botão
            val buttonText = when {
                !isCurrentExerciseDone -> "Finish Exercise"
                areAllExercisesDone -> "Complete Workout"
                currentIndex < workout.exercises.lastIndex -> "Next Exercise"
                else -> "Review Pending" // Último exercício feito, mas faltaram outros para trás
            }

            Button(
                onClick = {
                    when {
                        // Cenário 1: Finalizar o exercício atual
                        !isCurrentExerciseDone -> {
                            completedExercises[currentExerciseData.id] = true
                            totalXp += currentExerciseData.xp
                            restTimerSeconds = 0

                            // Avança para o próximo automaticamente, ou busca o primeiro não finalizado
                            if (currentIndex < workout.exercises.lastIndex) {
                                currentIndex++
                            } else if (!areAllExercisesDone) {
                                val firstUndone = workout.exercises.indexOfFirst { completedExercises[it.id] != true }
                                if (firstUndone != -1) currentIndex = firstUndone
                            }
                        }
                        // Cenário 2: Tudo pronto, finaliza o treino
                        areAllExercisesDone -> {
                            onFinish(totalXp)
                        }
                        // Cenário 3: Atual já feito, apenas navega para o próximo
                        currentIndex < workout.exercises.lastIndex -> {
                            currentIndex++
                        }
                        // Cenário 4: Chegou no fim, mas tem pendências para trás
                        else -> {
                            val firstUndone = workout.exercises.indexOfFirst { completedExercises[it.id] != true }
                            if (firstUndone != -1) currentIndex = firstUndone
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCurrentExerciseDone && !areAllExercisesDone && currentIndex == workout.exercises.lastIndex)
                        cs.error // Dá um destaque se precisar voltar para revisar
                    else cs.primary
                )
            ) {
                Text(buttonText, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                _root_ide_package_.com.example.presentation.screens.ui.workout.CurrentExerciseHeader(
                    currentExercise = currentExerciseData,
                    currentIndex = currentIndex,
                    total = workout.exercises.size
                )
            }

            item {
                AnimatedVisibility(
                    visible = restTimerSeconds > 0,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    _root_ide_package_.com.example.presentation.screens.ui.workout.RestTimerCard(
                        timeRemaining = restTimerSeconds,
                        onAddTime = { restTimerSeconds += 30 },
                        onSkip = { restTimerSeconds = 0 }
                    )
                }
            }

            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    divider = { Divider(color = cs.outlineVariant) },
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
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
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) cs.primary else cs.onSurfaceVariant
                                )
                            }
                        )
                    }
                }
            }

            item {
                when (selectedTab) {
                    0 -> {
                        _root_ide_package_.com.example.presentation.screens.ui.workout.SetsTable(
                            sets = currentSets,
                            exerciseType = currentExerciseData.type,
                            onSetChange = { updatedSets -> currentSets = updatedSets },
                            onSetComplete = { completedSet ->
                                if (completedSet.isCompleted && completedSet.setNumber < currentSets.size) {
                                    restTimerSeconds = currentExerciseData.restAfterSeconds
                                }
                            },
                            onAddSet = {
                                val nextSetNumber = currentSets.size + 1
                                val newSet =
                                    _root_ide_package_.com.example.presentation.screens.ui.workout.SetRecord(
                                        setNumber = nextSetNumber,
                                        reps = if (currentExerciseData.type == _root_ide_package_.com.example.presentation.screens.ui.workout.ExerciseType.REPS)
                                            currentExerciseData.reps.toString() else currentExerciseData.durationSeconds.toString(),
                                        weight = currentSets.lastOrNull()?.weight ?: ""
                                    )
                                allExerciseSets[currentExerciseData.id] = currentSets + newSet
                            },
                            restAfter = currentExerciseData.restAfterSeconds
                        )
                    }
                    1 -> {
                        val history = remember(currentExerciseData.id) {
                            _root_ide_package_.com.example.presentation.screens.ui.workout.getMockHistoryForExercise(
                                currentExerciseData.id
                            )
                        }
                        _root_ide_package_.com.example.presentation.screens.ui.workout.HistoryLogView(
                            history = history,
                            exerciseType = currentExerciseData.type
                        )
                    }
                }
            }
            item {
                if (selectedTab == 0) {
                    Text("Workout Plan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        itemsIndexed(workout.exercises) { index, ex ->
                            _root_ide_package_.com.example.presentation.screens.ui.workout.SmallExercisePreview(
                                ex = ex,
                                isSelected = index == currentIndex,
                                isDone = completedExercises[ex.id] == true, // Passando o novo status
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
fun HistoryLogView(history: List<com.example.presentation.screens.ui.workout.PastWorkoutLog>, exerciseType: com.example.presentation.screens.ui.workout.ExerciseType) {
    val cs = MaterialTheme.colorScheme

    if (history.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
            Text("No past history for this exercise.", color = cs.onSurfaceVariant)
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth().heightIn(max = 450.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(history.size) { index ->
            val log = history[index]
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Data do Treino
                    Text(
                        text = log.date,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = cs.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Cabeçalho do Histórico (Alinhado com a SetsTable)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("SET", modifier = Modifier.weight(0.8f), style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant, textAlign = TextAlign.Center)
                        Text("KG", modifier = Modifier.weight(1.5f), style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant, textAlign = TextAlign.Center)
                        Text(if (exerciseType == _root_ide_package_.com.example.presentation.screens.ui.workout.ExerciseType.REPS) "REPS" else "SECS", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant, textAlign = TextAlign.Center)
                        Text("REST", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant, textAlign = TextAlign.Center)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = cs.outlineVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Lista de Séries Realizadas
                    log.sets.forEach { setRecord ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Número do Set
                            Text(
                                text = "${setRecord.setNumber}",
                                modifier = Modifier.weight(0.8f),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                color = cs.onSurfaceVariant
                            )

                            // Peso
                            Text(
                                text = "${setRecord.weight.ifEmpty { "-" }}kg",
                                modifier = Modifier.weight(1.5f),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = cs.onSurface
                            )

                            // Repetições
                            Text(
                                text = setRecord.reps,
                                modifier = Modifier.weight(1.2f),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = cs.onSurface
                            )

                            // Descanso (Novo parâmetro)
                            Text(
                                text = "${setRecord.restTime.ifEmpty { "-" }}s",
                                modifier = Modifier.weight(1.2f),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = cs.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentExerciseHeader(currentExercise: com.example.presentation.screens.ui.workout.Exercise, currentIndex: Int, total: Int) {
    val cs = MaterialTheme.colorScheme
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Exercise ${currentIndex + 1} of $total",
            style = MaterialTheme.typography.labelMedium,
            color = cs.primary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = currentExercise.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = cs.onSurface
        )

        Spacer(Modifier.height(16.dp))

        // Área para a Animação (Substitua por Lottie/Coil)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(cs.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = cs.onSurfaceVariant, modifier = Modifier.size(48.dp))
            // Text(currentExercise.animationAsset ?: "Placeholder", color = cs.onSurfaceVariant)
        }
    }
}

@Composable
fun RestTimerCard(
    timeRemaining: Int,
    totalRestTime: Int = 60, // Adicionei para calcular a barra de progresso
    onAddTime: () -> Unit,
    onSkip: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Cálculo do progresso (0.0 a 1.0)
    val progress = if (totalRestTime > 0) timeRemaining.toFloat() / totalRestTime else 0f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = cs.primaryContainer.copy(alpha = 0.15f) // Fundo sutil e translúcido
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, cs.primaryContainer.copy(alpha = 0.3f)) // Borda fina para definição
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Lado Esquerdo: Ícone + Timer
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(cs.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow, // Pode trocar por um ícone de Timer/Ampulheta
                            contentDescription = null,
                            tint = cs.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text(
                            "RESTING",
                            style = MaterialTheme.typography.labelSmall,
                            color = cs.primary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            _root_ide_package_.com.example.presentation.screens.ui.workout.formatTime(
                                timeRemaining
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = cs.onSurface
                        )
                    }
                }

                // Lado Direito: Botões de Ação
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Botão +30s mais discreto
                    TextButton(
                        onClick = onAddTime,
                        colors = ButtonDefaults.textButtonColors(contentColor = cs.primary)
                    ) {
                        Text("+30s", fontWeight = FontWeight.Bold)
                    }

                    // Botão Skip mais "clicável"
                    FilledTonalButton(
                        onClick = onSkip,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text("Skip", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Barra de progresso sutil no fundo da card
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = cs.primary,
                trackColor = Color.Transparent,
            )
        }
    }
}

@Composable
fun SetsTable(
    sets: List<com.example.presentation.screens.ui.workout.SetRecord>,
    exerciseType: com.example.presentation.screens.ui.workout.ExerciseType,
    restAfter: Int, // Tempo padrão do exercício
    onSetChange: (List<com.example.presentation.screens.ui.workout.SetRecord>) -> Unit,
    onSetComplete: (com.example.presentation.screens.ui.workout.SetRecord) -> Unit,
    onAddSet: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Column(modifier = Modifier.fillMaxWidth()) {
        // --- CABEÇALHO COM PESOS DEFINIDOS ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("SET", modifier = Modifier.weight(0.8f), style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant, textAlign = TextAlign.Center)
            Text("KG/LBS", modifier = Modifier.weight(1.5f), style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant, textAlign = TextAlign.Center)
            Text("REPS", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant, textAlign = TextAlign.Center)
            Text("REST", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant, textAlign = TextAlign.Center)
            Text("DONE", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant, textAlign = TextAlign.Center)
        }

        // --- LINHAS (REPETIÇÕES) ---
        sets.forEachIndexed { index, setRecord ->
            _root_ide_package_.com.example.presentation.screens.ui.workout.SetRow(
                setRecord = setRecord,
                onSetChange = { updated ->
                    val newSets = sets.toMutableList()
                    newSets[index] = updated
                    onSetChange(newSets)
                },
                onSetComplete = { completed ->
                    val newSets = sets.toMutableList()
                    newSets[index] = completed
                    onSetChange(newSets)
                    onSetComplete(completed)
                }
            )
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onAddSet,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = cs.primary)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp).padding(end = 4.dp))
            Text("Add Set", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SetRow(
    setRecord: com.example.presentation.screens.ui.workout.SetRecord,
    onSetChange: (com.example.presentation.screens.ui.workout.SetRecord) -> Unit,
    onSetComplete: (com.example.presentation.screens.ui.workout.SetRecord) -> Unit,
    cs: ColorScheme = MaterialTheme.colorScheme
) {
    val isCompleted = setRecord.isCompleted
    val backgroundColor by animateColorAsState(
        targetValue = if (isCompleted) cs.primaryContainer.copy(alpha = 0.3f) else Color.Transparent,
        label = "bg_anim"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. SET NUMBER (Weight 0.8)
        Text(
            text = "${setRecord.setNumber}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (isCompleted) cs.primary else cs.onSurface,
            modifier = Modifier.weight(0.8f),
            textAlign = TextAlign.Center
        )

        // 2. WEIGHT (Weight 1.5)
        _root_ide_package_.com.example.presentation.screens.ui.workout.MinimalInput(
            value = setRecord.weight,
            onValueChange = { onSetChange(setRecord.copy(weight = it)) },
            placeholder = "0",
            modifier = Modifier.weight(1.5f),
            enabled = !isCompleted,
            suffix = "kg"
        )

        Spacer(Modifier.width(4.dp))

        // 3. REPS (Weight 1.2)
        _root_ide_package_.com.example.presentation.screens.ui.workout.MinimalInput(
            value = setRecord.reps,
            onValueChange = { onSetChange(setRecord.copy(reps = it)) },
            placeholder = "0",
            modifier = Modifier.weight(1.2f),
            enabled = !isCompleted
        )

        Spacer(Modifier.width(4.dp))

        // 4. REST (Weight 1.2)
        _root_ide_package_.com.example.presentation.screens.ui.workout.MinimalInput(
            value = setRecord.restTime,
            onValueChange = { onSetChange(setRecord.copy(restTime = it)) },
            placeholder = "-",
            modifier = Modifier.weight(1.2f),
            enabled = !isCompleted,
            suffix = "s"
        )

        // 5. CHECK BUTTON (Weight 1.0)
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { onSetComplete(setRecord.copy(isCompleted = !isCompleted)) },
                modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp))
                    .background(if (isCompleted) cs.primary else cs.surfaceVariant.copy(alpha = 0.6f))
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = if (isCompleted) cs.onPrimary else cs.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun MinimalInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    suffix: String = ""
) {
    val cs = MaterialTheme.colorScheme

    // Design do container do input
    Row(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (enabled) cs.surfaceVariant.copy(alpha = 0.4f) else Color.Transparent)
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        BasicTextField(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = if (enabled) cs.onSurface else cs.onSurface.copy(alpha = 0.5f)
            ),
            cursorBrush = SolidColor(cs.primary),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge,
                        color = cs.onSurfaceVariant.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )
                }
                innerTextField()
            }
        )
        if (suffix.isNotEmpty() && value.isNotEmpty()) {
            Text(
                text = " $suffix",
                style = MaterialTheme.typography.bodySmall,
                color = cs.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun SmallExercisePreview(ex: com.example.presentation.screens.ui.workout.Exercise, isSelected: Boolean, isDone: Boolean, onClick: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(100.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isDone && !isSelected) cs.surfaceVariant.copy(alpha = 0.5f) else cs.surfaceVariant
        ),
        // Adiciona uma borda diferente se estiver feito ou selecionado
        border = if (isSelected) BorderStroke(2.dp, cs.primary)
        else if (isDone) BorderStroke(1.dp, cs.primary.copy(alpha = 0.3f))
        else null,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp).fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = ex.title,
                    maxLines = 2,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDone) cs.onSurface.copy(alpha = 0.6f) else cs.onSurface,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )

                // NOVO: Mostra um ícone de ✅ se o exercício estiver finalizado
                if (isDone) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Done",
                        tint = cs.primary,
                        modifier = Modifier.size(16.dp).padding(start = 4.dp)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = "${ex.sets} sets • " + if (ex.type == _root_ide_package_.com.example.presentation.screens.ui.workout.ExerciseType.REPS) "${ex.reps} reps" else "${ex.durationSeconds}s",
                color = cs.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

private fun formatTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return "%02d:%02d".format(min, sec)
}
private fun String.format(min: Int, sec: Int): String {
    return "${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}"
}