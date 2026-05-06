package com.example.presentation.ui.planWorkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.workout.workout_plan.ExerciseLibraryItem
import com.example.presentation.ui.planWorkout.components.MuscleTag
import com.example.presentation.widgets.FitverseTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPLanExerciseDetailsScreen(
    exercise: ExerciseLibraryItem,
    onBack: () -> Unit,
    onAddExercise: (ExerciseLibraryItem) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        topBar = {
            FitverseTopAppBar(
                title = "DETALHES",
                onBack = onBack
            )
        },
        bottomBar = {
            // Botão Fixo no Rodapé com Gradiente ou Cor Sólida Vibrante
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colors.background,
                tonalElevation = 8.dp
            ) {
                Button(
                    onClick = { onAddExercise(exercise) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                ) {
                    Icon(Icons.Rounded.Add, null)
                    Spacer(Modifier.width(8.dp))
                    Text("ADICIONAR AO TREINO", fontWeight = FontWeight.Black)
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. Visualizer (GIF / Imagem)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant.copy(alpha = 0.3f)),
                    border = BorderStroke(1.dp, colors.secondary.copy(alpha = 0.2f))
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        // Aqui você usaria o Coil ou KMP-Image-Loader para carregar a URL/GIF
                        Icon(
                            imageVector = exercise.icon,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp),
                            tint = colors.secondary.copy(alpha = 0.5f)
                        )

                        // Badge de Categoria Neon
                        Surface(
                            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
                            color = colors.secondary,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                exercise.muscleGroup.uppercase(),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = colors.onSecondary
                            )
                        }
                    }
                }
            }

            // 2. Título e Informações Básicas
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = exercise.name.uppercase(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = colors.onSurface
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Info, null, modifier = Modifier.size(16.dp), tint = colors.primary)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "TÉCNICA DE EXECUÇÃO",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.primary
                        )
                    }
                }
            }
            // 3. Descrição / Instruções
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = colors.surface.copy(alpha = 0.7f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                ) {
                    Text(
                        text = exercise.description,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant,
                        lineHeight = 22.sp
                    )
                }
            }

            // 4. Músculos Trabalhados (Mini View)
            item {
                Text(
                    "MÚSCULOS ATIVADOS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                // Exemplo de tags para sinergistas
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MuscleTag(name = exercise.muscleGroup, isPrimary = true)
                    if (exercise.muscleGroup == "Peito") MuscleTag(
                        name = "Tríceps",
                        isPrimary = false
                    )
                }
            }
        }
    }
}

