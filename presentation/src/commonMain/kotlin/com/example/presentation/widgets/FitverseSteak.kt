package org.fitverse.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Model para representar o estado de cada dia
data class StreakDay(
    val label: String, // S, T, Q, Q, S, S, D
    val isCompleted: Boolean
)

@Composable
fun DailyStreakCard(
    currentStreak: Int,
    days: List<StreakDay>,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    // Cores baseadas no seu design (Laranja Vibrante do Streak)
    val streakColor = Color(0xFFFF3D00)
    val streakBackground = Color(0xFFFF3D00).copy(alpha = 0.15f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(cs.surface.copy(alpha = .5f), RoundedCornerShape(24.dp))
            .border(1.dp, color = Color(0xFF2a2a35), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        // --- HEADER SECTION ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🔥",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Daily Streak",
                        style = FitverseTheme.Typography.displayMedium.copy(
                            fontSize = 18.sp,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
                Text(
                    text = "Mantenha por $currentStreak dias!",
                    style = FitverseTheme.Typography.bodyMedium,
                    color = Color(0xFF4A4D5C) // Cinza focado em legibilidade
                )
            }

            // Pill de Dias (JetBrains Mono para números)
            Box(
                modifier = Modifier
                    .background(streakBackground, CircleShape)
                    .border(1.dp, streakColor.copy(alpha = 0.3f), CircleShape)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "$currentStreak dias",
                    color = streakColor,
                    style = FitverseTheme.Typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- DAYS ROW SECTION ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { day ->
                StreakDayItem(day = day, accentColor = streakColor)
            }
        }
    }
}

@Composable
private fun StreakDayItem(
    day: StreakDay,
    accentColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Label do Dia (S, T, Q...)
        Text(
            text = day.label,
            style = FitverseTheme.Typography.labelSmall,
            color = if (day.isCompleted) Color.White else Color(0xFF4A4D5C)
        )

        // Indicador de Fogo
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    if (day.isCompleted) accentColor.copy(alpha = 0.1f) else Color.Transparent,
                    CircleShape
                )
                .border(
                    width = 1.5.dp,
                    color = if (day.isCompleted) accentColor else Color(0xFF2A2B36),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🔥",
                fontSize = 16.sp,
                style = TextStyle(
                    // Utilizamos o laranja vibrante do Streak para harmonizar com a borda
                    color = if (day.isCompleted) Color(0xFFFF3D00) else Color(0xFF4A4D5C)
                ),
            )
        }
    }
}