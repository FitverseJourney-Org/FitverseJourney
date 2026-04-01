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
import com.example.presentation.screens.widgets.FitverseHeader
import com.example.presentation.theme.DarkGamifiedColors

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


@Composable
fun PlanMealsListScreenPro(
    state: MealPlanScreenState,
    meals: List<MealGroup>,
    onBackClick: () -> Unit,
    onEditGoalClick: () -> Unit,
    onAddFoodClick: (mealId: String) -> Unit
) {
    val colors = MaterialTheme.colorScheme // Atalho para o sistema de cores

    val caloriesConsumed = meals.sumOf { it.totalCalories }
    val proteinConsumed = meals.flatMap { it.foods }.sumOf { it.protein }
    val carbsConsumed = meals.flatMap { it.foods }.sumOf { it.carbs }
    val fatConsumed = meals.flatMap { it.foods }.sumOf { it.fat }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            FitverseHeader(xp = 2000, level = 104)
        }

        // 1. HEADER "ENERGY CORE"
        item {
            GoalNutritionHeader(
                objective = state.objective,
                targetCalories = state.dailyCaloriesTarget,
                consumedCalories = caloriesConsumed
            )
        }

        // 2. MACROS
        item {
            PlannedMacrosRow(
                targetMacros = state.dailyMacros,
                consumedProtein = proteinConsumed,
                consumedCarbs = carbsConsumed,
                consumedFat = fatConsumed
            )
        }

        // 3. CARD DE META DE PESO
        item {
            WeightGoalCard(
                current = state.currentWeight,
                target = state.targetWeight,
                cs = colors
            )
        }

        item {
            Text(
                text = "REFEIÇÕES DO DIA",
                color = colors.onBackground, // Branco Puro
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }

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
    // containerColor usa o Background Neutro (#0A0B0F)
}
/* ========================================================================== */
/* COMPONENTES DE REFEIÇÃO                                                    */
/* ========================================================================== */
@Composable
fun GoalNutritionHeader(
    objective: String,
    targetCalories: Int,
    consumedCalories: Int
) {
    val colors = MaterialTheme.colorScheme
    val remaining = (targetCalories - consumedCalories).coerceAtLeast(0)
    val progress = if (targetCalories > 0) (consumedCalories.toFloat() / targetCalories).coerceIn(0f, 1f) else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "arcAnim"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = colors.surface.copy(alpha = 0.7f), // Surface Escuro (#16171D)
        border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "MISSION: ${objective.uppercase()}",
                style = MaterialTheme.typography.labelMedium,
                color = colors.secondary, // Azul Elétrico para Metas
                fontWeight = FontWeight.Black
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // RESTANTE
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$remaining", color = colors.onSurface, fontSize = 22.sp, fontWeight = FontWeight.Black)
                    Text("Restante", color = colors.onSurfaceVariant, fontSize = 12.sp) // Cinza Muted
                }

                // ARCO CENTRAL
                Box(modifier = Modifier.size(140.dp), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            color = colors.outline.copy(alpha = 0.1f),
                            startAngle = 140f,
                            sweepAngle = 260f,
                            useCenter = false,
                            style = Stroke(width = 24f, cap = StrokeCap.Round)
                        )
                        drawArc(
                            // Degradê entre Roxo (Primary) e Azul (Secondary)
                            brush = Brush.sweepGradient(listOf(colors.primary, colors.secondary)),
                            startAngle = 140f,
                            sweepAngle = 260f * animatedProgress,
                            useCenter = false,
                            style = Stroke(width = 24f, cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$consumedCalories", color = colors.onSurface, fontSize = 32.sp, fontWeight = FontWeight.Black)
                        Text("kcal", color = colors.onSurfaceVariant, fontSize = 12.sp)
                    }
                }

                // META
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$targetCalories", color = colors.onSurface, fontSize = 22.sp, fontWeight = FontWeight.Black)
                    Text("Meta", color = colors.onSurfaceVariant, fontSize = 12.sp)
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
    consumedFat: Int
) {
    val colors = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = colors.surface.copy(alpha = 0.5f), // Bom em containers de widgets
        border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Proteína: Roxo (Primary)
            MacroItem("Proteína", consumedProtein, targetMacros.protein, colors.primary)
            // Carbos: Azul (Secondary)
            MacroItem("Carbos", consumedCarbs, targetMacros.carbs, colors.secondary)
            // Gordura: Verde (Tertiary)
            MacroItem("Gordura", consumedFat, targetMacros.fat, colors.tertiary)
        }
    }
}

@Composable
fun MacroItem(label: String, consumed: Int, target: Int, color: Color) {
    val progress = if (target > 0) (consumed.toFloat() / target).coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(1000))

    Column(modifier = Modifier.width(90.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
        Spacer(Modifier.height(8.dp))

        // Track Glassmorphism
        Box(modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.05f))) {
            // Indicator Neon
            Box(modifier = Modifier.fillMaxWidth(animatedProgress).fillMaxHeight().clip(CircleShape).background(color))
        }

        Spacer(Modifier.height(8.dp))
        Text("$consumed / ${target}g", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun WeightGoalCard(current: Double, target: Double) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        // Usamos PrimarySoft aqui para diferenciar dos cards normais
        color = DarkGamifiedColors.PrimarySoft.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, DarkGamifiedColors.PrimarySoft.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            WeightMetricPro("Peso Atual", "$current kg")
            Box(modifier = Modifier.size(1.dp, 30.dp).background(DarkGamifiedColors.PrimarySoft.copy(alpha = 0.5f)))
            WeightMetricPro("Meta", "$target kg")
        }
    }
}

@Composable
fun WeightMetricPro(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = DarkGamifiedColors.Primary)
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Color.White)
    }
}

@Composable
fun MealGroupCard(
    mealGroup: MealGroup,
    icon: ImageVector,
    onAddFoodClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.secondary, // Ícones de categoria em Azul
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = mealGroup.name.uppercase(),
                    color = colors.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
            }

            // Badge de Calorias: Fundo em tom PrimarySoft
            Surface(
                color = colors.primaryContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${mealGroup.totalCalories} kcal",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = colors.primary, // Texto em Roxo Vibrante
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = colors.surfaceVariant.copy(alpha = 0.4f), // Card background (#16171D)
            border = BorderStroke(1.dp, colors.outline.copy(alpha = 0.15f))
        ) {
            Column {
                if (mealGroup.foods.isEmpty()) {
                    EmptyMealState(onAddFoodClick, colors)
                } else {
                    mealGroup.foods.forEachIndexed { index, food ->
                        FoodEntryRowPro(food = food, colors = colors)
                        if (index < mealGroup.foods.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = colors.outline.copy(alpha = 0.3f)
                            )
                        }
                    }

                    // Botão de Adição com Tertiary (Verde Neon)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddFoodClick() }
                            .background(colors.tertiary.copy(alpha = 0.08f))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+ ADICIONAR ITEM",
                            color = colors.tertiary,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun FoodEntryRowPro(
    food: FoodEntry,
    colors: ColorScheme
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            // Título: Branco Puro
            Text(
                text = food.name,
                color = colors.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            // Porção: Cinza Muted
            Text(
                text = food.portion,
                color = colors.onSurfaceVariant,
                fontSize = 13.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            // Calorias: Destaque em Negrito
            Text(
                text = "${food.calories} kcal",
                color = colors.onSurface,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp
            )
            // Macros: Roxo Vibrante (Primary) para gamificação
            Text(
                text = "P: ${food.protein}g | C: ${food.carbs}g | G: ${food.fat}g",
                color = colors.primary,
                fontWeight = FontWeight.Bold,
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

@Composable
fun EmptyMealState(
    onAddFoodClick: () -> Unit,
    cs: ColorScheme
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAddFoodClick() }
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Ícone com opacidade baixa para não poluir
            Icon(
                imageVector = Icons.Rounded.AddCircleOutline,
                contentDescription = null,
                tint = cs.onSurfaceVariant.copy(alpha = 0.2f),
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nenhum dado registrado",
                color = cs.onSurfaceVariant, // Cinza Muted
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "TOQUE PARA ADICIONAR ENERGIA",
                color = cs.tertiary, // Verde Neon para Call to Action
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                letterSpacing = 1.sp
            )
        }
    }
}