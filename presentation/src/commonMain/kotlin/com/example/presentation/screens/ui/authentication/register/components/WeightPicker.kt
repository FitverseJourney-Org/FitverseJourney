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
    initialWeight: Int = 70,
    onWeightSelected: (Int) -> Unit
) {
    val weights = remember { (30..200).toList() }
    val initialIndex = weights.indexOf(initialWeight).takeIf { it >= 0 } ?: 40

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            WheelPicker(
                items = weights,
                initialIndex = initialIndex,
                onItemSelected = onWeightSelected
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