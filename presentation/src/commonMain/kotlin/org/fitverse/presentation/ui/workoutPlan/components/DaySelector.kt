package org.fitverse.presentation.ui.workoutPlan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.ui.workoutPlan.DayOfWeek

@Composable
fun DaySelector(selectedDay: DayOfWeek, colors: ColorScheme, onDaySelected: (DayOfWeek) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DayOfWeek.entries.forEach { day ->
            val isSelected = day.fullName == selectedDay.fullName

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onDaySelected(day) }
                    // PrimarySoft (#2D1B59) para estados de seleção
                    .background(if (isSelected) colors.primaryContainer else Color.Transparent)
                    .padding(vertical = 12.dp, horizontal = 10.dp)
                    .width(36.dp)
            ) {
                Text(
                    text = day.shortLabel,
                    color = if (isSelected) colors.primary else colors.onSurfaceVariant,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}