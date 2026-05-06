package com.example.presentation.ui.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Cores extraídas do Design (Ajuste no seu MaterialTheme/ColorPalette) ---
private val TextSecondaryHeader = Color(0xFF5A728A) // Tom azul/cinza do "BOM DIA"
private val TextPrimaryHeader = Color.White
private val SurfaceDark = Color(0xFF1C1C24)
private val IconDefaultColor = Color(0xFFA0A0B5)
private val SurfaceEnergy = Color(0xFF2A3018) // Fundo do botão de energia
private val EnergyAccentColor = Color(0xFFFFD700) // Raio amarelo

@Composable
fun HomeHeader(
    greeting: String,
    userName: String,
    onNotificationClick: () -> Unit,
    onEnergyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Bloco de Saudação
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = greeting.uppercase(),
                color = TextSecondaryHeader,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
            Text(
                text = userName.uppercase(),
                color = TextPrimaryHeader,
                fontSize = 26.sp, // Fonte robusta como no design
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )
        }

        // Bloco de Ações
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderIconButton(
                icon = Icons.Outlined.Notifications,
                contentDescription = "Notificações",
                containerColor = cs.surface,
                iconColor = IconDefaultColor,
                onClick = onNotificationClick
            )

            HeaderIconButton(
                icon = Icons.Filled.Bolt,
                contentDescription = "Ação Rápida / Energia",
                containerColor = SurfaceEnergy,
                iconColor = EnergyAccentColor,
                onClick = onEnergyClick
            )
        }
    }
}

// ==========================================
// COMPONENT: Reusable Icon Button
// ==========================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeaderIconButton(
    icon: ImageVector,
    contentDescription: String?,
    containerColor: Color,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    // Usando Surface para lidar com o click (ripple) de forma nativa e acessível
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp), // Arredondamento característico do design
        color = containerColor,
        border = BorderStroke(
            width = 1.dp,
            color = iconColor
        ),
        modifier = modifier.size(48.dp) // Padrão mínimo de touch target
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ==========================================
// PREVIEW
// ==========================================
