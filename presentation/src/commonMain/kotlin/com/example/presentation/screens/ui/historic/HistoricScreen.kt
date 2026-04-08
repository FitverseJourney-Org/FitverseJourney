package com.example.presentation.screens.ui.historic

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
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
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentDataType.Companion.Date
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expect.DateTimeFormatter
import com.example.expect.DateTimeManager
import com.example.expect.LocalAppLocale
import com.example.expect.NumberFormatter
import com.example.presentation.screens.widgets.FitverseIconBack
import com.example.presentation.screens.widgets.FitverseTopAppBar
import com.example.presentation.theme.PADDING_TOPAPPBAR_DEFAULT_HORIZONTAL
import com.example.presentation.theme.PADDING_TOPAPPBAR_DEFAULT_VERTICAL
import com.example.presentation.theme.THIRTY_DAYS_MILLIS

// Mocks e Data Classes mantidos iguais
data class WorkoutHistory(
    val id: String,
    val title: String,
    val dateDisplay: String,
    val timestamp: Long,
    val duration: String,
    val totalVolume: String,
    val muscleGroups: List<String>
)
// Encapsula o estado da busca para não poluir o Composable principal
data class HistoricSearchState(
    val startDate: Long? = null,
    val endDate: Long? = null,
    val isPremium: Boolean = false
) {
    val isFilterActive: Boolean get() = startDate != null
}
val currentMoment = DateTimeManager.now()
val dayMillis = 86_400_000L

val mockHistory = listOf(
    WorkoutHistory("1", "Push Day", "Hoje", currentMoment, "1h 15m", "5.200 kg", listOf("Peito", "Tríceps")),
    WorkoutHistory("2", "Leg Day", "Ontem", currentMoment - dayMillis, "1h 30m", "8.100 kg", listOf("Pernas")),
    WorkoutHistory("3", "Pull Day", "Ontem", currentMoment - dayMillis * 2, "1h 10m", "6.300 kg", listOf("Costas", "Bíceps"))
)


// ... (Suas data classes e mocks permanecem iguais) ...
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricScreen(
    onBack: () -> Unit,
    allHistory: List<WorkoutHistory> = mockHistory,
    isPremium: Boolean = false
) {
    val cs = MaterialTheme.colorScheme

    // 1. Estados de UI
    var showDatePicker by remember { mutableStateOf(false) }
    var searchState by remember { mutableStateOf(HistoricSearchState(isPremium = isPremium)) }
    val dateRangePickerState = rememberDateRangePickerState()

    // 2. Performance: Filtro e Cálculos pesados em derivedStateOf
    // Isso garante que o filtro só rode quando searchState ou allHistory mudarem de verdade
    val filteredData = remember(searchState, allHistory) {
        // 1. Primeiro filtramos a lista bruta
        val listResult = if (searchState.startDate == null) {
            allHistory
        } else {
            val start = searchState.startDate!!
            val end = (searchState.endDate ?: start) + 86_399_999L
            allHistory.filter { it.timestamp in start..end }
        }

        // 2. Realizamos os cálculos baseados na lista filtrada
        val totalVolumeRaw = listResult.sumOf { parseVolume(it.totalVolume) }
        val totalMinutesRaw = listResult.sumOf { parseDurationToMinutes(it.duration) }

        // 3. Retornamos o objeto com todas as propriedades necessárias
        object {
            val list = listResult // <-- Agora 'list' está disponível externamente
            val totalWorkouts = listResult.size
            val totalVolume = totalVolumeRaw
            val totalMinutes = totalMinutesRaw
            val volumeDisplay = if (totalVolumeRaw >= 1000) {
                "${NumberFormatter.formatOneDecimal(totalVolumeRaw / 1000)}t"
            } else {
                "${totalVolumeRaw.toInt()}kg"
            }
        }
    }

    // 3. Formatação do Título (Extraído para legibilidade)
    val displayDateRange = remember(searchState) {
        if (searchState.startDate == null) "Visão Geral"
        else {
            val start = DateTimeFormatter.formatShortDate(searchState.startDate!!)
            val end = searchState.endDate?.let { " - ${DateTimeFormatter.formatShortDate(it)}" } ?: ""
            "$start$end"
        }
    }

    Scaffold(
        topBar = {
            FitverseTopAppBar(
                title = "HISTÓRICO",
                onBack = onBack,
                actions = {
                    FilterActionIcon(
                        isActive = searchState.isFilterActive,
                        isPremium = isPremium,
                        onClick = { showDatePicker = true }
                    )
                },
            )
        },
        containerColor = cs.background
    ) { paddingValues ->

        // Diálogo de Data (Extraído para função dedicada abaixo)
        if (showDatePicker) {
            FitverseDateRangePicker(
                state = dateRangePickerState,
                isPremium = isPremium,
                onDismiss = { showDatePicker = false },
                onConfirm = { start, end ->
                    searchState = searchState.copy(startDate = start, endDate = end)
                    showDatePicker = false
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .animateContentSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Espaçamento levemente mais compacto
        ) {
            // Card de Resumo (Design Polido)
            item {
                MonthlySummaryCard(
                    title = displayDateRange.uppercase(),
                    count = filteredData.totalWorkouts.toString(),
                    time = "${filteredData.totalMinutes / 60}h ${filteredData.totalMinutes % 60}m",
                    volume = filteredData.volumeDisplay // Usa a variável já formatada
                )
            }

            // Chip de Filtro com Feedback de "Limpar"
            if (searchState.isFilterActive) {
                item {
                    ActiveFilterIndicator(
                        label = displayDateRange,
                        onClear = { searchState = searchState.copy(startDate = null, endDate = null) }
                    )
                }
            }

            // Lista de Itens ou Empty State
            if (filteredData.list.isEmpty()) { // <-- Use .list aqui
                item {
                    EmptyWorkoutHistory(onClearFilters = {
                        searchState = searchState.copy(startDate = null, endDate = null)
                    })
                }
            } else {
                items(filteredData.list, key = { it.id }) { workout -> // <-- E .list aqui
                    WorkoutHistoryCard(workout = workout, onClick = { /* Detalhes */ })
                }
            }
        }
    }
}
@Composable
fun ActiveFilterIndicator(
    label: String,
    onClear: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Usamos um Row com animação para uma transição suave ao aparecer/desaparecer
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = cs.primary.copy(alpha = 0.05f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, cs.primary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Ícone sutil para reforçar que é um filtro de calendário
                Icon(
                    imageVector = Icons.Rounded.CalendarMonth,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = cs.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Exibindo período: ",
                    style = MaterialTheme.typography.labelMedium,
                    color = cs.onSurfaceVariant.copy(alpha = 0.7f)
                )

                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = cs.primary
                )
            }

            // Botão de fechar (Limpar) com área de toque otimizada (48dp padrão UX)
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(cs.primary.copy(alpha = 0.1f))
                    .clickable { onClear() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Limpar Filtro",
                    modifier = Modifier.size(14.dp),
                    tint = cs.primary
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FitverseDateRangePicker(
    state: DateRangePickerState,
    isPremium: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Long?, Long?) -> Unit
) {
    val selectedStart = state.selectedStartDateMillis
    val selectedEnd = state.selectedEndDateMillis

    val isRangeValid = remember(selectedStart, selectedEnd, isPremium) {
        if (isPremium || selectedStart == null || selectedEnd == null) true
        else (selectedEnd - selectedStart) <= THIRTY_DAYS_MILLIS
    }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedStart, selectedEnd) },
                enabled = isRangeValid && selectedStart != null
            ) { Text("APLICAR", fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("CANCELAR") }
        }
    ) {
        DateRangePicker(
            state = state,
            title = {
                Text("Filtrar Período", Modifier.padding(24.dp), style = MaterialTheme.typography.titleLarge)
            },
            headline = {
                PremiumValidationHeader(isPremium, isRangeValid)
            },
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )
        )
    }
}
@Composable
private fun PremiumValidationHeader(isPremium: Boolean, isValid: Boolean) {
    val color = if (isValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    Row(Modifier.padding(horizontal = 24.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            if (isPremium) Icons.Rounded.History else Icons.Rounded.Timer,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = color
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = when {
                isPremium -> "Modo Premium: Sem limite de busca"
                !isValid -> "Limite de 30 dias excedido (Plano Free)"
                else -> "Limite plano Free: 30 dias"
            },
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}
@Composable
private fun FilterActionIcon(isActive: Boolean, isPremium: Boolean, onClick: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Box {
        IconButton(onClick = onClick) {
            Icon(
                Icons.Rounded.CalendarMonth,
                contentDescription = null,
                tint = if (isActive) cs.primary else cs.onSurfaceVariant
            )
        }
        if (isActive) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(8.dp)
                    .background(if (isPremium) Color(0xFFFFD700) else cs.primary, CircleShape)
                    .border(1.5.dp, cs.background)
            )
        }
    }
}
@Composable
fun EmptyWorkoutHistory(
    onClearFilters: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(cs.primary.copy(alpha = 0.08f), CircleShape), // Leve aumento no alfa para visibilidade
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.size(70.dp),
                shape = CircleShape,
                color = cs.surface,
                border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f)),
                shadowElevation = 4.dp // Dá uma sensação 3D de botão vazio
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.SearchOff,
                        contentDescription = null,
                        tint = cs.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "SILÊNCIO NO GINÁSIO",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = cs.onBackground,
            letterSpacing = 1.sp // Reduzido de 1.5 para melhor leitura
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Não encontramos nenhum registro\npara os filtros selecionados.",
            style = MaterialTheme.typography.bodyMedium,
            color = cs.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp, // Melhor legibilidade em múltiplas linhas
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = onClearFilters,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, cs.primary.copy(alpha = 0.5f)),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = cs.primary,
                containerColor = cs.primary.copy(alpha = 0.05f) // Fundo muito sutil no botão
            ),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp) // Área de clique UX
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

// Helpers
@Composable
fun MonthlySummaryCard(title: String, count: String, time: String, volume: String) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.08f)), // Borda mais sutil
        shadowElevation = 2.dp // Sutil elevação para destacar do fundo
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = cs.onSurfaceVariant,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryStatItem("Treinos", count, Icons.Rounded.History, cs.primary)
                SummaryStatItem("Tempo", time, Icons.Rounded.Timer, cs.secondary)
                SummaryStatItem("Volume", volume, Icons.Rounded.FitnessCenter, cs.tertiary) // Usar tertiary se houver, ou primary
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
                .size(44.dp) // Área de toque visualmente melhorada
                .clip(RoundedCornerShape(14.dp))
                .background(accentColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(22.dp), tint = accentColor)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = value, fontWeight = FontWeight.Black, fontSize = 18.sp, color = cs.onBackground)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
    }
}

@Composable
fun WorkoutHistoryCard(workout: WorkoutHistory, onClick: () -> Unit) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }, // Adicionado affordance de clique
        shape = RoundedCornerShape(20.dp),
        color = cs.surface.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top // Alinhado ao topo para melhor encaixe do badge
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = workout.title, // Removido Uppercase forçado, deixado capitalizado padrão para legibilidade
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = cs.onBackground,
                        letterSpacing = 0.2.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = workout.dateDisplay,
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }

                // Badge de Duração (Neon Glass)
                Surface(
                    color = cs.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(0.5.dp, cs.primary.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = workout.duration,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = cs.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Muscle Groups Chips - Usando FlowRow no futuro ou Scroll, aqui mantemos Row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                workout.muscleGroups.forEach { muscle ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = cs.onSurface.copy(alpha = 0.05f), // Contraste sutil e elegante
                        border = BorderStroke(0.5.dp, cs.outline.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = muscle.uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp, // Levemente maior (de 9 para 10) para acessibilidade
                            fontWeight = FontWeight.Bold,
                            color = cs.onSurfaceVariant,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp, // Alterado para 1.dp com opacidade muito baixa para renderização melhor em telas HD
                color = cs.outline.copy(alpha = 0.05f)
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
                        modifier = Modifier.size(16.dp),
                        tint = cs.secondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "VOLUME TOTAL",
                        style = MaterialTheme.typography.labelSmall,
                        color = cs.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = workout.totalVolume,
                    style = MaterialTheme.typography.titleMedium, // Aumentado destaque do número
                    fontWeight = FontWeight.Black,
                    color = cs.onBackground
                )
            }
        }
    }
}
// ... (Helpers de parseVolume e parseDurationToMinutes mantidos) ...
private fun parseVolume(volumeStr: String): Double {
    return volumeStr.replace(" kg", "").replace(".", "").toDoubleOrNull() ?: 0.0
}

private fun parseDurationToMinutes(durationStr: String): Int {
    val hours = durationStr.substringBefore("h", "0").trim().toIntOrNull() ?: 0
    val minutes = durationStr.substringAfter("h", "0").substringBefore("m", "0").trim().toIntOrNull() ?: 0
    return (hours * 60) + minutes
}