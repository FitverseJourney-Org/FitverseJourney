package com.example.presentation.screens.main.dashboard.components

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
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.AccentGreen
import com.example.presentation.theme.OnSurfaceText
import com.example.presentation.theme.SurfaceGreen

@Composable
fun StreakCard(
    days: Int,
    onCheckIn: () -> Unit,
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
                    imageVector = Icons.Filled.LocalFireDepartment,
                    contentDescription = null,
                    tint = AccentGreen
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Streak",
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceText
                )
            }

            // Days highlight
            Text(
                text = "$days days in a row",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AccentGreen
            )

            Text(
                text = "Keep showing up daily to earn bonuses",
                fontSize = 13.sp,
                color = OnSurfaceText.copy(alpha = 0.75f)
            )

            Button(
                onClick = onCheckIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentGreen,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Daily Check-in",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}