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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.workout.WorkoutCategory
import com.example.presentation.screens.widgets.FitverseCardRecommended
import com.example.presentation.screens.widgets.FitverseHeader
import com.example.presentation.theme.DarkGamifiedColors

@Composable
fun WorkoutScreenV2(
    hasActivePlan: Boolean = true,
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
            FitverseHeader(
                level = 23,
                xp = 340
            )
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
            FitverseCardRecommended(
                workout = workout,
                onClick = {}
            )
        }
    }
}

@Composable
fun WorkoutCategorySelector(selected: WorkoutCategory,onSelect: (WorkoutCategory) -> Unit) {
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
fun ActiveWorkoutCard(
    planName: String = "CHEST DAY ALPHA",
    subtitle: String = "Hypertrophy Session • Phase 2",
    time: String = "00:42:15",
    volume: String = "8,420",
    setsCompleted: String = "12",
    setsTotal: String = "18",
    bpm: String = "134",
    onStart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // Fundo Cinza Escuro
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)) // Borda Roda Sutil
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Top Row (Título e Tempo)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = planName.uppercase(),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = subtitle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = time,
                        color = MaterialTheme.colorScheme.tertiary, // Verde Neon
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace // Dá o aspecto digital do relógio
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "DURATION",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Row (As 3 caixinhas na base)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WorkoutStatBox(label = "VOLUME", value = volume, unit = "kg", modifier = Modifier.weight(1f))
                WorkoutStatBox(label = "SETS", value = setsCompleted, unit = "/$setsTotal", modifier = Modifier.weight(1f))
                WorkoutStatBox(label = "BPM", value = bpm, unit = "", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão de Iniciar/Retomar Treino (Bottom Center)
            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp) // Botão no estilo "Pill/Rounded" do seu Design System
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "RESUME SESSION",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    fontSize = 14.sp
                )
            }
        }
    }
}
@Composable
fun CreatePlanCard(onCreate: () -> Unit) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth().height(110.dp),
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
fun WorkoutHeader(level: Int, xp: Int) {

}
@Composable
fun WorkoutStatBox(label: String, value: String, unit: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background, // Fundo ainda mais escuro que o Card
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                if (unit.isNotEmpty()) {
                    Text(
                        text = " $unit",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 2.dp) // Alinha a base da fonte menor
                    )
                }
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