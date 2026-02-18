package com.example.presentation.screens.main.nutrition

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.core.utils.TimeProvider.nowLocalTime
import com.example.presentation.theme.AccentGreen
import com.example.presentation.theme.SurfaceGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Clock


val TextPrimary = Color.White
val TextSecondary = Color.White.copy(alpha = 0.7f)

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
    onOpenMeal: (com.example.presentation.screens.main.nutrition.MealModel) -> Unit = {}
) {
    fun sampleMeals(): List<com.example.presentation.screens.main.nutrition.MealModel> = listOf(

        // 🌅 BREAKFAST
        MealModel(
            id = "1",
            title = "Oatmeal with Banana",
            kcal = 420,
            time = "08:15",
            period = _root_ide_package_.com.example.presentation.screens.main.nutrition.MealPeriod.BREAKFAST,
            protein = 22,
            carbs = 55,
            fat = 10,
            items = listOf(
                "Oats",
                "Banana",
                "Whey protein",
                "Peanut butter"
            )
        ),

        _root_ide_package_.com.example.presentation.screens.main.nutrition.MealModel(
            id = "2",
            title = "Eggs & Whole Toast",
            kcal = 380,
            time = "09:00",
            period = _root_ide_package_.com.example.presentation.screens.main.nutrition.MealPeriod.BREAKFAST,
            protein = 28,
            carbs = 30,
            fat = 16,
            items = listOf(
                "2 eggs",
                "Whole grain bread",
                "Olive oil"
            )
        ),

        // 🍛 LUNCH
        _root_ide_package_.com.example.presentation.screens.main.nutrition.MealModel(
            id = "3",
            title = "Grilled Chicken Bowl",
            kcal = 620,
            time = "13:10",
            period = _root_ide_package_.com.example.presentation.screens.main.nutrition.MealPeriod.LUNCH,
            protein = 42,
            carbs = 60,
            fat = 14,
            items = listOf(
                "Grilled chicken breast",
                "Brown rice",
                "Broccoli",
                "Olive oil"
            )
        ),

        _root_ide_package_.com.example.presentation.screens.main.nutrition.MealModel(
            id = "4",
            title = "Salmon & Sweet Potato",
            kcal = 700,
            time = "14:00",
            period = _root_ide_package_.com.example.presentation.screens.main.nutrition.MealPeriod.LUNCH,
            protein = 38,
            carbs = 50,
            fat = 26,
            items = listOf(
                "Salmon",
                "Sweet potato",
                "Asparagus"
            )
        ),

        // 🍌 SNACK
        _root_ide_package_.com.example.presentation.screens.main.nutrition.MealModel(
            id = "5",
            title = "Greek Yogurt & Honey",
            kcal = 260,
            time = "16:30",
            period = _root_ide_package_.com.example.presentation.screens.main.nutrition.MealPeriod.SNACK,
            protein = 20,
            carbs = 28,
            fat = 6,
            items = listOf(
                "Greek yogurt",
                "Honey",
                "Granola"
            )
        ),

        _root_ide_package_.com.example.presentation.screens.main.nutrition.MealModel(
            id = "6",
            title = "Protein Shake",
            kcal = 220,
            time = "17:45",
            period = _root_ide_package_.com.example.presentation.screens.main.nutrition.MealPeriod.SNACK,
            protein = 30,
            carbs = 15,
            fat = 3,
            items = listOf(
                "Whey protein",
                "Almond milk",
                "Ice"
            )
        ),

        // 🌙 DINNER
        _root_ide_package_.com.example.presentation.screens.main.nutrition.MealModel(
            id = "7",
            title = "Lean Beef & Rice",
            kcal = 650,
            time = "20:10",
            period = _root_ide_package_.com.example.presentation.screens.main.nutrition.MealPeriod.DINNER,
            protein = 40,
            carbs = 55,
            fat = 18,
            items = listOf(
                "Lean beef",
                "White rice",
                "Mixed vegetables"
            )
        ),

        _root_ide_package_.com.example.presentation.screens.main.nutrition.MealModel(
            id = "8",
            title = "Chicken Salad",
            kcal = 480,
            time = "21:00",
            period = _root_ide_package_.com.example.presentation.screens.main.nutrition.MealPeriod.DINNER,
            protein = 35,
            carbs = 20,
            fat = 22,
            items = listOf(
                "Chicken breast",
                "Lettuce",
                "Tomatoes",
                "Avocado",
                "Olive oila"
            )
        )
    )

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            _root_ide_package_.com.example.presentation.screens.main.nutrition.NutritionHeaderAnimated(
                calories,
                targetCalories
            )
        }
        item {
            _root_ide_package_.com.example.presentation.screens.main.nutrition.MacrosRow(
                protein,
                carbs,
                fat
            )
        }
        item {
            _root_ide_package_.com.example.presentation.screens.main.nutrition.HydrationCardV3(
                initialConsumed = hydrationMl,
                goal = hydrationGoal,
                onAdd = onAddWater,
            )
        }
        item {
            _root_ide_package_.com.example.presentation.screens.main.nutrition.NutritionMealsV2(
                meals = sampleMeals(),
                onOpenMeal = { meal ->
                    // navegar para detalhes
                },
                onAddMeal = { period ->
                    // abrir modal de adicionar refeição já filtrado
                }
            )

        }
        item { NutritionTipCardV2() }

        // snackbar overlay
        item {

        }
    }
}

/* ----- Models ----- */


/* ----- Header with circular animated progress ----- */
@Composable
fun NutritionHeaderAnimated(
    current: Int,
    target: Int
) {
    val progress = (current.toFloat() / target.coerceAtLeast(1))
        .coerceIn(0f, 2f)

    val animated by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(700)
    )

    val isOverTarget = current > target

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        // 🟢 TITLE + SUBTITLE
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Fuel your body today",
                color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextPrimary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )

            Text(
                text = if (!isOverTarget)
                    "You’re on track — keep fueling your goals."
                else
                    "You’ve reached your goal — balance wisely.",
                color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextSecondary,
                fontSize = 13.sp
            )
        }

        // 🔄 CALORIE RING
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Box(
                modifier = Modifier.size(96.dp),
                contentAlignment = Alignment.Center
            ) {
                val stroke = with(LocalDensity.current) { 10.dp.toPx() }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    // background ring
                    drawArc(
                        color = Color.White.copy(alpha = 0.06f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = stroke)
                    )

                    // progress ring
                    drawArc(
                        color = if (!isOverTarget) AccentGreen else Color(0xFFFFC107),
                        startAngle = -90f,
                        sweepAngle = (animated * 360f).coerceAtMost(360f),
                        useCenter = false,
                        style = Stroke(
                            width = stroke,
                            cap = StrokeCap.Round
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$current",
                        color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "of $target kcal",
                        color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            // 📊 SIDE INFO
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = if (!isOverTarget) "Calories remaining"
                    else "Calories exceeded",
                    color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextSecondary,
                    fontSize = 12.sp
                )

                Text(
                    text = if (!isOverTarget)
                        "${target - current} kcal"
                    else
                        "+${current - target} kcal",
                    color = if (!isOverTarget) AccentGreen else Color(0xFFFFC107),
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
    fat: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        _root_ide_package_.com.example.presentation.screens.main.nutrition.MacroCard(
            title = "Protein",
            value = "$protein g",
            accent = AccentGreen,
            modifier = Modifier.weight(1f)
        )

        _root_ide_package_.com.example.presentation.screens.main.nutrition.MacroCard(
            title = "Carbs",
            value = "$carbs g",
            accent = Color(0xFFFFC107),
            modifier = Modifier.weight(1f)
        )

        _root_ide_package_.com.example.presentation.screens.main.nutrition.MacroCard(
            title = "Fat",
            value = "$fat g",
            accent = Color(0xFF90A4AE),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun MacroCard(
    title: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box {
            // Accent strip
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(accent)
            )

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 14.dp, bottom = 14.dp, end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Text(
                    text = title.uppercase(),
                    color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextSecondary,
                    fontSize = 11.sp,
                    letterSpacing = 0.6.sp
                )

                Text(
                    text = value,
                    color = accent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
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
    onAdd: (Int) -> Unit = {}
) {
    val logs = remember { mutableStateListOf<com.example.presentation.screens.main.nutrition.HydrationLog>() }
    var consumed by rememberSaveable { mutableStateOf(initialConsumed.coerceAtLeast(0)) }
    val percent = (consumed.toFloat() / goal).coerceIn(0f, 1f)
    val animatedPercent by animateFloatAsState(
        targetValue = percent,
        animationSpec = tween(700, easing = FastOutSlowInEasing)
    )

    var openLog by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val nowFormatter = remember { nowLocalTime() }

    // pulse when goal reached
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFF102814)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            // Header row: title + numeric + small action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // icon circle
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF12321A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalDrink,
                            contentDescription = "Hydration",
                            tint = Color(0xFF8EE0A8),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text("Hydration", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text("${consumed} / $goal ml", color = Color(0xFFBFCFC0), fontSize = 12.sp)
                    }
                }

                // toggle log / summary
                TextButton(onClick = { openLog = !openLog }) {
                    Text(if (openLog) "Hide log" else "View log", color = Color(0xFFBFCFC0))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // main content: ring + quick adds
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // progress ring
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .scale(pulseScale),
                    contentAlignment = Alignment.Center
                ) {
                    // ring canvas
                    Canvas(modifier = Modifier.matchParentSize()) {
                        val stroke = 12f
                        val radius = size.minDimension / 2f
                        // background ring
                        drawArc(
                            color = Color.White.copy(alpha = 0.06f),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = stroke)
                        )
                        // foreground
                        drawArc(
                            color = Color(0xFF7DE39D),
                            startAngle = -90f,
                            sweepAngle = (animatedPercent * 360f),
                            useCenter = false,
                            style = Stroke(width = stroke, cap = Stroke.DefaultCap)
                        )
                        // small center glow
                        drawCircle(
                            color = Color(0xFF7DE39D).copy(alpha = 0.06f),
                            radius = radius * 0.6f,
                            center = Offset(size.width / 2f, size.height / 2f)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${(animatedPercent * 100).toInt()}%", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("${(goal - consumed).coerceAtLeast(0)} ml left", color = Color(0xFFBFCFC0), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // quick add column
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceBetween) {
                    // quick chips row
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.fillMaxWidth()) {
                        listOf(100, 200, 300).forEach { ml ->
                            _root_ide_package_.com.example.presentation.screens.main.nutrition.QuickAddPill(
                                ml = ml
                            ) {
                                // animate + add log
                                consumed = (consumed + ml).coerceAtMost(goal)
                                logs.add(
                                    0,
                                    _root_ide_package_.com.example.presentation.screens.main.nutrition.HydrationLog(
                                        amount = ml,
                                        time = nowFormatter
                                    )
                                )
                                onAdd(ml)
                                // brief feedback
                                scope.launch {
                                    // small haptic/visual delay if wanted
                                    delay(140)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // CTA + subtle hint
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = { /* open full hydration screen */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7DE39D)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text("Log water", color = Color.Black)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Tip: sip through the day",
                            color = Color(0xFFBFCFC0),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Logs area (collapsible)
            AnimatedVisibility(visible = openLog) {
                Column {
                    if (logs.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.02f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No entries yet — add some water 💧", color = Color(0xFFBFCFC0))
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 180.dp),
                            contentPadding = PaddingValues(vertical = 6.dp)
                        ) {
                            itemsIndexed(logs) { idx, log ->
                                _root_ide_package_.com.example.presentation.screens.main.nutrition.LogRowV2(
                                    log = log,
                                    isNewest = idx == 0,
                                    onRemove = { id -> logs.removeAll { it.id == id } }
                                )
                                if (idx < logs.lastIndex) Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { logs.clear() }) {
                                Text("Clear", color = Color(0xFFBFCFC0))
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
fun QuickAddPill(ml: Int, onClick: () -> Unit) {
    val pressed = remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (pressed.value) 0.96f else 1f, animationSpec = tween(120))
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .scale(scale)
            .clickable {
                pressed.value = true
                scope.launch {
                    onClick()
                    delay(120)
                    pressed.value = false
                }
            },
        color = Color.White.copy(alpha = 0.04f)
    ) {
        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("+${ml} ml", color = Color.White)
        }
    }
}

@Composable
fun LogRowV2(log: com.example.presentation.screens.main.nutrition.HydrationLog, isNewest: Boolean, onRemove: (Long) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.012f))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // check icon / badge
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (isNewest) Color(0xFF7DE39D) else Color.White.copy(alpha = 0.04f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = if (isNewest) Color.Black else Color(0xFFBFCFC0))
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text("${log.amount} ml", color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(log.time, color = Color(0xFFBFCFC0), fontSize = 12.sp)
        }

        TextButton(onClick = { onRemove(log.id) }) {
            Text("Remove", color = Color(0xFFBFCFC0), fontSize = 12.sp)
        }
    }
}
/** Helper: human time label e.g. "18:32" */
data class HydrationLog(
    val id: Long = Clock.System.now().toEpochMilliseconds(),
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
    val period: com.example.presentation.screens.main.nutrition.MealPeriod,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val items: List<String>
)
@Composable
fun NutritionMealsV2(
    meals: List<com.example.presentation.screens.main.nutrition.MealModel>,
    onOpenMeal: (com.example.presentation.screens.main.nutrition.MealModel) -> Unit,
    onAddMeal: (com.example.presentation.screens.main.nutrition.MealPeriod) -> Unit
) {
    val grouped = meals.groupBy { it.period }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        grouped.forEach { (period, periodMeals) ->
            _root_ide_package_.com.example.presentation.screens.main.nutrition.MealPeriodSection(
                period = period,
                meals = periodMeals,
                onOpenMeal = onOpenMeal,
                onAddMeal = { onAddMeal(period) }
            )
        }
    }
}
@Composable
fun MealPeriodSection(
    period: com.example.presentation.screens.main.nutrition.MealPeriod,
    meals: List<com.example.presentation.screens.main.nutrition.MealModel>,
    onOpenMeal: (com.example.presentation.screens.main.nutrition.MealModel) -> Unit,
    onAddMeal: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                period.label,
                color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            TextButton(onClick = onAddMeal) {
                Text("+ Add", color = AccentGreen)
            }
        }

        Spacer(Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            meals.forEach { meal ->
                _root_ide_package_.com.example.presentation.screens.main.nutrition.MealCardV4(
                    meal = meal,
                    onOpen = onOpenMeal
                )
            }
        }
    }
}
@Composable
fun MealBadge(meal: com.example.presentation.screens.main.nutrition.MealModel) {
    val badge = when {
        meal.protein >= 30 -> "High Protein"
        meal.carbs >= 50 -> "High Carbs"
        meal.kcal < 400 -> "Light"
        else -> "Balanced"
    }

    Box(
        modifier = Modifier
            .background(
                AccentGreen.copy(alpha = 0.15f),
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            badge,
            color = AccentGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun MiniMacrosRow(protein: Int, carbs: Int, fat: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        _root_ide_package_.com.example.presentation.screens.main.nutrition.MiniMacro(
            "P",
            protein,
            AccentGreen
        )
        _root_ide_package_.com.example.presentation.screens.main.nutrition.MiniMacro(
            "C",
            carbs,
            Color(0xFFFFC107)
        )
        _root_ide_package_.com.example.presentation.screens.main.nutrition.MiniMacro(
            "F",
            fat,
            Color(0xFF90A4AE)
        )
    }
}

@Composable
fun MiniMacro(label: String, value: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(color, CircleShape)
        )
        Spacer(Modifier.width(4.dp))
        Text("$label ${value}g", color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextSecondary, fontSize = 11.sp)
    }
}
@Composable
fun MealCardV4(
    meal: com.example.presentation.screens.main.nutrition.MealModel,
    onOpen: (com.example.presentation.screens.main.nutrition.MealModel) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 90f else 0f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = SurfaceGreen),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(if (expanded) 6.dp else 2.dp)
    ) {
        Column(Modifier.padding(14.dp)) {

            // Top row
            Row(verticalAlignment = Alignment.CenterVertically) {

                Column(Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            meal.title,
                            color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        _root_ide_package_.com.example.presentation.screens.main.nutrition.MealBadge(
                            meal
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        "${meal.kcal} kcal • ${meal.time}",
                        color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(Modifier.height(6.dp))

                    _root_ide_package_.com.example.presentation.screens.main.nutrition.MiniMacrosRow(
                        protein = meal.protein,
                        carbs = meal.carbs,
                        fat = meal.fat
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = AccentGreen,
                    modifier = Modifier.rotate(rotation)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(Modifier.padding(top = 10.dp)) {
                    Divider(color = Color.White.copy(alpha = 0.06f))
                    Spacer(Modifier.height(8.dp))

                    meal.items.forEach {
                        Text("• $it", color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextSecondary, fontSize = 13.sp)
                    }

                    Spacer(Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(
                            onClick = { onOpen(meal) },
                            label = { Text("Details") }
                        )
                        AssistChip(
                            onClick = { /* log */ },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = AccentGreen.copy(alpha = 0.15f),
                                labelColor = AccentGreen
                            ),
                            label = {Text("Log")}
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun MealCardV3(
    meal: com.example.presentation.screens.main.nutrition.MealModel,
    onOpen: (com.example.presentation.screens.main.nutrition.MealModel) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(250)
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceGreen),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 6.dp else 2.dp)
    ) {
        Column {
            // Top content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Icon badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(AccentGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = null,
                        tint = AccentGreen
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        meal.title,
                        color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "${meal.kcal} kcal • ${meal.time}",
                        color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = AccentGreen,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }

            // Expanded content
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Divider(color = Color.White.copy(alpha = 0.05f))

                    Spacer(Modifier.height(8.dp))

                    meal.items.forEach { item ->
                        Text(
                            text = "• $item",
                            color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextSecondary,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AssistChip(
                            onClick = { onOpen(meal) },
                            label = { Text("Details") }
                        )

                        AssistChip(
                            onClick = { /* log meal */ },
                            label = { Text("Log") },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = AccentGreen.copy(alpha = 0.15f),
                                labelColor = AccentGreen
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
fun NutritionTipCardV2() {
    Card(colors = CardDefaults.cardColors(containerColor = SurfaceGreen), shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text("Tip of the day", color = AccentGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Include a source of protein in each meal to support recovery and satiety.",
                color = _root_ide_package_.com.example.presentation.screens.main.nutrition.TextSecondary
            )
        }
    }
}

