package com.example.presentation.screens.ui.authentication.register.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.authentication.register.RegisterGender

data class Genre(
    val icon: ImageVector,
    val name: String,
)

@Composable
fun GenreSelection(
    selectedGenre: String?, // Recebe o valor atual (ex: "MALE")
    onGenreSelected: (RegisterGender) -> Unit, // Callback para atualizar o ViewModel
    errorList: List<String>,
    modifier: Modifier = Modifier
) {
    val genres = listOf(
        Genre(icon = Icons.Default.Male, name = "Male"),
        Genre(icon = Icons.Default.Female, name = "Female")
    )

    val colors = MaterialTheme.colorScheme

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Choose your gender",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = colors.onSurface
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            genres.forEach { genre ->
                val isSelected = selectedGenre?.equals(genre.name, ignoreCase = true) == true
                GenreButton(
                    genre = genre,
                    isSelected = isSelected,
                    onClick = { onGenreSelected(
                        when (genre.name) {
                            "Male" -> RegisterGender.MALE
                            "Female" -> RegisterGender.FEMALE
                            else -> {
                                throw IllegalArgumentException("Invalid genre name")
                            }
                        }
                    ) },
                    hasError = errorList.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Exibe o texto de erro abaixo se existir
        if (errorList.isNotEmpty()) {
            Text(
                text = errorList.first(),
                color = colors.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun GenreButton(
    genre: Genre,
    isSelected: Boolean,
    onClick: () -> Unit,
    hasError: Boolean = false,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    // Background: quando selecionado, usar primaryContainer / primary com alpha para boa leitura
    val targetBg = when {
        hasError -> colors.surfaceVariant // deixar neutro; borda indicará erro
        isSelected -> colors.primary.copy(alpha = 0.14f)
        else -> colors.surface
    }
    val backgroundColor by animateColorAsState(
        targetValue = targetBg,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )

    // Conteúdo (ícone + texto)
    val contentTarget = when {
        isSelected -> colors.onPrimary
        else -> colors.onSurface
    }
    val contentColor by animateColorAsState(
        targetValue = contentTarget,
        animationSpec = tween(durationMillis = 260)
    )

    // Escala ao selecionar (pulso)
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.04f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    // Borda: erro > selected(primary) > outline/transparent
    val borderTarget = when {
        hasError -> colors.error
        isSelected -> colors.primary
        else -> colors.outline
    }
    val borderColor by animateColorAsState(targetValue = borderTarget, animationSpec = tween(250))

    Surface(
        onClick = onClick,
        modifier = modifier
            .height(50.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale),
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
        interactionSource = remember { MutableInteractionSource() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = genre.icon,
                    contentDescription = genre.name,
                    tint = contentColor
                )

                Text(
                    text = genre.name,
                    color = contentColor,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                    )
                )
            }
        }
    }
}