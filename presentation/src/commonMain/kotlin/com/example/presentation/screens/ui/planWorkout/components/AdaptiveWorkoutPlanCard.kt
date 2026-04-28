package com.example.presentation.screens.ui.planWorkout.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.workout.workout_plan.WorkoutPlanItem

@Composable
fun AdaptiveWorkoutPlanCard(
    plan: WorkoutPlanItem,
    level: String = "Intermediário", // Adicionado para o exemplo
    objective: String = "Hipertrofia", // Adicionado para o exemplo
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Define a opacidade e cores com base no estado ativo
    val contentAlpha = 1f

    Surface(
        modifier = Modifier.fillMaxWidth().heightIn(min = 110.dp), // Altura mínima para acomodar as tags sem quebrar
        shape = RoundedCornerShape(24.dp),
        color = cs.surface.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        onClick = onClick,
        tonalElevation = if (plan.isActive) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(Modifier.width(16.dp))

            // --- CONTEÚDO CENTRAL ---
            Column(
                modifier = Modifier
                    .weight(1f)
                    .alpha(contentAlpha),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = plan.title.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp,
                    maxLines = 1
                )

                Spacer(Modifier.height(8.dp))

                // Row de Badges (Nível e Objetivo)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WorkoutBadge(
                        text = objective,
                        containerColor = cs.tertiaryContainer.copy(alpha = 0.4f),
                        contentColor = cs.onTertiaryContainer
                    )
                    WorkoutBadge(
                        text = level,
                        containerColor = cs.secondaryContainer.copy(alpha = 0.4f),
                        contentColor = cs.onSecondaryContainer
                    )
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = plan.frequency,
                    style = MaterialTheme.typography.labelMedium,
                    color = cs.onSurfaceVariant
                )
            }

            // --- ÍCONE DE NAVEGAÇÃO ---
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .alpha(if (plan.isActive) 1f else 0.3f),
                tint = cs.primary
            )
        }
    }
}