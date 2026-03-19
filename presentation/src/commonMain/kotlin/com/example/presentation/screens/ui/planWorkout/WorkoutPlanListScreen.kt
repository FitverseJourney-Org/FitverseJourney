package com.example.presentation.screens.ui.planWorkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expect.getDayOfWeek
import kotlinx.datetime.DayOfWeek

// --- MODELOS ---

data class ExerciseModel(
    val id: String = "1",
    val name: String,
    val sets: Int,
    val reps: String,
    val restTimeSeconds: Int = 60,
    val weightKgs: Double? = null
)

data class WorkoutCategory(
    val id: String = "1",
    val name: String,         // Ex: "Treino A"
    val description: String,  // Ex: "Peito e Tríceps"
    val exercises: List<com.example.presentation.screens.ui.planWorkout.ExerciseModel> = emptyList()
)

data class DailyWorkoutState(
    val dayOfWeek: DayOfWeek,
    val dayNameShort: String,
    val workoutCategory: com.example.presentation.screens.ui.planWorkout.WorkoutCategory?
)

// --- TELA PRINCIPAL ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanDashboardScreen(
    toBack: () -> Unit,
    onEditWorkout: (com.example.presentation.screens.ui.planWorkout.WorkoutCategory) -> Unit,
    toNewWorkout: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Estado do dia selecionado (Inicia no dia atual do sistema)
    val today = getDayOfWeek() // Assume que retorna a String do nome do dia
    var selectedDayName by remember { mutableStateOf(today) }

    // Mock de dados para a Versão 1.0
    val weekPlan = remember {
        listOf(
            _root_ide_package_.com.example.presentation.screens.ui.planWorkout.DailyWorkoutState(
                dayOfWeek = DayOfWeek.MONDAY,
                dayNameShort = "Seg",
                workoutCategory = _root_ide_package_.com.example.presentation.screens.ui.planWorkout.WorkoutCategory(
                    name = "Treino A",
                    description = "Peito e Tríceps",
                    exercises = _root_ide_package_.com.example.presentation.screens.ui.planWorkout.getMockExercises(
                        1
                    )
                )
            ),
            _root_ide_package_.com.example.presentation.screens.ui.planWorkout.DailyWorkoutState(
                dayOfWeek = DayOfWeek.TUESDAY,
                dayNameShort = "Ter",
                workoutCategory = _root_ide_package_.com.example.presentation.screens.ui.planWorkout.WorkoutCategory(
                    name = "Treino B",
                    description = "Costas e Bíceps",
                    exercises = _root_ide_package_.com.example.presentation.screens.ui.planWorkout.getMockExercises(
                        2
                    )
                )
            ),
            _root_ide_package_.com.example.presentation.screens.ui.planWorkout.DailyWorkoutState(
                dayOfWeek = DayOfWeek.WEDNESDAY,
                dayNameShort = "Qua",
                workoutCategory = null // Dia de Descanso
            ),
            _root_ide_package_.com.example.presentation.screens.ui.planWorkout.DailyWorkoutState(
                dayOfWeek = DayOfWeek.THURSDAY,
                dayNameShort = "Qui",
                workoutCategory = _root_ide_package_.com.example.presentation.screens.ui.planWorkout.WorkoutCategory(
                    name = "Treino C",
                    description = "Pernas Completas",
                    exercises = _root_ide_package_.com.example.presentation.screens.ui.planWorkout.getMockExercises(
                        3
                    )
                )
            ),
            _root_ide_package_.com.example.presentation.screens.ui.planWorkout.DailyWorkoutState(
                dayOfWeek = DayOfWeek.FRIDAY,
                dayNameShort = "Sex",
                workoutCategory = _root_ide_package_.com.example.presentation.screens.ui.planWorkout.WorkoutCategory(
                    name = "Treino D",
                    description = "Ombros e Abdômen",
                    exercises = _root_ide_package_.com.example.presentation.screens.ui.planWorkout.getMockExercises(
                        4
                    )
                )
            ),
            _root_ide_package_.com.example.presentation.screens.ui.planWorkout.DailyWorkoutState(
                dayOfWeek = DayOfWeek.SATURDAY,
                dayNameShort = "Sáb",
                workoutCategory = null
            ),
            _root_ide_package_.com.example.presentation.screens.ui.planWorkout.DailyWorkoutState(
                dayOfWeek = DayOfWeek.SUNDAY,
                dayNameShort = "Dom",
                workoutCategory = null
            )
        )
    }

    val selectedWorkoutState = weekPlan.find { it.dayOfWeek.name == selectedDayName }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Meu Plano Atual", fontWeight = FontWeight.Black, fontSize = 20.sp)
                        Text(
                            "Foco: Hipertrofia Masculina",
                            fontSize = 12.sp,
                            color = cs.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        toBack()
                    }) { Icon(Icons.Rounded.ArrowBack, null) }
                },
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            // criar condição caso nao exita planilha de treino para o dia atual
            ExtendedFloatingActionButton(
                onClick = toNewWorkout,
                containerColor = cs.primary,
                contentColor = cs.onPrimary,
                icon = { Icon(Icons.Rounded.Add, null) },
                text = { Text("Editar Plano", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 1. Seletor de Dias
            _root_ide_package_.com.example.presentation.screens.ui.planWorkout.WeekDaySelector(
                weekDays = weekPlan,
                selectedDayName = selectedDayName,
                onDaySelected = { selectedDayName = it },
                cs = cs
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = cs.outlineVariant.copy(alpha = 0.4f)
            )

            // 2. Área de Conteúdo Dinâmica
            Box(modifier = Modifier.fillMaxSize()) {
                if (selectedWorkoutState?.workoutCategory != null) {
                    _root_ide_package_.com.example.presentation.screens.ui.planWorkout.ActiveWorkoutView(
                        workout = selectedWorkoutState.workoutCategory,
                        cs = cs,
                        onEditClick = { onEditWorkout(selectedWorkoutState.workoutCategory) }
                    )
                } else {
                    _root_ide_package_.com.example.presentation.screens.ui.planWorkout.RestDayView(
                        cs = cs
                    )
                }
            }
        }
    }
}

// --- COMPONENTES ---

@Composable
fun WeekDaySelector(
    weekDays: List<com.example.presentation.screens.ui.planWorkout.DailyWorkoutState>,
    selectedDayName: String,
    onDaySelected: (String) -> Unit,
    cs: ColorScheme
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekDays.forEach { state ->
            val isSelected = state.dayOfWeek.name == selectedDayName
            val hasWorkout = state.workoutCategory != null

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onDaySelected(state.dayOfWeek.name) }
                    .background(if (isSelected) cs.primaryContainer else Color.Transparent)
                    .padding(vertical = 8.dp, horizontal = 10.dp)
            ) {
                Text(
                    text = state.dayNameShort,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) cs.onPrimaryContainer else cs.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Indicador de treino
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(
                            color = if (hasWorkout) cs.primary else cs.outlineVariant.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun ActiveWorkoutView(
    workout: com.example.presentation.screens.ui.planWorkout.WorkoutCategory,
    cs: ColorScheme,
    onEditClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        workout.name,
                        color = cs.primary,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                    Text(workout.description, color = cs.onSurfaceVariant, fontSize = 14.sp)
                }
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.background(cs.secondaryContainer, CircleShape)
                ) {
                    Icon(
                        Icons.Rounded.Edit,
                        "Editar",
                        tint = cs.onSecondaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(workout.exercises, key = { it.id }) { exercise ->
            _root_ide_package_.com.example.presentation.screens.ui.planWorkout.ExerciseViewerCard(
                exercise,
                cs
            )
        }

        item { Spacer(modifier = Modifier.height(80.dp)) } // Espaço pro FAB
    }
}

@Composable
fun ExerciseViewerCard(exercise: com.example.presentation.screens.ui.planWorkout.ExerciseModel, cs: ColorScheme) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cs.surface),
        border = BorderStroke(1.dp, cs.outlineVariant.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(cs.primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.FitnessCenter,
                    null,
                    tint = cs.primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(exercise.name, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    _root_ide_package_.com.example.presentation.screens.ui.planWorkout.ExerciseTag(
                        "${exercise.sets} séries",
                        cs.secondaryContainer,
                        cs.onSecondaryContainer
                    )
                    _root_ide_package_.com.example.presentation.screens.ui.planWorkout.ExerciseTag(
                        exercise.reps,
                        cs.tertiaryContainer,
                        cs.onTertiaryContainer
                    )
                }
            }
            Icon(Icons.Rounded.ChevronRight, null, tint = cs.outlineVariant)
        }
    }
}

@Composable
fun ExerciseTag(label: String, bgColor: Color, txtColor: Color) {
    Surface(color = bgColor, shape = RoundedCornerShape(8.dp)) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = txtColor
        )
    }
}

@Composable
fun RestDayView(cs: ColorScheme) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Rounded.SelfImprovement,
            null,
            modifier = Modifier.size(64.dp),
            tint = cs.tertiary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Dia de Descanso", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(
            "Seus músculos crescem durante o repouso. Hidrate-se e recupere suas energias!",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = cs.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

// --- MOCKS ---
fun getMockExercises(variation: Int): List<com.example.presentation.screens.ui.planWorkout.ExerciseModel> {
    val names =
        listOf("Supino", "Agachamento", "Remada", "Rosca Direta", "Leg Press", "Desenvolvimento")
    return List(4) { i ->
        _root_ide_package_.com.example.presentation.screens.ui.planWorkout.ExerciseModel(
            id = "$variation$i",
            name = names.random(),
            sets = 4,
            reps = "10-12"
        )
    }
}