package com.example.presentation.screens.ui.dashboard.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.dashboard.TaskIcon
import com.example.domain.model.dashboard.TaskItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTaskItemAvatar(
    task: TaskItem,
    isSelected: Boolean = false, // ESSENCIAL: Controla o estado de seleção para troca
    onSelect: () -> Unit,        // ESSENCIAL: Dispara a seleção (ativa o FAB)
    onToggle: () -> Unit         // ESSENCIAL: Alterna entre concluído/pendente
) {
    val cs = MaterialTheme.colorScheme
    val completed = task.completed

    // Animações de estado
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else if (completed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    val containerColor by animateColorAsState(
        targetValue = if (completed) cs.surfaceVariant.copy(alpha = 0.4f) else cs.surface,
        label = "containerColor"
    )

    val borderWeight by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 1.dp,
        label = "borderWeight"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isSelected -> cs.primary
            completed -> Color.Transparent
            else -> cs.outlineVariant.copy(alpha = 0.5f)
        },
        label = "borderColor"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (completed) 0.6f else 1f,
        label = "contentAlpha"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        onClick = { onSelect() }, // O clique no corpo do card seleciona para troca
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        border = BorderStroke(borderWeight, borderColor),
        shadowElevation = if (completed || isSelected) 0.dp else 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .alpha(contentAlpha),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox/Ícone Gamificado
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (completed) cs.primary else cs.primaryContainer.copy(alpha = 0.3f))
                    .border(
                        width = if (completed) 0.dp else 2.dp,
                        color = if (completed) Color.Transparent else cs.primary.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    // Importante: clickable aqui para não disparar o onSelect do Surface
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = completed,
                    transitionSpec = {
                        (scaleIn() + fadeIn()) togetherWith (scaleOut() + fadeOut())
                    },
                    label = "iconTransition"
                ) { isCompleted ->
                    Icon(
                        imageVector = if (isCompleted) Icons.Rounded.Check else {
                            when (task.iconType) {
                                TaskIcon.WORKOUT -> Icons.Rounded.FitnessCenter
                                TaskIcon.NUTRITION -> Icons.Rounded.LocalDining
                                TaskIcon.RUN -> Icons.Rounded.DirectionsRun
                                else -> Icons.Rounded.StarOutline
                            }
                        },
                        contentDescription = null,
                        tint = if (isCompleted) cs.onPrimary else cs.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            // Textos
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurface,
                    textDecoration = if (completed) TextDecoration.LineThrough else null,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = cs.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // XP Badge
            Surface(
                color = if (completed) cs.surfaceVariant else cs.secondaryContainer.copy(alpha = 0.5f),
                shape = CircleShape
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        Icons.Rounded.Star,
                        contentDescription = null,
                        tint = if (completed) cs.onSurfaceVariant else cs.secondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "+${task.xp}",
                        color = if (completed) cs.onSurfaceVariant else cs.onSecondaryContainer,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}