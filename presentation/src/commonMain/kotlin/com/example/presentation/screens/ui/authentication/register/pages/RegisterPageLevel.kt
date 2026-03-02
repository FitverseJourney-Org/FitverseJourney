package com.example.presentation.screens.ui.authentication.register.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.authentication.register.RegisterAction
import com.example.domain.model.authentication.register.TrainingLevel
import com.example.presentation.states.authentication.RegisterState

/**
 * Page root: usa MaterialTheme colors (onBackground / onSurfaceVariant) para texto e componentes.
 */
@Composable
fun RegisterPageLevel(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "What’s your training level?",
            color = colors.onBackground,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "This helps us personalize your workouts.",
            color = colors.onBackground.copy(alpha = 0.78f),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(32.dp))

        TrainingLevelSelector(
            selectedLevel = state.trainingLevel,
            onLevelSelected = {
                onAction(RegisterAction.TrainingLevelChanged(it))
            }
        )
    }
}

/**
 * Selector list — mapeia níveis e monta itens com ícones e micro-animação.
 */
@Composable
fun TrainingLevelSelector(
    selectedLevel: TrainingLevel?,
    onLevelSelected: (TrainingLevel) -> Unit
) {
    val goals = remember {
        listOf(
            Triple(TrainingLevel.BEGINNER, "Beginner", "New to training or getting back on track"),
            Triple(TrainingLevel.INTERMEDIATE, "Intermediate", "You train regularly and know the basics"),
            Triple(TrainingLevel.ADVANCED, "Advanced", "High experience and intense training routine")
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        goals.forEach { (level, title, desc) ->
            TrainingLevelItem(
                title = title,
                description = desc,
                isSelected = selectedLevel == level,
                onClick = { onLevelSelected(level) },
                level = level
            )
        }
    }
}

/**
 * Item visual para cada nível.
 * - Ícone em círculo com gradiente sutil
 * - Texto com cores do tema
 * - Carta com elevação discreta ao selecionar / sombra leve
 */
@Composable
fun TrainingLevelItem(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    level: TrainingLevel,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    // background muda com seleção (uso de surfaceVariant / primaryContainer para coerência)
    val targetBackground = if (isSelected) colors.primaryContainer.copy(alpha = 0.14f) else colors.surfaceVariant
    val backgroundColor by animateColorAsState(targetValue = targetBackground)

    // border / accent
    val accent = when (level) {
        TrainingLevel.BEGINNER -> colors.primary.copy(alpha = 0.95f)
        TrainingLevel.INTERMEDIATE -> colors.secondary
        TrainingLevel.ADVANCED -> colors.tertiary
    }

    // scale subtle
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.02f else 1f, animationSpec = spring())

    // icon mapping
    val (icon, iconTint) = levelToIcon(level, colors, accent)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // leading icon circle (gradiente leve)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(accent.copy(alpha = 0.22f), accent.copy(alpha = 0.10f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Icon(
                    imageVector = icon,
                    contentDescription = "$title icon",
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = colors.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = colors.onSurface.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // right-side hint: quando selecionado exibimos etiqueta "Selected" elegante
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(accent.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Selected",
                        color = accent,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/** Mapeia icon + tint por nível — usa colorScheme para garantir contraste. */
private fun levelToIcon(level: TrainingLevel, colors: androidx.compose.material3.ColorScheme, accent: Color): Pair<ImageVector, Color> {
    return when (level) {
        TrainingLevel.BEGINNER -> Icons.Default.AccessibilityNew to accent
        TrainingLevel.INTERMEDIATE -> Icons.Default.DirectionsRun to accent
        TrainingLevel.ADVANCED -> Icons.Default.FitnessCenter to accent
    }
}