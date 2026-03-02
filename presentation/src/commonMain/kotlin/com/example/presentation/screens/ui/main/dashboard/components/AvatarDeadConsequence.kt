package com.example.presentation.screens.ui.main.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.main.nutrition.TextPrimary
import com.example.presentation.screens.ui.main.nutrition.TextSecondary
import com.example.presentation.theme.AccentGreen

@Composable
fun SmallConsequenceLine(label: String, sub: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(AccentGreen)
                .padding(top = 4.dp)
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(text = label, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(text = sub, color = TextSecondary, fontSize = 12.sp)
        }
    }
}