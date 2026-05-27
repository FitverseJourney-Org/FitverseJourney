package org.fitverse.presentation.ui.workoutPlan.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PlanHeaderSection(
    planName: String,
    colors: ColorScheme,
    onNameChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
        Text(
            text = "NOME DO PLANO",
            style = MaterialTheme.typography.labelSmall,
            // Texto secundário: Cinza Muted
            color = colors.onSurfaceVariant,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = planName,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "Ex: PROTOCOLO TITÃ",
                    color = colors.onSurfaceVariant.copy(alpha = 0.4f)
                )
            },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colors.onSurface,
                unfocusedTextColor = colors.onSurface,
                // Surface escuro para o container
                focusedContainerColor = colors.surfaceVariant.copy(alpha = 0.4f),
                unfocusedContainerColor = colors.surfaceVariant.copy(alpha = 0.2f),
                // Brilho Neon ao focar: Roxo Primary
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.outline.copy(alpha = 0.5f)
            )
        )
    }
}