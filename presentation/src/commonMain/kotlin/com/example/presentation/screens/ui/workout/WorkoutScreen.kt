package com.example.presentation.screens.ui.workout

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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.workout.WorkoutCategory

@Composable
fun WorkoutScreenV2(
    hasActivePlan: Boolean = false,
    onStart: () -> Unit,
    onCreatePlan: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf(WorkoutCategory.STRENGTH) }
    val cs = MaterialTheme.colorScheme

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp) // Respiro maior entre blocos
    ) {

        item {
            WorkoutHeader(stamina = 78, xp = 90)
        }

        item {
            if (hasActivePlan) {
                ActiveWorkoutCard(
                    planName = "Hypertrophy A",
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
                fontWeight = FontWeight.ExtraBold,
                color = cs.onBackground, // Branco Puro
                letterSpacing = (-0.5).sp
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
            Spacer(Modifier.height(12.dp)) // Espaço sutil entre os cards da lista
        }
    }
}

@Composable
fun WorkoutCategorySelector(
    selected: WorkoutCategory,
    onSelect: (WorkoutCategory) -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Scroll horizontal caso adicione mais categorias
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(WorkoutCategory.entries.toTypedArray()) { category ->
            val isSelected = category == selected

            Surface(
                onClick = { onSelect(category) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) cs.primary else cs.surface,
                border = BorderStroke(1.dp, if (isSelected) cs.primary else cs.outline.copy(alpha = 0.2f))
            ) {
                Text(
                    text = category.name.lowercase().replaceFirstChar { it.uppercase() },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                    color = if (isSelected) Color.Black else cs.onSurface,
                    fontSize = 14.sp
                )
            }
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
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(cs.secondary.copy(alpha = 0.1f)), // Fundo roxo suave
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = null,
                    tint = cs.secondary, // Ícone roxo elétrico
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = workout.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = cs.onBackground,
                    maxLines = 1
                )

                Spacer(Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = cs.onSurfaceVariant
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = workout.duration,
                        style = MaterialTheme.typography.labelMedium,
                        color = cs.onSurfaceVariant
                    )

                    Text(
                        text = "  •  ",
                        color = cs.outline
                    )

                    // XP em Dourado
                    Text(
                        text = "+${workout.xp} XP",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = cs.tertiary
                    )
                }
            }

            // Tag de Intensidade
            Surface(
                color = cs.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Bolt,
                    contentDescription = null,
                    tint = cs.primary, // Raio Neon
                    modifier = Modifier.padding(8.dp).size(16.dp)
                )
            }
        }
    }
}

// --- 1. Card para quando o usuário JÁ TEM um treino ---
@Composable
fun ActiveWorkoutCard(
    planName: String = "Today's Plan",
    onStart: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onStart() },
        colors = CardDefaults.cardColors(containerColor = cs.primary), // Neon Volt
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "START WORKOUT",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = Color.Black, // Contraste máximo no Neon
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$planName • Gain XP",
                    fontSize = 14.sp,
                    color = Color.Black.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold
                )
            }

            // Botão "Recortado" escuro com ícone Neon
            Surface(
                shape = CircleShape,
                color = cs.surface, // Fundo escuro
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Start",
                        tint = cs.primary, // Ícone Neon
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

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        onClick = onCreate,
        border = BorderStroke(1.5.dp, cs.primary.copy(alpha = 0.3f)) // Borda Neon sutil
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            cs.primary.copy(alpha = 0.05f), // Glow no início do card
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
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(cs.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AddCircleOutline,
                        contentDescription = null,
                        tint = cs.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "NO ACTIVE PLAN",
                        style = MaterialTheme.typography.labelSmall,
                        color = cs.primary,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Create Workout Plan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = cs.onBackground
                    )
                    Text(
                        text = "Build a routine & track progress",
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = cs.outline,
                    modifier = Modifier.size(24.dp)
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
                    text = "WORKOUT",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = cs.onBackground,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Ready to train today?",
                    fontSize = 13.sp,
                    color = cs.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // XP = Dourado (Tertiary)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = cs.tertiary, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("$xp XP", color = cs.onBackground, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                // Stamina = Neon Volt (Primary)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Bolt, null, tint = cs.primary, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("$stamina%", color = cs.onBackground, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Barra de Stamina Minimalista
        LinearProgressIndicator(
            progress = { stamina / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = cs.primary,
            trackColor = cs.primary.copy(alpha = 0.1f),
            strokeCap = StrokeCap.Round
        )
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

data class WorkoutItem(
    val id: Int,
    val title: String,
    val duration: String,
    val xp: Int,
    val category: WorkoutCategory
)

val workoutList = listOf(
    _root_ide_package_.com.example.presentation.screens.ui.workout.WorkoutItem(
        1,
        "Upper Body Strength",
        "35 min",
        140,
        WorkoutCategory.STRENGTH
    ),
    _root_ide_package_.com.example.presentation.screens.ui.workout.WorkoutItem(
        2,
        "Leg Day Power",
        "40 min",
        160,
        WorkoutCategory.STRENGTH
    ),

    _root_ide_package_.com.example.presentation.screens.ui.workout.WorkoutItem(
        3,
        "Morning Cardio Burn",
        "25 min",
        120,
        WorkoutCategory.CARDIO
    ),
    _root_ide_package_.com.example.presentation.screens.ui.workout.WorkoutItem(
        4,
        "Endurance Run",
        "45 min",
        180,
        WorkoutCategory.CARDIO
    ),

    _root_ide_package_.com.example.presentation.screens.ui.workout.WorkoutItem(
        5,
        "HIIT Fat Burner",
        "20 min",
        150,
        WorkoutCategory.HIIT
    ),
    _root_ide_package_.com.example.presentation.screens.ui.workout.WorkoutItem(
        6,
        "Explosive HIIT",
        "30 min",
        200,
        WorkoutCategory.HIIT
    ),

    _root_ide_package_.com.example.presentation.screens.ui.workout.WorkoutItem(
        7,
        "Full Body Stretch",
        "15 min",
        80,
        WorkoutCategory.STRETCH
    ),
    _root_ide_package_.com.example.presentation.screens.ui.workout.WorkoutItem(
        8,
        "Post Workout Stretch",
        "20 min",
        90,
        WorkoutCategory.STRETCH
    )
)