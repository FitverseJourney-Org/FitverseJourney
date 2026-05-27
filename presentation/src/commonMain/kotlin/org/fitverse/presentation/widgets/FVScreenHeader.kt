package org.fitverse.presentation.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.theme.FVTypography

@Composable
fun FVScreenHeader(
    title: String,
    sub: String = "",
    onBack: () -> Unit,
    action: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(36.dp)
                .background(FitColors.Surface2, RoundedCornerShape(10.dp))
        ) {
            Text("←", style = FVTypography.headlineMedium, color = FitColors.TextPrimary)
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text  = title,
                style = FVTypography.headlineLarge,
                color = FitColors.TextPrimary,
            )
            if (sub.isNotEmpty()) {
                Text(
                    text  = sub,
                    style = FVTypography.labelMedium,
                    color = FitColors.TextMuted,
                )
            }
        }
        action?.invoke()
    }
    Divider(color = FitColors.Outline, thickness = 0.5.dp)
}


@Composable
fun FVFilterPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(if (selected) FitColors.Accent else FitColors.Surface2)
            .border(
                width = if (selected) 0.dp else 1.dp,
                color = if (selected) Color.Transparent else FitColors.Outline,
                shape = RoundedCornerShape(100.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text  = label,
            style = FVTypography.labelLarge,
            color = if (selected) FitColors.Bg else FitColors.TextMuted,
        )
    }
}

@Composable
fun FVCard(
    modifier: Modifier = Modifier,
    glowColor: Color? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val borderColor = glowColor?.copy(alpha = 0.3f) ?: FitColors.Outline
    val shadow = if (glowColor != null)
        Modifier.shadow(0.dp, shape = RoundedCornerShape(16.dp))
    else Modifier

    Column(
        modifier = modifier
            .then(shadow)
            .clip(RoundedCornerShape(16.dp))
            .background(FitColors.Surface)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        content = content
    )
}

@Composable
fun FVSectionLabel(title: String, action: String = "", onAction: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text     = title.uppercase(),
            style    = FVTypography.overline,
            color    = FitColors.TextMuted,
            modifier = Modifier.weight(1f),
        )
        if (action.isNotEmpty()) {
            Text(
                text     = action,
                style    = FVTypography.labelMedium,
                color    = FitColors.Accent,
                modifier = Modifier.clickable(onClick = onAction),
            )
        }
    }
}