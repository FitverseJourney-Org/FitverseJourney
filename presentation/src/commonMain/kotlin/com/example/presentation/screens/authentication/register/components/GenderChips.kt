package com.example.presentation.screens.authentication.register.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class Genre(
    val icon: ImageVector,
    val name: String,
)

@Composable
fun GenreSelection(
    selectedGenre: String?, // Recebe o valor atual (ex: "MALE")
    onGenreSelected: (String) -> Unit, // Callback para atualizar o ViewModel
    errorList: List<String>,
    modifier: Modifier = Modifier
) {
    val genres = listOf(
        Genre(
            icon = Icons.Default.Male,
            name = "Male"
        ),
        Genre(
            icon = Icons.Default.Female,
            name = "Female"
        )
    )

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(
            text = "Choose your gender",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            genres.forEach { genre ->
                val isSelected = selectedGenre?.equals(genre.name, ignoreCase = true) == true
                GenreButton(
                    genre = genre,
                    isSelected = isSelected,
                    onClick = { onGenreSelected(genre.name.uppercase()) },
                    hasError = errorList.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Exibe o texto de erro abaixo se existir
        if (errorList.isNotEmpty()) {
            Text(
                text = errorList.first(),
                color = Color.Red,
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
    // 1. Animação de Cor do Background
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF033101) else Color.Transparent,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        label = "bgColor"
    )

    // 2. Animação de Cor do Texto/Ícone
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Gray,
        animationSpec = tween(durationMillis = 300),
        label = "contentColor"
    )

    // 3. Animação de Escala (Pulso ao selecionar)
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    val borderColor by animateColorAsState(
        targetValue = if (hasError) Color(0xFFFF0000) else Color(0xFF033101),
        label = "borderColor"
    )

    Surface(
        onClick = onClick,
        modifier = modifier
            .height(50.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale), // Aplica a animação de escala
        shape = RoundedCornerShape(6.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
        interactionSource = remember { MutableInteractionSource() } // Remove o ripple padrão se desejar algo customizado
    ) {
        Box(contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Icon(
                    imageVector = genre.icon,
                    contentDescription = null,
                    tint = contentColor,
                )
                Text(
                    text = genre.name,
                    color = contentColor,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontFamily = FontFamily.Serif
                    )
                )
            }
        }
    }
}

@Composable
fun rememberShakeState(): com.example.presentation.screens.authentication.register.components.ShakeController {
    val shakeOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    return remember(shakeOffset, scope) {
        _root_ide_package_.com.example.presentation.screens.authentication.register.components.ShakeController(
            shakeOffset,
            scope
        )
    }
}

class ShakeController(
    val shakeOffset: Animatable<Float, AnimationVector1D>,
    val scope: CoroutineScope
) {
    fun shake() {
        scope.launch {
            // Sequência de movimentos: direita, esquerda, direita, esquerda, centro
            val shakeIntensity = 5f
            val duration = 50

            repeat(3) {
                shakeOffset.animateTo(shakeIntensity, tween(duration))
                shakeOffset.animateTo(-shakeIntensity, tween(duration))
            }
            shakeOffset.animateTo(0f, tween(duration))
        }
    }
}