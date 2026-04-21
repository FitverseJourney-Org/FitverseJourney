package com.example.presentation.screens.ui.authentication.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WeightPicker(
    initialWeight: Double = 70.0,
    onWeightSelected: (Double) -> Unit
) {
    val weights = remember { (300..2000).map { it / 10.0 } }
    val initialIndex = weights.indexOfFirst { it == initialWeight }.takeIf { it >= 0 }
        ?: weights.indexOfFirst { it == 70.0 }.coerceAtLeast(0)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            WheelPicker(
                items = weights,
                initialIndex = initialIndex,
                onItemSelected = onWeightSelected,
                itemContent = { v ->
                    val intPart = v.toInt()
                    val decPart = kotlin.math.round((v - intPart) * 10).toInt()
                    "$intPart.$decPart"
                }
            )
        }
        Text(
            text = "kg",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}