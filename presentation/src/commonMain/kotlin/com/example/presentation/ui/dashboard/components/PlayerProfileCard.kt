package com.example.presentation.ui.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.presentation.widgets.AvatarWithLevel
import com.example.presentation.widgets.BottomStatItem
import com.example.presentation.widgets.FitverseTheme
import com.example.presentation.widgets.StatRow

@Composable
fun PlayerProfileCard(modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp), // Corner radius system
        colors = CardDefaults.cardColors(containerColor = cs.surface.copy(alpha = .5f)),
        border = BorderStroke(1.dp, color = Color(0xFF2a2a35))
    ) {
        Column(
            modifier = Modifier.padding(16.dp) // Card padding system
        ) {
            // Top Section (Avatar + Stats)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarWithLevel(
                    level = 23,
                    progress = 0.68f // 340 / 500
                )

                Spacer(modifier = Modifier.width(20.dp)) // Margin horizontal system

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ALEX RIVERS",
                        style = FitverseTheme.Typography.displayMedium
                    )
                    Text(
                        text = "WARRIOR CLASS",
                        style = FitverseTheme.Typography.bodyMedium,
                        color = FitverseTheme.TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp)) // Gap padrão

                    // Stat Bars
                    StatRow(
                        icon = Icons.Rounded.Favorite,
                        label = "HP",
                        current = 82,
                        max = 100,
                        color = FitverseTheme.ColorHp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StatRow(
                        icon = Icons.Rounded.Bolt,
                        label = "ENERGY",
                        current = 67,
                        max = 100,
                        color = FitverseTheme.ColorEnergy
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StatRow(
                        icon = Icons.Rounded.AutoAwesome,
                        label = "XP",
                        current = 340,
                        max = 500,
                        color = FitverseTheme.ColorXp
                    )
                }
            }

            // Divider
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = FitverseTheme.SurfaceOutline
            )

            // Bottom Section (Milestones)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomStatItem(
                    value = "+100",
                    label = "PTS HOJE",
                    valueColor = FitverseTheme.ColorDaily
                )
                VerticalDivider(modifier = Modifier.height(32.dp), color = FitverseTheme.SurfaceOutline)
                BottomStatItem(
                    value = "7d",
                    label = "STREAK",
                    valueColor = FitverseTheme.ColorStreak
                )
                VerticalDivider(modifier = Modifier.height(32.dp), color = FitverseTheme.SurfaceOutline)
                BottomStatItem(
                    value = "1/4",
                    label = "MISSÕES",
                    valueColor = FitverseTheme.ColorEnergy
                )
            }
        }
    }
}