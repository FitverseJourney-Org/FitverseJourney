package com.example.presentation.screens.ui.planMeals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.presentation.screens.widgets.FitverseTopAppBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// --- DOMAIN / STATE ---
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
    val dailyMacros: Macros = Macros(protein = 160, carbs = 200, fat = 73),
    val activePlans: List<MealPlanItem> = emptyList()
)

class MealPlanViewModel : ViewModel() {
    private val _state = MutableStateFlow(MealPlanScreenState())
    val state: StateFlow<MealPlanScreenState> = _state.asStateFlow()
}

// --- TELA PRINCIPAL ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanMealsListScreen(
    state: MealPlanScreenState,
    onBack: () -> Unit,
    onNewPlanClick: () -> Unit,
    onPlanClick: (MealPlanItem) -> Unit // Nova ação para alterar/ver o plano
) {
    Scaffold(
        topBar = {
            FitverseTopAppBar(
                title = "Planos Alimentares", // Em title case para um visual mais limpo
                onBack = onBack,
                actions = {
                    IconButton(onClick = { /* Lógica para editar perfil/metas */ }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar Metas")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNewPlanClick,
                icon = { Icon(Icons.Filled.Add, contentDescription = "Criar Plano") },
                text = { Text("Novo Plano") },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp) // Flat FAB
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espaçamento automático entre os itens
            ) {
                item {
                    SectionHeader(title = "Visão Geral")
                    Spacer(modifier = Modifier.height(8.dp))
                    GoalSummaryCard(state = state)
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    SectionHeader(title = "Seus Planos")
                }

                items(state.activePlans, key = { it.id }) { plan ->
                    MealPlanCard(
                        plan = plan,
                        onClick = { onPlanClick(plan) }
                    )
                }
            }
        }
    }
}

// --- COMPONENTES VISUAIS ---

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
    )
}

@Composable
fun GoalSummaryCard(state: MealPlanScreenState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp), // Cantos mais arredondados, pegada moderna
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Flat design
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Objetivo atual",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = state.objective,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "${state.dailyCaloriesTarget} kcal",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sessão de Peso
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GoalMetric(label = "Peso Atual", value = "${state.currentWeight} kg")
                GoalMetric(label = "Meta", value = "${state.targetWeight} kg")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sessão de Macros
            Text(
                text = "Distribuição de Macros",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            MacroBar(macros = state.dailyMacros)
        }
    }
}

@Composable
fun MacroBar(macros: Macros) {
    val total = macros.protein + macros.carbs + macros.fat
    val pWeight = if (total > 0) macros.protein.toFloat() / total else 1f
    val cWeight = if (total > 0) macros.carbs.toFloat() / total else 1f
    val fWeight = if (total > 0) macros.fat.toFloat() / total else 1f

    // Usando as cores nativas do tema para harmonia perfeita
    val colorProtein = MaterialTheme.colorScheme.primary
    val colorCarbs = MaterialTheme.colorScheme.secondary
    val colorFat = MaterialTheme.colorScheme.tertiary

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(CircleShape)
        ) {
            Box(modifier = Modifier.weight(pWeight).fillMaxHeight().background(colorProtein))
            Box(modifier = Modifier.weight(cWeight).fillMaxHeight().background(colorCarbs))
            Box(modifier = Modifier.weight(fWeight).fillMaxHeight().background(colorFat))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MacroLegend(color = colorProtein, label = "Proteína", value = "${macros.protein}g")
            MacroLegend(color = colorCarbs, label = "Carbo", value = "${macros.carbs}g")
            MacroLegend(color = colorFat, label = "Gordura", value = "${macros.fat}g")
        }
    }
}

@Composable
fun MacroLegend(color: Color, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$label ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun GoalMetric(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun MealPlanCard(
    plan: MealPlanItem,
    onClick: () -> Unit
) {
    // OutlinedCard traz um ar mais limpo e leve do que o card preenchido
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }, // Adicionado interatividade
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (plan.isActive) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = CardDefaults.outlinedCardBorder(
            plan.isActive // Destaque na borda apenas se for o plano ativo
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = plan.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (plan.isActive) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ) {
                            Text("Ativo", modifier = Modifier.padding(horizontal = 4.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = plan.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Ícone de chevron para indicar que o usuário pode abrir para editar/ver detalhes
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Ver detalhes do plano",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}