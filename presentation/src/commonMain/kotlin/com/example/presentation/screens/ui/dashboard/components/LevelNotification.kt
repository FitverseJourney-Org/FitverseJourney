package com.example.presentation.screens.ui.dashboard.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.CardBgDefaultColor
import com.example.presentation.theme.DangerRed
import com.example.presentation.theme.DarkGamifiedColors
import com.example.presentation.theme.StaminaYellow


enum class LevelNotification(
    val icon: ImageVector,
    val color: Color
){
    INFO(
        icon = Icons.Rounded.Info,
        color = DarkGamifiedColors.Primary // Roxo Premium para logs normais
    ),
    WARNING(
        icon = Icons.Rounded.Warning,
        color = StaminaYellow      // Dourado para alertas/atenção
    ),
    URGENT(
        icon = Icons.Rounded.ErrorOutline,
        color = DangerRed // Vermelho para perigo/falhas
    )
}

data class Notification(
    val level: LevelNotification = LevelNotification.INFO,
    val title: String,
    val description: String,
    val date: String,
    val time: String
)

@Composable
fun NotificationCard(data: Notification) {

    val cs = MaterialTheme.colorScheme
    var isExpanded by remember { mutableStateOf(false) }
    var isClickable by remember { mutableStateOf(false) }

    // Pegamos a cor direto da propriedade que mudamos no enum
    val levelColor = data.level.color

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable(enabled = isClickable) { isExpanded = !isExpanded },
        shape = RoundedCornerShape(20.dp),
        // Fundo translucido padrão do Glassmorphism
        color = cs.surface.copy(alpha = 0.6f),
        // Borda reage ao estado: Brilha na cor do nível se expandido
        border = BorderStroke(
            width = if (isExpanded) 1.5.dp else 1.dp,
            color = if (isExpanded) levelColor.copy(alpha = 0.6f) else DarkGamifiedColors.Outline.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // --- HEADER ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Ícone com Glow baseado no nível (INFO/WARNING/URGENT)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(levelColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = data.level.icon,
                            contentDescription = null,
                            tint = levelColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                IconButton(
                    onClick = { /* Lógica de deletar */ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Delete",
                        tint = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // --- DESCRIÇÃO ---
            Text(
                text = data.description,
                maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f),
                lineHeight = 20.sp,
                onTextLayout = { layout ->
                    if (!isExpanded) isClickable = layout.didOverflowHeight
                }
            )

            // --- FOOTER ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${data.date} • ${data.time}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.4f),
                    fontWeight = FontWeight.Bold
                )

                if (isClickable) {
                    Text(
                        text = if (isExpanded) "VER MENOS" else "LER MAIS",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = levelColor, // Link usa a cor de destaque da notificação
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}