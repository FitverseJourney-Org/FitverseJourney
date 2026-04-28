package com.example.presentation.screens.ui.planWorkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.workout.workout_plan.DayOfWeek
import com.example.domain.models.workout.workout_plan.Exercise
import com.example.presentation.screens.ui.planWorkout.components.DaySelector
import com.example.presentation.screens.ui.planWorkout.components.ExerciseListSection
import com.example.presentation.screens.ui.planWorkout.components.PlanHeaderSection
import com.example.presentation.screens.widgets.FitverseIconBack
import com.example.presentation.screens.widgets.FitverseTopAppBar

// --- MODELOS ---



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanBuilderScreen(
    onBack: () -> Unit,
    toAddExercises: () -> Unit,
    savedPlan: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var planName by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf(DayOfWeek.MON) }

    val restDaysMap = remember { mutableStateMapOf<DayOfWeek, Boolean>() }
    val workoutMap = remember { mutableStateMapOf<DayOfWeek, List<Exercise>>() }
    val isCurrentDayRest = restDaysMap[selectedDay] ?: false

    // Gradiente dinâmico: Surface (#16171D) para Background (#0A0B0F)
    val screenGradient = Brush.verticalGradient(
        colors = listOf(colors.surface, colors.background)
    )

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            FitverseTopAppBar(
                title = "MONTAR MISSÃO",
                onBack = onBack,
                actions = {
                    TextButton(onClick = {
                        savedPlan()
                    }) {
                        // Action principal em Secondary (Azul)
                        Text("SALVAR", color = colors.secondary, fontWeight = FontWeight.Black)
                    }
                }
            )
        },
        floatingActionButton = {
            if (!isCurrentDayRest) {
                ExtendedFloatingActionButton(
                    onClick = toAddExercises,
                    containerColor = colors.primary, // Roxo Vibrante para ação principal
                    contentColor = Color.Black,
                    shape = RoundedCornerShape(16.dp),
                    icon = { Icon(Icons.Rounded.Add, null) },
                    text = { Text("ADICIONAR EXERCÍCIO", fontWeight = FontWeight.Black) }
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
            PlanHeaderSection(planName, colors) { planName = it }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "CRONOGRAMA SEMANAL",
                style = MaterialTheme.typography.labelLarge,
                color = colors.secondary, // Azul para subtítulos de seção
                modifier = Modifier.padding(horizontal = 20.dp),
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            DaySelector(selectedDay, colors) { selectedDay = it }

            ExerciseListSection(
                dayName = selectedDay.fullName,
                exercises = workoutMap[selectedDay] ?: emptyList(),
                isRestDay = isCurrentDayRest,
                onToggleRestDay = { restDaysMap[selectedDay] = it },
                colors = colors
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}