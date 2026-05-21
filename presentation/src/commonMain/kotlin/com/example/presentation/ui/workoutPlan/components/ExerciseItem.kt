package org.fitverse.presentation.ui.workoutPlan.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.ui.workout.Exercise

@Composable
fun ExerciseItem(exercise: Exercise, colors: ColorScheme) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp),
        color = colors.surfaceVariant.copy(alpha = 0.5f), // Card background (#16171D)
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(colors.primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.FitnessCenter, null, tint = colors.primary, modifier = Modifier.size(22.dp))
            }

            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(exercise.title, fontWeight = FontWeight.Black, fontSize = 14.sp, color = colors.onSurface)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Padrão de status: Primary para Sets, Secondary para Reps
                    Text("${exercise.sets} SETS", color = colors.primary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = exercise.reps.toString(), color = colors.secondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Icon(Icons.Rounded.DragHandle, null, tint = colors.onSurfaceVariant.copy(alpha = 0.3f))
        }
    }
}