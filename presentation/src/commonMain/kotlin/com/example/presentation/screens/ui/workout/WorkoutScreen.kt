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
        verticalArrangement = Arrangement.spacedBy(14.dp) // Respiro maior entre blocos
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
            items = workoutList.filter { workout -> workout.category.contains(selectedCategory) },
            key = { it.id }
        ) { workout ->
            FitverseCardRecommended(
                workout = workout,
                onClick = {

                }
            )
        }
    }
}

@Composable
fun WorkoutCategorySelector(selected: WorkoutCategory, onSelect: (WorkoutCategory) -> Unit) {
    val cs = MaterialTheme.colorScheme

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(WorkoutCategory.entries.toTypedArray()) { category ->
            val isSelected = category == selected

            Surface(
                onClick = { onSelect(category) },
                shape = RoundedCornerShape(100.dp), // Sênior: Estilo 'Pill' fica muito mais orgânico em listas horizontais
                // Se não estiver selecionado, usamos a transparência sênior para recuar o item
                color = if (isSelected) cs.primary else cs.surface.copy(alpha = 0.4f),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) cs.primary else Color.White.copy(alpha = 0.05f)
                )
            ) {
                Text(
                    text = category.name.lowercase().replaceFirstChar { it.uppercase() },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                    color = if (isSelected) Color.White else cs.onSurfaceVariant,
                    fontSize = 13.sp,
                    letterSpacing = 0.5.sp
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
    val cs = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        // Sênior: Glassmorphism unificado
        colors = CardDefaults.cardColors(containerColor = cs.surface.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(24.dp),
        // Sênior: Borda em Tertiary (Verde Neon) com Alpha para indicar "Em Progresso"
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // Top Row (Título e Tempo)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = planName.uppercase(),
                        color = cs.onSurface,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 26.sp,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        color = cs.onSurfaceVariant.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // O tempo em Tertiary funciona como um cronômetro mental
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = time,
                        color = cs.tertiary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "DURATION",
                        color = cs.onSurfaceVariant,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Row (Nested Glassmorphism)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WorkoutStatBox(label = "VOLUME", value = volume, unit = "kg", modifier = Modifier.weight(1f))
                WorkoutStatBox(label = "SETS", value = setsCompleted, unit = "/$setsTotal", modifier = Modifier.weight(1f))
                WorkoutStatBox(label = "BPM", value = bpm, unit = "", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botão de Iniciar/Retomar Treino
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = cs.tertiary), // Botão de Ação Ativa em Tertiary
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = 0.8f), // Ícone escuro sobre botão neon gera muito contraste
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "RESUME SESSION",
                    color = Color.Black.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp,
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
        color = cs.surface.copy(alpha = 0.7f), // Glassmorphism padrão
        onClick = onCreate,
        border = BorderStroke(1.dp, cs.primary.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            cs.primary.copy(alpha = 0.1f), // Glow inicial reforçado
                            Color.Transparent
                        )
                    )
                )
        ){
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(cs.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AddCircleOutline,
                        contentDescription = null,
                        tint = cs.primary,
                        modifier = Modifier.size(28.dp)
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
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Create Workout Plan",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = cs.onSurface
                    )
                    Text(
                        text = "Build a routine & track progress",
                        fontSize = 12.sp,
                        color = cs.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }

                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = cs.onSurfaceVariant,
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
    val cs = MaterialTheme.colorScheme
    Surface(
        modifier = modifier,
        // Sênior: Transparência profunda para simular um 'buraco' (inner shadow) no card de vidro
        color = Color.Black.copy(alpha = 0.2f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.03f))
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                color = cs.onSurfaceVariant,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    color = cs.onSurface, // Valor principal em branco para leitura rápida
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                if (unit.isNotEmpty()) {
                    Text(
                        text = unit,
                        color = cs.tertiary, // A unidade ou barra total (ex: /18) ganha o toque neon
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 3.dp, start = 2.dp)
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
    val category: List<WorkoutCategory>
)
val workoutList = listOf(
    WorkoutItem(
        1,
        "Upper Body Strength",
        "35 min",
        140,
        listOf(
            WorkoutCategory.STRENGTH,
            WorkoutCategory.ALL
        )
    ),
    WorkoutItem(
        2,
        "Leg Day Power",
        "40 min",
        160,
        listOf(
            WorkoutCategory.STRENGTH,
            WorkoutCategory.ALL
        )
    ),

    WorkoutItem(
        3,
        "Morning Cardio Burn",
        "25 min",
        120,
        listOf(
            WorkoutCategory.CARDIO,
            WorkoutCategory.ALL
        )
    ),
    WorkoutItem(
        4,
        "Endurance Run",
        "45 min",
        180,
        listOf(
            WorkoutCategory.CARDIO,
            WorkoutCategory.ALL
        )
    ),

    WorkoutItem(
        5,
        "HIIT Fat Burner",
        "20 min",
        150,
        listOf(
            WorkoutCategory.HIIT,
            WorkoutCategory.ALL
        )
    ),
    WorkoutItem(
        6,
        "Explosive HIIT",
        "30 min",
        200,
        listOf(
            WorkoutCategory.HIIT,
            WorkoutCategory.ALL
        )
    ),

    WorkoutItem(
        7,
        "Full Body Stretch",
        "15 min",
        80,
        listOf(
            WorkoutCategory.STRETCH,
            WorkoutCategory.ALL
        )
    ),
    WorkoutItem(
        8,
        "Post Workout Stretch",
        "20 min",
        90,
        listOf(
            WorkoutCategory.STRETCH,
            WorkoutCategory.ALL
        )
    )
)