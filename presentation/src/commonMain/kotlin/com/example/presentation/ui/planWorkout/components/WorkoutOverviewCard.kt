package com.example.presentation.ui.planWorkout.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.workout.workout_plan.WorkoutScreenState

@Composable
fun WorkoutOverviewCard(state: WorkoutScreenState) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = cs.surface.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Sênior: Usando weight para garantir que o texto não quebre o layout
                Column(modifier = Modifier.weight(1.2f)) {
                    Text("OBJETIVO ATUAL", style = MaterialTheme.typography.labelSmall, color = cs.secondary)
                    Text(state.objective, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                }

                // Card de Nível Adaptativo
                Surface(
                    modifier = Modifier.weight(0.8f).aspectRatio(2.5f),
                    color = cs.secondary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(state.experienceLevel.uppercase(), color = cs.secondary, fontSize = 10.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Barra de Frequência Semanal Adaptativa
            Text("FREQUÊNCIA SEMANAL", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(state.workoutsPerWeek) { index ->
                    val isDone = index < state.completedThisWeek
                    Box(
                        modifier = Modifier
                            .weight(1f) // Divide o espaço igualmente independente de quantos dias são
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(if (isDone) cs.tertiary else cs.outline.copy(alpha = 0.2f))
                    )
                }
            }
        }
    }
}