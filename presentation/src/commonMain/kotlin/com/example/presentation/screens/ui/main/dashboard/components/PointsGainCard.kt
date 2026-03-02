package com.example.presentation.screens.ui.main.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.AccentGreen
import com.example.presentation.theme.OnSurfaceText
import com.example.presentation.theme.SurfaceGreen

@Composable
fun PointsGainCard(
    todayPts: Int,
    totalPts: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceGreen)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Bolt,
                    contentDescription = null,
                    tint = AccentGreen
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Today’s Gain Points",
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceText
                )
            }

            // XP destaque
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$todayPts",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentGreen
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Pts earned",
                    fontSize = 14.sp,
                    color = OnSurfaceText.copy(alpha = 0.8f)
                )
            }

            // Pontos totais
            Text(
                text = "Total points: $totalPts",
                fontSize = 13.sp,
                color = OnSurfaceText.copy(alpha = 0.75f)
            )
        }
    }
}