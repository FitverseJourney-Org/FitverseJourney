package com.example.presentation.screens.ui.dashboard.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.presentation.theme.CardBgDefaultColor


enum class LevelNotification(
    val icon: ImageVector,
    val color: @Composable () -> Color
){
    INFO(
        icon = Icons.Default.Info,
        color = { Color(0xFF7D53FF) } // Roxo Elétrico para Informação/Tecnologia
    ),
    WARNING(
        icon = Icons.Default.Warning,
        color = { Color(0xFFB6FF00) } // Amarelo Neon para Alertas/Atenção
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

    val levelColor = data.level.color()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        onClick = { if (isClickable) isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(
            containerColor = cs.surfaceVariant.copy(alpha = 0.5f) // Fundo semi-transparente frio
        ),
        border = BorderStroke(
            0.5.dp,
            if (isExpanded) levelColor.copy(alpha = 0.5f) else cs.outline.copy(alpha = 0.2f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // HEADER: Nível, Título e Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Ícone com Glow sutil
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(levelColor.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = data.level.icon,
                            contentDescription = null,
                            tint = levelColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                IconButton(
                    onClick = { /* Lógica de deletar */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = cs.error.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // DESCRIÇÃO
            Text(
                text = data.description,
                maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurfaceVariant,
                onTextLayout = { layout ->
                    if (!isExpanded) isClickable = layout.didOverflowHeight
                }
            )

            // FOOTER: Data/Hora e Botão "Ver mais"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${data.date} • ${data.time}",
                    style = MaterialTheme.typography.labelSmall,
                    color = cs.onSurfaceVariant.copy(alpha = 0.5f)
                )

                if (isClickable) {
                    Text(
                        text = if (isExpanded) "Ver menos" else "Ler mais",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = levelColor // Usa a cor do nível para o link
                    )
                }
            }
        }
    }
}