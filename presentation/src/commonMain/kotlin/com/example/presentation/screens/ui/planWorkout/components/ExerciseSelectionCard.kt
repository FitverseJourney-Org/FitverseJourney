package com.example.presentation.screens.ui.planWorkout.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.workout.workout_plan.ExerciseLibraryItem

@Composable
fun ExerciseSelectionCard(
    exercise: ExerciseLibraryItem,
    colors: ColorScheme,
    onDetails: () -> Unit,
    onAdd: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        onClick = onDetails,
        color = colors.surfaceVariant.copy(alpha = 0.6f), // Card background (#16171D)
        border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone com Aura em Azul (Secondary)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(colors.secondary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    exercise.icon,
                    null,
                    tint = colors.secondary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(
                    text = exercise.name.uppercase(),
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = colors.onSurface
                )
                Text(
                    text = exercise.muscleGroup.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurfaceVariant // Cinza Muted
                )
            }

            // Botão "Adicionar": Roxo (Primary) para combinar com a FAB da tela anterior
            IconButton(
                onClick = onAdd,
                modifier = Modifier
                    .background(colors.primary, RoundedCornerShape(12.dp))
                    .size(40.dp)
            ) {
                Icon(
                    Icons.Rounded.Add,
                    null,
                    tint = colors.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}