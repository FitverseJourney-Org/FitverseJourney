package com.example.presentation.screens.ui.meals

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
import androidx.compose.ui.graphics.Color
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
    val period: MealPeriod,
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
    val id: String, val name: String, val foods: List<FoodEntry> = emptyList()
) {
    val totalCalories: Int get() = foods.sumOf { it.calories }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanMealsListScreenPro(
    state: MealPlanScreenState,
    meals: List<MealGroup>,
    onBackClick: () -> Unit,
    onEditGoalClick: () -> Unit,
    onAddFoodClick: (mealId: String) -> Unit
) {
    // Cores Fitverse
    val colorSurface = Color(0xFF171720)
    val cs = MaterialTheme.colorScheme

    // Suposição: Adicionamos valores de "Consumido" ao seu State para o cálculo
    // Se ainda não tiver no state, você pode passar valores de teste aqui
    val caloriesConsumed = meals.sumOf { it.totalCalories }
    val proteinConsumed = meals.flatMap { it.foods }.sumOf { it.protein }
    val carbsConsumed = meals.flatMap { it.foods }.sumOf { it.carbs }
    val fatConsumed = meals.flatMap { it.foods }.sumOf { it.fat }

    Scaffold(
        containerColor = Color.Transparent,
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. HEADER "FOOD LOG FOCUS" (ARCO)
            item {
                GoalNutritionHeader(
                    objective = state.objective,
                    targetCalories = state.dailyCaloriesTarget,
                    consumedCalories = caloriesConsumed,
                    cs = cs
                )
            }

            // 2. ROW DE MACROS LINEARES
            item {
                PlannedMacrosRow(
                    targetMacros = state.dailyMacros,
                    consumedProtein = proteinConsumed,
                    consumedCarbs = carbsConsumed,
                    consumedFat = fatConsumed,
                    cs = cs
                )
            }

            // 3. CARD DE META DE PESO
            item {
                WeightGoalCard(
                    current = state.currentWeight,
                    target = state.targetWeight,
                    cs = cs
                )
            }

            // 4. TÍTULO DA LISTA
            item {
                Text(
                    text = "Refeições do dia",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            // 5. LISTA DE REFEIÇÕES
            items(meals, key = { it.id }) { mealGroup ->
                val icon = when {
                    mealGroup.name.contains("Café", true) -> Icons.Rounded.BakeryDining
                    mealGroup.name.contains("Almoço", true) -> Icons.Rounded.LunchDining
                    mealGroup.name.contains("Jantar", true) -> Icons.Rounded.Nightlight
                    else -> Icons.Rounded.Restaurant
                }

                MealGroupCard(
                    mealGroup = mealGroup,
                    icon = icon,
                    onAddFoodClick = { onAddFoodClick(mealGroup.id) }
                )
            }
        }
    }
}
/* ========================================================================== */
/* COMPONENTES DE REFEIÇÃO                                                    */
/* ========================================================================== */
@Composable
fun GoalNutritionHeader(
    objective: String,
    targetCalories: Int,
    consumedCalories: Int,
    cs: ColorScheme
) {
    val remaining = (targetCalories - consumedCalories).coerceAtLeast(0)
    val progress = if (targetCalories > 0) (consumedCalories.toFloat() / targetCalories).coerceIn(0f, 1f) else 0f

    val colorNeon = Color(0xFFB6FF00)
    val colorTrack = Color(0xFF3F4458).copy(alpha = 0.3f)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "arcAnim"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFF2A2E3C),
        border = BorderStroke(1.dp, Color(0xFF3F4458).copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Food Log Focus - ${objective.uppercase()}",
                style = MaterialTheme.typography.labelMedium,
                color = colorNeon,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // RESTANTE
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$remaining", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
                    Text("Restante", color = Color.Gray, fontSize = 12.sp)
                }

                // ARCO CENTRAL
                Box(modifier = Modifier.size(140.dp), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            color = colorTrack,
                            startAngle = 140f,
                            sweepAngle = 260f,
                            useCenter = false,
                            style = Stroke(width = 30f, cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = colorNeon,
                            startAngle = 140f,
                            sweepAngle = 260f * animatedProgress,
                            useCenter = false,
                            style = Stroke(width = 30f, cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$consumedCalories", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black)
                        Text("Consumido", color = Color.Gray, fontSize = 12.sp)
                    }
                }

                // META
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$targetCalories", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
                    Text("Meta", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun PlannedMacrosRow(
    targetMacros: Macros,
    consumedProtein: Int,
    consumedCarbs: Int,
    consumedFat: Int,
    cs: ColorScheme
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF2A2E3C),
        border = BorderStroke(1.dp, Color(0xFF3F4458).copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MacroItem("Proteína", consumedProtein, targetMacros.protein, Color(0xFFFF5252))
            MacroItem("Gordura", consumedFat, targetMacros.fat, Color(0xFFFFD700))
            MacroItem("Carbos", consumedCarbs, targetMacros.carbs, Color(0xFF00E5FF))
        }
    }
}
@Composable
fun MacroItem(label: String, consumed: Int, target: Int, color: Color) {
    val progress = if (target > 0) (consumed.toFloat() / target).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(1000))

    Column(modifier = Modifier.width(90.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        // Track
        Box(modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(color.copy(alpha = 0.1f))) {
            // Indicator
            Box(modifier = Modifier.fillMaxWidth(animatedProgress).fillMaxHeight().clip(CircleShape).background(color))
        }

        Spacer(Modifier.height(8.dp))
        Text("$consumed / ${target}g", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun MealGroupCard(
    mealGroup: MealGroup,
    icon: ImageVector,
    onAddFoodClick: () -> Unit
) {
    // Paleta Fitverse
    val colorNeon = Color(0xFFB6FF00)
    val colorSurface = Color(0xFF2A2E3C)
    val colorOutline = Color(0xFF3F4458).copy(alpha = 0.4f)
    val textPrimary = Color.White
    val textSecondary = Color(0xFFA0A0A5)

    Column(modifier = Modifier.fillMaxWidth()) {
        // HEADER DA REFEIÇÃO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colorNeon, // Ícone em destaque neon
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = mealGroup.name,
                    color = textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold // Aumentado para melhor leitura
                )
            }

            // Badge de Calorias Totais
            Surface(
                color = colorNeon.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, colorNeon.copy(alpha = 0.2f))
            ) {
                Text(
                    text = "${mealGroup.totalCalories} kcal",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = colorNeon,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // CORPO DO CARD (LISTA DE ALIMENTOS)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp), // Cantos mais arredondados (Style Pro)
            color = colorSurface,
            border = BorderStroke(1.dp, colorOutline)
        ) {
            Column {
                if (mealGroup.foods.isEmpty()) {
                    // ESTADO VAZIO
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddFoodClick() }
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Rounded.Restaurant,
                                contentDescription = null,
                                tint = textSecondary.copy(alpha = 0.5f),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Nenhum alimento adicionado",
                                color = textSecondary,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Toque para planejar",
                                color = colorNeon,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                } else {
                    // LISTA DE ITENS
                    mealGroup.foods.forEachIndexed { index, food ->
                        FoodEntryRowPro(
                            food = food
                        )

                        // Divisor sutil entre alimentos
                        if (index < mealGroup.foods.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = colorOutline.copy(alpha = 0.2f)
                            )
                        }
                    }

                    // BOTÃO ADICIONAR (RODAPÉ DO CARD)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddFoodClick() }
                            .background(colorNeon.copy(alpha = 0.05f)) // Feedback visual leve
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = colorNeon
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "ADICIONAR ALIMENTO",
                                color = colorNeon,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun FoodEntryRowPro(food: FoodEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = food.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = food.portion,
                color = Color.Gray,
                fontSize = 13.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${food.calories} kcal",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp
            )
            // Opcional: Mostrar macros pequenos embaixo das kcal
            Text(
                text = "P: ${food.protein}g | C: ${food.carbs}g",
                color = Color(0xFFB6FF00).copy(alpha = 0.7f),
                fontSize = 11.sp
            )
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
            WeightMetricPro(
                "Peso Atual",
                "$current kg",
                cs
            )
            Box(modifier = Modifier.size(1.dp, 30.dp).background(cs.outlineVariant))
            WeightMetricPro(
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
