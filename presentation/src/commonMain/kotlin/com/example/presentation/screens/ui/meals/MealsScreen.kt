package com.example.presentation.screens.ui.meals // Ajuste para o seu package

import FoodEntryRow
import MealGroup
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.LunchDining
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.planMeals.Macros
import com.example.presentation.screens.ui.planMeals.MealPlanScreenState

/* ========================================================================== */
/* MODELS & MOCKS                                                             */
/* ========================================================================== */
enum class MealPeriod(val label: String) {
    BREAKFAST("Breakfast"), LUNCH("Lunch"), SNACK("Snack"), DINNER("Dinner")
}

data class MealModel(
    val id: String,
    val title: String,
    val kcal: Int,
    val time: String,
    val period: com.example.presentation.screens.ui.meals.MealPeriod,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val items: List<String>
)

// --- Data Models Necessários ---
data class FoodEntry(
    val id: String, val name: String, val portion: String,
    val calories: Int, val protein: Int, val carbs: Int, val fat: Int
)

data class MealGroup(
    val id: String, val name: String, val foods: List<com.example.presentation.screens.ui.meals.FoodEntry> = emptyList()
) {
    val totalCalories: Int get() = foods.sumOf { it.calories }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanMealsListScreenPro(
    state: com.example.presentation.screens.ui.planMeals.MealPlanScreenState,
    meals: List<MealGroup>, // Adicionado para exibir as refeições
    onBackClick: () -> Unit,
    onEditGoalClick: () -> Unit,
    onAddFoodClick: (mealId: String) -> Unit
) {
    val cs = MaterialTheme.colorScheme

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header de Calorias e Objetivo (Adaptado do V2)
        item {
            _root_ide_package_.com.example.presentation.screens.ui.meals.GoalNutritionHeader(
                objective = state.objective,
                targetCalories = state.dailyCaloriesTarget,
                cs = cs
            )
        }

        // 2. Row de Macros Planejados
        item {
            _root_ide_package_.com.example.presentation.screens.ui.meals.PlannedMacrosRow(
                macros = state.dailyMacros,
                cs = cs
            )
        }

        // 3. Card de Meta de Peso (Estilo Pro)
        item {
            _root_ide_package_.com.example.presentation.screens.ui.meals.WeightGoalCard(
                current = state.currentWeight,
                target = state.targetWeight,
                cs = cs
            )
        }

        // 4. Lista de Planos
        item {
            Text(
                text = "Refeições do dia",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
        }

        items(meals, key = { it.id }) { mealGroup ->
            val icon = when {
                mealGroup.name.contains("Café", true) -> Icons.Outlined.BakeryDining
                mealGroup.name.contains("Almoço", true) || mealGroup.name.contains("Jantar", true) -> Icons.Outlined.LunchDining
                else -> Icons.Outlined.Nightlight
            }

            _root_ide_package_.com.example.presentation.screens.ui.meals.MealGroupCard(
                mealGroup = mealGroup,
                icon = icon,
                onAddFoodClick = { onAddFoodClick(mealGroup.id) }
            )
        }
    }
}
/* ========================================================================== */
/* COMPONENTES DE REFEIÇÃO                                                    */
/* ========================================================================== */

@Composable
fun MealGroupCard(mealGroup: MealGroup, icon: ImageVector, onAddFoodClick: () -> Unit) {
    Column(modifier = Modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = mealGroup.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "${mealGroup.totalCalories} kcal",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            border = if (mealGroup.foods.isEmpty()) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null
        ) {
            Column {
                if (mealGroup.foods.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddFoodClick() }
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Restaurant, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Toque para adicionar", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                } else {
                    mealGroup.foods.forEach { food ->
                        FoodEntryRow(food = food)
                    }
                    // Botão de adição compacto no rodapé do card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddFoodClick() }
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(8.dp))
                            Text("ADICIONAR MAIS", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
/* ========================================================================== */
/* COMPONENTES ADAPTADOS PARA O ESTILO PRO                                    */
/* ========================================================================== */

@Composable
fun GoalNutritionHeader(objective: String, targetCalories: Int, cs: ColorScheme) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        tonalElevation = 2.dp,
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = cs.primaryContainer.copy(alpha = 0.4f),
                    shape = CircleShape
                ) {
                    Text(
                        text = objective.uppercase(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = cs.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text("Meta Diária", color = cs.onSurfaceVariant, fontSize = 14.sp)
                Text("$targetCalories kcal", fontWeight = FontWeight.Black, fontSize = 32.sp, color = cs.onSurface)
            }

            // Canvas de Progresso Circular (Visual apenas para meta)
            Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                val strokeWidth = with(LocalDensity.current) { 8.dp.toPx() }
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = cs.outline.copy(alpha = 0.1f),
                        startAngle = 0f, sweepAngle = 360f, useCenter = false,
                        style = Stroke(width = strokeWidth)
                    )
                    drawArc(
                        brush = Brush.sweepGradient(listOf(cs.primary, cs.tertiary, cs.primary)),
                        startAngle = -90f, sweepAngle = 280f, // Valor fixo ilustrativo para meta
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
                Icon(Icons.Rounded.LocalFireDepartment, contentDescription = null, tint = cs.primary, modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Composable
fun PlannedMacrosRow(macros: com.example.presentation.screens.ui.planMeals.Macros, cs: ColorScheme) {
    // Estado para disparar a animação ao entrar na tela
    var animationPlayed by remember { mutableStateOf(false) }

    // Animação de 0 a 1 (0% a 100%)
    val progressAnimation by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1200, // Duração da animação
            easing = FastOutSlowInEasing
        ),
        label = "macroFillAnimation"
    )

    // Dispara a animação assim que o Composable é montado
    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listOf(
            Triple("Prot", "${macros.protein}g", cs.primary),
            Triple("Carb", "${macros.carbs}g", cs.secondary),
            Triple("Fat", "${macros.fat}g", cs.tertiary)
        ).forEach { (label, value, color) ->
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                color = cs.surface,
                border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Container da Barra (Track)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(color.copy(alpha = 0.1f))
                    ) {
                        // Barra Animada (Indicator)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressAnimation) // Aqui acontece a mágica
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(color)
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = label,
                        color = cs.onSurfaceVariant,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = value,
                        color = cs.onSurface,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun WeightGoalCard(current: Double, target: Double, cs: ColorScheme) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.secondaryContainer.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, cs.secondary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            _root_ide_package_.com.example.presentation.screens.ui.meals.WeightMetricPro(
                "Peso Atual",
                "$current kg",
                cs
            )
            Box(modifier = Modifier.size(1.dp, 30.dp).background(cs.outlineVariant))
            _root_ide_package_.com.example.presentation.screens.ui.meals.WeightMetricPro(
                "Meta",
                "$target kg",
                cs
            )
        }
    }
}

@Composable
fun WeightMetricPro(label: String, value: String, cs: ColorScheme) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = cs.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = cs.onSurface)
    }
}