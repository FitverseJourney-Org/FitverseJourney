package com.example.presentation.ui.planWorkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.workout.workout_plan.ExerciseLibraryItem
import com.example.domain.models.workout.workout_plan.mocks.libraryExercises
import com.example.domain.models.workout.workout_plan.mocks.muscleGroups
import com.example.presentation.ui.planWorkout.components.EmptySearchState
import com.example.presentation.ui.planWorkout.components.ExerciseSelectionCard
import com.example.presentation.widgets.FitverseTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanExercisesScreen(
    onBack: () -> Unit,
    onAddExercise: (ExerciseLibraryItem) -> Unit,
    onDetails: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }

    // Fundo padrão: Deep Neutral (#0A0B0F)
    Scaffold(
        modifier = Modifier,
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
                        modifier = Modifier,
                        shape = RoundedCornerShape(12.dp),
                        onClick = { selectedFilter = group },
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
                            ExerciseSelectionCard(
                                exercise, colors, onDetails = onDetails
                            ) { onAddExercise(exercise) }
                        }
                    }
                }
            }
        }

    }
}