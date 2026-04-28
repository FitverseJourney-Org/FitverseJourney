package com.example.presentation.screens.ui.authentication.register.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.domain.models.auth.register.RegisterStep
import com.example.presentation.theme.RegisterDimens

@Composable
fun RegisterStepBar(
    currentStep: RegisterStep,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val type   = MaterialTheme.typography

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        RegisterStep.entries.forEach { step ->
            val isActive = step == currentStep
            val isDone   = step.index < currentStep.index

            val barColor by animateColorAsState(
                targetValue = when {
                    isDone   -> colors.tertiary
                    isActive -> colors.primary
                    else     -> colors.outline
                },
                animationSpec = tween(300),
                label = "stepBar_${step.name}",
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(RegisterDimens.stepBarHeight)
                        .clip(MaterialTheme.shapes.small)
                        .background(barColor),
                )
                Text(
                    text  = step.label,
                    style = type.labelLarge.copy(
                        color = if (isActive) colors.primary else colors.onSurfaceVariant,
                    ),
                )
            }
        }
    }
}