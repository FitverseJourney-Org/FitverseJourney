package com.example.presentation.screens.ui.meals

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

data class MealPlanScreenState(
    val isLoading: Boolean = false,
    val objective: String = "Déficit Calórico",
    val currentWeight: Double = 85.0,
    val targetWeight: Double = 78.0,
    val dailyCaloriesTarget: Int = 2100,
    val dailyMacros: Macros = Macros(protein = 160, carbs = 200, fat = 73),
    val activePlan: MealPlanItem? = null, // Plano atual destacado
    val availablePlans: List<MealPlanItem> = emptyList() // Banco de reservas
)
data class MealPlanItem(
    val id: String,
    val title: String,
    val description: String,
    val focus: String = "Reeducação", // Ex: "Low Carb", "Cetogênica"
    val isActive: Boolean
)
data class Macros(val protein: Int, val carbs: Int, val fat: Int)
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

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0,0,0,0),
        topBar = {
            FitverseHeader(xp = 2000, level = 104)
        }
    ){
        LazyColumn(
            modifier = Modifier.padding(it).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                    color = colors.onBackground,
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
        // Sênior: Glassmorphism base
        color = colors.surface.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "MISSÃO: ${objective.uppercase()}",
                style = MaterialTheme.typography.labelMedium,
                color = colors.primary, // Primary para a missão geral
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
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
                    Text("Restante", color = colors.onSurfaceVariant.copy(alpha = 0.7f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // ARCO CENTRAL (O Protagonista)
                Box(modifier = Modifier.size(140.dp), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            color = colors.outline.copy(alpha = 0.3f),
                            startAngle = 140f,
                            sweepAngle = 260f,
                            useCenter = false,
                            style = Stroke(width = 24f, cap = StrokeCap.Round)
                        )
                        drawArc(
                            // Sênior: O gradiente termina em Tertiary para indicar "sucesso" conforme a barra enche
                            brush = Brush.sweepGradient(listOf(colors.primary, colors.tertiary)),
                            startAngle = 140f,
                            sweepAngle = 260f * animatedProgress,
                            useCenter = false,
                            style = Stroke(width = 24f, cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Sênior: O número mais importante da tela recebe o destaque Tertiary
                        Text("$consumedCalories", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Black)
                        Text("kcal", color = colors.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // META
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$targetCalories", color = colors.onSurface, fontSize = 22.sp, fontWeight = FontWeight.Black)
                    Text("Meta", color = colors.onSurfaceVariant.copy(alpha = 0.7f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
        color = colors.surface.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
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
                // Fundo sutil para o ícone
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(colors.primary.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ){
                    Icon(imageVector = icon, contentDescription = null, tint = colors.primary, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = mealGroup.name.uppercase(),
                    color = colors.onBackground,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }

            // Sênior: Badge de calorias com contraste forte, mas elegante
            Surface(
                color = colors.surface.copy(alpha = 0.7f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, colors.tertiary.copy(alpha = 0.2f))
            ) {
                Text(
                    text = "${mealGroup.totalCalories} kcal",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    color = colors.tertiary, // Destaque na soma total
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = colors.surface.copy(alpha = 0.5f), // Transparência um pouco maior para o grupo
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
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
                                color = Color.White.copy(alpha = 0.05f) // Divisor quase invisível
                            )
                        }
                    }

                    // Botão de Adição com visual premium
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddFoodClick() }
                            .background(colors.tertiary.copy(alpha = 0.05f))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+ ADICIONAR ITEM",
                            color = colors.tertiary,
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


@Composable
fun FoodEntryRowPro(food: FoodEntry, colors: ColorScheme) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = food.name,
                color = colors.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = food.portion,
                color = colors.onSurfaceVariant.copy(alpha = 0.8f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            // Sênior: Calorias da porção em Tertiary (o valor mais buscado pelo usuário)
            Text(
                text = "${food.calories} kcal",
                color = colors.tertiary,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            // Macros como subtítulo
            Text(
                text = "P: ${food.protein}g • C: ${food.carbs}g • G: ${food.fat}g",
                color = colors.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun WeightGoalCard(current: Double, target: Double, cs: ColorScheme) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.primary.copy(alpha = 0.3f),
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