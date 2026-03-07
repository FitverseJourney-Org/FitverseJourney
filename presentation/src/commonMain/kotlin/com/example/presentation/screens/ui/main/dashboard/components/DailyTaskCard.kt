package com.example.presentation.screens.ui.main.dashboard.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

@Composable
fun DailyTaskItemAvatar(
    task: TaskItem,
    avatarState: AvatarState,
    onToggle: (TaskItem) -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val completed = task.completed

    val scale by animateFloatAsState(
        targetValue = if (completed) 0.985f else 1f,
        animationSpec = tween(220)
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (completed) 0.12f else 0f,
        animationSpec = tween(450)
    )

    val elevation by animateDpAsState(
        targetValue = if (completed) 2.dp else 6.dp,
        animationSpec = tween(300)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .scale(scale),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(
            containerColor = cs.surfaceVariant
        ),
        onClick = { onToggle(task) }
    ) {
        Box {

            // ✨ Glow sutil quando completo
            if (completed) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            color = cs.primary.copy(alpha = glowAlpha),
                            shape = RoundedCornerShape(20.dp)
                        )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                cs.surfaceVariant,
                                cs.surface
                            )
                        )
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ▍ Indicador lateral refinado
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight(0.7f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (completed)
                                cs.primary
                            else
                                cs.outline.copy(alpha = 0.25f)
                        )
                )

                Spacer(Modifier.width(14.dp))

                // 🔘 Ícone principal
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (completed)
                                cs.primary
                            else
                                cs.surface
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (completed) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = cs.onPrimary
                        )
                    } else {
                        Icon(
                            imageVector = when (task.iconType) {
                                TaskIcon.WORKOUT -> Icons.Filled.FitnessCenter
                                TaskIcon.NUTRITION -> Icons.Filled.LocalDining
                                TaskIcon.RUN -> Icons.Filled.DirectionsRun
                                else -> Icons.Filled.Star
                            },
                            contentDescription = null,
                            tint = cs.onSurface.copy(alpha = 0.9f)
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                // 📝 Conteúdo
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = task.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = cs.onSurface
                    )

                    if (task.description.isNotBlank()) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = task.description,
                            fontSize = 13.sp,
                            color = cs.onSurface.copy(alpha = 0.65f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.width(8.dp))

                // ⭐ XP badge moderno
                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (completed)
                                    cs.secondary.copy(alpha = 0.15f)
                                else
                                    cs.secondary.copy(alpha = 0.08f)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = cs.secondary,
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(Modifier.width(4.dp))

                            Text(
                                text = "+${task.xp}",
                                color = cs.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (completed) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Completed",
                            fontSize = 11.sp,
                            color = cs.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}