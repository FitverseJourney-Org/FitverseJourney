package com.example.presentation.screens.ui.onboarding.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.presentation.theme.AccentGreen

@Composable
fun OnboardingIndicator(
    current: Int,
    total: Int
) {
    Row(modifier = Modifier.padding(vertical = 15.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(total) { index ->
            val width by animateDpAsState(
                targetValue = if (index == current) 24.dp else 8.dp,
                label = "indicatorWidth"
            )

            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width = width)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (index == current)
                            AccentGreen
                        else
                            Color.White.copy(alpha = 0.35f)
                    )
            )
        }
    }
}
