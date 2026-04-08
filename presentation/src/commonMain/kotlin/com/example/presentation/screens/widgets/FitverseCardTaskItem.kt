package com.example.presentation.screens.widgets

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.dashboard.TaskItem
import com.example.presentation.theme.DarkGamifiedColors

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FitverseTaskItem(
    task: TaskItem,
    isSelected: Boolean,
    onToggle: () -> Unit,
    onSelect: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggle,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) DarkGamifiedColors.PrimarySoft.copy(alpha = 0.15f) else cs.surface.copy(alpha = 0.75f),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) cs.surface.copy(alpha = 0.5f) else DarkGamifiedColors.PrimarySoft.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Container do Ícone: Neon quando inativo, Sólido quando concluído
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (isSelected) cs.primary else cs.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isSelected) Icons.Rounded.Check else Icons.Rounded.Assignment,
                    contentDescription = null,
                    tint = if (isSelected) Color.Black else cs.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    color = cs.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = task.description,
                    color = cs.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Text(
                text = "+${task.xp} XP",
                color = DarkGamifiedColors.Tertiary,
                fontWeight = FontWeight.Black,
                fontSize = 13.sp
            )
        }
    }
}