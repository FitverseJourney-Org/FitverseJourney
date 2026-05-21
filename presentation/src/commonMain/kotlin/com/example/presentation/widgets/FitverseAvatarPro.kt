package org.fitverse.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==========================================
// 🎨 DESIGN SYSTEM TOKENS
// ==========================================
object FitverseTheme {
    val SurfaceDark = Color(0xFF16171D)
    val SurfaceOutline = Color(0xFF2A2B36)
    val SurfaceTrack = Color(0xFF22232C)

    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFF8B8D98)

    val ColorHp = Color(0xFFFF5252)
    val ColorEnergy = Color(0xFF00E676)
    val ColorXp = Color(0xFF8C52FF)
    val ColorDaily = Color(0xFFC6FF00) // Verde Limão
    val ColorStreak = Color(0xFFFF3D00) // Laranja

    // Tipografia baseada na sua escala (Plugar Bebas/DM Sans depois)
    val Typography = Typography(
        displayMedium = TextStyle(
            fontFamily = FontFamily.SansSerif, // Trocar por Bebas Neue/Industry
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            letterSpacing = 1.sp,
            color = TextPrimary
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily.SansSerif, // Trocar por DM Sans/Satoshi
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = TextSecondary
        ),
        labelSmall = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            letterSpacing = 0.5.sp,
            color = TextSecondary
        ),
        bodySmall = TextStyle( // JetBrains Mono fallback
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = TextSecondary
        )
    )
}




@Composable
fun AvatarWithLevel(level: Int, progress: Float) {
    Box(
        modifier = Modifier.padding(bottom = 12.dp), // Espaço para a tag de nível não ser cortada
        contentAlignment = Alignment.Center
    ) {
        // Círculo de fundo do Avatar
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(FitverseTheme.SurfaceTrack),
            contentAlignment = Alignment.Center
        ) {
            // Fallback para o ícone de espadas. Substitua por seu Drawable (Phosphor)
            Text("⚔️", fontSize = 32.sp)
        }

        // Barra de progresso circular (XP Ring)
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(72.dp),
            color = FitverseTheme.ColorXp,
            trackColor = Color.Transparent,
            strokeWidth = 4.dp,
            strokeCap = StrokeCap.Round
        )

        // Pill de Level (100px corner radius = CircleShape)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 12.dp)
                .background(FitverseTheme.ColorXp, CircleShape)
                .padding(horizontal = 12.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "LVL $level",
                style = FitverseTheme.Typography.labelSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            )
        }
    }
}

@Composable
fun StatRow(
    icon: ImageVector,
    label: String,
    current: Int,
    max: Int,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = FitverseTheme.Typography.labelSmall,
                color = color
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "$current/$max",
                style = FitverseTheme.Typography.bodySmall // JetBrains Mono
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        // Progress Bar Customizada para total controle de UI (Cantos e Cores)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(FitverseTheme.SurfaceTrack, CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (current.toFloat() / max.toFloat()).coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .background(color, CircleShape)
            )
        }
    }
}

@Composable
fun BottomStatItem(value: String, label: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = FitverseTheme.Typography.displayMedium.copy(fontSize = 20.sp),
            color = valueColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = FitverseTheme.Typography.labelSmall
        )
    }
}