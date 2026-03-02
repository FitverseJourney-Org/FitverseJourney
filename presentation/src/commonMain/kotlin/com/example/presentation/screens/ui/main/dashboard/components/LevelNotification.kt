package com.example.presentation.screens.ui.main.dashboard.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
    val level: LevelNotification = LevelNotification.INFO,
    val title: String,
    val description: String,
    val date: String,
    val time: String
)

@Composable
fun NotificationCard(data: Notification) {
    val colors = MaterialTheme.colorScheme

    var isExpanded by remember { mutableStateOf(false) }
    var isClickable by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(14.dp),
        onClick = { if (isClickable) isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(
                top = 12.dp,
                bottom = 20.dp,
                start = 16.dp,
                end = 16.dp
            )
        ) {

            // Top row (date + delete)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = data.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant
                    )
                    Text(
                        text = data.time,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant
                    )
                }

                IconButton(onClick = { }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = colors.error
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Title + Level Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                val levelColor = when (data.level) {
                    LevelNotification.INFO -> colors.primary
                    LevelNotification.WARNING -> colors.error
                }

                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = data.level.icon,
                    contentDescription = null,
                    tint = levelColor
                )

                Text(
                    text = data.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = colors.onSurface
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            Text(
                text = data.description,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
                onTextLayout = { textLayoutResult ->
                    if (!isExpanded) {
                        isClickable = textLayoutResult.didOverflowHeight
                    }
                }
            )

            if (isClickable) {
                Text(
                    text = if (isExpanded) "Ver menos" else "Ler mais",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = colors.primary,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.End)
                )
            }
        }
    }
}