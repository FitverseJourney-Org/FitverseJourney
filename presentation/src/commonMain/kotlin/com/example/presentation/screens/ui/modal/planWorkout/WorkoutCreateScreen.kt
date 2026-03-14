package com.example.presentation.screens.ui.modal.planWorkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.transparent
import kotlinx.coroutines.launch

data class WorkoutCategory(
    val name: String, // "Treino A", "Treino B"
    val description: String, // "Peito e Tríceps"
    val exercises: MutableList<ExerciseModel> = mutableStateListOf()
)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WorkoutCreateScreen(
    navigateBack: () -> Unit,
    onDismiss: () -> Unit = {},
    onSaveWorkout: (List<WorkoutCategory>) -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme

    // Lista de Categorias com exercícios pré-definidos
    val workoutCategories = remember {
        mutableStateListOf(
            WorkoutCategory(
                name = "Treino A",
                description = "Peito e Tríceps",
                exercises = mutableStateListOf(
                    ExerciseModel("1", "Supino Reto", 4, "10 reps"),
                    ExerciseModel("2", "Supino Inclinado H.", 3, "12 reps"),
                    ExerciseModel("3", "Crossover High-to-Low", 3, "15 reps"),
                    ExerciseModel("4", "Tríceps Pulley", 4, "12 reps")
                )
            ),
            WorkoutCategory(
                name = "Treino B",
                description = "Costas e Bíceps",
                exercises = mutableStateListOf(
                    ExerciseModel("5", "Puxada Pulley", 4, "10-12 reps"),
                    ExerciseModel("6", "Remada Curvada", 3, "10 reps"),
                    ExerciseModel("7", "Remada Unilateral", 3, "12 reps"),
                    ExerciseModel("8", "Rosca Direta W", 4, "10 reps"),
                    ExerciseModel("9", "Rosca Martelo", 3, "12 reps")
                )
            ),
            WorkoutCategory(
                name = "Treino C",
                description = "Pernas Completo",
                exercises = mutableStateListOf(
                    ExerciseModel("10", "Agachamento Livre", 4, "8-10 reps"),
                    ExerciseModel("11", "Leg Press 45°", 3, "12-15 reps"),
                    ExerciseModel("12", "Extensora", 4, "Falha"),
                    ExerciseModel("13", "Mesa Flexora", 4, "12 reps"),
                    ExerciseModel("14", "Panturrilha em Pé", 4, "20 reps")
                )
            )
        )
    }

    // Estado para controlar qual aba está selecionada
    val pagerState = rememberPagerState { workoutCategories.size }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(transparent)) {
                TopAppBar(
                    title = { Text("Build New Plan", fontWeight = FontWeight.Black, fontSize = 16.sp) },
                    windowInsets = WindowInsets(0.dp),
                    navigationIcon = {
                        IconButton(onClick = onDismiss) { Icon(Icons.Rounded.ChevronLeft, null) }
                    },
                    actions = {
                        TextButton(onClick = { onSaveWorkout(workoutCategories) }) {
                            Text("Save All", color = cs.primary, fontWeight = FontWeight.Bold)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )

                // Barra de Abas (Tabs) para selecionar A, B ou C
                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = Color.Transparent,
                    edgePadding = 16.dp,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            color = cs.primary
                        )
                    }
                ) {
                    workoutCategories.forEachIndexed { index, category ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(category.name, fontWeight = FontWeight.Bold)
                                    Text(category.description, fontSize = 10.sp, color = cs.onSurfaceVariant)
                                }
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* Lógica para adicionar na categoria atual: workoutCategories[pagerState.currentPage] */ },
                containerColor = cs.primary,
                contentColor = cs.onPrimary,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Rounded.Add, null) },
                text = { Text("Add to ${workoutCategories[pagerState.currentPage].name}", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        // Pager que permite deslizar entre os treinos A, B e C
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(padding).fillMaxSize()
        ) { pageIndex ->
            val currentCategory = workoutCategories[pageIndex]

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
            ) {
                // Insights automáticos para a categoria específica
                item {
                    WorkoutInsightsCard(
                        exerciseCount = currentCategory.exercises.size,
                        estTime = currentCategory.exercises.size * 10,
                        cs = cs
                    )
                }

                item {
                    Text(
                        "Exercises for ${currentCategory.name}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = cs.primary
                    )
                }

                if (currentCategory.exercises.isEmpty()) {
                    item { EmptyExercisesPlaceholder(cs) }
                } else {
                    items(currentCategory.exercises) { exercise ->
                        ExerciseDraftCard(exercise, cs) {
                            currentCategory.exercises.remove(exercise)
                        }
                    }
                }
            }
        }
    }
}

/* -------------------------------------------------------------------------- */
/* COMPONENTES DE APOIO                                                       */
/* -------------------------------------------------------------------------- */

@Composable
fun WorkoutInsightsCard(exerciseCount: Int, estTime: Int, cs: ColorScheme) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.primary.copy(alpha = 0.05f),
        border = BorderStroke(1.dp, cs.primary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InsightItem("Exercises", "$exerciseCount", Icons.Rounded.FitnessCenter, cs)
            VerticalDivider(modifier = Modifier.height(40.dp), color = cs.outline.copy(alpha = 0.1f))
            InsightItem("Est. Time", "$estTime min", Icons.Rounded.Timer, cs)
            VerticalDivider(modifier = Modifier.height(40.dp), color = cs.outline.copy(alpha = 0.1f))
            InsightItem("Intensity", "Medium", Icons.Rounded.Whatshot, cs)
        }
    }
}

@Composable
fun InsightItem(label: String, value: String, icon: ImageVector, cs: ColorScheme) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = cs.primary, modifier = Modifier.size(16.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = cs.onSurface)
        Text(label, fontSize = 10.sp, color = cs.onSurfaceVariant)
    }
}

@Composable
fun ExerciseDraftCard(exercise: ExerciseModel, cs: ColorScheme, onRemove: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Miniatura ou Ícone do Exercício
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(cs.secondary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.FitnessCenter, null, tint = cs.secondary)
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(exercise.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("${exercise.sets} sets x ${exercise.reps} reps", fontSize = 12.sp, color = cs.onSurfaceVariant)
            }

            IconButton(onClick = onRemove) {
                Icon(Icons.Rounded.DeleteOutline, null, tint = cs.error.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
fun EmptyExercisesPlaceholder(cs: ColorScheme) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Rounded.Layers, null,
            modifier = Modifier.size(48.dp),
            tint = cs.outline.copy(alpha = 0.3f)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "No exercises added yet",
            color = cs.onSurfaceVariant,
            fontSize = 14.sp
        )
    }
}

/* --- Models Mock --- */
data class ExerciseModel(val id: String, val name: String, val sets: Int, val reps: String)
data class WorkoutPlan(val name: String, val exercises: List<ExerciseModel>)