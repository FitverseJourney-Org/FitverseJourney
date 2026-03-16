package com.example.presentation.screens.ui.modal.planWorkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Modelos ---

data class ExerciseLibraryItem(
    val id: String,
    val name: String,
    val muscleGroup: String,
    val icon: ImageVector = Icons.Rounded.FitnessCenter
)

// --- Mocks ---

val muscleGroups = listOf("Todos", "Peito", "Costas", "Pernas", "Braços", "Ombros")

val libraryExercises = listOf(
    ExerciseLibraryItem("1", "Supino Reto", "Peito"),
    ExerciseLibraryItem("2", "Puxada Alta", "Costas"),
    ExerciseLibraryItem("3", "Agachamento Livre", "Pernas"),
    ExerciseLibraryItem("4", "Rosca Direta", "Braços"),
    ExerciseLibraryItem("5", "Elevação Lateral", "Ombros"),
    ExerciseLibraryItem("6", "Leg Press 45", "Pernas"),
    ExerciseLibraryItem("7", "Tríceps Pulley", "Braços"),
    ExerciseLibraryItem("8", "Cadeira Extensora", "Costas"),
    ExerciseLibraryItem("9", "Rosca Inversa", "Braços"),
    ExerciseLibraryItem("10", "Abdominal", "Ombros"),
    ExerciseLibraryItem("11", "Crucifixo", "Peito"),
    ExerciseLibraryItem("12", "Remada Baixa", "Costas"),
    ExerciseLibraryItem("13", "Mesa Flexora", "Braços"),
    ExerciseLibraryItem("14", "Elevação Frontal", "Ombros"),
    ExerciseLibraryItem("15", "Leg Curl", "Pernas"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanExercisesScreen(
    onBack: () -> Unit,
    onAddExercise: (ExerciseLibraryItem) -> Unit
) {
    val cs = MaterialTheme.colorScheme
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(cs.surface)) {
                CenterAlignedTopAppBar(
                    title = { Text("Biblioteca", fontWeight = FontWeight.Black) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Rounded.ArrowBack, contentDescription = "Voltar")
                        }
                    },
                    windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )

                // Barra de Busca
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Pesquisar exercício...") },
                    leadingIcon = { Icon(Icons.Rounded.Search, null, tint = cs.primary) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Rounded.Close, null)
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = cs.primary,
                        unfocusedBorderColor = cs.outlineVariant
                    )
                )

                // Filtros Horizontais
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(muscleGroups) { group ->
                        val isSelected = selectedFilter == group
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilter = group },
                            label = { Text(group) },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = cs.primary,
                                selectedLabelColor = cs.onPrimary
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        val filteredList = libraryExercises.filter {
            (selectedFilter == "Todos" || it.muscleGroup == selectedFilter) &&
                    it.name.contains(searchQuery, ignoreCase = true)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (filteredList.isEmpty()) {
                item { EmptySearchState(cs) }
            } else {
                items(filteredList, key = { it.id }) { exercise ->
                    ExerciseSelectionCard(exercise, cs) {
                        onAddExercise(exercise)
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseSelectionCard(
    exercise: ExerciseLibraryItem,
    cs: ColorScheme,
    onAdd: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant.copy(alpha = 0.3f)),
        border = BorderStroke(1.dp, cs.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone Representativo
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(cs.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(exercise.icon, null, tint = cs.primary, modifier = Modifier.size(24.dp))
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = exercise.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = cs.onSurface
                )
                Text(
                    text = exercise.muscleGroup,
                    fontSize = 12.sp,
                    color = cs.onSurfaceVariant
                )
            }

            // Botão Adicionar
            IconButton(
                onClick = onAdd,
                modifier = Modifier.background(cs.primary, CircleShape).size(36.dp)
            ) {
                Icon(Icons.Rounded.Add, null, tint = cs.onPrimary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun EmptySearchState(cs: ColorScheme) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Rounded.History, null, modifier = Modifier.size(48.dp), tint = cs.outline)
        Spacer(Modifier.height(12.dp))
        Text("Nenhum exercício encontrado", color = cs.onSurfaceVariant)
    }
}