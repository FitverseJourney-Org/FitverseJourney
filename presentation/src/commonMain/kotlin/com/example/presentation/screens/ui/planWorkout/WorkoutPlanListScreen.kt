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
import com.example.presentation.screens.widgets.FitverseIconBack
import com.example.presentation.screens.widgets.FitverseTopAppBar
import com.example.presentation.theme.DarkGamifiedColors
import com.example.presentation.theme.PADDING_TOPAPPBAR_DEFAULT_HORIZONTAL
import com.example.presentation.theme.PADDING_TOPAPPBAR_DEFAULT_VERTICAL
import kotlinx.datetime.DayOfWeek

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
    val name: String,
    val description: List<ExerciseModel>,
    val exercises: List<ExerciseModel> = emptyList()
)

data class DailyWorkoutState(
    val dayOfWeek: DayOfWeek,
    val dayNameShort: String,
    val workoutCategory: WorkoutCategory?
)

// --- TELA PRINCIPAL ---


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanDashboardScreen(
    onBack: () -> Unit,
    onEditWorkout: (WorkoutCategory) -> Unit,
    toNewWorkout: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val today = getDayOfWeek()
    var selectedDayName by remember { mutableStateOf(today) }

    // Mock atualizado para usar o estado das DailyWorkoutState
    val weekPlan = remember {
        listOf(
            DailyWorkoutState(DayOfWeek.MONDAY, "Seg", WorkoutCategory("Treino A", "Peito e Tríceps", getMockExercises(1))),
            DailyWorkoutState(DayOfWeek.TUESDAY, "Ter", WorkoutCategory("Treino B", "Costas e Bíceps", getMockExercises(2))),
            DailyWorkoutState(DayOfWeek.WEDNESDAY, "Qua", null),
            DailyWorkoutState(DayOfWeek.THURSDAY, "Qui", WorkoutCategory("Treino C", "Pernas Completas", getMockExercises(3))),
            DailyWorkoutState(DayOfWeek.FRIDAY, "Sex", WorkoutCategory("Treino D", "Ombros e Abdômen", getMockExercises(4))),
            DailyWorkoutState(DayOfWeek.SATURDAY, "Sáb", null),
            DailyWorkoutState(DayOfWeek.SUNDAY, "Dom", null)
        )
    }
    val selectedWorkoutState = weekPlan.find { it.dayOfWeek.name == selectedDayName }
    Scaffold(
        containerColor = colors.background, // Deep Neutral (#0A0B0F)
        topBar = {
            FitverseTopAppBar(
                title = "MEU PLANO",
                subtitle = {
                    Text(
                        text = "FOCO: HIPERTROFIA",
                        fontSize = 12.sp,
                        color = colors.secondary,
                        fontWeight = FontWeight.Bold
                    )
                },
                onBack = onBack,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = toNewWorkout,
                containerColor = colors.primary, // Roxo Vibrante para ação de edição
                contentColor = Color.White,
                icon = { Icon(Icons.Rounded.Edit, null) },
                text = { Text("EDITAR PLANO", fontWeight = FontWeight.Black) }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            WeekDaySelector(
                weekDays = weekPlan,
                selectedDayName = selectedDayName,
                onDaySelected = { selectedDayName = it },
                colors = colors
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = colors.outline.copy(alpha = 0.2f)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                if (selectedWorkoutState?.workoutCategory != null) {
                    ActiveWorkoutView(
                        workout = selectedWorkoutState.workoutCategory,
                        onEditClick = { onEditWorkout(selectedWorkoutState.workoutCategory) },
                        colors = colors
                    )
                } else {
                    RestDayView(colors = colors)
                }
            }
        }
    }
}

// --- COMPONENTES ---
@Composable
fun WeekDaySelector(
    weekDays: List<DailyWorkoutState>,
    selectedDayName: String,
    onDaySelected: (String) -> Unit,
    colors: ColorScheme
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
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
                    // Fundo Roxo Profundo para seleção (#2D1B59)
                    .background(if (isSelected) colors.primaryContainer.copy(alpha = 0.5f) else Color.Transparent)
                    .padding(vertical = 8.dp, horizontal = 10.dp)
            ) {
                Text(
                    text = state.dayNameShort,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
                    color = if (isSelected) colors.primary else colors.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Dot indicador: Roxo se tiver treino, cinza se não
                Box(
                    modifier = Modifier.size(6.dp).background(
                        color = if (hasWorkout) colors.primary else colors.outline.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
                )
            }
        }
    }
}

@Composable
fun ActiveWorkoutView(
    workout: WorkoutCategory,
    onEditClick: () -> Unit,
    colors: ColorScheme
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
                    // Nome do Treino: Roxo Neon (#7C3AED)
                    Text(
                        text = workout.name,
                        color = colors.primary,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        letterSpacing = 0.5.sp
                    )
                    // Descrição/Músculos: Cinza Muted
                    Text(
                        text = workout.name, // Ajustado para exibir o nome/descrição real
                        color = colors.onSurfaceVariant,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Botão de Edição Rápida
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .size(44.dp)
                        .background(colors.surfaceVariant, CircleShape) // Fundo Cinza Escuro
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = "Editar Treino",
                        tint = colors.onSurface, // Ícone em Branco Puro
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Lista de Exercícios: Certifique-se de passar o 'colors' aqui
        items(workout.exercises, key = { it.id }) { exercise ->
            ExerciseViewerCard(exercise = exercise, colors = colors)
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}


@Composable
fun ExerciseViewerCard(exercise: ExerciseModel, colors: ColorScheme) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant.copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(colors.secondary.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.FitnessCenter, null, tint = colors.secondary, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(exercise.name.uppercase(), fontWeight = FontWeight.Black, fontSize = 14.sp, color = colors.onSurface)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Séries em Roxo (Esforço) e Reps em Azul (Metodologia)
                    ExerciseTag("${exercise.sets} séries", colors.primaryContainer.copy(alpha = 0.3f), colors.primary)
                    ExerciseTag(exercise.reps, colors.secondaryContainer.copy(alpha = 0.3f), colors.secondary)
                }
            }
            Icon(Icons.Rounded.ChevronRight, null, tint = colors.onSurfaceVariant.copy(alpha = 0.3f))
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
            fontWeight = FontWeight.Black,
            color = txtColor
        )
    }
}


@Composable
fun RestDayView(colors: ColorScheme) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícone em Verde Neon (Tertiary) para representar saúde/recuperação
        Icon(Icons.Rounded.Spa, null, modifier = Modifier.size(64.dp), tint = colors.tertiary)
        Spacer(modifier = Modifier.height(16.dp))
        Text("DIA DE DESCANSO", fontWeight = FontWeight.Black, fontSize = 20.sp, color = colors.onBackground)
        Text(
            "Seus músculos crescem durante o repouso. Hidrate-se e recupere seu vigor!",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = colors.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}


// --- MOCKS ---
fun getMockExercises(variation: Int): List<ExerciseModel> {
    val names = listOf("Supino", "Agachamento", "Remada", "Rosca Direta", "Leg Press", "Desenvolvimento")
    return List(4) { i ->
        ExerciseModel(id = "$variation$i", name = names.random(), sets = 4, reps = "10-12")
    }
}