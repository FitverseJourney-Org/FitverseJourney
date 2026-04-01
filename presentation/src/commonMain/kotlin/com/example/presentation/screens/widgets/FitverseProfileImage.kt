package com.example.presentation.screens.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FitverseProfileImage(
    avatarInitials: String
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.size(54.dp),
        shape = CircleShape,
        color = cs.surfaceVariant, // Fundo mais escuro que a superfície normal
        border = BorderStroke(1.dp, cs.primary.copy(alpha = 0.5f)) // Borda Neon sutil
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = avatarInitials,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = cs.primary // Texto da inicial em Neon
            )
        }
    }
}