package com.example.presentation.screens.ui.meals

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
import androidx.compose.foundation.Indication
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
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
import com.example.presentation.screens.widgets.FitVerseSpacer
import com.example.presentation.theme.FitverseColors

private data class Meal(
    val emoji: String,
    val name: String,
    val foods: List<String>,
    val kcal: Int
)

private val meals = listOf(
    Meal("☀️", "Café da Manhã", listOf("Ovos mexidos (3)", "Pão integral", "Whey protein"), 420),
    Meal("⛅", "Almoço",        listOf("Frango grelhado", "Arroz integral", "Salada"),       680),
    Meal("⚡", "Lanche",        listOf("Banana", "Pasta de amendoim"),                        280),
    Meal("🌙", "Jantar",        listOf("Salmão", "Batata doce", "Brócolis"),                 520),
)

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
            Text("$calories", color = FitverseColors.TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Black)
            Text("kcal",      color = FitverseColors.TextMuted,   fontSize = 11.sp)
            Text("de $total", color = FitverseColors.TextMuted,   fontSize = 10.sp)
        }
    }
}

@Composable
fun MacroRow(label: String, value: String, color: Color) {
    val pct = value.split("/").let {
        it[0].toFloatOrNull()?.div(it[1].replace("g", "").toFloatOrNull() ?: 1f) ?: 0f
    }
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text(label, color = FitverseColors.TextMuted, fontSize = 12.sp)
            Text(value, color = color,                    fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
        Box(
            modifier = Modifier
                .width(130.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(FitverseColors.Surface2)
        ) {
            Box(Modifier.fillMaxHeight().fillMaxWidth(pct).background(color))
        }
    }
}

@Composable
fun NutritionStat(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color,                    fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(label, color = FitverseColors.TextMuted, fontSize = 11.sp)
    }
}

@Composable
fun MealCard(
    emoji: String,
    name: String,
    foods: List<String>,
    kcal: Int,
    kcalGoal: Int = 800, // novo parâmetro para progresso
    accentColor: Color = FitverseColors.Accent,
    defaultExpanded: Boolean = false,
) {
    var expanded by remember { mutableStateOf(defaultExpanded) }
    val cs = MaterialTheme.colorScheme

    // Animação de rotação do chevron
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = tween(durationMillis = 250, easing = EaseInOut),
        label = "chevron_rotation"
    )

    // Progresso de calorias (0f..1f)
    val progress = (kcal.toFloat() / kcalGoal.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600, easing = EaseOut),
        label = "calorie_progress"
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = cs.outlineVariant,         // usa token do tema, não hardcode
                shape = RoundedCornerShape(18.dp)
            )
            .clip(RoundedCornerShape(18.dp))       // mesmo radius que o border
            .background(cs.surface)
            .clickable{ expanded = !expanded }
    ) {
        // ── Header ───────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(emoji, fontSize = 20.sp)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = name,
                        color = FitverseColors.TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "$kcal kcal",
                        color = FitverseColors.TextMuted,
                        fontSize = 12.sp
                    )
                }
            }

            // Chevron com rotação animada — sem lógica invertida
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = if (expanded) "Recolher $name" else "Expandir $name",
                tint = FitverseColors.TextMuted,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(chevronRotation)
            )
        }

        // ── Barra de progresso de calorias ───────────────────────
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = accentColor,
            trackColor = cs.outlineVariant,
        )
        FitVerseSpacer(vertical = true, value = 10.dp)
        // ── Conteúdo expandido ───────────────────────────────────
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(tween(200)) + expandVertically(tween(250, easing = EaseOut)),
            exit = fadeOut(tween(150)) + shrinkVertically(tween(200, easing = EaseIn)),
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 14.dp)
            ) {
                // Cabeçalho da seção com fração de calorias
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ALIMENTOS",
                        color = accentColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "$kcal / $kcalGoal kcal",
                        color = FitverseColors.TextMuted,
                        fontSize = 11.sp
                    )
                }

                Spacer(Modifier.height(8.dp))

                if (foods.isEmpty()) {
                    Text(
                        text = "Nenhum alimento registrado",
                        color = FitverseColors.TextMuted,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    foods.forEach { food ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Box(
                                Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(accentColor)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(food, color = FitverseColors.TextMuted, fontSize = 13.sp)
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Botão "Adicionar alimento" com accentColor
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = cs.outlineVariant,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .clickable{ /* navegar para tela de adição */ }
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Adicionar alimento",
                        color = accentColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
@Composable
fun MealsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item { NutritionHeader() }
        item { MacrosSummaryCard() }
        item { MealsSectionHeader() }

        items(meals) { meal ->
            MealCard(
                emoji = meal.emoji,
                name  = meal.name,
                foods = meal.foods,
                kcal  = meal.kcal
            )
            Spacer(Modifier.height(10.dp))
        }
    }
}

// ── Sub-composables privados ──────────────────

@Composable
private fun NutritionHeader() {
    Column(Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
        Text(
            "SEGUNDA-FEIRA",
            color = FitverseColors.TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.5.sp
        )
        Text(
            "NUTRIÇÃO",
            color = FitverseColors.TextPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp
        )
    }
}

@Composable
private fun MacrosSummaryCard() {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .border(width = 1.dp, color = cs.primary.copy(.5f), shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cs.surface)
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CalorieRing(calories = 1830, total = 2400, modifier = Modifier.size(120.dp))
                Spacer(Modifier.width(24.dp))
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "MACROS DO DIA",
                        color = FitverseColors.TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )
                    MacroRow("Proteína",     "168/180g", FitverseColors.Accent)
                    MacroRow("Carboidratos", "210/250g", FitverseColors.Blue)
                    MacroRow("Gorduras",     "68/80g",   FitverseColors.Purple)
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = FitverseColors.TextDim, thickness = 0.5.dp)
            Spacer(Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                NutritionStat("2.4L",    "Água",     FitverseColors.Blue)
                NutritionStat("570kcal", "Restante", FitverseColors.Orange)
                NutritionStat("76%",     "Meta",     FitverseColors.Accent)
            }
        }
    }
    Spacer(Modifier.height(24.dp))
}

@Composable
private fun MealsSectionHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "REFEIÇÕES DO DIA",
            color = FitverseColors.TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )
        Text(
            modifier = Modifier
                .padding(10.dp, 5.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable {

                },
            text ="+ Adicionar",
            color = FitverseColors.Accent,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
    Spacer(Modifier.height(12.dp))
}