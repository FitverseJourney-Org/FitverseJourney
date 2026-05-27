package org.fitverse.presentation.ui.meals

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.DailyMacros
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.FoodItemRecord
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.MealEntryRecord
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.theme.FVTypography
import org.fitverse.presentation.ui.dashboard.components.SectionHeader
import org.fitverse.presentation.ui.meals.viewmodel.MealsIntent
import org.fitverse.presentation.ui.meals.viewmodel.MealsUiState
import org.fitverse.presentation.ui.workout.FitChip

private val mealAccentColors = listOf(
    FitColors.Orange,
    FitColors.Green,
    FitColors.Purple,
    FitColors.Blue,
    FitColors.Accent,
)

// ── Screen ────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealsScreen(
    uiState: MealsUiState,
    onIntent: (MealsIntent) -> Unit,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onBottomSheetOpen: (Boolean) -> Unit = {},
    onNavigateToManualFood: (mealId: String, mealName: String) -> Unit = { _, _ -> },
) {
    var showAddFoodSheet by remember { mutableStateOf(false) }
    var selectedMeal by remember { mutableStateOf<MealEntryRecord?>(null) }
    var showCreateMealSheet by remember { mutableStateOf(false) }

    LaunchedEffect(showAddFoodSheet, showCreateMealSheet) {
        onBottomSheetOpen(showAddFoodSheet || showCreateMealSheet)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh    = onRefresh,
            modifier     = Modifier.fillMaxSize(),
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                containerColor = Color.Transparent,
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    item { MealsHeader(uiState.dailyMacros) }
                    item { MacrosSummaryCard(uiState.dailyMacros) }
                    item {
                        SectionHeader(
                            title = "REFEIÇÕES DO DIA",
                            actionText = "+ ADICIONAR",
                            onActionClick = { showCreateMealSheet = true },
                        )
                    }
                    if (uiState.meals.isEmpty() && !uiState.isLoading) {
                        item { EmptyMealsPlaceholder { showCreateMealSheet = true } }
                    }
                    itemsIndexed(uiState.meals, key = { _, r -> r.id }) { index, record ->
                        MealCard(
                            record      = record,
                            foods       = uiState.foodsByMeal[record.id].orEmpty(),
                            accentColor = mealAccentColors[index % mealAccentColors.size],
                            onAddFood   = {
                                selectedMeal = record
                                showAddFoodSheet = true
                            },
                            onDelete    = { onIntent(MealsIntent.DeleteMeal(record.id)) },
                        )
                    }
                }
            }
        }

        if (showAddFoodSheet) {
            AddFoodBottomSheet(
                mealName  = selectedMeal?.name.orEmpty(),
                onCamera  = { showAddFoodSheet = false },
                onCustom  = {
                    val id   = selectedMeal?.id.orEmpty()
                    val name = selectedMeal?.name.orEmpty()
                    showAddFoodSheet = false
                    onNavigateToManualFood(id, name)
                },
                onDismiss = { showAddFoodSheet = false },
            )
        }

        if (showCreateMealSheet) {
            CreateMealBottomSheet(
                onConfirm = { name ->
                    onIntent(MealsIntent.CreateMeal(name))
                    showCreateMealSheet = false
                },
                onDismiss = { showCreateMealSheet = false },
            )
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun MealsHeader(macros: DailyMacros?) {
    val kcal = macros?.kcal ?: 0
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text  = "HOJE",
            style = FVTypography.overlineLarge,
            color = FitColors.TextMuted,
        )
        Text(
            text  = "NUTRIÇÃO",
            style = FVTypography.displayLarge,
            color = FitColors.TextPrimary,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FitChip("$kcal KCAL", FitColors.OrangeDim, textColor = FitColors.Orange)
        }
    }
}

// ── Macros summary card ───────────────────────────────────────────────────────

@Composable
private fun MacrosSummaryCard(macros: DailyMacros?) {
    val kcal    = macros?.kcal?.toInt() ?: 0
    val protein = macros?.protein?.toFloat() ?: 0f
    val carbs   = macros?.carbs?.toFloat() ?: 0f
    val fat     = macros?.fat?.toFloat() ?: 0f

    Box(
        modifier = Modifier
            .border(1.dp, FitColors.Accent.copy(alpha = 0.20f), RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(FitColors.SurfaceModal),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                FitColors.Accent.copy(alpha = 0.6f),
                                FitColors.Accent,
                                FitColors.Accent.copy(alpha = 0.6f),
                                Color.Transparent,
                            )
                        )
                    )
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CalorieRing(
                        calories = kcal,
                        total    = 2000,
                        modifier = Modifier.size(110.dp),
                    )

                    Spacer(Modifier.width(20.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text  = "MACROS DO DIA",
                            style = FVTypography.overline,
                            color = FitColors.TextMuted,
                        )
                        MacroRow("Proteína",     protein, "g", FitColors.Accent)
                        MacroRow("Carboidratos", carbs,   "g", FitColors.Blue)
                        MacroRow("Gorduras",     fat,     "g", FitColors.Purple)
                    }
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = FitColors.Border, thickness = 0.5.dp)
                Spacer(Modifier.height(14.dp))

                Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround) {
                    NutritionStat(Icons.Rounded.LocalFireDepartment, "${kcal}kcal",          "Consumido", FitColors.Orange)
                    NutritionStat(Icons.Rounded.WaterDrop,           "${protein.toInt()}g",  "Proteína",  FitColors.Accent)
                    NutritionStat(Icons.Rounded.EmojiEvents,         "${carbs.toInt()}g",    "Carbos",    FitColors.Blue)
                }
            }
        }
    }
}

// ── Calorie ring ──────────────────────────────────────────────────────────────

@Composable
fun CalorieRing(calories: Int, total: Int, modifier: Modifier = Modifier) {
    val progress = if (total > 0) (calories.toFloat() / total).coerceIn(0f, 1f) else 0f
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val sw   = 10.dp.toPx()
            val half = sw / 2f
            val r    = Rect(half, half, size.width - half, size.height - half)
            drawArc(FitColors.Surface2, -90f, 360f, false, r.topLeft, r.size, style = Stroke(sw, cap = StrokeCap.Round))
            drawArc(FitColors.Accent,   -90f, 360f * progress, false, r.topLeft, r.size, style = Stroke(sw, cap = StrokeCap.Round))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$calories", style = FVTypography.monoLarge, color = FitColors.TextPrimary)
            Text(text = "kcal",      style = FVTypography.labelMedium, color = FitColors.TextMuted)
        }
    }
}

// ── Macro row ─────────────────────────────────────────────────────────────────

@Composable
fun MacroRow(label: String, value: Float, unit: String, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text(text = label, style = FVTypography.bodySmall, color = FitColors.TextMuted)
            Text(
                text  = "${value.toInt()}$unit",
                style = FVTypography.titleSmall,
                color = color,
            )
        }
    }
}

// ── Nutrition stat ────────────────────────────────────────────────────────────

@Composable
private fun NutritionStat(icon: ImageVector, value: String, label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Text(text = value, style = FVTypography.headlineSmall, color = color)
        Text(text = label, style = FVTypography.labelMedium,   color = FitColors.TextMuted)
    }
}

// ── Meal card (real data) ─────────────────────────────────────────────────────

@Composable
fun MealCard(
    record: MealEntryRecord,
    foods: List<FoodItemRecord> = emptyList(),
    accentColor: Color,
    onAddFood: () -> Unit,
    onDelete: () -> Unit,
    defaultExpanded: Boolean = false,
) {
    var expanded by remember { mutableStateOf(defaultExpanded) }

    val chevronRotation by animateFloatAsState(
        targetValue   = if (expanded) 90f else 0f,
        animationSpec = tween(250, easing = EaseInOut),
        label         = "chevron",
    )

    Column(
        modifier = Modifier
            .border(1.dp, Color(0xFF2a2a35), RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { expanded = !expanded },
    ) {
        // ── Header ────────────────────────────────────────────
        Row(
            modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.10f))
                    .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ){
                Icon(
                    imageVector        = Icons.Rounded.Restaurant,
                    contentDescription = null,
                    tint               = accentColor,
                    modifier           = Modifier.size(22.dp),
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(text = record.name,               style = FVTypography.titleLarge, color = FitColors.TextPrimary)
                Text(text = "${record.totalKcal} kcal", style = FVTypography.bodySmall,  color = FitColors.TextMuted)
            }

            Icon(
                imageVector        = Icons.Rounded.Delete,
                contentDescription = "Excluir",
                tint               = FitColors.TextDisabled,
                modifier           = Modifier.size(18.dp).clickable(
                    indication        = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick           = onDelete,
                ),
            )

            Spacer(Modifier.width(10.dp))

            Icon(
                imageVector        = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint               = FitColors.TextDisabled,
                modifier           = Modifier.size(20.dp).rotate(chevronRotation),
            )
        }

        // Progress strip
        val progress = if (record.totalKcal > 0) (record.totalKcal.toFloat() / 600f).coerceIn(0f, 1f) else 0f
        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(FitColors.Surface2)) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(accentColor.copy(alpha = 0.75f)),
            )
        }

        // ── Expanded content ──────────────────────────────────
        AnimatedVisibility(
            visible = expanded,
            enter   = fadeIn(tween(200)) + expandVertically(tween(250, easing = EaseOut)),
            exit    = fadeOut(tween(150)) + shrinkVertically(tween(200, easing = EaseIn)),
        ) {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 14.dp)) {
                Spacer(Modifier.height(14.dp))

                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Text(
                        text  = "ALIMENTOS",
                        style = FVTypography.overline,
                        color = accentColor,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        MacroChip("P ${record.totalProtein.toInt()}g", FitColors.Accent)
                        MacroChip("C ${record.totalCarbs.toInt()}g",   FitColors.Blue)
                        MacroChip("G ${record.totalFat.toInt()}g",     FitColors.Purple)
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (foods.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        foods.forEach { food ->
                            FoodItemRow(food = food, accentColor = accentColor)
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }

                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.08f))
                        .border(1.dp, accentColor.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                        .clickable(onClick = onAddFood)
                        .padding(vertical = 11.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(text = "Adicionar alimento", style = FVTypography.titleMedium, color = accentColor)
                }
            }
        }
    }
}

@Composable
private fun FoodItemRow(food: FoodItemRecord, accentColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(FitColors.Surface2)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(accentColor),
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text  = food.name,
                style = FVTypography.titleMedium,
                color = FitColors.TextPrimary,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "${food.portion.toInt()} ${food.unit}", style = FVTypography.labelMedium, color = FitColors.TextMuted)
                Text(text = "·", style = FVTypography.labelMedium, color = FitColors.TextDisabled)
                Text(text = "P ${food.protein.toInt()}g", style = FVTypography.labelLarge, color = FitColors.Accent)
                Text(text = "C ${food.carbs.toInt()}g",   style = FVTypography.labelLarge, color = FitColors.Blue)
                Text(text = "G ${food.fat.toInt()}g",     style = FVTypography.labelLarge, color = FitColors.Purple)
            }
        }
        Text(
            text  = "${food.kcal} kcal",
            style = FVTypography.monoSmall.copy(fontWeight = FontWeight.Bold),
            color = accentColor,
        )
    }
}

@Composable
private fun MacroChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.10f))
            .padding(horizontal = 6.dp, vertical = 2.dp),
    ) {
        Text(text = text, style = FVTypography.overline.copy(fontWeight = FontWeight.SemiBold), color = color)
    }
}

// ── Empty state ───────────────────────────────────────────────────────────────

@Composable
private fun EmptyMealsPlaceholder(onCreateMeal: () -> Unit) {
    Column(
        modifier            = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            imageVector        = Icons.Rounded.Restaurant,
            contentDescription = null,
            tint               = FitColors.TextDisabled,
            modifier           = Modifier.size(48.dp),
        )
        Text(
            text  = "Nenhuma refeição ainda",
            style = FVTypography.titleLarge,
            color = FitColors.TextMuted,
        )
        Text(
            text      = "Crie sua primeira refeição\ncom o nome que preferir",
            style     = FVTypography.bodyMedium,
            color     = FitColors.TextDisabled,
            textAlign = TextAlign.Center,
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(FitColors.Accent.copy(alpha = 0.12f))
                .border(1.dp, FitColors.Accent.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                .clickable(onClick = onCreateMeal)
                .padding(horizontal = 20.dp, vertical = 10.dp),
        ) {
            Text(text = "+ Nova refeição", style = FVTypography.bodyLarge.copy(fontWeight = FontWeight.Bold), color = FitColors.Accent)
        }
    }
}

// ── Create meal bottom sheet ──────────────────────────────────────────────────

@Composable
fun CreateMealBottomSheet(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(
                indication        = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick           = onDismiss,
            ),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication        = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick           = {},
                )
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(FitColors.Surface)
                .padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(FitColors.Border2)
                    .align(Alignment.CenterHorizontally),
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text  = "Nova refeição",
                style = FVTypography.headlineMedium,
                color = FitColors.TextPrimary,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text  = "Dê um nome para sua refeição",
                style = FVTypography.bodyMedium,
                color = FitColors.TextMuted,
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value         = name,
                onValueChange = { name = it },
                modifier      = Modifier.fillMaxWidth().focusRequester(focusRequester),
                placeholder   = { Text("Ex: Pré-treino, Jantar tardio...", color = FitColors.TextDisabled, fontSize = 14.sp) },
                singleLine    = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction      = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { if (name.isNotBlank()) onConfirm(name) }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = FitColors.Accent,
                    unfocusedBorderColor = FitColors.Border2,
                    focusedTextColor     = FitColors.TextPrimary,
                    unfocusedTextColor   = FitColors.TextPrimary,
                    cursorColor          = FitColors.Accent,
                ),
                shape = RoundedCornerShape(14.dp),
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FitColors.Surface2)
                        .clickable(onClick = onDismiss)
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Cancelar", color = FitColors.TextMuted, fontWeight = FontWeight.SemiBold)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (name.isNotBlank()) FitColors.Accent
                            else FitColors.Accent.copy(alpha = 0.3f)
                        )
                        .clickable(enabled = name.isNotBlank()) { onConfirm(name) }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Criar refeição", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

// ── Add food bottom sheet ─────────────────────────────────────────────────────

@Composable
fun AddFoodBottomSheet(
    mealName: String,
    onCamera: () -> Unit,
    onCustom: () -> Unit,
    onDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(
                indication        = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick           = onDismiss,
            ),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication        = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick           = {},
                )
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(FitColors.Surface)
                .padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(FitColors.Border2)
                    .align(Alignment.CenterHorizontally),
            )

            Spacer(Modifier.height(20.dp))

            Text(text = "Adicionar alimento", style = FVTypography.headlineMedium, color = FitColors.TextPrimary)
            Spacer(Modifier.height(4.dp))
            Text(text = "Em: $mealName", style = FVTypography.bodyMedium, color = FitColors.TextMuted)

            Spacer(Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AddFoodOption(
                    modifier    = Modifier.weight(1f),
                    icon        = Icons.Rounded.CameraAlt,
                    title       = "Via Câmera",
                    description = "IA identifica o alimento automaticamente",
                    badgeLabel  = "✦ IA",
                    accentColor = FitColors.Green,
                    onClick     = onCamera,
                )
                AddFoodOption(
                    modifier    = Modifier.weight(1f),
                    icon        = Icons.Rounded.Edit,
                    title       = "Personalizado",
                    description = "Insira nome, calorias e macros manualmente",
                    badgeLabel  = "Manual",
                    accentColor = FitColors.Purple,
                    onClick     = onCustom,
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AddFoodOption(
    modifier: Modifier = Modifier,
    icon: ImageVector,
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
            .background(FitColors.SurfaceModal)
            .clickable(onClick = onClick)
            .padding(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(accentColor.copy(alpha = 0.12f))
                .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.height(12.dp))
        Text(text = title, style = FVTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 14.sp), color = accentColor)
        Spacer(Modifier.height(4.dp))
        Text(text = description, style = FVTypography.labelMedium, color = FitColors.TextMuted)
        Spacer(Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(accentColor.copy(alpha = 0.12f))
                .padding(horizontal = 8.dp, vertical = 3.dp),
        ) {
            Text(text = badgeLabel, style = FVTypography.overline.copy(fontWeight = FontWeight.SemiBold), color = accentColor)
        }
    }
}
