package com.example.presentation.screens.ui.planWorkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.workout.workout_plan.state.DailyWorkoutState

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