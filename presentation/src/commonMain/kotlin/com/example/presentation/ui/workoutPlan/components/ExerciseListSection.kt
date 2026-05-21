package org.fitverse.presentation.ui.workoutPlan.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.fitverse.presentation.ui.workout.Exercise

@Composable
fun ExerciseListSection(
    dayName: String,
    exercises: List<Exercise>,
    isRestDay: Boolean,
    onToggleRestDay: (Boolean) -> Unit,
    onUseTemplate: () -> Unit,
    colors: ColorScheme
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        // ── Header: nome do dia + toggle descanso ─────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text       = dayName.uppercase(),
                style      = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color      = colors.onSurface
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text       = "DESCANSO",
                    style      = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color      = if (isRestDay) colors.tertiary else colors.onSurfaceVariant
                )
                Spacer(Modifier.width(8.dp))
                Switch(
                    checked         = isRestDay,
                    onCheckedChange = onToggleRestDay,
                    colors          = SwitchDefaults.colors(
                        checkedThumbColor   = colors.tertiary,
                        checkedTrackColor   = colors.tertiary.copy(alpha = 0.2f),
                        uncheckedThumbColor = colors.onSurfaceVariant,
                        uncheckedTrackColor = colors.outline.copy(alpha = 0.2f)
                    )
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // ── Conteúdo condicional ──────────────────────────────────
        when {
            isRestDay           -> RestDayPlaceholder(colors = colors)
            exercises.isEmpty() -> EmptyExercisesPlaceholder(
                colors        = colors,
                onUseTemplate = onUseTemplate
            )
            else                -> exercises.forEach { value ->
                ExerciseItem(
                    exercise = value,
                    colors   = colors
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}
