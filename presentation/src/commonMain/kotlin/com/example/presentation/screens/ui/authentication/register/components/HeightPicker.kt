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
fun HeightPicker(
    initialHeight: Int = 170,
    onHeightSelected: (Int) -> Unit
) {
    val heights = remember { (100..250).toList() }
    val initialIndex = heights.indexOf(initialHeight).takeIf { it >= 0 } ?: 70

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            WheelPicker(
                items = heights,
                initialIndex = initialIndex,
                onItemSelected = onHeightSelected
            )
        }
        Text(
            text = "cm",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}