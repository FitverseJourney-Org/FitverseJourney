package org.fitverse.presentation.ui.meals

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.theme.FitColors

@Composable
fun AddManualFoodScreen(
    mealName: String,
    onSave: (name: String, portion: Double, unit: String, kcal: Int, protein: Double, carbs: Double, fat: Double) -> Unit,
    onBack: () -> Unit,
) {
    // ── Form state ────────────────────────────────────────────────────────────
    var foodName          by remember { mutableStateOf("") }
    var portion           by remember { mutableStateOf("") }
    var selectedUnitIndex by remember { mutableStateOf(0) }
    var protein           by remember { mutableStateOf("") }
    var carbs             by remember { mutableStateOf("") }
    var fat               by remember { mutableStateOf("") }
    var currentIngredient by remember { mutableStateOf("") }
    val ingredients = remember { mutableStateListOf<String>() }

    val units = listOf("g", "ml", "unid.")

    // ── Derived ───────────────────────────────────────────────────────────────
    val pInt = protein.toIntOrNull() ?: 0
    val cInt = carbs.toIntOrNull()   ?: 0
    val fInt = fat.toIntOrNull()     ?: 0
    val calculatedKcal = pInt * 4 + cInt * 4 + fInt * 9
    val isFormValid = foodName.isNotBlank() && calculatedKcal > 0

    val animatedKcal by animateIntAsState(
        targetValue   = calculatedKcal,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label         = "kcal",
    )

    val kcalColor = when {
        calculatedKcal == 0   -> FitColors.TextDisabled
        calculatedKcal < 300  -> FitColors.Green
        calculatedKcal < 600  -> FitColors.Accent
        calculatedKcal < 900  -> FitColors.Orange
        else                  -> FitColors.Red
    }

    // ── Layout ────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = Color.Transparent,
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    start  = 16.dp,
                    end    = 16.dp,
                    top    = 16.dp,

                ),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                // ── Cabeçalho ─────────────────────────────────────────────
                item {
                    ScreenHeader(mealName = mealName, onBack = onBack)
                }

                // ── Card de preview de calorias ────────────────────────────
                item {
                    LiveCalorieCard(
                        kcal      = animatedKcal,
                        kcalColor = kcalColor,
                        protein   = pInt,
                        carbs     = cInt,
                        fat       = fInt,
                    )
                }

                // ── Informações básicas ────────────────────────────────────
                item {
                    FormSection(title = "INFORMAÇÕES BÁSICAS") {
                        FitTextField(
                            value         = foodName,
                            onValueChange = { foodName = it },
                            label         = "Nome do alimento",
                            placeholder   = "Ex: Frango grelhado",
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        )

                        Spacer(Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            FitTextField(
                                value         = portion,
                                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) portion = it },
                                label         = "Porção",
                                placeholder   = "100",
                                modifier      = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            )
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                Text(
                                    "Unidade",
                                    color     = FitColors.TextMuted,
                                    fontSize  = 12.sp,
                                    fontWeight= FontWeight.Medium,
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    units.forEachIndexed { index, unit ->
                                        val isSelected = index == selectedUnitIndex
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    if (isSelected) FitColors.Accent
                                                    else FitColors.Surface2
                                                )
                                                .border(
                                                    width  = if (isSelected) 0.dp else 1.dp,
                                                    color  = Color(0xFF2a2a35),
                                                    shape  = RoundedCornerShape(8.dp),
                                                )
                                                .clickable { selectedUnitIndex = index }
                                                .padding(horizontal = 10.dp, vertical = 8.dp),
                                        ) {
                                            Text(
                                                unit,
                                                color      = if (isSelected) FitColors.Bg else FitColors.TextMuted,
                                                fontSize   = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // ── Macronutrientes ───────────────────────────────────────
                item {
                    FormSection(title = "MACRONUTRIENTES") {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            MacroInputCard(
                                label         = "Proteína (g)",
                                value         = protein,
                                color         = FitColors.Accent,
                                onValueChange = { protein = it },
                                modifier      = Modifier.weight(1f),
                            )
                            MacroInputCard(
                                label         = "Carbo (g)",
                                value         = carbs,
                                color         = FitColors.Blue,
                                onValueChange = { carbs = it },
                                modifier      = Modifier.weight(1f),
                            )
                            MacroInputCard(
                                label         = "Gordura (g)",
                                value         = fat,
                                color         = FitColors.Purple,
                                onValueChange = { fat = it },
                                modifier      = Modifier.weight(1f),
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        // Fórmula explicativa
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(FitColors.Surface2)
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                "Prot×4 + Carbo×4 + Gord×9",
                                color   = FitColors.TextDisabled,
                                fontSize = 11.sp,
                            )
                            Text(
                                "= $calculatedKcal kcal",
                                color      = kcalColor,
                                fontSize   = 12.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }

                // ── Ingredientes (opcional) ────────────────────────────────
                item {
                    FormSection(title = "INGREDIENTES  •  OPCIONAL") {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment     = Alignment.CenterVertically,
                        ) {
                            FitTextField(
                                value         = currentIngredient,
                                onValueChange = { currentIngredient = it },
                                label         = "Adicionar item",
                                placeholder   = "Ex: Ovo mexido",
                                modifier      = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                            )
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (currentIngredient.isNotBlank()) FitColors.Accent
                                        else FitColors.Surface2
                                    )
                                    .clickable {
                                        if (currentIngredient.isNotBlank()) {
                                            ingredients.add(currentIngredient.trim())
                                            currentIngredient = ""
                                        }
                                    },
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Adicionar",
                                    tint = if (currentIngredient.isNotBlank()) FitColors.Bg
                                           else FitColors.TextDisabled,
                                    modifier = Modifier.size(22.dp),
                                )
                            }
                        }

                        if (ingredients.isNotEmpty()) {
                            Spacer(Modifier.height(10.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                ingredients.forEach { ingredient ->
                                    IngredientItem(
                                        ingredient = ingredient,
                                        onRemove   = { ingredients.remove(ingredient) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── Botão fixo ────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        0.35f to FitColors.Bg.copy(alpha = 0.95f),
                        1.0f to FitColors.Bg,
                    )
                )
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 20.dp),
        ) {
            Button(
                onClick = {
                    onSave(
                        foodName,
                        portion.toDoubleOrNull() ?: 0.0,
                        units[selectedUnitIndex],
                        calculatedKcal,
                        pInt.toDouble(),
                        cInt.toDouble(),
                        fInt.toDouble(),
                    )
                },
                enabled  = isFormValid,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor         = FitColors.Accent,
                    contentColor           = FitColors.Bg,
                    disabledContainerColor = FitColors.Surface2,
                    disabledContentColor   = FitColors.TextDisabled,
                ),
            ) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "SALVAR ALIMENTO",
                    fontSize      = 15.sp,
                    fontWeight    = FontWeight.Black,
                    letterSpacing = 1.sp,
                )
            }
        }
    }
}

// ── Sub-composables ───────────────────────────────────────────────────────────

@Composable
private fun ScreenHeader(mealName: String, onBack: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(FitColors.Surface2)
                .border(1.dp, Color(0xFF2a2a35), RoundedCornerShape(12.dp))
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Rounded.KeyboardArrowLeft,
                contentDescription = "Voltar",
                tint     = FitColors.TextPrimary,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(Modifier.width(14.dp))

        Column {
            Text(
                "ADICIONAR ALIMENTO",
                color         = FitColors.TextPrimary,
                fontSize      = 16.sp,
                fontWeight    = FontWeight.Black,
                letterSpacing = 0.5.sp,
            )
            Text(
                "Em: $mealName",
                color    = FitColors.TextMuted,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
private fun LiveCalorieCard(
    kcal: Int,
    kcalColor: Color,
    protein: Int,
    carbs: Int,
    fat: Int,
) {
    Box(
        modifier = Modifier
            .border(1.dp, kcalColor.copy(alpha = if (kcal > 0) 0.30f else 0.10f), RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(FitColors.SurfaceModal),
    ) {
        Column {
            // Gradient top strip — cor varia com o nível calórico
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                kcalColor.copy(alpha = if (kcal > 0) 0.7f else 0.2f),
                                kcalColor.copy(alpha = if (kcal > 0) 1.0f else 0.3f),
                                kcalColor.copy(alpha = if (kcal > 0) 0.7f else 0.2f),
                                Color.Transparent,
                            )
                        )
                    )
            )

            Row(
                modifier              = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                // ── Calorias ──────────────────────────────────────────
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "CALORIAS ESTIMADAS",
                        color         = FitColors.TextMuted,
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text       = if (kcal == 0) "--" else "$kcal",
                            color      = kcalColor,
                            fontSize   = 34.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-1).sp,
                        )
                        if (kcal > 0) {
                            Text(
                                " kcal",
                                color    = FitColors.TextMuted,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 5.dp),
                            )
                        }
                    }
                    Text(
                        "Prot×4  +  Carbo×4  +  Gord×9",
                        color    = FitColors.TextDisabled,
                        fontSize = 10.sp,
                    )
                }

                // ── Breakdown dos macros ──────────────────────────────
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    MacroPill("P", protein, FitColors.Accent)
                    MacroPill("C", carbs,   FitColors.Blue)
                    MacroPill("G", fat,     FitColors.Purple)
                }
            }
        }
    }
}

@Composable
private fun MacroPill(label: String, value: Int, color: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.10f))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            "$label: ${if (value == 0) "--" else "${value}g"}",
            color      = color,
            fontSize   = 11.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun FormSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column {
        Text(
            title,
            color         = FitColors.TextMuted,
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Bold,
            letterSpacing = 1.2.sp,
        )
        Spacer(Modifier.height(12.dp))
        content()
    }
}

@Composable
private fun FitTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        value       = value,
        onValueChange = onValueChange,
        label       = { Text(label, fontSize = 12.sp) },
        placeholder = { Text(placeholder, color = FitColors.TextDisabled, fontSize = 14.sp) },
        modifier    = modifier.fillMaxWidth(),
        singleLine  = true,
        shape       = RoundedCornerShape(12.dp),
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = FitColors.Accent,
            unfocusedBorderColor = Color(0xFF2a2a35),
            focusedLabelColor    = FitColors.Accent,
            unfocusedLabelColor  = FitColors.TextMuted,
            cursorColor          = FitColors.Accent,
            focusedTextColor     = FitColors.TextPrimary,
            unfocusedTextColor   = FitColors.TextPrimary,
        ),
    )
}

@Composable
private fun IngredientItem(ingredient: String, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(FitColors.Surface2)
            .border(1.dp, Color(0xFF2a2a35), RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(FitColors.Accent),
        )
        Text(
            ingredient,
            color    = FitColors.TextPrimary,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(FitColors.Red.copy(alpha = 0.12f))
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = "Remover",
                tint     = FitColors.Red.copy(alpha = 0.8f),
                modifier = Modifier.size(14.dp),
            )
        }
    }
}
