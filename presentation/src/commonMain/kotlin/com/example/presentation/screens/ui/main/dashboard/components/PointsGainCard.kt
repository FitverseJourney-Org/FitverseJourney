package com.example.presentation.screens.ui.main.dashboard.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.AccentGreen
import com.example.presentation.theme.DarkGamifiedColors.Divider
import com.example.presentation.theme.OnSurfaceText
import com.example.presentation.theme.SurfaceGreen

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

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(700)
    )

    val animatedPoints by animateIntAsState(
        targetValue = todayPts,
        animationSpec = tween(600)
    )

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = cs.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(cs.surfaceVariant, cs.surface)
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Bolt,
                    contentDescription = null,
                    tint = cs.secondary
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = "Daily Points",
                    fontWeight = FontWeight.SemiBold,
                    color = cs.onSurface
                )
            }

            // Número principal
            Row(verticalAlignment = Alignment.Bottom) {

                Text(
                    text = "$animatedPoints",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (goalReached) cs.primary else cs.secondary
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    text = "/ $dailyGoal pts",
                    fontSize = 14.sp,
                    color = cs.onSurface.copy(alpha = 0.7f)
                )
            }

            // Barra de progresso
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(cs.outline.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .background(
                            if (goalReached)
                                cs.primary
                            else
                                cs.secondary
                        )
                )
            }

            // Mensagem dinâmica
            Text(
                text = when {
                    goalReached -> "Goal achieved. Excellent consistency."
                    else -> "${dailyGoal - todayPts} pts remaining to hit today's goal."
                },
                fontSize = 13.sp,
                color = cs.onSurface.copy(alpha = 0.7f)
            )

            Divider(
                color = cs.outline.copy(alpha = 0.2f),
                thickness = 1.dp
            )

            Text(
                text = "Total accumulated: $totalPts pts",
                fontSize = 13.sp,
                color = cs.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}