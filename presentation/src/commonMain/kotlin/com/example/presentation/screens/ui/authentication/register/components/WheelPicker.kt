package com.example.presentation.screens.ui.authentication.register.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> WheelPicker(
    items: List<T>,
    initialIndex: Int = 0,
    itemHeight: Dp = 56.dp, // Aumentado levemente para melhor área de toque
    visibleItemsCount: Int = 5,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    selectedTextColor: Color = MaterialTheme.colorScheme.primary,
    unselectedTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    onItemSelected: (T) -> Unit,
    itemContent: @Composable (T) -> String = { it.toString() }
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val hapticFeedback = LocalHapticFeedback.current

    // SnapshotFlow é mais eficiente para observar mudanças contínuas de layout e disparar side-effects
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty()) return@snapshotFlow initialIndex

            val center = layoutInfo.viewportStartOffset + (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2
            layoutInfo.visibleItemsInfo.minByOrNull { abs((it.offset + it.size / 2) - center) }?.index ?: initialIndex
        }
            .distinctUntilChanged()
            .collect { index ->
                if (listState.isScrollInProgress) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
                onItemSelected(items[index])
            }
    }

    Box(
        modifier = Modifier
            .height(itemHeight * visibleItemsCount)
            .fillMaxWidth()
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
                drawContent()
                // Fading edge mais suave
                drawRect(
                    brush = Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.3f to Color.Black,
                        0.7f to Color.Black,
                        1f to Color.Transparent
                    ),
                    blendMode = BlendMode.DstIn
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Indicador de Seleção Central Moderno (Atrás do texto)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(
                    color = selectedTextColor.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        LazyColumn(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = itemHeight * (visibleItemsCount / 2))
        ) {
            items(items.size) { index ->
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .graphicsLayer {
                            val layoutInfo = listState.layoutInfo
                            val visibleInfo = layoutInfo.visibleItemsInfo.find { it.index == index }

                            if (visibleInfo != null) {
                                val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
                                val centerOfViewport = layoutInfo.viewportStartOffset + viewportHeight / 2f
                                val itemCenter = visibleInfo.offset + visibleInfo.size / 2f

                                val distance = itemCenter - centerOfViewport
                                val fraction = (distance / (viewportHeight / 2f)).coerceIn(-1f, 1f)

                                // Easing suave para o efeito 3D (não linear)
                                val interpolatedFraction = abs(fraction) * abs(fraction)

                                rotationX = fraction * 45f
                                val scale = 1f - 0.25f * interpolatedFraction
                                scaleX = scale
                                scaleY = scale

                                alpha = 1f - 0.6f * interpolatedFraction
                            } else {
                                alpha = 0f
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Calculamos a cor baseada no fato de ser o centro ou não, de forma simplificada
                    // A animação real de cor/alfa ocorre no graphicsLayer acima
                    Text(
                        text = itemContent(items[index]),
                        style = textStyle.copy(fontWeight = FontWeight.Bold),
                        color = selectedTextColor,
                        maxLines = 1
                    )
                }
            }
        }
    }
}