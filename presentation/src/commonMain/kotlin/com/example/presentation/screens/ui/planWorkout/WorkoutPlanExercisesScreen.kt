package com.example.presentation.screens.ui.planWorkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.modal.planWorkout.EmptySearchState
import com.example.presentation.screens.widgets.FitverseIconBack
import com.example.presentation.screens.widgets.FitverseTopAppBar
import com.example.presentation.theme.DarkGamifiedColors

// --- MODELOS ---

data class ExerciseLibraryItem(
    val id: String,
    val name: String,
    val muscleGroup: String,
    val icon: ImageVector = Icons.Rounded.FitnessCenter
)

// --- MOCKS (Limpados e Tipados) ---

val muscleGroups = listOf("Todos", "Peito", "Costas", "Pernas", "Braços", "Ombros")

val libraryExercises = listOf(
    ExerciseLibraryItem("1", "Supino Reto", "Peito"),
    ExerciseLibraryItem("2", "Puxada Alta", "Costas"),
    ExerciseLibraryItem("3", "Agachamento Livre", "Pernas"),
    ExerciseLibraryItem("4", "Rosca Direta", "Braços"),
    ExerciseLibraryItem("5", "Elevação Lateral", "Ombros"),
    ExerciseLibraryItem("6", "Leg Press 45", "Pernas"),
    ExerciseLibraryItem("7", "Tríceps Pulley", "Braços"),
    ExerciseLibraryItem("8", "Cadeira Extensora", "Pernas"),
    ExerciseLibraryItem("9", "Rosca Inversa", "Braços"),
    ExerciseLibraryItem("10", "Abdominal Infra", "Core"),
    ExerciseLibraryItem("11", "Crucifixo", "Peito"),
    ExerciseLibraryItem("12", "Remada Baixa", "Costas"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanExercisesScreen(
    onBack: () -> Unit,
    onAddExercise: (ExerciseLibraryItem) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }

    // Fundo padrão: Deep Neutral (#0A0B0F)
    val screenGradient = Brush.verticalGradient(
        colors = listOf(colors.surface, colors.background)
    )

    Scaffold(
        modifier = Modifier.background(screenGradient),
        containerColor = colors.background,
        topBar = {
            FitverseTopAppBar(
                title = "BIBLIOTECA",
                onBack = onBack,
            )
        }
    ) { padding ->
        val filteredList = libraryExercises.filter {
            (selectedFilter == "Todos" || it.muscleGroup == selectedFilter) &&
                    it.name.contains(searchQuery, ignoreCase = true)
        }

        Column(
            modifier = Modifier.padding(padding).padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                placeholder = {
                    Text("BUSCAR EXERCÍCIO...", color = colors.onSurfaceVariant.copy(alpha = 0.5f), fontWeight = FontWeight.Bold)
                },
                leadingIcon = { Icon(Icons.Rounded.Search, null, tint = colors.secondary) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Rounded.Close, null, tint = colors.onSurfaceVariant)
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colors.onSurface,
                    unfocusedTextColor = colors.onSurface,
                    focusedContainerColor = colors.surfaceVariant.copy(alpha = 0.5f),
                    unfocusedContainerColor = colors.surfaceVariant.copy(alpha = 0.3f),
                    focusedBorderColor = colors.secondary, // Azul ao buscar
                    unfocusedBorderColor = colors.outline.copy(alpha = 0.5f)
                )
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(muscleGroups) { group ->
                    val isSelected = selectedFilter == group
                    Surface(
                        modifier = Modifier.clickable { selectedFilter = group },
                        shape = RoundedCornerShape(12.dp),
                        // Se selecionado, brilha em Azul (Secondary)
                        color = if (isSelected) colors.secondary else colors.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) colors.secondary else colors.outline.copy(alpha = 0.3f)
                        )
                    ) {
                        Text(
                            text = group.uppercase(),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSelected) colors.onSecondary else colors.onSurfaceVariant
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredList.isEmpty()) {
                    item { EmptySearchState(colors) }
                } else {
                    if (filteredList.isEmpty()) {
                        item {
                            // Aqui é onde a função é utilizada
                            EmptySearchState(colors = colors)
                        }
                    }
                    else {
                        items(filteredList, key = { it.id }) { exercise ->
                            ExerciseSelectionCard(exercise, colors) { onAddExercise(exercise) }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun ExerciseSelectionCard(
    exercise: ExerciseLibraryItem,
    colors: ColorScheme,
    onAdd: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
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

@Composable
fun EmptySearchState(colors: ColorScheme) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Rounded.SearchOff,
            null,
            modifier = Modifier.size(64.dp),
            tint = colors.onSurfaceVariant.copy(alpha = 0.1f)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "NENHUMA HABILIDADE ENCONTRADA",
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            color = colors.onSurfaceVariant.copy(alpha = 0.3f)
        )
    }
}