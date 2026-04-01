package com.example.presentation.screens.ui.historic

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FilterAltOff
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expect.DateTimeManager
import com.example.presentation.screens.widgets.FitverseIconBack
import com.example.presentation.screens.widgets.FitverseTopAppBar
import com.example.presentation.theme.PADDING_TOPAPPBAR_DEFAULT_HORIZONTAL
import com.example.presentation.theme.PADDING_TOPAPPBAR_DEFAULT_VERTICAL

data class WorkoutHistory(
    val id: String,
    val title: String,
    val dateDisplay: String, // O que aparece na tela (ex: "Hoje")
    val timestamp: Long,      // O valor real para filtros e cálculos
    val duration: String,
    val totalVolume: String,
    val muscleGroups: List<String>
)
val currentMoment = DateTimeManager.now()
val dayMillis = 86_400_000L

val mockHistory = listOf(
    WorkoutHistory(
        id = "1",
        title = "Push Day",
        dateDisplay = "Hoje",
        timestamp = currentMoment, // Usando o expect
        duration = "1h 15m",
        totalVolume = "5.200 kg",
        muscleGroups = listOf("Peito")
    ),
    WorkoutHistory(
        id = "2",
        title = "Leg Day",
        dateDisplay = "Ontem",
        timestamp = currentMoment - dayMillis,
        duration = "1h 30m",
        totalVolume = "8.100 kg",
        muscleGroups = listOf("Pernas")
    ),
    WorkoutHistory(
        id = "3",
        title = "Leg Day",
        dateDisplay = "Ontem",
        timestamp = currentMoment - dayMillis * 2,
        duration = "1h 30m",
        totalVolume = "8.100 kg",
        muscleGroups = listOf("Pernas")
    ),
    WorkoutHistory(
        id = "4",
        title = "Leg Day",
        dateDisplay = "Ontem",
        timestamp = currentMoment - dayMillis * 2,
        duration = "1h 30m",
        totalVolume = "8.100 kg",
        muscleGroups = listOf("Pernas")
    ),
    WorkoutHistory(
        id = "5",
        title = "Leg Day",
        dateDisplay = "Ontem",
        timestamp = currentMoment - dayMillis * 2,
        duration = "1h 30m",
        totalVolume = "8.100 kg",
        muscleGroups = listOf("Pernas")
    )
)


// ... (Suas data classes e mocks permanecem iguais) ...
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricScreen(
    onBack: () -> Unit,
    allHistory: List<WorkoutHistory> = mockHistory
) {
    val cs = MaterialTheme.colorScheme
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    val datePickerState = rememberDatePickerState()

    val filteredHistory = remember(selectedDateMillis, allHistory) {
        if (selectedDateMillis == null) {
            allHistory
        } else {
            val targetDate = DateTimeManager.formatMillisToDate(selectedDateMillis!!)
            allHistory.filter { workout ->
                val workoutDate = DateTimeManager.formatMillisToDate(workout.timestamp)
                workoutDate == targetDate
            }
        }
    }

    val totalWorkouts = filteredHistory.size
    val totalVolume = filteredHistory.sumOf { parseVolume(it.totalVolume) }
    val totalMinutes = filteredHistory.sumOf { parseDurationToMinutes(it.duration) }

    val summaryTitle = selectedDateMillis?.let {
        DateTimeManager.formatMillisToDate(it)
    } ?: "Visão Geral"

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateMillis = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) { Text("Filtrar", color = cs.primary) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar", color = cs.onSurface) }
            },
            colors = DatePickerDefaults.colors(
                containerColor = cs.surface,
                titleContentColor = cs.primary,
                headlineContentColor = cs.onBackground
            )
        ) { DatePicker(state = datePickerState) }
    }

    Scaffold(
        topBar = {
            FitverseTopAppBar(
                title = "HISTÓRICO",
                onBack = onBack,
                actions = {
                    // Ícone ganha destaque Neon quando há um filtro ativo
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            Icons.Rounded.CalendarMonth,
                            null,
                            tint = if (selectedDateMillis != null) cs.primary else cs.onSurface
                        )
                    }
                },
            )
        },
        containerColor = cs.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues).padding(5.dp),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MonthlySummaryCard(
                    title = summaryTitle.uppercase(),
                    count = totalWorkouts.toString(),
                    time = "${totalMinutes / 60}h ${totalMinutes % 60}m",
                    volume = if (totalVolume >= 1000) "${totalVolume / 1000}t" else "${totalVolume}kg"
                )
            }

            if (selectedDateMillis != null) {
                item {
                    FilterChip(
                        selected = true,
                        onClick = { selectedDateMillis = null },
                        label = { Text("Filtro: $summaryTitle", fontWeight = FontWeight.Bold) },
                        trailingIcon = { Icon(Icons.Rounded.Close, null, Modifier.size(16.dp)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = cs.primary.copy(alpha = 0.15f),
                            selectedLabelColor = cs.primary,
                            selectedTrailingIconColor = cs.primary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = cs.primary.copy(alpha = 0.5f),
                            enabled = true, selected = true
                        )
                    )
                }
            }

            items(filteredHistory, key = { it.id }) { workout ->
                WorkoutHistoryCard(workout)
            }

            if (filteredHistory.isEmpty()) {
                item {
                    EmptyWorkoutHistory(
                        onClearFilters = { selectedDateMillis = null }
                    )
                }
            }
        }
    }
}
@Composable
fun EmptyWorkoutHistory(
    onClearFilters: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. Ícone Estilizado com "Glow"
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(cs.primary.copy(alpha = 0.05f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Círculo interno mais denso
            Surface(
                modifier = Modifier.size(70.dp),
                shape = CircleShape,
                color = cs.surface,
                border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.SearchOff,
                        contentDescription = null,
                        tint = cs.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Título e Descrição
        Text(
            text = "SILÊNCIO NO GINÁSIO",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = cs.onBackground,
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Não encontramos nenhum registro para os filtros selecionados.",
            style = MaterialTheme.typography.bodyMedium,
            color = cs.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 3. Botão de Ação (CTA) - Limpar Filtros
        OutlinedButton(
            onClick = onClearFilters,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, cs.primary.copy(alpha = 0.5f)),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = cs.primary
            )
        ) {
            Icon(Icons.Rounded.FilterAltOff, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                "LIMPAR FILTROS",
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}
@Composable
fun MonthlySummaryCard(title: String, count: String, time: String, volume: String) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = cs.onSurfaceVariant,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryStatItem("Treinos", count, Icons.Rounded.History, cs.primary)
                SummaryStatItem("Tempo", time, Icons.Rounded.Timer, cs.secondary)
                SummaryStatItem("Volume", volume, Icons.Rounded.FitnessCenter, cs.primary)
            }
        }
    }
}

@Composable
fun SummaryStatItem(label: String, value: String, icon: ImageVector, accentColor: Color) {
    val cs = MaterialTheme.colorScheme

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(accentColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = accentColor)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, fontWeight = FontWeight.Black, fontSize = 18.sp, color = cs.onBackground)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
    }
}

@Composable
fun WorkoutHistoryCard(workout: WorkoutHistory) {
    val cs = MaterialTheme.colorScheme

    // Usamos Surface para a estrutura, mas com a cor de fundo ultra-escura
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        // Mudamos de cs.surface para cs.background (ou um tom ainda mais escuro)
        color = cs.surface,
        // Aumentamos levemente a visibilidade da borda para o card não sumir no fundo
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = workout.title.uppercase(), // Uppercase para visual mais técnico
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black, // Peso máximo para o título
                        color = cs.onBackground,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = workout.dateDisplay,
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant.copy(alpha = 0.6f) // Texto de suporte mais discreto
                    )
                }

                // Badge de Duração (Neon Glass) - Mantemos o brilho para contraste
                Surface(
                    color = cs.primary.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(0.5.dp, cs.primary.copy(alpha = 0.25f))
                ) {
                    Text(
                        text = workout.duration,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = cs.primary // Neon Volt
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Muscle Groups Chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                workout.muscleGroups.forEach { muscle ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        // O chip agora é levemente mais claro (surface) para contrastar com o card escuro (background)
                        color = cs.surface.copy(alpha = 0.4f),
                        border = BorderStroke(0.5.dp, cs.outline.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = muscle.uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = cs.onSurfaceVariant,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            // Divisor quase invisível (estilo premium)
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 0.5.dp,
                color = cs.outline.copy(alpha = 0.08f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.FitnessCenter,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = cs.secondary // Roxo elétrico
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "VOLUME TOTAL",
                        style = MaterialTheme.typography.labelSmall,
                        color = cs.onSurfaceVariant.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = workout.totalVolume,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Black,
                    color = cs.onBackground // Destaque no valor numérico
                )
            }
        }
    }
}

// ... (Helpers de parseVolume e parseDurationToMinutes mantidos) ...



private fun parseVolume(volumeStr: String): Double {
    // Remove " kg", troca "," por "." e converte para Double
    return volumeStr.replace(" kg", "").replace(".", "").toDoubleOrNull() ?: 0.0
}

private fun parseDurationToMinutes(durationStr: String): Int {
    // Ex: "1h 15m" -> 75
    val hours = durationStr.substringBefore("h", "0").trim().toIntOrNull() ?: 0
    val minutes = durationStr.substringAfter("h", "0").substringBefore("m", "0").trim().toIntOrNull() ?: 0
    return (hours * 60) + minutes
}