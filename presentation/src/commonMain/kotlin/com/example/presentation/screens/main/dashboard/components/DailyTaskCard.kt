package com.example.presentation.screens.main.dashboard.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.dashboard.TaskIcon
import com.example.domain.model.dashboard.TaskItem
import com.example.presentation.screens.main.dashboard.AvatarState
import com.example.presentation.theme.AccentGreen
import com.example.presentation.theme.OnSurfaceText
import com.example.presentation.theme.SurfaceGreen

@Composable
fun DailyTaskItemAvatar(
    task: TaskItem,
    avatarState: AvatarState,
    onToggle: (TaskItem) -> Unit
) {
    val completed = task.completed

    val scale by animateFloatAsState(
        targetValue = if (completed) 0.98f else 1f,
        animationSpec = tween(200)
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (completed) 0.18f else 0f,
        animationSpec = tween(400)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp)
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (completed)
                SurfaceGreen.copy(alpha = 0.95f)
            else
                SurfaceGreen
        ),
        onClick = { onToggle(task) }
    ) {
        Box {

            // ✨ Glow de sucesso
            if (completed) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            AccentGreen.copy(alpha = glowAlpha),
                            RoundedCornerShape(16.dp)
                        )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ▍ Barra lateral de progresso
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight(0.7f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (completed) AccentGreen
                            else Color.White.copy(alpha = 0.08f)
                        )
                )

                Spacer(Modifier.width(12.dp))

                // 🔘 Ícone principal
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (completed) AccentGreen
                            else Color.White.copy(alpha = 0.06f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (completed) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Completed",
                            tint = Color.Black
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
                            tint = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                Spacer(Modifier.width(14.dp))

                // 📝 Texto
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = task.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurfaceText
                    )

                    if (task.description.isNotBlank()) {
                        Text(
                            text = task.description,
                            fontSize = 13.sp,
                            color = OnSurfaceText.copy(alpha = 0.75f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.width(8.dp))

                // ⚡ XP
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = AccentGreen
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "+${task.xp}",
                            color = AccentGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (completed) {
                        Text(
                            text = "Done",
                            fontSize = 11.sp,
                            color = AccentGreen
                        )
                    }
                }
            }
        }
    }
}