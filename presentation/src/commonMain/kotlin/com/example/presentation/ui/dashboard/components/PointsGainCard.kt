package com.example.presentation.ui.dashboard.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PointsGainCard(
    todayPts: Int,
    totalPts: Int,
    dailyGoal: Int = 200,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val progress = (todayPts.coerceAtMost(dailyGoal)) / dailyGoal.toFloat()
    val goalReached = todayPts >= dailyGoal

    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(1000, easing = FastOutSlowInEasing))
    val animatedPoints by animateIntAsState(targetValue = todayPts, animationSpec = tween(800))

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f)),
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header com Badge Premium
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(36.dp).clip(CircleShape).background(cs.secondary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.Bolt, contentDescription = null, tint = cs.secondary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Text("Daily Points", fontWeight = FontWeight.Bold, color = cs.onSurface)
                }

                // Meta Badge
                Surface(color = cs.surfaceVariant, shape = RoundedCornerShape(8.dp)) {
                    Text("Goal: $dailyGoal", fontSize = 12.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            // Scoreboard Principal
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$animatedPoints",
                    fontSize = 42.sp, // Bem grande para impacto
                    fontWeight = FontWeight.Black,
                    color = if (goalReached) cs.primary else cs.onSurface
                )
                Text(
                    text = " pts",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Barra de Progresso Arredondada
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
                color = if (goalReached) cs.primary else cs.secondary,
                trackColor = cs.onSurface.copy(alpha = 0.08f),
                strokeCap = StrokeCap.Round
            )

            Spacer(Modifier.height(20.dp))

            // Footer de Acumulado
            Row(
                modifier = Modifier.fillMaxWidth().background(cs.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp)).padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Accumulated", fontSize = 13.sp, color = cs.onSurfaceVariant)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Stars, contentDescription = null, tint = cs.secondary, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("$totalPts pts", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = cs.onSurface)
                }
            }
        }
    }
}