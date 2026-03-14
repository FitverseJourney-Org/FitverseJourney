package com.example.presentation.screens.ui.main.nutrition // Ajuste para o seu package

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

/* ========================================================================== */
/* MAIN SCREEN                                                                */
/* ========================================================================== */
@Composable
fun NutritionScreenV3(
    targetCalories: Int = 2200,
    targetProtein: Int = 160,
    targetCarbs: Int = 200,
    targetFat: Int = 70,
    initialHydrationMl: Int = 1200,
    hydrationGoal: Int = 2500,
    onAddWater: (ml: Int) -> Unit = {},
    onAddMeal: () -> Unit = {},
    onOpenMeal: (MealModel) -> Unit = {},
    navigateToAddMeal: (MealPeriod) -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Estado da Lista de Refeições
    val meals = remember {
        mutableStateListOf(
            MealModel("m1", "Omelete de Claras e Aveia", 390, "07:30", MealPeriod.BREAKFAST, 32, 45, 10, listOf("4 Claras", "1 Ovo", "50g Aveia")),
            MealModel("m2", "Frango e Arroz Basmati", 530, "12:30", MealPeriod.LUNCH, 45, 65, 10, listOf("150g Frango", "200g Arroz")),
            MealModel("m3", "Shake de Whey", 310, "16:00", MealPeriod.SNACK, 26, 40, 4, listOf("30g Whey", "1 Banana")),
            MealModel("m4", "Salmão com Batata", 480, "20:30", MealPeriod.DINNER, 35, 35, 22, listOf("150g Salmão", "120g Batata Doce"))
        )
    }

    // Cálculos derivados dos Macros Consumidos
    val consumedKcal by remember { derivedStateOf { meals.sumOf { it.kcal } } }
    val consumedProtein by remember { derivedStateOf { meals.sumOf { it.protein } } }
    val consumedCarbs by remember { derivedStateOf { meals.sumOf { it.carbs } } }
    val consumedFat by remember { derivedStateOf { meals.sumOf { it.fat } } }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Resumo Diário (Calorias)
        item {
            NutritionHeaderPro(
                current = consumedKcal,
                target = targetCalories,
                cs = cs
            )
        }

        // 2. Detalhamento de Macros (Consumido vs Meta)
        item {
            MacrosRowPro(
                protein = consumedProtein, targetProtein = targetProtein,
                carbs = consumedCarbs, targetCarbs = targetCarbs,
                fat = consumedFat, targetFat = targetFat,
                cs = cs
            )
        }

        // 3. Progresso de Hidratação
        item {
            HydrationCardPro(
                initialConsumed = initialHydrationMl,
                goal = hydrationGoal,
                onAdd = onAddWater,
                cs = cs
            )
        }

        // 4. Lista e Gestão de Refeições
        item {
            NutritionMealsPro(
                meals = meals,
                onOpenMeal = onOpenMeal,
                navigateToAddMeal = { period ->
                    navigateToAddMeal(period)
                }
            )
        }

        // 5. Insights / Dicas
        item {
            NutritionTipCardPro(cs = cs)
            Spacer(Modifier.height(24.dp))
        }
    }
}

/* ========================================================================== */
/* COMPONENTS: HEADER & MACROS                                                */
/* ========================================================================== */
@Composable
fun NutritionHeaderPro(current: Int, target: Int, cs: ColorScheme) {
    val progress = (current.toFloat() / target.coerceAtLeast(1)).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(1000, easing = FastOutSlowInEasing), label = "progress")
    val isOverTarget = current >= target

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f)),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.LocalFireDepartment, contentDescription = null, tint = cs.primary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Daily Fuel", color = cs.onSurface, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                }
                Spacer(Modifier.height(8.dp))

                Text(
                    text = "${(target - current).coerceAtLeast(0)} kcal",
                    color = if (isOverTarget) cs.secondary else cs.onSurface,
                    fontWeight = FontWeight.Black,
                    fontSize = 28.sp
                )
                Text(
                    text = if (isOverTarget) "Goal reached!" else "Remaining today",
                    color = cs.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }

            Box(modifier = Modifier.size(86.dp), contentAlignment = Alignment.Center) {
                val strokeWidth = with(LocalDensity.current) { 8.dp.toPx() }
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = cs.outline.copy(alpha = 0.1f),
                        startAngle = -90f, sweepAngle = 360f, useCenter = false,
                        style = Stroke(width = strokeWidth)
                    )
                    drawArc(
                        brush = Brush.sweepGradient(listOf(cs.primary, cs.tertiary, cs.primary)),
                        startAngle = -90f, sweepAngle = animatedProgress * 360f, useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$current", color = cs.onSurface, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("eaten", color = cs.onSurfaceVariant, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun MacrosRowPro(
    protein: Int, targetProtein: Int,
    carbs: Int, targetCarbs: Int,
    fat: Int, targetFat: Int,
    cs: ColorScheme
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MacroCardPro("Protein", "${protein}g", targetProtein, cs.primary, Modifier.weight(1f), cs)
        MacroCardPro("Carbs", "${carbs}g", targetCarbs, cs.secondary, Modifier.weight(1f), cs)
        MacroCardPro("Fat", "${fat}g", targetFat, cs.tertiary, Modifier.weight(1f), cs)
    }
}

@Composable
fun MacroCardPro(title: String, value: String, target: Int, accentColor: Color, modifier: Modifier = Modifier, cs: ColorScheme) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f)),
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(accentColor))
                Spacer(Modifier.width(6.dp))
                Text(title, color = cs.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(8.dp))
            Text(value, color = cs.onSurface, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Text("of ${target}g", color = cs.onSurfaceVariant, fontSize = 10.sp)
        }
    }
}

/* ========================================================================== */
/* COMPONENTS: HYDRATION & OTHERS                                             */
/* ========================================================================== */
@Composable
fun HydrationCardPro(initialConsumed: Int, goal: Int, onAdd: (Int) -> Unit, cs: ColorScheme) {
    var consumed by rememberSaveable { mutableIntStateOf(initialConsumed) }
    val progress = (consumed.toFloat() / goal).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(800, easing = FastOutSlowInEasing), label = "hydroProgress")
    val isGoalReached = consumed >= goal

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, if (isGoalReached) cs.primary.copy(alpha = 0.5f) else cs.outline.copy(alpha = 0.1f)),
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(cs.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.WaterDrop, contentDescription = null, tint = cs.primary, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Hydration", color = cs.onSurface, fontWeight = FontWeight.Bold)
                    Text("$consumed / $goal ml", color = cs.onSurfaceVariant, fontSize = 12.sp)
                }
            }

            Spacer(Modifier.height(20.dp))

            Box(modifier = Modifier.fillMaxWidth().height(12.dp).clip(CircleShape).background(cs.outline.copy(alpha = 0.1f))) {
                Box(modifier = Modifier.fillMaxWidth(animatedProgress).fillMaxHeight().background(Brush.horizontalGradient(listOf(cs.primary, cs.secondary))))
            }

            Spacer(Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(150 to "Cup", 300 to "Glass", 500 to "Bottle").forEach { (ml, _) ->
                    Surface(
                        modifier = Modifier.weight(1f).clickable {
                            consumed = (consumed + ml).coerceAtMost(goal * 2)
                            onAdd(ml)
                        },
                        shape = RoundedCornerShape(14.dp),
                        color = cs.primary.copy(alpha = 0.08f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            Text("+$ml", color = cs.primary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("ml", color = cs.primary.copy(alpha = 0.7f), fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NutritionTipCardPro(cs: ColorScheme) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = cs.tertiary.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, cs.tertiary.copy(alpha = 0.2f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.TipsAndUpdates, contentDescription = null, tint = cs.tertiary, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Pro Tip", color = cs.tertiary, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                Spacer(Modifier.height(2.dp))
                Text("Include a source of protein in each meal to support muscle recovery.", color = cs.onSurface, fontSize = 13.sp)
            }
        }
    }
}

/* ========================================================================== */
/* COMPONENTS: MEALS & BOTTOM SHEET                                           */
/* ========================================================================== */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealSelectorSheet(onOptionSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = cs.surfaceColorAtElevation(4.dp),
        dragHandle = { BottomSheetDefaults.DragHandle(color = cs.onSurfaceVariant.copy(0.4f)) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 40.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Adicionar Refeição", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, modifier = Modifier.padding(bottom = 8.dp))
            MealOptionItem("Manual", "Digite os alimentos detalhadamente.", Icons.Rounded.EditNote, cs.primary) { onOptionSelected("MANUAL") }
            MealOptionItem("Foto", "Tire uma foto do seu prato.", Icons.Rounded.PhotoCamera, Color(0xFF4CAF50)) { onOptionSelected("FOTO") }
            MealOptionItem("IA", "Descreva sua refeição.", Icons.Rounded.AutoAwesome, Color(0xFF9C27B0)) { onOptionSelected("AI") }
        }
    }
}

@Composable
fun MealOptionItem(title: String, description: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = cs.surfaceVariant.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(color.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = cs.onSurface)
                Text(description, fontSize = 12.sp, color = cs.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun NutritionMealsPro(meals: List<MealModel>, onOpenMeal: (MealModel) -> Unit, navigateToAddMeal: (MealPeriod) -> Unit) {
    val grouped = meals.groupBy { it.period }
    val cs = MaterialTheme.colorScheme
    var showSheet by remember { mutableStateOf(false) }
    var selectedPeriod by remember { mutableStateOf<MealPeriod?>(null) }

    if (showSheet && selectedPeriod != null) {
        AddMealSelectorSheet(
            onOptionSelected = { type ->
                navigateToAddMeal(MealPeriod.valueOf(selectedPeriod!!.name))
                showSheet = false
            },
            onDismiss = { showSheet = false }
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        grouped.forEach { (period, periodMeals) ->
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(period.label, color = cs.onSurface, fontWeight = FontWeight.Black, fontSize = 18.sp)
                    Surface(
                        shape = CircleShape, color = cs.primary.copy(alpha = 0.1f),
                        modifier = Modifier.clickable { selectedPeriod = period; showSheet = true }
                    ) {
                        Icon(Icons.Rounded.Add, "Add", tint = cs.primary, modifier = Modifier.padding(6.dp).size(20.dp))
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    periodMeals.forEach { meal -> MealCardPro(meal, onOpenMeal, cs) }
                }
            }
        }
    }
}

@Composable
fun MealCardPro(meal: MealModel, onOpen: (MealModel) -> Unit, cs: ColorScheme) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f, label = "rotate")
    val elevation by animateDpAsState(if (expanded) 6.dp else 1.dp, label = "elevation")

    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { expanded = !expanded },
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, if (expanded) cs.primary.copy(alpha = 0.3f) else cs.outline.copy(alpha = 0.1f)),
        tonalElevation = elevation
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(52.dp).clip(RoundedCornerShape(16.dp)).background(cs.primary.copy(alpha = 0.08f)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.RestaurantMenu, null, tint = cs.primary, modifier = Modifier.size(24.dp))
                }
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text(meal.title, color = cs.onSurface, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Schedule, null, tint = cs.onSurfaceVariant, modifier = Modifier.size(12.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("${meal.time} • ${meal.kcal} kcal", color = cs.onSurfaceVariant, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
                Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(cs.onSurface.copy(alpha = 0.05f)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.KeyboardArrowDown, null, tint = cs.onSurfaceVariant, modifier = Modifier.rotate(rotation))
                }
            }
            AnimatedVisibility(visible = expanded, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
                Column(Modifier.padding(top = 20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        MiniMacroPro("Proteína", "${meal.protein}g", Color(0xFF3949AB), Modifier.weight(1f))
                        MiniMacroPro("Carbo", "${meal.carbs}g", Color(0xFFE65100), Modifier.weight(1f))
                        MiniMacroPro("Gordura", "${meal.fat}g", Color(0xFFB18D00), Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("Ingredientes", style = MaterialTheme.typography.labelLarge, color = cs.onSurface, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    meal.items.forEach { item ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
                            Box(Modifier.size(6.dp).clip(CircleShape).background(cs.primary.copy(alpha = 0.4f)))
                            Spacer(Modifier.width(12.dp))
                            Text(item, color = cs.onSurfaceVariant, fontSize = 14.sp)
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { onOpen(meal) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = cs.primary.copy(alpha = 0.1f), contentColor = cs.primary),
                        elevation = null
                    ) { Text("Ver detalhes da refeição", fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

@Composable
fun MiniMacroPro(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = color, fontWeight = FontWeight.Black, fontSize = 14.sp)
            Text(label, color = color.copy(alpha = 0.7f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}