package com.example.presentation.ui.planWorkout.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MuscleTag(name: String, isPrimary: Boolean) {
    val color = if (isPrimary) MaterialTheme.colorScheme.secondary else Color.Gray.copy(alpha = 0.5f)
    Surface(
        shape = CircleShape,
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Text(
            name.uppercase(),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isPrimary) color else Color.White
        )
    }
}