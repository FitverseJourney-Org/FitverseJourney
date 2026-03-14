package com.example.presentation.screens.ui.main.dashboard.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalDining
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.dashboard.TaskIcon
import com.example.domain.model.dashboard.TaskItem
import com.example.presentation.screens.ui.main.dashboard.AvatarState
import com.example.presentation.theme.AccentGreen
import com.example.presentation.theme.OnSurfaceText
import com.example.presentation.theme.SurfaceGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTaskItemAvatar(
    task: TaskItem,
    avatarState: AvatarState, // Mantido caso você vá usar depois
    onToggle: (TaskItem) -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val completed = task.completed

    // Animações de estado
    val scale by animateFloatAsState(targetValue = if (completed) 0.96f else 1f, animationSpec = tween(300, easing = FastOutSlowInEasing))
    val containerColor by animateColorAsState(targetValue = if (completed) cs.surfaceVariant.copy(alpha = 0.4f) else cs.surface)
    val borderColor by animateColorAsState(targetValue = if (completed) Color.Transparent else cs.outline.copy(alpha = 0.1f))

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        onClick = { onToggle(task) },
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = if (completed) 0.dp else 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox Gamificado
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (completed) cs.primary else cs.primary.copy(alpha = 0.05f))
                    .border(if (completed) 0.dp else 2.dp, cs.primary.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (completed) {
                    Icon(Icons.Rounded.Check, contentDescription = null, tint = cs.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Icon(
                        imageVector = when (task.iconType) {
                            TaskIcon.WORKOUT -> Icons.Rounded.FitnessCenter
                            TaskIcon.NUTRITION -> Icons.Rounded.LocalDining
                            TaskIcon.RUN -> Icons.Rounded.DirectionsRun
                            else -> Icons.Rounded.StarOutline
                        },
                        contentDescription = null,
                        tint = cs.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            // Textos com Strikethrough
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (completed) cs.onSurfaceVariant else cs.onSurface,
                    textDecoration = if (completed) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                )
                if (task.description.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = task.description,
                        fontSize = 13.sp,
                        color = cs.onSurfaceVariant.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // XP Badge
            Surface(
                color = if (completed) cs.secondary.copy(alpha = 0.2f) else cs.secondaryContainer.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = if (completed) cs.secondary else cs.secondary.copy(alpha = 0.7f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "+${task.xp}",
                        color = if (completed) cs.secondary else cs.onSecondaryContainer,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}