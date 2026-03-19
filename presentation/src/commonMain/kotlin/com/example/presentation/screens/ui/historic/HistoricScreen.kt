package com.example.presentation.screens.ui.historic

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.expect.DateTimeManager

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
        id = "2",
        title = "Leg Day",
        dateDisplay = "Ontem",
        timestamp = currentMoment - dayMillis * 2,
        duration = "1h 30m",
        totalVolume = "8.100 kg",
        muscleGroups = listOf("Pernas")
    )
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricScreen(
    navigateBack: () -> Unit,
    allHistory: List<WorkoutHistory> = mockHistory // Recebe a lista completa
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    val datePickerState = rememberDatePickerState()

    // 1. Filtragem Dinâmica
    // No mundo real, isso viria do ViewModel. Aqui filtramos a lista baseada na data.
    val filteredHistory = remember(selectedDateMillis, allHistory) {
        if (selectedDateMillis == null) {
            allHistory
        } else {
            // Formata a data que queremos buscar (ex: "15/03/2026")
            val targetDate = DateTimeManager.formatMillisToDate(selectedDateMillis!!)

            allHistory.filter { workout ->
                // Formata o timestamp do treino para o padrão "dd/MM/yyyy" e compara
                val workoutDate = DateTimeManager.formatMillisToDate(workout.timestamp)
                workoutDate == targetDate
            }
        }
    }

    // 2. Cálculos Dinâmicos para o Card de Resumo
    val totalWorkouts = filteredHistory.size
    val totalVolume = filteredHistory.sumOf { parseVolume(it.totalVolume) }
    val totalMinutes = filteredHistory.sumOf { parseDurationToMinutes(it.duration) }

    // Formata o título do mês ou data selecionada
    val summaryTitle = selectedDateMillis?.let {
        DateTimeManager.formatMillisToDate(it)
    } ?: "Março de 2026"

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateMillis = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) { Text("Filtrar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Histórico", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) { Icon(Icons.Default.ArrowBack, null) }
                },
                actions = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            null,
                            tint = if (selectedDateMillis != null) MaterialTheme.colorScheme.primary else LocalContentColor.current
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                windowInsets = WindowInsets(0,0,0,0)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card de Resumo Refletindo os Filtros
            item {
                MonthlySummaryCard(
                    title = summaryTitle,
                    count = totalWorkouts.toString(),
                    time = "${totalMinutes / 60}h ${totalMinutes % 60}m",
                    volume = if (totalVolume >= 1000) "${totalVolume / 1000}t" else "${totalVolume}kg"
                )
            }

            // Chip de Limpeza
            if (selectedDateMillis != null) {
                item {
                    FilterChip(
                        selected = true,
                        onClick = { selectedDateMillis = null },
                        label = { Text("Filtro: $summaryTitle") },
                        trailingIcon = { Icon(Icons.Default.Close, null, Modifier.size(18.dp)) }
                    )
                }
            }

            // Lista Filtrada
            items(filteredHistory) { workout ->
                WorkoutHistoryCard(workout)
            }

            if (filteredHistory.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        Text("Nenhum treino nesta data.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

// --- Componentes Atualizados para receberem Parâmetros ---

@Composable
fun MonthlySummaryCard(title: String, count: String, time: String, volume: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SummaryStatItem("Treinos", count, Icons.Default.History)
                SummaryStatItem("Tempo", time, Icons.Default.Timer)
                SummaryStatItem("Volume", volume, Icons.Default.FitnessCenter)
            }
        }
    }
}

// --- Helpers para o Cálculo (Essencial para o TCC demonstrar lógica) ---

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


@Composable
fun MonthlySummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Março de 2026",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryStatItem("Treinos", "12", Icons.Default.History)
                SummaryStatItem("Tempo", "18h", Icons.Default.Timer)
                SummaryStatItem("Volume", "45t", Icons.Default.FitnessCenter)
            }
        }
    }
}

@Composable
fun SummaryStatItem(label: String, value: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
        Text(text = value, fontWeight = FontWeight.Black, fontSize = 18.sp)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun WorkoutHistoryCard(workout: WorkoutHistory) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = workout.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = workout.dateDisplay,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // Badge de Duração
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = workout.duration,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Muscle Groups Chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                workout.muscleGroups.forEach { muscle ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = muscle, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.FitnessCenter,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Volume Total: ",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = workout.totalVolume,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}