package com.example.presentation.ui.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.FitverseColors
import com.example.presentation.widgets.DarkGamifiedDashboardBackground

private val categories = listOf("Força", "Cardio", "Hipertrofia", "HIIT")

private val recommendedWorkouts = listOf(
    Triple("💪", "PEITO & TRÍCEPS", "Intermediário · 45 min · 18 séries" to "+150XP"),
    Triple("🏋️", "COSTAS & BÍCEPS", "Intermediário · 50 min · 20 séries" to "+160XP"),
    Triple("🦵", "PERNAS COMPLETO",  "Avançado · 60 min · 24 séries"      to "+200XP"),
)

@Composable
fun WorkoutScreen(
    onStartWorkout: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize(),
        content = {
            DarkGamifiedDashboardBackground()
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets(0,0,0,0),
                containerColor = Color.Transparent,
                content = {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item { WorkoutHeader() }
                        item { WorkoutMainCard(onStart = onStartWorkout) }
                        item {
                            CategoryFilter(
                                selected = selectedCategory,
                                onSelect = { selectedCategory = it })
                        }
                        item { SectionTitle("RECOMENDADOS PARA VOCÊ") }

                        items(recommendedWorkouts) { (emoji, name, detail) ->
                            WorkoutCard(
                                emoji = emoji,
                                name = name,
                                subtitle = detail.first,
                                xp = detail.second
                            )
                        }
                    }
                }
            )
        }
    )
}

// ── Sub-composables privados ──────────────────

@Composable
private fun WorkoutHeader() {
    Column{
        Text(
            text = "SEGUNDA-FEIRA",
            color = FitverseColors.TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.5.sp
        )
        Text(
            text = "TREINO DO DIA",
            color = FitverseColors.TextPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FitChip("LEVEL 23", FitverseColors.Purple)
            FitChip("+150 XP",  FitverseColors.AccentDim, textColor = FitverseColors.Accent)
        }
    }
}

@Composable
fun FitChip(
    text: String,
    color: Color,
    textColor: Color = FitverseColors.Bg

) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(color)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun WorkoutMainCard(onStart: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .border(width = 1.dp, color = Color(0xFF2a2a35), shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cs.surface)
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "HYPERTROPHY A",
                    color = FitverseColors.TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FitverseColors.Accent)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("42:15", color = FitverseColors.Bg
                        , fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text("Fase 2 · Semana 3", color = FitverseColors.TextMuted, fontSize = 13.sp)
            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                WorkoutStat("18",   "Séries")
                WorkoutStat("8.4k", "Volume")
                WorkoutStat("134",  "BPM")
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitverseColors.Accent)
            ) {
                Text(
                    "▶  INICIAR TREINO",
                    color = FitverseColors.Bg
                    ,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }
    }
    Spacer(Modifier.height(20.dp))
}

@Composable
private fun CategoryFilter(selected: Int, onSelect: (Int) -> Unit) {
    val cs = MaterialTheme.colorScheme
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(categories) { index, cat ->
            val isSelected = index == selected
            Box(
                modifier = Modifier
                    .border(width = 1.dp, color = Color(0xFF2a2a35), shape = RoundedCornerShape(50.dp))
                    .clip(RoundedCornerShape(50.dp))
                    .background(if (isSelected) FitverseColors.Accent else cs.surface)
                    .clickable { onSelect(index) }
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                Text(
                    cat,
                    color = if (isSelected) FitverseColors.Bg
                    else FitverseColors.TextMuted,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
    Spacer(Modifier.height(20.dp))
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = FitverseColors.TextMuted,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(horizontal = 20.dp)
    )
    Spacer(Modifier.height(10.dp))
}

@Composable
fun WorkoutCard(
    emoji: String,
    name: String,
    subtitle: String,
    xp: String,
    onClick: () -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .border(width = 1.dp, color = Color(0xFF2a2a35), shape = RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(cs.surface.copy(alpha = .5f))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(FitverseColors.Surface2),
            contentAlignment = Alignment.Center
        ) { Text(emoji, fontSize = 24.sp) }

        Spacer(Modifier.width(14.dp))

        Column(Modifier.weight(1f)) {
            Text(name,     color = FitverseColors.TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = FitverseColors.TextMuted,   fontSize = 12.sp)
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(FitverseColors.AccentDim.copy(alpha = 0.2f))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) { Text(xp, color = FitverseColors.Accent, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

        Spacer(Modifier.width(8.dp))
        Text("›", color = FitverseColors.TextMuted, fontSize = 18.sp)
    }
}

@Composable
fun WorkoutStat(value: String, label: String) {
    Column {
        Text(value, color = FitverseColors.TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(label, color = FitverseColors.TextMuted,   fontSize = 12.sp)
    }
}