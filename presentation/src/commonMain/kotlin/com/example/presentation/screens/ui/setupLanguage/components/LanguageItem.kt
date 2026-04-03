package com.example.presentation.screens.ui.setupLanguage.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.domain.model.local.language.AppLanguageItem
import org.jetbrains.compose.resources.painterResource

@Composable
fun LanguageItem(
    appLanguageItem: AppLanguageItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected)
            colors.primary.copy(alpha = 0.10f)
        else
            colors.surface
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected)
            colors.primary
        else
            colors.outline.copy(alpha = 0.5f)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(appLanguageItem.flagRes),
            contentDescription = appLanguageItem.name,
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.width(14.dp))

        Text(
            text = appLanguageItem.name,
            style = MaterialTheme.typography.bodyLarge,
            color = colors.onSurface,
            modifier = Modifier.weight(1f)
        )

        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = colors.primary,
                unselectedColor = colors.outline
            )
        )
    }
}