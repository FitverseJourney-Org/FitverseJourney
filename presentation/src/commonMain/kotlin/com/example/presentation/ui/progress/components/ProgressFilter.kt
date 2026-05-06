package com.example.presentation.ui.progress.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.domain.models.progress.Exercise
import com.example.presentation.ui.progress.PeriodFilter
import com.example.presentation.utils.MonthNames

// ─────────────────────────────────────────────────────────────────────────────
// SplitTabRow — substitui o DropdownMenu de ficha
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Tabs horizontais com scroll para seleção de ficha/split de treino.
 *
 * Troca o DropdownMenu original por um padrão de navegação mais natural
 * em mobile. O indicador segue a aba selecionada com animação padrão do M3.
 *
 * @param splits          Lista de fichas disponíveis.
 * @param selectedSplit   Ficha atualmente ativa.
 * @param onSplitSelected Callback ao tocar em uma aba.
 */
@Composable
fun SplitTabRow(
    splits: List<String>,
    selectedSplit: String,
    onSplitSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedIndex = splits.indexOf(selectedSplit).coerceAtLeast(0)

    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier.fillMaxWidth(),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            if (selectedIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
        divider = {
            HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 0.5.dp)
        },
    ) {
        splits.forEachIndexed { index, split ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onSplitSelected(split) },
                text = {
                    Text(
                        text = "Ficha $split",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (index == selectedIndex) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                    )
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(alpha = 0.45f),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ExerciseSelectorButton + ExerciseBottomSheet
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Botão que abre o [ExerciseBottomSheet] ao ser tocado.
 * Design inspirado em "pill selector" — mais espaçoso que um dropdown.
 */
@Composable
fun ExerciseSelectorButton(
    selectedExercise: Exercise?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.08f)),
        tonalElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Exercício",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = selectedExercise?.name ?: "Selecione um exercício",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (selectedExercise != null) Color.White
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Selecionar exercício",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/**
 * BottomSheet com busca textual para seleção de exercício.
 *
 * Substitui o DropdownMenu de 220dp — a busca embutida é essencial
 * quando o usuário tem muitos exercícios cadastrados.
 *
 * @param exercises          Lista de exercícios disponíveis para a ficha selecionada.
 * @param selectedExercise   Exercício atual (exibe checkmark).
 * @param onExerciseSelected Callback ao escolher um item.
 * @param onDismiss          Callback para fechar o sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseBottomSheet(
    exercises: List<Exercise>,
    selectedExercise: Exercise?,
    onExerciseSelected: (Exercise) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var query by remember { mutableStateOf("") }

    val filtered = remember(query, exercises) {
        if (query.isBlank()) exercises
        else exercises.filter { it.name.contains(query, ignoreCase = true) }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp),
        ) {
            // Header
            Text(
                text = "Selecionar Exercício",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            // Campo de busca
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = {
                    Text(
                        "Buscar exercício...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            )

            // Lista de exercícios filtrada
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                if (filtered.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                "Nenhum exercício encontrado",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                items(filtered, key = { it.id }) { exercise ->
                    val isSelected = exercise.id == selectedExercise?.id
                    ExerciseSheetItem(
                        exercise = exercise,
                        isSelected = isSelected,
                        onClick = { onExerciseSelected(exercise) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ExerciseSheetItem(
    exercise: Exercise,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        color = if (isSelected)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        else Color.Transparent,
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                )
                Text(
                    text = exercise.muscleGroup,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selecionado",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PeriodFilterChips — substitui os dois DropdownMenus de mês
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Linha de chips para seleção de mês início e fim do período.
 *
 * Usa [FilterChip] do M3 para manter consistência visual com o resto da UI.
 * Os chips de endMonth são desabilitados se forem anteriores ao startMonth.
 *
 * @param period          Filtro atual de período.
 * @param onPeriodChanged Callback com o novo filtro ao selecionar.
 */
@Composable
fun PeriodFilterChips(
    period: PeriodFilter,
    onPeriodChanged: (PeriodFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Início
        Text(
            text = "Período de análise",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "De",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(24.dp),
            )
            MonthChipRow(
                selectedMonth = period.startMonth,
                enabledRange = 1..12,
                onMonthSelected = { month ->
                    onPeriodChanged(
                        period.copy(
                            startMonth = month,
                            endMonth = maxOf(period.endMonth, month),
                        )
                    )
                },
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Até",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(24.dp),
            )
            MonthChipRow(
                selectedMonth = period.endMonth,
                enabledRange = period.startMonth..12,
                onMonthSelected = { month -> onPeriodChanged(period.copy(endMonth = month)) },
            )
        }
    }
}

@Composable
private fun MonthChipRow(
    selectedMonth: Int,
    enabledRange: IntRange,
    onMonthSelected: (Int) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 4.dp),
    ) {
        items((1..12).toList(), key = { it }) { month ->
            val isSelected = month == selectedMonth
            val isEnabled = month in enabledRange
            FilterChip(
                selected = isSelected,
                onClick = { if (isEnabled) onMonthSelected(month) },
                enabled = isEnabled,
                label = {
                    Text(
                        text = MonthNames.short(month),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.White,
                    selectedLabelColor = Color.Black,
                    containerColor = Color.White.copy(alpha = 0.06f),
                    labelColor = Color.White.copy(alpha = 0.6f),
                    disabledContainerColor = Color.White.copy(alpha = 0.02f),
                    disabledLabelColor = Color.White.copy(alpha = 0.2f),
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = isEnabled,
                    selected = isSelected,
                    borderColor = Color.White.copy(alpha = 0.12f),
                    selectedBorderColor = Color.Transparent,
                    borderWidth = 0.5.dp,
                ),
            )
        }
    }
}