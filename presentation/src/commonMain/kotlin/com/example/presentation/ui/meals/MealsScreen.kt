package com.example.presentation.ui.meals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.widgets.FitVerseSpacer
import com.example.presentation.theme.FitverseColors
import com.example.presentation.ui.dashboard.components.SectionHeader

// ── Data ──────────────────────────────────────────────────────────────────────

data class Meal(
    val emoji: String,
    val name: String,
    val foods: List<String>,
    val kcal: Int,
    val kcalGoal: Int,
    val accentColor: Color,
)

private val meals = listOf(
    Meal("☀️", "Café da Manhã", listOf("Ovos mexidos (3)", "Pão integral", "Whey protein"), 420, 600, FitverseColors.Orange),
    Meal("⛅", "Almoço",        listOf("Frango grelhado", "Arroz integral", "Salada"),       680, 800, FitverseColors.Green),
    Meal("⚡", "Lanche",        listOf("Banana", "Pasta de amendoim"),                        280, 350, FitverseColors.Purple),
    Meal("🌙", "Jantar",        listOf("Salmão", "Batata doce", "Brócolis"),                 520, 700, FitverseColors.Blue),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun MealsScreen(onBottomSheetOpen: (Boolean) -> Unit = {}) {
    var showAddSheet by remember { mutableStateOf(false) }
    var selectedMeal by remember { mutableStateOf<Meal?>(null) }


    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0,0,0,0),
            containerColor = Color.Transparent,
            content = {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    item { NutritionHeader() }
                    item { MacrosSummaryCard() }
                    item {  SectionHeader(title = "REFEIÇÕES DO DIA",actionText = "+ ADICIONAR")}
                    items(meals) { meal ->
                        MealCard(
                            meal = meal,
                            onAddFood = {
                                selectedMeal = meal
                                showAddSheet = true
                                onBottomSheetOpen(true)
                            },
                        )
                    }
                }
                if (showAddSheet) {
                    AddFoodBottomSheet(
                        mealName = selectedMeal?.name.orEmpty(),
                        onCamera  = { showAddSheet = false; onBottomSheetOpen(false) },
                        onCustom  = { showAddSheet = false; onBottomSheetOpen(false) },
                        onDismiss = { showAddSheet = false; onBottomSheetOpen(false) },
                    )
                }
            }
        )
    }
}

// ── Calorie ring ──────────────────────────────────────────────────────────────

@Composable
fun CalorieRing(calories: Int, total: Int, modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val sw   = 10.dp.toPx()
            val half = sw / 2f
            val r    = Rect(half, half, size.width - half, size.height - half)
            drawArc(FitverseColors.Surface2, -90f, 360f, false, r.topLeft, r.size, style = Stroke(sw, cap = StrokeCap.Round))
            drawArc(FitverseColors.Accent,   -90f, 360f * calories / total, false, r.topLeft, r.size, style = Stroke(sw, cap = StrokeCap.Round))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$calories",  color = FitverseColors.TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Black)
            Text("kcal",       color = FitverseColors.TextMuted,   fontSize = 11.sp)
            Text("de $total",  color = FitverseColors.TextMuted,   fontSize = 10.sp)
        }
    }
}

// ── Macro row ─────────────────────────────────────────────────────────────────

@Composable
fun MacroRow(label: String, current: Float, goal: Float, unit: String, color: Color) {
    val progress = (current / goal).coerceIn(0f, 1f)
    val animated by animateFloatAsState(
        targetValue  = progress,
        animationSpec = tween(600, easing = EaseOut),
        label        = "macro_$label",
    )
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text(label, color = FitverseColors.TextMuted,   fontSize = 12.sp)
            Text(
                "${current.toInt()}/${goal.toInt()}$unit",
                color      = color,
                fontSize   = 12.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()                             // ← era 130.dp fixo
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(FitverseColors.Surface2),
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animated)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color),
            )
        }
    }
}
@Composable
fun AddFoodBottomSheet(
    mealName: String,
    onCamera: () -> Unit,
    onCustom: () -> Unit,
    onDismiss: () -> Unit,
) {
    // Overlay escuro
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(
                indication             = null,
                interactionSource      = remember { MutableInteractionSource() },
                onClick                = onDismiss,
            ),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication        = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick           = {},           // absorve o clique pra não fechar
                )
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(FitverseColors.Surface)
                .padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            // Handle
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(FitverseColors.Border2)
                    .align(Alignment.CenterHorizontally),
            )

            Spacer(Modifier.height(20.dp))

            Text(
                "Adicionar alimento",
                color      = FitverseColors.TextPrimary,
                fontSize   = 17.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Em: $mealName",
                color    = FitverseColors.TextMuted,
                fontSize = 13.sp,
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier            = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AddFoodOption(
                    modifier     = Modifier.weight(1f),
                    emoji        = "📷",
                    title        = "Via Câmera",
                    description  = "IA identifica o alimento automaticamente",
                    badgeLabel   = "✦ IA",
                    accentColor  = FitverseColors.Green,
                    onClick      = onCamera,
                )
                AddFoodOption(
                    modifier     = Modifier.weight(1f),
                    emoji        = "✏️",
                    title        = "Personalizado",
                    description  = "Insira nome, calorias e macros manualmente",
                    badgeLabel   = "Manual",
                    accentColor  = FitverseColors.Purple,
                    onClick      = onCustom,
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AddFoodOption(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    description: String,
    badgeLabel: String,
    accentColor: Color,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .background(FitverseColors.SurfaceCard)
            .clickable(onClick = onClick)
            .padding(16.dp),
    ) {
        // Emoji badge
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(accentColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(emoji, fontSize = 22.sp)
        }

        Spacer(Modifier.height(12.dp))

        Text(
            title,
            color      = accentColor,
            fontSize   = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(Modifier.height(4.dp))

        Text(
            description,
            color      = FitverseColors.TextMuted,
            fontSize   = 11.sp,
            lineHeight = 15.sp,
        )

        Spacer(Modifier.height(10.dp))

        // Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(accentColor.copy(alpha = 0.12f))
                .padding(horizontal = 8.dp, vertical = 3.dp),
        ) {
            Text(
                badgeLabel,
                color      = accentColor,
                fontSize   = 10.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
// ── Nutrition stat ────────────────────────────────────────────────────────────

@Composable
fun NutritionStat(emoji: String, value: String, label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(emoji, fontSize = 16.sp)
        Text(value, color = color,                    fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(label, color = FitverseColors.TextMuted, fontSize = 11.sp)
    }
}

// ── Meal card ─────────────────────────────────────────────────────────────────

@Composable
fun MealCard(
    meal: Meal,
    onAddFood: () -> Unit,
    defaultExpanded: Boolean = false,
) {
    val cs = MaterialTheme.colorScheme
    var expanded by remember { mutableStateOf(defaultExpanded) }

    val chevronRotation by animateFloatAsState(
        targetValue   = if (expanded) 90f else 0f,
        animationSpec = tween(250, easing = EaseInOut),
        label         = "chevron",
    )

    ElevatedCard(
        modifier = Modifier.fillMaxWidth().border(1.dp, FitverseColors.Border, RoundedCornerShape(18.dp)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = cs.surface,
        ),
        onClick = {
            expanded = !expanded
        },
        shape = RoundedCornerShape(14.dp)
    ){
        // ── Header ────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(meal.accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(meal.emoji, fontSize = 20.sp)
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(meal.name,         color = FitverseColors.TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text("${meal.kcal} kcal", color = FitverseColors.TextMuted,   fontSize = 12.sp)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(meal.accentColor.copy(alpha = 0.12f))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    "${((meal.kcal.toFloat() / meal.kcalGoal) * 100).toInt()}%",
                    color      = meal.accentColor,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(Modifier.width(8.dp))

            Icon(
                imageVector        = Icons.Default.ChevronRight,
                contentDescription = null,
                tint               = FitverseColors.TextMuted2,
                modifier           = Modifier.size(20.dp).rotate(chevronRotation),
            )
        }

        // ── Expanded ──────────────────────────────────────────
        AnimatedVisibility(
            visible = expanded,
            enter   = fadeIn(tween(200)) + expandVertically(tween(250, easing = EaseOut)),
            exit    = fadeOut(tween(150)) + shrinkVertically(tween(200, easing = EaseIn)),
        ) {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 14.dp)) {

                HorizontalDivider(color = FitverseColors.Border, thickness = 0.5.dp)
                Spacer(Modifier.height(14.dp))

                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Text(
                        "ALIMENTOS",
                        color         = meal.accentColor,
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                    )
                    Text(
                        "${meal.kcal} / ${meal.kcalGoal} kcal",
                        color    = FitverseColors.TextMuted,
                        fontSize = 11.sp,
                    )
                }

                Spacer(Modifier.height(10.dp))

                if (meal.foods.isEmpty()) {
                    Text(
                        "Nenhum alimento registrado",
                        color     = FitverseColors.TextMuted,
                        fontSize  = 13.sp,
                        modifier  = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        textAlign = TextAlign.Center,
                    )
                } else {
                    meal.foods.forEach { food ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier          = Modifier.padding(vertical = 5.dp),
                        ) {
                            Box(
                                Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(meal.accentColor),
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(food, color = FitverseColors.TextMuted, fontSize = 13.sp)
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Botão adicionar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(meal.accentColor.copy(alpha = 0.08f))
                        .clickable(onClick = onAddFood)       // ← abre o sheet
                        .padding(vertical = 11.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(meal.accentColor.copy(alpha = 0.20f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint     = meal.accentColor,
                            modifier = Modifier.size(12.dp),
                        )
                    }
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Adicionar alimento",
                        color      = meal.accentColor,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

// ── Sub-composables privados ──────────────────────────────────────────────────

@Composable
private fun NutritionHeader() {
    Column {
        Text(
            "SEGUNDA-FEIRA",
            color         = FitverseColors.TextMuted,
            fontSize      = 11.sp,
            fontWeight    = FontWeight.Medium,
            letterSpacing = 1.5.sp,
        )
        Text(
            "NUTRIÇÃO",
            color         = FitverseColors.TextPrimary,
            fontSize      = 28.sp,
            fontWeight    = FontWeight.Black,
            letterSpacing = (-0.5).sp,
        )
    }
}

@Composable
private fun MacrosSummaryCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, FitverseColors.Accent.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
            .background(FitverseColors.SurfaceCard)
            .padding(20.dp),
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CalorieRing(
                    calories = 1830,
                    total    = 2400,
                    modifier = Modifier.size(110.dp),
                )

                Spacer(Modifier.width(20.dp))

                Column(
                    modifier            = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        "MACROS DO DIA",
                        color         = FitverseColors.TextMuted,
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                    )
                    MacroRow("Proteína",     168f, 180f, "g", FitverseColors.Accent)
                    MacroRow("Carboidratos", 210f, 250f, "g", FitverseColors.Blue)
                    MacroRow("Gorduras",      68f,  80f, "g", FitverseColors.Purple)
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = FitverseColors.Border, thickness = 0.5.dp)
            Spacer(Modifier.height(14.dp))

            Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround) {
                NutritionStat("💧", "2.4L",    "Água",     FitverseColors.Blue)
                NutritionStat("⚡", "570kcal", "Restante", FitverseColors.Orange)
                NutritionStat("🎯", "76%",     "Meta",     FitverseColors.Accent)
            }
        }
    }
}