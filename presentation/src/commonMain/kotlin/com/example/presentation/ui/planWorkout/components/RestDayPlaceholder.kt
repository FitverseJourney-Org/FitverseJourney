package com.example.presentation.ui.planWorkout.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RestDayPlaceholder(colors: ColorScheme) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        color = colors.tertiary.copy(alpha = 0.05f), // Verde Neon suave para descanso
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, colors.tertiary.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Rounded.Spa, null, tint = colors.tertiary, modifier = Modifier.size(40.dp))
            Spacer(Modifier.height(12.dp))
            Text("RECUPERAÇÃO ATIVA", fontWeight = FontWeight.Black, color = colors.tertiary)
            Text(
                "O descanso é onde o músculo realmente cresce.",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}