package com.example.presentation.screens.ui.main.workout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WorkoutScreenV2(
    hasActivePlan: Boolean = false,
    onStart: () -> Unit,
    onCreatePlan: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf(WorkoutCategory.STRENGTH) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            WorkoutHeader(
                stamina = 78,
                xp = 90
            )
        }

        // Lógica de apresentação baseada no status do usuário
        item {
            if (hasActivePlan) {
                ActiveWorkoutCard(
                    planName = "Hypertrophy A", // Isso viria do seu banco de dados
                    onStart = onStart
                )
            } else {
                CreatePlanCard(
                    onCreate = onCreatePlan
                )
            }
        }

        item {
            WorkoutCategorySelector(
                selected = selectedCategory,
                onSelect = { selectedCategory = it }
            )
        }

        item {
            Text(
                text = "Recommended for you",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(
            workoutList.filter { it.category == selectedCategory },
            key = { it.id }
        ) { workout ->
            WorkoutItemCard(
                workout = workout,
                onClick = {}
            )
        }
    }
}

@Composable
fun WorkoutCategorySelector(
    selected: WorkoutCategory,
    onSelect: (WorkoutCategory) -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        WorkoutCategory.entries.forEach { category ->
            val isSelected = category == selected

            AssistChip(
                onClick = { onSelect(category) },
                label = {
                    Text(
                        text = category.name.lowercase()
                            .replaceFirstChar { it.uppercase() },
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) cs.onPrimary else cs.onSurface
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (isSelected) cs.primary else cs.surfaceVariant,
                    labelColor = if (isSelected) cs.onPrimary else cs.onSurface
                )
            )
        }
    }
}

@Composable
fun WorkoutItemCard(
    workout: WorkoutItem,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(20.dp), // Arredondamento moderno
        color = cs.surface,
        border = BorderStroke(1.dp, cs.onSurface.copy(alpha = 0.08f)), // Borda quase invisível mas presente
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp), // Padding interno equilibrado
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Ícone de Ação com Estilo "Glass"
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(cs.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    tint = cs.primary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            // 2. Informações Principais
            Column(Modifier.weight(1f)) {
                Text(
                    text = workout.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = cs.onSurface,
                    maxLines = 1 // Evita quebra de layout
                )

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Ícone pequeno de tempo
                    Icon(
                        imageVector = Icons.Rounded.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = cs.onSurfaceVariant
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = workout.duration,
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant
                    )

                    Text(
                        text = " • ",
                        color = cs.onSurfaceVariant.copy(alpha = 0.5f)
                    )

                    // Destaque para o XP
                    Text(
                        text = "+${workout.xp} XP",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = cs.secondary // Cor diferente para gamificação
                    )
                }
            }

            // 3. Tag de Intensidade (O raio estilizado)
            Surface(
                color = cs.secondaryContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Bolt,
                        contentDescription = null,
                        tint = cs.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    // Se o seu WorkoutItem tiver um nível de intensidade (ex: "Hard")
                    // Text(text = "Hard", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

// --- 1. Card para quando o usuário JÁ TEM um treino ---
@Composable
fun ActiveWorkoutCard(
    planName: String = "Today's Plan", // Dar contexto do que ele vai treinar melhora a UX
    onStart: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onStart() },
        colors = CardDefaults.cardColors(containerColor = cs.primary),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) // Dá um leve destaque na tela
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Start Workout",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = cs.onPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$planName • Gain XP",
                    fontSize = 14.sp,
                    color = cs.onPrimary.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Medium
                )
            }

            // Botão de Play mais robusto e "clicável" visualmente
            Surface(
                shape = CircleShape,
                color = cs.onPrimary,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Start",
                        tint = cs.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

// --- 2. Card para quando o usuário NÃO TEM um treino (Empty State) ---
@Composable
fun CreatePlanCard(onCreate: () -> Unit) {
    val cs = MaterialTheme.colorScheme

    // Usamos um Surface para controle total de elevação e forma
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp), // Um pouco mais alto para respirar melhor
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        onClick = {
            onCreate()
        },
        border = BorderStroke(
            width = 1.5.dp,
            // Usamos um gradiente na borda para um ar mais Premium
            brush = Brush.linearGradient(
                colors = listOf(cs.primary.copy(alpha = 0.5f), cs.primary.copy(alpha = 0.05f))
            )
        ),
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                // Gradiente sutil de fundo para não ser uma cor "morta"
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            cs.primary.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    )
                )
        ){
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Ícone com Estilo "Glass"
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(cs.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AddCircleOutline, // Ícone mais moderno
                        contentDescription = null,
                        tint = cs.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 2. Textos com Hierarquia Clara
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "No active plan",
                        style = MaterialTheme.typography.labelLarge,
                        color = cs.primary,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Create Workout Plan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = cs.onSurface
                    )
                    Text(
                        text = "Build a routine & track progress",
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }

                // 3. Botão de ação minimalista
                Icon(
                    imageVector = Icons.Rounded.ArrowForwardIos,
                    contentDescription = null,
                    tint = cs.onSurfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
@Composable
fun WorkoutHeader(stamina: Int, xp: Int) {
    val cs = MaterialTheme.colorScheme

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Workout",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurface
                )
                Text(
                    text = "Ready to train today?",
                    fontSize = 13.sp,
                    color = cs.onSurface.copy(alpha = 0.6f)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, null, tint = cs.primary)
                    Spacer(Modifier.width(6.dp))
                    Text("$xp%", color = cs.primary, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Bolt, null, tint = cs.secondary)
                    Spacer(Modifier.width(6.dp))
                    Text("$stamina%", color = cs.secondary, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Barra simples de stamina — fundo = surfaceVariant, fill = secondary
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
            shape = RoundedCornerShape(6.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(stamina / 100f)
                    .height(6.dp),
                colors = CardDefaults.cardColors(containerColor = cs.secondary),
                shape = RoundedCornerShape(6.dp)
            ) {}
        }
    }
}

@Composable
fun StartWorkoutCard(onStart: () -> Unit) {
    val cs = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clickable { onStart() },
        colors = CardDefaults.cardColors(containerColor = cs.primary),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Start Workout",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = cs.onPrimary
                )
                Text(
                    "Burn calories • Gain XP",
                    fontSize = 13.sp,
                    color = cs.onPrimary.copy(alpha = 0.85f)
                )
            }

            Card(
                shape = RoundedCornerShape(50),
                colors = CardDefaults.cardColors(containerColor = cs.onSurface.copy(alpha = 0.12f))
            ) {
                Icon(
                    Icons.Filled.PlayArrow,
                    null,
                    tint = cs.onPrimary,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                )
            }
        }
    }
}

enum class WorkoutCategory {
    STRENGTH, CARDIO, HIIT, STRETCH
}

data class WorkoutItem(
    val id: Int,
    val title: String,
    val duration: String,
    val xp: Int,
    val category: WorkoutCategory
)

val workoutList = listOf(
    WorkoutItem(
        1,
        "Upper Body Strength",
        "35 min",
        140,
        WorkoutCategory.STRENGTH
    ),
    WorkoutItem(
        2,
        "Leg Day Power",
        "40 min",
        160,
        WorkoutCategory.STRENGTH
    ),

    WorkoutItem(
        3,
        "Morning Cardio Burn",
        "25 min",
        120,
        WorkoutCategory.CARDIO
    ),
    WorkoutItem(
        4,
        "Endurance Run",
        "45 min",
        180,
        WorkoutCategory.CARDIO
    ),

    WorkoutItem(
        5,
        "HIIT Fat Burner",
        "20 min",
        150,
        WorkoutCategory.HIIT
    ),
    WorkoutItem(
        6,
        "Explosive HIIT",
        "30 min",
        200,
        WorkoutCategory.HIIT
    ),

    WorkoutItem(
        7,
        "Full Body Stretch",
        "15 min",
        80,
        WorkoutCategory.STRETCH
    ),
    WorkoutItem(
        8,
        "Post Workout Stretch",
        "20 min",
        90,
        WorkoutCategory.STRETCH
    )
)