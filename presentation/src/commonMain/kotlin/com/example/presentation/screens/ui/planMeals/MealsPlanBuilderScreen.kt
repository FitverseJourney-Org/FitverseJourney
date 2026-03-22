import BuilderScreenState
import CompactMacroItem
import FoodEntry
import FoodEntryRow
import MacroProgressHeader
import MealGroup
import MealGroupCard
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.LunchDining
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 1. ESTADOS DA TELA (Em Memória) ---

data class FoodEntry(
    val id: String,
    val name: String,
    val portion: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int
)

data class MealGroup(
    val id: String,
    val name: String, // Ex: "Café da Manhã"
    val foods: List<FoodEntry>
) {
    val totalCalories: Int get() = foods.sumOf { it.calories }
    val totalProtein: Int get() = foods.sumOf { it.protein }
    val totalCarbs: Int get() = foods.sumOf { it.carbs }
    val totalFat: Int get() = foods.sumOf { it.fat }
}

data class BuilderScreenState(
    val planName: String = "Cutting de Verão",
    val targetCalories: Int = 2100,
    val targetProtein: Int = 160,
    val targetCarbs: Int = 200,
    val targetFat: Int = 73,
    val meals: List<MealGroup> = emptyList()
) {
    val currentCalories: Int get() = meals.sumOf { it.totalCalories }
    val currentProtein: Int get() = meals.sumOf { it.totalProtein }
    val currentCarbs: Int get() = meals.sumOf { it.totalCarbs }
    val currentFat: Int get() = meals.sumOf { it.totalFat }
}

// --- 2. TELA COMPOSABLE ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealsPlanBuilderScreen(
    onBackClick: () -> Unit,
    onSavePlan: () -> Unit,
    onAddFoodClick: (mealGroupId: String) -> Unit
) {
    val state by remember {
        mutableStateOf(
            BuilderScreenState(
                meals = listOf(
                    MealGroup("m1", "Café da Manhã", listOf(
                        FoodEntry("f1", "Ovos Mexidos", "3 unidades", 210, 18, 2, 15),
                        FoodEntry("f2", "Pão Integral", "2 fatias", 120, 6, 22, 2)
                    )),
                    MealGroup("m2", "Almoço", emptyList()),
                    MealGroup("m3", "Jantar", emptyList())
                )
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Montar Plano", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            shape = CircleShape
                        ) {
                            Text(
                                text = state.planName,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    Button(
                        onClick = onSavePlan,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Salvar", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Header de Progresso com Gradient
            item {
                MacroProgressHeader(state = state)
            }

            item {
                Text(
                    text = "Refeições do Dia",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(16.dp)
                )
            }

            items(state.meals, key = { it.id }) { mealGroup ->
                // Mapeamento de ícones por nome de refeição
                val icon = when {
                    mealGroup.name.contains("Café") -> Icons.Outlined.BakeryDining
                    mealGroup.name.contains("Almoço") -> Icons.Outlined.LunchDining
                    else -> Icons.Outlined.Nightlight
                }

                MealGroupCard(
                    mealGroup = mealGroup,
                    icon = icon,
                    onAddFoodClick = { onAddFoodClick(mealGroup.id) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun MacroProgressHeader(state: BuilderScreenState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(12.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular Progress sutil para Calorias
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(80.dp)) {
                    CircularProgressIndicator(
                        progress = { (state.currentCalories.toFloat() / state.targetCalories).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxSize(),
                        strokeWidth = 8.dp,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${state.targetCalories - state.currentCalories}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text("restantes", style = MaterialTheme.typography.labelSmall, fontSize = 8.sp)
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    Text("Total Consumido", style = MaterialTheme.typography.labelMedium)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${state.currentCalories}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = " / ${state.targetCalories} kcal",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CompactMacroItem("Proteína", state.currentProtein, state.targetProtein, Color(0xFFF44336))
                CompactMacroItem("Carbos", state.currentCarbs, state.targetCarbs, Color(0xFF4CAF50))
                CompactMacroItem("Gordura", state.currentFat, state.targetFat, Color(0xFFFFC107))
            }
        }
    }
}

@Composable
fun CompactMacroItem(label: String, current: Int, target: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("$current / ${target}g", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { (current.toFloat() / target).coerceIn(0f, 1f) },
            modifier = Modifier
                .width(80.dp)
                .height(4.dp)
                .clip(CircleShape),
            color = color,
            trackColor = color.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun MealGroupCard(mealGroup: MealGroup, icon: ImageVector, onAddFoodClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = mealGroup.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "${mealGroup.totalCalories} kcal",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            border = if (mealGroup.foods.isEmpty()) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null
        ) {
            Column {
                if (mealGroup.foods.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddFoodClick() }
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Restaurant, contentDescription = null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Toque para adicionar", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                } else {
                    mealGroup.foods.forEach { food ->
                        FoodEntryRow(food = food)
                    }
                    // Botão de adição compacto no rodapé do card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddFoodClick() }
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                            Text("ADICIONAR MAIS", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodEntryRow(food: FoodEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            // Pequeno bullet colorido por macro principal (ex: proteina)
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFF44336)))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(food.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${food.portion} • P:${food.protein}g C:${food.carbs}g G:${food.fat}g", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Text("${food.calories} kcal", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}