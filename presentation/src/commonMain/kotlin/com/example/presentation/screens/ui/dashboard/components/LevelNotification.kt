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
){
    INFO(
        icon = Icons.Default.Info
    ),
    WARNING(
        icon = Icons.Default.Warning
    )
}

data class Notification(
    val level: com.example.presentation.screens.ui.dashboard.components.LevelNotification = _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.LevelNotification.INFO,
    val title: String,
    val description: String,
    val date: String,
    val time: String
)

@Composable
fun NotificationCard(data: com.example.presentation.screens.ui.dashboard.components.Notification) {

    val cs = MaterialTheme.colorScheme

    var isExpanded by remember { mutableStateOf(false) }
    var isClickable by remember { mutableStateOf(false) }

    val levelColor = when (data.level) {
        _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.LevelNotification.INFO -> cs.primary
        _root_ide_package_.com.example.presentation.screens.ui.dashboard.components.LevelNotification.WARNING -> cs.error
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        onClick = { if (isClickable) isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(
            containerColor = cs.surfaceVariant
        ),
        border = BorderStroke(
            1.dp,
            cs.primary.copy(alpha = 0.08f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(
            modifier = Modifier.padding(
                top = 5.dp,
                bottom = 10.dp,
                start = 10.dp,
                end = 10.dp
            )
        ) {

            // ───────────────── HEADER ─────────────────

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {

                    Text(
                        text = data.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant
                    )

                    Text(
                        text = data.time,
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = cs.error
                    )
                }
            }

            Spacer(Modifier.height(3.dp))

            Divider(
                color = cs.outline.copy(alpha = 0.1f)
            )

            Spacer(Modifier.height(5.dp))

            // ───────────── TITLE + ICON ─────────────

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(levelColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = data.level.icon,
                        contentDescription = null,
                        tint = levelColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = data.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = cs.onSurface
                )
            }

            Spacer(Modifier.height(12.dp))

            // ───────────── DESCRIPTION ─────────────

            Text(
                text = data.description,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurfaceVariant,
                onTextLayout = { layout ->
                    if (!isExpanded) {
                        isClickable = layout.didOverflowHeight
                    }
                }
            )

            if (isClickable) {

                Spacer(Modifier.height(8.dp))

                Text(
                    text = if (isExpanded) "Ver menos" else "Ler mais",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = cs.primary,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}