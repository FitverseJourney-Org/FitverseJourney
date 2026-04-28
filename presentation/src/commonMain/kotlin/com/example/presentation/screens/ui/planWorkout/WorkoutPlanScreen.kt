package com.example.presentation.screens.ui.planWorkout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.workout.workout_plan.WorkoutCategory
import com.example.domain.models.workout.workout_plan.state.DailyWorkoutState
import com.example.expect.DateTimeFormatter.getDayOfWeek
import com.example.presentation.screens.ui.planWorkout.components.ActiveWorkoutView
import com.example.presentation.screens.ui.planWorkout.components.RestDayView
import com.example.presentation.screens.ui.planWorkout.components.WeekDaySelector
import com.example.presentation.screens.ui.planWorkout.components.getMockExercises
import com.example.presentation.screens.widgets.FitverseTopAppBar
import com.example.domain.models.workout.workout_plan.DayOfWeek


// --- TELA PRINCIPAL ---


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanScreen(
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
            DailyWorkoutState(DayOfWeek.MON, "Seg", WorkoutCategory("Treino A", "Peito e Tríceps", getMockExercises(1))),
            DailyWorkoutState(DayOfWeek.TUE, "Ter", WorkoutCategory("Treino B", "Costas e Bíceps", getMockExercises(2))),
            DailyWorkoutState(DayOfWeek.WED, "Qua", null),
            DailyWorkoutState(DayOfWeek.THU, "Qui", WorkoutCategory("Treino C", "Pernas Completas", getMockExercises(3))),
            DailyWorkoutState(DayOfWeek.FRI, "Sex", WorkoutCategory("Treino D", "Ombros e Abdômen", getMockExercises(4))),
            DailyWorkoutState(DayOfWeek.SAT, "Sáb", null),
            DailyWorkoutState(DayOfWeek.SUN, "Dom", null)
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
                        workout = selectedWorkoutState.workoutCategory!!,
                        onEditClick = { onEditWorkout(selectedWorkoutState.workoutCategory!!) },
                        colors = colors
                    )
                } else {
                    RestDayView(colors = colors)
                }
            }
        }
    }
}
