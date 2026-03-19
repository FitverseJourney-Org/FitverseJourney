package com.example.presentation.screens.ui.planMeals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.compose.viewmodel.koinViewModel

data class Macros(val protein: Int, val carbs: Int, val fat: Int)

data class MealPlanItem(
    val id: String,
    val title: String,
    val description: String,
    val isActive: Boolean
)

data class MealPlanScreenState(
    val isLoading: Boolean = false,
    val objective: String = "Déficit Calórico",
    val currentWeight: Double = 85.0,
    val targetWeight: Double = 78.0,
    val dailyCaloriesTarget: Int = 2100,
    val dailyMacros: com.example.presentation.screens.ui.planMeals.Macros = _root_ide_package_.com.example.presentation.screens.ui.planMeals.Macros(
        protein = 160,
        carbs = 200,
        fat = 73
    ),
    val activePlans: List<com.example.presentation.screens.ui.planMeals.MealPlanItem> = emptyList()
)

// Exemplo de ViewModel (Clean Architecture)
class MealPlanViewModel : ViewModel() {
    private val _state = MutableStateFlow(_root_ide_package_.com.example.presentation.screens.ui.planMeals.MealPlanScreenState())
    val state: StateFlow<com.example.presentation.screens.ui.planMeals.MealPlanScreenState> = _state.asStateFlow()

    // Aqui você chamaria seus UseCases para carregar os dados do banco/API
}

// --- TELA BURRA (Stateless) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanMealsListScreen(
    state: com.example.presentation.screens.ui.planMeals.MealPlanScreenState,
    onBackClick: () -> Unit,
    onNewPlanClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Planos Alimentares") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    //edit
                    IconButton(onClick = { /* Lógica para editar */ }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onNewPlanClick()
                },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Criar Plano") },
                text = { Text("Novo Plano") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    _root_ide_package_.com.example.presentation.screens.ui.planMeals.GoalSummaryCard(
                        state = state
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Planos Salvos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(state.activePlans, key = { it.id }) { plan ->
                    _root_ide_package_.com.example.presentation.screens.ui.planMeals.MealPlanCard(
                        plan = plan
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

// --- COMPONENTES VISUAIS ---
@Composable
fun GoalSummaryCard(state: com.example.presentation.screens.ui.planMeals.MealPlanScreenState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Objetivo Atual",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        text = state.objective,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Text(
                    text = "${state.dailyCaloriesTarget} kcal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))

            // Sessão de Peso
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                _root_ide_package_.com.example.presentation.screens.ui.planMeals.GoalMetric(
                    label = "Peso Atual",
                    value = "${state.currentWeight} kg"
                )
                _root_ide_package_.com.example.presentation.screens.ui.planMeals.GoalMetric(
                    label = "Meta",
                    value = "${state.targetWeight} kg"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sessão de Macros
            Text(
                text = "Distribuição de Macros (Diário)",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            _root_ide_package_.com.example.presentation.screens.ui.planMeals.MacroBar(macros = state.dailyMacros)
        }
    }
}

@Composable
fun MacroBar(macros: com.example.presentation.screens.ui.planMeals.Macros) {
    val total = macros.protein + macros.carbs + macros.fat
    // Evita divisão por zero
    val pWeight = if (total > 0) macros.protein.toFloat() / total else 1f
    val cWeight = if (total > 0) macros.carbs.toFloat() / total else 1f
    val fWeight = if (total > 0) macros.fat.toFloat() / total else 1f

    Column {
        // Barra colorida proporcional
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
        ) {
            Box(modifier = Modifier.weight(pWeight).fillMaxHeight().background(Color(0xFFE53935))) // Vermelho - Proteína
            Box(modifier = Modifier.weight(cWeight).fillMaxHeight().background(Color(0xFF43A047))) // Verde - Carbo
            Box(modifier = Modifier.weight(fWeight).fillMaxHeight().background(Color(0xFFFFB300))) // Amarelo - Gordura
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Legendas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            _root_ide_package_.com.example.presentation.screens.ui.planMeals.MacroLegend(
                color = Color(
                    0xFFE53935
                ), label = "Prot", value = "${macros.protein}g"
            )
            _root_ide_package_.com.example.presentation.screens.ui.planMeals.MacroLegend(
                color = Color(
                    0xFF43A047
                ), label = "Carb", value = "${macros.carbs}g"
            )
            _root_ide_package_.com.example.presentation.screens.ui.planMeals.MacroLegend(
                color = Color(
                    0xFFFFB300
                ), label = "Gord", value = "${macros.fat}g"
            )
        }
    }
}

@Composable
fun MacroLegend(color: Color, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun GoalMetric(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun MealPlanCard(plan: com.example.presentation.screens.ui.planMeals.MealPlanItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plan.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = plan.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
            if (plan.isActive) {
                Badge(containerColor = Color(0xFF4CAF50)) {
                    Text("Ativo", modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                }
            }
        }
    }
}