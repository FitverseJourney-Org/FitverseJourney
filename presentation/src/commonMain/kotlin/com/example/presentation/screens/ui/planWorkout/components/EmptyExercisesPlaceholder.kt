package com.example.presentation.screens.ui.planWorkout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyExercisesPlaceholder(colors: ColorScheme) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp), // Aumentei o respiro para destacar o vazio
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícone com a cor de contorno/variante para parecer um "ghost" element
        Icon(
            imageVector = Icons.Rounded.FitnessCenter,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = colors.onSurfaceVariant.copy(alpha = 0.15f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "NENHUMA ATIVIDADE",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Black,
            // Texto sutil para indicar que o slot está disponível
            color = colors.onSurfaceVariant.copy(alpha = 0.3f),
            letterSpacing = 2.sp
        )

        Text(
            text = "Aguardando definição da missão",
            style = MaterialTheme.typography.bodySmall,
            color = colors.onSurfaceVariant.copy(alpha = 0.2f),
            textAlign = TextAlign.Center
        )
    }
}