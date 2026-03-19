package com.example.presentation.screens.ui.planWorkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.collections.set

// DATA MODELS
enum class DayOfWeek(val shortName: String, val fullName: String) {
    MON("S", "Segunda"), TUE("T", "Terça"), WED("Q", "Quarta"),
    THU("Q", "Quinta"), FRI("S", "Sexta"), SAT("S", "Sábado"), SUN("D", "Domingo")
}

data class Exercise(
    val id: String = "1",
    val name: String,
    val sets: String = "3",
    val reps: String = "12",
    val rest: String = "60s"
)

data class DailyWorkout(
    val day: com.example.presentation.screens.ui.planWorkout.DayOfWeek,
    val exercises: List<com.example.presentation.screens.ui.planWorkout.Exercise> = emptyList(),
    val isRestDay: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanBuilderScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    toAddExercises: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // ESTADOS GLOBAIS DA TELA
    var planName by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf(_root_ide_package_.com.example.presentation.screens.ui.planWorkout.DayOfWeek.MON) }

    // Mapeia quais dias são de descanso
    val restDaysMap = remember { mutableStateMapOf<com.example.presentation.screens.ui.planWorkout.DayOfWeek, Boolean>() }
    // Mock de exercícios
    val workoutMap = remember { mutableStateMapOf<com.example.presentation.screens.ui.planWorkout.DayOfWeek, List<com.example.presentation.screens.ui.planWorkout.Exercise>>() }

    // Lógica para saber se o dia atual selecionado é descanso
    val isCurrentDayRest = restDaysMap[selectedDay] ?: false

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Novo Plano", fontWeight = FontWeight.Bold) },
                windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Rounded.ArrowBack, null) }
                },
                actions = {
                    TextButton(onClick = onSave) {
                        Text("Concluir", color = cs.primary, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            // SÓ EXIBE OU ATIVA O FAB SE NÃO FOR DIA DE DESCANSO
            if (!isCurrentDayRest) {
                ExtendedFloatingActionButton(
                    onClick = toAddExercises,
                    containerColor = cs.primary,
                    icon = { Icon(Icons.Rounded.Add, null) },
                    text = { Text("Adicionar Exercício") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            _root_ide_package_.com.example.presentation.screens.ui.planWorkout.PlanHeaderSection(
                planName
            ) { planName = it }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Cronograma Semanal",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 20.dp),
                fontWeight = FontWeight.Bold
            )

            _root_ide_package_.com.example.presentation.screens.ui.planWorkout.DaySelector(
                selectedDay
            ) { selectedDay = it }

            Spacer(modifier = Modifier.height(16.dp))

            val exercisesForDay = workoutMap[selectedDay] ?: emptyList()

            ExerciseListSection(
                dayName = selectedDay.fullName,
                exercises = exercisesForDay,
                isRestDay = isCurrentDayRest, // Passa o estado atual
                onToggleRestDay = {

                }
            )
        }
    }
}

@Composable
fun DaySelector(selectedDay: DayOfWeek, onDaySelected: (DayOfWeek) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DayOfWeek.entries.forEach { day ->
            val isSelected = day == selectedDay
            val cs = MaterialTheme.colorScheme

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onDaySelected(day) }
                    .background(if (isSelected) cs.primary else cs.surfaceVariant.copy(alpha = 0.3f))
                    .padding(vertical = 12.dp, horizontal = 10.dp)
                    .width(32.dp)
            ) {
                Text(
                    text = day.shortName,
                    color = if (isSelected) cs.onPrimary else cs.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ExerciseItem(exercise: com.example.presentation.screens.ui.planWorkout.Exercise, onRemove: () -> Unit = {}) {
    val cs = MaterialTheme.colorScheme
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = cs.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        border = BorderStroke(1.dp, cs.outlineVariant.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Container do Ícone
            Box(
                modifier = Modifier
                    .size(44.dp)
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

            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(
                    exercise.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = cs.onSurface
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    _root_ide_package_.com.example.presentation.screens.ui.planWorkout.ParamChip(
                        value = "${exercise.sets} séries",
                        cs
                    )
                    Text("•", color = cs.outline, fontSize = 10.sp)
                    _root_ide_package_.com.example.presentation.screens.ui.planWorkout.ParamChip(
                        value = exercise.reps,
                        cs
                    )
                }
            }

            Icon(
                Icons.Rounded.DragHandle,
                contentDescription = "Reordenar",
                tint = cs.outlineVariant
            )
        }
    }
}
@Composable
fun ParamChip(value: String, cs: ColorScheme) {
    Text(
        text = value,
        fontSize = 12.sp,
        color = cs.onSurfaceVariant,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun PlanHeaderSection(planName: String, onNameChange: (String) -> Unit) {
    val cs = MaterialTheme.colorScheme
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
        Text(
            text = "Identificação",
            style = MaterialTheme.typography.labelMedium,
            color = cs.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = planName,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ex: Hipertrofia Avançada", color = cs.onSurfaceVariant.copy(alpha = 0.5f)) },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = cs.primary,
                unfocusedBorderColor = cs.outlineVariant
            )
        )
    }
}

@Composable
fun ExerciseListSection(
    dayName: String,
    exercises: List<Exercise>,
    isRestDay: Boolean, // Recebe do pai
    onToggleRestDay: (Boolean) -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Treino de $dayName",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Descanso", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.width(8.dp))
                androidx.compose.material3.Switch(
                    checked = isRestDay,
                    onCheckedChange = onToggleRestDay
                )
            }
        }

        if (isRestDay) {
            _root_ide_package_.com.example.presentation.screens.ui.planWorkout.RestDayPlaceholder()
        } else if (exercises.isEmpty()) {
            _root_ide_package_.com.example.presentation.screens.ui.planWorkout.EmptyExercisesPlaceholder(
                cs
            )
        } else {
            exercises.forEach { exercise ->
                _root_ide_package_.com.example.presentation.screens.ui.planWorkout.ExerciseItem(
                    exercise
                )
            }
        }
    }
}
@Composable
fun EmptyExercisesPlaceholder(cs: ColorScheme) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 80.dp), // Espaço extra para não bater no FAB
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícone Ilustrativo
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(cs.surfaceVariant.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.FitnessCenter,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = cs.onSurfaceVariant.copy(alpha = 0.3f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Textos de Apoio
        Text(
            text = "Nenhum exercício ainda",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = cs.onSurface
        )

        Text(
            text = "Toque no botão abaixo para montar\no treino deste dia.",
            style = MaterialTheme.typography.bodySmall,
            color = cs.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 18.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
@Composable
fun RestDayPlaceholder() {
    val cs = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = cs.secondaryContainer.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Spa, // Ícone de relaxamento
                contentDescription = null,
                tint = cs.secondary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "Dia de Recuperação",
                fontWeight = FontWeight.Bold,
                color = cs.onSecondaryContainer
            )
            Text(
                "O descanso é essencial para o crescimento muscular.",
                style = MaterialTheme.typography.bodySmall,
                color = cs.onSecondaryContainer.copy(alpha = 0.7f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}