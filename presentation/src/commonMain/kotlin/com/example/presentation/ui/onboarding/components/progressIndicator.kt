package com.example.presentation.ui.onboarding.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingIndicator(
    current: Int,
    total: Int
) {

    val cs = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.padding(vertical = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        repeat(total) { index ->

            val width by animateDpAsState(
                targetValue = if (index == current) 26.dp else 8.dp,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                ),
                label = "indicatorWidth"
            )

            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (index == current)
                            cs.primary
                        else
                            cs.onBackground.copy(alpha = 0.25f)
                    )
            )
        }
    }
}