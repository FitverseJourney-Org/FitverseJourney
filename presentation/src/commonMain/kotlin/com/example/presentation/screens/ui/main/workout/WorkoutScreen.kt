package com.example.presentation.screens.ui.main.workout

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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WorkoutScreenV2(
    modifier: Modifier = Modifier,
    onStart: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(WorkoutCategory.STRENGTH) }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            WorkoutHeader(
                stamina = 78,
                xp = 90
            )
        }

        item {
            StartWorkoutCard(
                onStart = onStart
            )
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Ícone lateral
            Card(
                shape = RoundedCornerShape(50),
                colors = CardDefaults.cardColors(containerColor = cs.primary.copy(alpha = 0.15f))
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = cs.primary,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = workout.title,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${workout.duration} • +${workout.xp} XP",
                    color = cs.onSurface.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
            }

            Icon(
                imageVector = Icons.Filled.Bolt,
                contentDescription = null,
                tint = cs.secondary
            )
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

// --- Data / models (mantive igual) ---

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