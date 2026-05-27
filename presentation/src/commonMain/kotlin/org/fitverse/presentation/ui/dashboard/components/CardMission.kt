package org.fitverse.presentation.ui.dashboard.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.fitverse.presentation.theme.FitColors

@Composable
fun MissionCard(
    title: String,
    description: String,
    xp: Int?,
    icon: ImageVector,
    iconColor: Color,
    isCompleted: Boolean = false,
    isChallengeType: Boolean = false,
    onClaim: (() -> Unit)? = null
) {
    val cardAlpha = if (isCompleted) 0.5f else 1f
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(20.dp)

    val animatedBorderModifier: Modifier = if (isChallengeType && !isCompleted) {
        val pulse = rememberInfiniteTransition(label = "challenge_border")
        val borderAlpha by pulse.animateFloat(
            initialValue = 0.45f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(900, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "border_pulse"
        )
        Modifier.border(
            width = 1.5.dp,
            brush = Brush.horizontalGradient(
                listOf(
                    FitColors.Red.copy(alpha = borderAlpha * 0.5f),
                    FitColors.Red.copy(alpha = borderAlpha),
                    FitColors.Red.copy(alpha = borderAlpha * 0.5f),
                )
            ),
            shape = shape
        )
    } else Modifier

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(cardAlpha)
            .then(animatedBorderModifier),
        shape = shape,
        color = if (isChallengeType) FitColors.Red.copy(alpha = 0.06f)
                else cs.surface.copy(alpha = 0.5f),
        border = if (isChallengeType) null
                 else BorderStroke(1.dp, Color(0xFF2a2a35))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(iconColor.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                    .border(1.dp, iconColor.copy(alpha = 0.35f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Column(modifier = Modifier.weight(1f).padding(horizontal = 14.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = cs.primary,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    modifier = Modifier
                        .clickable(role = Role.Button) { onClaim?.invoke() }
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconColor.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                        .border(1.dp, iconColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    text = "+${xp} XP",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = iconColor
                )
            }
        }
    }
}
