package com.example.presentation.screens.ui.meals

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expect.getHourOfDay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddManualMealScreen(
    initialPeriod: com.example.presentation.screens.ui.meals.MealPeriod,
    onNavigateBack: () -> Unit,
    onSaveMeal: (com.example.presentation.screens.ui.meals.MealModel) -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Estados do Formulário
    var title by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var manualKcal by remember { mutableStateOf("") }

    // Estado da Lista de Ingredientes
    var currentIngredient by remember { mutableStateOf("") }
    val ingredients = remember { mutableStateListOf<String>() }

    // Cálculo Automático de Calorias ("Smart Form")
    val pInt = protein.toIntOrNull() ?: 0
    val cInt = carbs.toIntOrNull() ?: 0
    val fInt = fat.toIntOrNull() ?: 0
    val calculatedKcal = (pInt * 4) + (cInt * 4) + (fInt * 9)

    // Validação simples
    val isFormValid = title.isNotBlank() && (calculatedKcal > 0 || manualKcal.isNotBlank())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova Refeição", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = cs.background)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = cs.surface,
                tonalElevation = 8.dp
            ) {
                Button(
                    onClick = {
                        if (isFormValid) {
                            val finalKcal = manualKcal.toIntOrNull() ?: calculatedKcal
                            val currentTime = getHourOfDay().toString()

                            val newMeal =
                                _root_ide_package_.com.example.presentation.screens.ui.meals.MealModel(
                                    id = "1", // ID único temporário
                                    title = title,
                                    kcal = finalKcal,
                                    time = currentTime,
                                    period = initialPeriod,
                                    protein = pInt,
                                    carbs = cInt,
                                    fat = fInt,
                                    items = ingredients.toList()
                                )
                            onSaveMeal(newMeal)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    enabled = isFormValid,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Salvar Refeição", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = cs.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            item { Spacer(Modifier.height(8.dp)) }

            // 1. Informações Básicas
            item {
                _root_ide_package_.com.example.presentation.screens.ui.meals.SectionTitle("Informações Básicas")
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nome da refeição (ex: Frango com Batata)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))
                // Exibe o período escolhido (Apenas visual, já que vem por parâmetro)
                OutlinedTextField(
                    value = initialPeriod.label,
                    onValueChange = { },
                    label = { Text("Período") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = cs.onSurface,
                        disabledBorderColor = cs.outline.copy(alpha = 0.5f),
                        disabledLabelColor = cs.onSurfaceVariant
                    )
                )
            }

            // 2. Macros e Calorias
            item {
                _root_ide_package_.com.example.presentation.screens.ui.meals.SectionTitle("Macronutrientes")
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    _root_ide_package_.com.example.presentation.screens.ui.meals.MacroInputCard(
                        label = "Proteína (g)", value = protein, color = Color(0xFF3949AB),
                        onValueChange = { protein = it }, modifier = Modifier.weight(1f)
                    )
                    _root_ide_package_.com.example.presentation.screens.ui.meals.MacroInputCard(
                        label = "Carbo (g)", value = carbs, color = Color(0xFFE65100),
                        onValueChange = { carbs = it }, modifier = Modifier.weight(1f)
                    )
                    _root_ide_package_.com.example.presentation.screens.ui.meals.MacroInputCard(
                        label = "Gordura (g)", value = fat, color = Color(0xFFB18D00),
                        onValueChange = { fat = it }, modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Card de Calorias Calculadas vs Manuais
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = cs.primary.copy(alpha = 0.05f),
                    border = BorderStroke(1.dp, cs.primary.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Calorias Totais", fontWeight = FontWeight.Bold, color = cs.onSurface)
                            Text("Calculado pelos macros: $calculatedKcal kcal", fontSize = 12.sp, color = cs.onSurfaceVariant)
                        }
                        OutlinedTextField(
                            value = manualKcal,
                            onValueChange = { manualKcal = it },
                            placeholder = { Text("$calculatedKcal") },
                            modifier = Modifier.width(100.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }
            }

            // 3. Ingredientes
            item {
                _root_ide_package_.com.example.presentation.screens.ui.meals.SectionTitle("Ingredientes (Opcional)")
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = currentIngredient,
                        onValueChange = { currentIngredient = it },
                        label = { Text("Adicionar item") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    IconButton(
                        onClick = {
                            if (currentIngredient.isNotBlank()) {
                                ingredients.add(currentIngredient.trim())
                                currentIngredient = ""
                            }
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(cs.primary)
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = "Adicionar", tint = cs.onPrimary)
                    }
                }
            }

            // Lista de Ingredientes Adicionados
            items(ingredients) { item ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = cs.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth().animateItem()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item, color = cs.onSurface)
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = "Remover",
                            tint = cs.error,
                            modifier = Modifier.clickable { ingredients.remove(item) }
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(80.dp)) } // Espaço para o BottomBar não cobrir o último item
        }
    }
}

/* --- Componentes Auxiliares da Tela --- */

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun MacroInputCard(
    label: String,
    value: String,
    color: Color,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            // Permite apenas números
            if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                onValueChange(it)
            }
        },
        label = { Text(label, fontSize = 11.sp, color = color, fontWeight = FontWeight.Bold) },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = color,
            focusedLabelColor = color,
            cursorColor = color
        )
    )
}