package com.example.presentation.screens.ui.authentication.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expect.DateTimeManager

@Composable
fun DatePicker(
    onDateSelected: (day: Int, month: Int, year: Int) -> Unit
) {
    // 1. Captura o ano máximo permitido (Ano Atual - 18) usando a API Calendar
    val maxAllowedYear = remember {
        DateTimeManager.getCurrentYear() - 18
    }

    val days = remember { (1..31).toList() }
    val months = remember { listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez") }

    // 2. Gera a lista de anos de 1950 até o ano máximo permitido para +18
    val years = remember(maxAllowedYear) { (1950..maxAllowedYear).toList() }

    var selectedDay by remember { mutableIntStateOf(15) }
    var selectedMonth by remember { mutableStateOf(months[6]) }

    // 3. Garante que o ano padrão selecionado (1995) seja válido, caso contrário pega o último ano disponível
    var selectedYear by remember {
        mutableIntStateOf(if (years.contains(1995)) 1995 else maxAllowedYear)
    }

    LaunchedEffect(selectedDay, selectedMonth, selectedYear) {
        val monthIndex = months.indexOf(selectedMonth) + 1
        onDateSelected(selectedDay, monthIndex, selectedYear)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.weight(0.8f)) {
            WheelPicker(
                items = days,
                initialIndex = days.indexOf(selectedDay).takeIf { it >= 0 } ?: 14,
                onItemSelected = { selectedDay = it }
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            WheelPicker(
                items = months,
                initialIndex = months.indexOf(selectedMonth).takeIf { it >= 0 } ?: 6,
                onItemSelected = { selectedMonth = it }
            )
        }
        Box(modifier = Modifier.weight(1.2f)) {
            WheelPicker(
                items = years,
                // Fallback seguro dinâmico para evitar OutOfBounds
                initialIndex = years.indexOf(selectedYear).takeIf { it >= 0 } ?: (years.size / 2),
                onItemSelected = { selectedYear = it }
            )
        }
    }
}