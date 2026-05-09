package com.example.presentation.ui.authentication.register.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domain.models.user.ClassType
import com.example.presentation.ui.authentication.register.helpers.bonuses
import com.example.presentation.ui.authentication.register.helpers.displayName
import com.example.presentation.ui.authentication.register.helpers.quote
import com.example.presentation.ui.authentication.register.helpers.subtitle
import com.example.presentation.theme.RegisterDimens
import com.example.presentation.ui.authentication.register.helpers.accentColor
import com.example.presentation.ui.authentication.register.helpers.forca
import com.example.presentation.ui.authentication.register.helpers.iconEmoji
import com.example.presentation.ui.authentication.register.helpers.nutricao
import com.example.presentation.ui.authentication.register.helpers.resistencia
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.stat_forca
import fitversejourneyapp.presentation.generated.resources.stat_nutricao
import fitversejourneyapp.presentation.generated.resources.stat_resistencia
import org.jetbrains.compose.resources.stringResource

@Composable
fun ClassCard(
    classType: ClassType,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val type   = MaterialTheme.typography
    val shapes = MaterialTheme.shapes

    val borderColor by animateColorAsState(
        targetValue   = if (isSelected) classType.accentColor() else colors.outline,
        animationSpec = tween(250),
        label         = "border_${classType.name}",
    )
    val bgColor by animateColorAsState(
        targetValue   = if (isSelected) classType.accentColor().copy(alpha = 0.08f)
        else colors.surfaceVariant,
        animationSpec = tween(250),
        label         = "bg_${classType.name}",
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shapes.medium)
            .background(bgColor)
            .border(1.5.dp, borderColor, shapes.medium)
            .clickable { onSelect() }
            .padding(RegisterDimens.cardPadding),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(shapes.small)
                    .background(classType.accentColor().copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = classType.iconEmoji(), style = type.titleLarge)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text  = classType.displayName(),
                    style = type.titleLarge.copy(color = colors.onBackground),
                )
                Text(
                    text  = classType.subtitle(),
                    style = type.labelLarge.copy(
                        color         = colors.onSurfaceVariant,
                    ),
                )
            }
            // Selection dot
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        if (isSelected) classType.accentColor() else colors.outline,
                        CircleShape,
                    )
                    .background(if (isSelected) classType.accentColor() else Color.Transparent),
            )
        }

        // Quote
        Text(
            text  = classType.quote(),
            style = type.bodySmall.copy(color = colors.onSurfaceVariant),
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            StatBar(stringResource(Res.string.stat_forca),       classType.forca(),       activeColor = classType.accentColor(), modifier = Modifier.fillMaxWidth())
            StatBar(stringResource(Res.string.stat_resistencia), classType.resistencia(), activeColor = classType.accentColor(), modifier = Modifier.fillMaxWidth())
            StatBar(stringResource(Res.string.stat_nutricao),    classType.nutricao(),    activeColor = classType.accentColor(), modifier = Modifier.fillMaxWidth())
        }

        // Bonuses
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            classType.bonuses().forEachIndexed { index, _ ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(classType.accentColor()),
                    )
                    Text(text = classType.bonuses()[index], style = type.bodySmall.copy(color = colors.onBackground))
                }
            }
        }
    }
}