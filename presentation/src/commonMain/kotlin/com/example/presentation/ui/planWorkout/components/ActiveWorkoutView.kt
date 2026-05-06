package com.example.presentation.ui.planWorkout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.workout.workout_plan.WorkoutCategory

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