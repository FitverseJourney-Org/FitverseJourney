package com.example.presentation.screens.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.presentation.theme.DarkGamifiedColors

@Composable
fun FitverseIconStreak(
    streakCount: Int,
    onStreakClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Surface(
        onClick = { onStreakClick() },
        shape = RoundedCornerShape(16.dp),
        // Usando transparência para o efeito de vidro
        color = cs.surface.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, DarkGamifiedColors.PrimarySoft.copy(alpha = 0.2f)),
        modifier = Modifier.height(48.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.LocalFireDepartment,
                contentDescription = "Streaks",
                // Usamos a cor de Acento para o fogo se destacar como "premium"
                tint = Color(0xFFAD2B03),
                modifier = Modifier.size(22.dp)
            )
            if (streakCount > 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$streakCount",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    color = Color.White // Texto sempre branco para leitura no escuro
                )
            }
        }
    }
}


@Composable
fun FitverseIconNotifications(
    onNotificationsClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    Surface(
        onClick = { onNotificationsClick() },
        shape = RoundedCornerShape(16.dp),
        color = cs.surface.copy(alpha = 0.6f),
        border = BorderStroke(1.dp, DarkGamifiedColors.PrimarySoft.copy(alpha = 0.2f)),
        modifier = Modifier.size(48.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Rounded.NotificationsNone,
                contentDescription = "Notifications",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            // Bolinha de Notificação
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp) // Ajustado para ficar mais na ponta
                    .size(10.dp)
                    .background(Color(0xFFFF4D4D), CircleShape) // Vermelho vibrante Gamificado
                    .border(2.dp, cs.surface, CircleShape) // "Corta" a bolinha do fundo
            )
        }
    }
}

val SurfaceDark = Color(0xFF16171D)


@Composable
fun FitverseIconBack(
    onBack: () -> Unit,
){
    IconButton(
        onClick = { onBack() },
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Icon(Icons.Default.ChevronLeft, contentDescription = "Back", tint = Color.White)
    }
}

@Composable
fun FitverseIconClose(
    onClose: () -> Unit,
){
    IconButton(
        onClick = { onClose() },
        modifier = Modifier
            .background(SurfaceDark, RoundedCornerShape(12.dp))
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Icon(Icons.Default.Close, contentDescription = "Back", tint = Color.White)
    }
}