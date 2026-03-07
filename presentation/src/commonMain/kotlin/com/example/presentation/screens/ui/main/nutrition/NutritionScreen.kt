package com.example.presentation.screens.ui.main.nutrition

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.core.utils.TimeProvider.nowLocalTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Clock as KClock // kept only if you use kotlin.time APIs elsewhere

@Composable
fun NutritionScreenV3(
    modifier: Modifier = Modifier,
    calories: Int = 1450,
    targetCalories: Int = 2200,
    protein: Int = 95,
    carbs: Int = 180,
    fat: Int = 60,
    hydrationMl: Int = 1200,
    hydrationGoal: Int = 2500,
    onAddWater: (ml: Int) -> Unit = {},
    onAddMeal: () -> Unit = {},
    onOpenMeal: (MealModel) -> Unit = {}
) {
    fun sampleMeals(): List<MealModel> = listOf(
        MealModel(
            id = "1",
            title = "Oatmeal with Banana",
            kcal = 420,
            time = "08:15",
            period = MealPeriod.BREAKFAST,
            protein = 22,
            carbs = 55,
            fat = 10,
            items = listOf("Oats", "Banana", "Whey protein", "Peanut butter")
        ),
        MealModel(
            id = "2",
            title = "Eggs & Whole Toast",
            kcal = 380,
            time = "09:00",
            period = MealPeriod.BREAKFAST,
            protein = 28,
            carbs = 30,
            fat = 16,
            items = listOf("2 eggs", "Whole grain bread", "Olive oil")
        ),
        MealModel(
            id = "3",
            title = "Grilled Chicken Bowl",
            kcal = 620,
            time = "13:10",
            period = MealPeriod.LUNCH,
            protein = 42,
            carbs = 60,
            fat = 14,
            items = listOf("Grilled chicken breast", "Brown rice", "Broccoli", "Olive oil")
        ),
        MealModel(
            id = "4",
            title = "Salmon & Sweet Potato",
            kcal = 700,
            time = "14:00",
            period = MealPeriod.LUNCH,
            protein = 38,
            carbs = 50,
            fat = 26,
            items = listOf("Salmon", "Sweet potato", "Asparagus")
        ),
        MealModel(
            id = "5",
            title = "Greek Yogurt & Honey",
            kcal = 260,
            time = "16:30",
            period = MealPeriod.SNACK,
            protein = 20,
            carbs = 28,
            fat = 6,
            items = listOf("Greek yogurt", "Honey", "Granola")
        ),
        MealModel(
            id = "6",
            title = "Protein Shake",
            kcal = 220,
            time = "17:45",
            period = MealPeriod.SNACK,
            protein = 30,
            carbs = 15,
            fat = 3,
            items = listOf("Whey protein", "Almond milk", "Ice")
        ),
        MealModel(
            id = "7",
            title = "Lean Beef & Rice",
            kcal = 650,
            time = "20:10",
            period = MealPeriod.DINNER,
            protein = 40,
            carbs = 55,
            fat = 18,
            items = listOf("Lean beef", "White rice", "Mixed vegetables")
        ),
        MealModel(
            id = "8",
            title = "Chicken Salad",
            kcal = 480,
            time = "21:00",
            period = MealPeriod.DINNER,
            protein = 35,
            carbs = 20,
            fat = 22,
            items = listOf("Chicken breast", "Lettuce", "Tomatoes", "Avocado", "Olive oil")
        )
    )

    val cs = MaterialTheme.colorScheme
    val textPrimary = cs.onSurface
    val textSecondary = cs.onSurface.copy(alpha = 0.7f)

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            NutritionHeaderAnimated(
                current = calories,
                target = targetCalories,
                cs = cs,
                textPrimary = textPrimary,
                textSecondary = textSecondary
            )
        }
        item {
            MacrosRow(
                protein = protein,
                carbs = carbs,
                fat = fat,
                cs = cs,
                textSecondary = textSecondary
            )
        }
        item {
            HydrationCardV3(
                initialConsumed = hydrationMl,
                goal = hydrationGoal,
                onAdd = onAddWater,
                cs = cs,
                textSecondary = textSecondary
            )
        }
        item {
            NutritionMealsV2(
                meals = sampleMeals(),
                onOpenMeal = onOpenMeal,
                onAddMeal = { /* forward if needed */ }
            )
        }
        item { NutritionTipCardV2(cs = cs, textSecondary = textSecondary) }
    }
}

/* ----- Header with circular animated progress ----- */
@Composable
fun NutritionHeaderAnimated(
    current: Int,
    target: Int,
    cs: ColorScheme,
    textPrimary: androidx.compose.ui.graphics.Color,
    textSecondary: androidx.compose.ui.graphics.Color
) {
    val progress = (current.toFloat() / target.coerceAtLeast(1)).coerceIn(0f, 2f)
    val animated by animateFloatAsState(targetValue = progress, animationSpec = tween(700))
    val isOverTarget = current > target

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Fuel your body today",
                color = textPrimary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
            Text(
                text = if (!isOverTarget) "You’re on track — keep fueling your goals." else "You’ve reached your goal — balance wisely.",
                color = textSecondary,
                fontSize = 13.sp
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(modifier = Modifier.size(96.dp), contentAlignment = Alignment.Center) {
                val stroke = with(LocalDensity.current) { 10.dp.toPx() }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = cs.onSurface.copy(alpha = 0.06f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = stroke)
                    )
                    drawArc(
                        color = if (!isOverTarget) cs.primary else cs.secondary,
                        startAngle = -90f,
                        sweepAngle = (animated * 360f).coerceAtMost(360f),
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$current",
                        color = textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(text = "of $target kcal", color = textSecondary, fontSize = 11.sp)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (!isOverTarget) "Calories remaining" else "Calories exceeded",
                    color = textSecondary,
                    fontSize = 12.sp
                )
                Text(
                    text = if (!isOverTarget) "${target - current} kcal" else "+${current - target} kcal",
                    color = if (!isOverTarget) cs.primary else cs.secondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

/* ----- Macros row (compact cards) ----- */
@Composable
fun MacrosRow(
    protein: Int,
    carbs: Int,
    fat: Int,
    cs: ColorScheme,
    textSecondary: androidx.compose.ui.graphics.Color
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MacroCard(
            title = "Protein",
            value = "$protein g",
            accent = cs.primary,
            modifier = Modifier.weight(1f),
            cs = cs,
            textSecondary = textSecondary
        )
        MacroCard(
            title = "Carbs",
            value = "$carbs g",
            accent = cs.secondary,
            modifier = Modifier.weight(1f),
            cs = cs,
            textSecondary = textSecondary
        )
        MacroCard(
            title = "Fat",
            value = "$fat g",
            accent = cs.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.weight(1f),
            cs = cs,
            textSecondary = textSecondary
        )
    }
}

@Composable
fun MacroCard(
    title: String,
    value: String,
    accent: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    cs: ColorScheme,
    textSecondary: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box {
            Box(modifier = Modifier.width(4.dp).fillMaxHeight().background(accent))
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 14.dp,
                    bottom = 14.dp,
                    end = 12.dp
                ), verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = title.uppercase(),
                    color = textSecondary,
                    fontSize = 11.sp,
                    letterSpacing = 0.6.sp
                )
                Text(text = value, color = accent, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HydrationCardV3(
    initialConsumed: Int,
    goal: Int,
    modifier: Modifier = Modifier,
    onAdd: (Int) -> Unit = {},
    cs: ColorScheme,
    textSecondary: androidx.compose.ui.graphics.Color
) {
    val logs = remember { mutableStateListOf<HydrationLog>() }
    var consumed by rememberSaveable { mutableStateOf(initialConsumed.coerceAtLeast(0)) }
    val percent = (consumed.toFloat() / goal).coerceIn(0f, 1f)
    val animatedPercent by animateFloatAsState(
        targetValue = percent,
        animationSpec = tween(700, easing = FastOutSlowInEasing)
    )

    var openLog by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val nowFormatter = remember { nowLocalTime() }

    val pulse = rememberInfiniteTransition()
    val pulseScale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = if (percent >= 1f) 1.06f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(44.dp).clip(CircleShape)
                            .background(cs.surface.copy(alpha = 0.06f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalDrink,
                            contentDescription = "Hydration",
                            tint = cs.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text("Hydration", color = cs.onSurface, fontWeight = FontWeight.SemiBold)
                        Text("${consumed} / $goal ml", color = textSecondary, fontSize = 12.sp)
                    }
                }

                TextButton(onClick = { openLog = !openLog }) {
                    Text(if (openLog) "Hide log" else "View log", color = textSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(110.dp).scale(pulseScale),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val stroke = 12f
                        val radius = size.minDimension / 2f
                        drawArc(
                            color = cs.onSurface.copy(alpha = 0.06f),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = stroke)
                        )
                        drawArc(
                            color = cs.primary,
                            startAngle = -90f,
                            sweepAngle = (animatedPercent * 360f),
                            useCenter = false,
                            style = Stroke(width = stroke, cap = Stroke.DefaultCap)
                        )
                        drawCircle(
                            color = cs.primary.copy(alpha = 0.06f),
                            radius = radius * 0.6f,
                            center = Offset(size.width / 2f, size.height / 2f)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "${(animatedPercent * 100).toInt()}%",
                            color = cs.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${(goal - consumed).coerceAtLeast(0)} ml left",
                            color = textSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(100, 200, 300).forEach { ml ->
                            QuickAddPill(
                                ml = ml,
                                cs = cs,
                                onClick = {
                                    consumed = (consumed + ml).coerceAtMost(goal)
                                    logs.add(0, HydrationLog(amount = ml, time = nowFormatter))
                                    onAdd(ml)
                                    scope.launch { delay(140) }
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = { /* open full hydration screen */ },
                            colors = ButtonDefaults.buttonColors(containerColor = cs.primary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text("Log water", color = cs.onPrimary)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Tip: sip through the day",
                            color = textSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            AnimatedVisibility(visible = openLog) {
                Column {
                    if (logs.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(64.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(cs.onSurface.copy(alpha = 0.02f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No entries yet — add some water 💧", color = textSecondary)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth().heightIn(max = 180.dp),
                            contentPadding = PaddingValues(vertical = 6.dp)
                        ) {
                            itemsIndexed(logs) { idx, log ->
                                LogRowV2(
                                    log = log,
                                    isNewest = idx == 0,
                                    onRemove = { id -> logs.removeAll { it.id == id } },
                                    cs = cs,
                                    textSecondary = textSecondary
                                )
                                if (idx < logs.lastIndex) Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { logs.clear() }) {
                                Text(
                                    "Clear",
                                    color = textSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/** ---- Helpers ---- */
@Composable
fun QuickAddPill(ml: Int, onClick: () -> Unit, cs: ColorScheme) {
    val pressed = remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed.value) 0.96f else 1f,
        animationSpec = tween(120)
    )
    val scope = rememberCoroutineScope()

    Surface(modifier = Modifier.clip(RoundedCornerShape(12.dp)).scale(scale).clickable {
        pressed.value = true
        scope.launch {
            onClick()
            delay(120)
            pressed.value = false
        }
    }, color = cs.onSurface.copy(alpha = 0.04f)) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "+${ml} ml",
                color = cs.onSurface,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun LogRowV2(
    log: HydrationLog,
    isNewest: Boolean,
    onRemove: (Long) -> Unit,
    cs: ColorScheme,
    textSecondary: androidx.compose.ui.graphics.Color
) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
            .background(cs.onSurface.copy(alpha = 0.012f)).padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(CircleShape)
                .background(if (isNewest) cs.primary else cs.onSurface.copy(alpha = 0.04f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = if (isNewest) cs.onPrimary else cs.onSurface.copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text("${log.amount} ml", color = cs.onSurface, fontWeight = FontWeight.SemiBold)
            Text(log.time, color = textSecondary, fontSize = 12.sp)
        }

        TextButton(onClick = { onRemove(log.id) }) {
            Text("Remove", color = textSecondary, fontSize = 12.sp)
        }
    }
}

/** Helper: human time label e.g. "18:32" */
data class HydrationLog(
    val id: Long = KClock.System.now().toEpochMilliseconds(),
    val amount: Int,
    val time: String
)

/* ----- Meals section with expandable items and dialog ----- */
enum class MealPeriod(val label: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    SNACK("Snack"),
    DINNER("Dinner")
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

@Composable
fun NutritionMealsV2(
    meals: List<MealModel>,
    onOpenMeal: (MealModel) -> Unit,
    onAddMeal: (MealPeriod) -> Unit
) {
    val grouped = meals.groupBy { it.period }
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        grouped.forEach { (period, periodMeals) ->
            MealPeriodSection(
                period = period,
                meals = periodMeals,
                onOpenMeal = onOpenMeal,
                onAddMeal = { onAddMeal(period) })
        }
    }
}

@Composable
fun MealPeriodSection(
    period: MealPeriod,
    meals: List<MealModel>,
    onOpenMeal: (MealModel) -> Unit,
    onAddMeal: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(period.label, color = cs.onSurface, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            TextButton(onClick = onAddMeal) { Text("+ Add", color = cs.primary) }
        }

        Spacer(Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            meals.forEach { meal ->
                MealCardV4(meal = meal, onOpen = onOpenMeal)
            }
        }
    }
}

@Composable
fun MealBadge(meal: MealModel) {
    val cs = MaterialTheme.colorScheme
    val badge = when {
        meal.protein >= 30 -> "High Protein"
        meal.carbs >= 50 -> "High Carbs"
        meal.kcal < 400 -> "Light"
        else -> "Balanced"
    }

    Box(
        modifier = Modifier.background(cs.primary.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(badge, color = cs.primary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun MiniMacrosRow(protein: Int, carbs: Int, fat: Int) {
    val cs = MaterialTheme.colorScheme
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        MiniMacro("P", protein, cs.primary)
        MiniMacro("C", carbs, cs.secondary)
        MiniMacro("F", fat, cs.onSurface.copy(alpha = 0.6f))
    }
}

@Composable
fun MiniMacro(label: String, value: Int, color: androidx.compose.ui.graphics.Color) {
    val cs = MaterialTheme.colorScheme
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(6.dp).background(color, CircleShape))
        Spacer(Modifier.width(4.dp))
        Text("$label ${value}g", color = cs.onSurface.copy(alpha = 0.7f), fontSize = 11.sp)
    }
}

@Composable
fun MealCardV4(meal: MealModel, onOpen: (MealModel) -> Unit) {
    val cs = MaterialTheme.colorScheme
    var expanded by rememberSaveable { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 90f else 0f)

    Card(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(if (expanded) 6.dp else 2.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(meal.title, color = cs.onSurface, fontWeight = FontWeight.SemiBold)
                        MealBadge(meal)
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        "${meal.kcal} kcal • ${meal.time}",
                        color = cs.onSurface.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(6.dp))

                    MiniMacrosRow(protein = meal.protein, carbs = meal.carbs, fat = meal.fat)
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = cs.primary,
                    modifier = Modifier.rotate(rotation)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(Modifier.padding(top = 10.dp)) {
                    Divider(color = cs.onSurface.copy(alpha = 0.06f))
                    Spacer(Modifier.height(8.dp))
                    meal.items.forEach {
                        Text(
                            "• $it",
                            color = cs.onSurface.copy(alpha = 0.7f),
                            fontSize = 13.sp
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(onClick = { onOpen(meal) }, label = { Text("Details") })
                        AssistChip(
                            onClick = { /* log */ },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = cs.primary.copy(alpha = 0.15f),
                                labelColor = cs.primary
                            ),
                            label = { Text("Log") })
                    }
                }
            }
        }
    }
}

@Composable
fun MealCardV3(meal: MealModel, onOpen: (MealModel) -> Unit) {
    val cs = MaterialTheme.colorScheme
    var expanded by rememberSaveable { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(250)
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 6.dp else 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape)
                        .background(cs.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = null,
                        tint = cs.primary
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(meal.title, color = cs.onSurface, fontWeight = FontWeight.SemiBold)
                    Text(
                        "${meal.kcal} kcal • ${meal.time}",
                        color = cs.onSurface.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = cs.primary,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Divider(color = cs.onSurface.copy(alpha = 0.05f))
                    Spacer(Modifier.height(8.dp))
                    meal.items.forEach { item ->
                        Text(
                            text = "• $item",
                            color = cs.onSurface.copy(alpha = 0.7f),
                            fontSize = 13.sp
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(onClick = { onOpen(meal) }, label = { Text("Details") })
                        AssistChip(
                            onClick = { /* log meal */ },
                            label = { Text("Log") },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = cs.primary.copy(alpha = 0.15f),
                                labelColor = cs.primary
                            )
                        )
                    }
                }
            }
        }
    }
}

/* ----- Tip card ----- */
@Composable
fun NutritionTipCardV2(cs: ColorScheme, textSecondary: androidx.compose.ui.graphics.Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                "Tip of the day",
                color = cs.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Include a source of protein in each meal to support recovery and satiety.",
                color = textSecondary
            )
        }
    }
}