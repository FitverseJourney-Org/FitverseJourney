package com.example.presentation.screens.authentication.register.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RangeSelector(
    title: String,
    unit: String,
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "$value $unit",
            color = Color.White,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = range.first.toFloat()..range.last.toFloat(),
            steps = range.last - range.first - 1
        )
    }
}
@Composable
fun CustomRangeSlider(
    value: Int,
    range: IntRange,
    unit: String,
    title: String,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.Red,
    inactiveColor: Color = Color.DarkGray
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        // Title
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(6.dp))

        // Value
        Text(
            text = "$value $unit",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        ) {
            val widthPx = constraints.maxWidth.toFloat()
            val progress =
                (value - range.first).toFloat() / (range.last - range.first)

            val thumbRadius = 14.dp
            val thumbRadiusPx = with(LocalDensity.current) { thumbRadius.toPx() }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures { change, _ ->
                            val x = change.position.x
                                .coerceIn(0f, widthPx)

                            val percent = x / widthPx
                            val newValue =
                                range.first + ((range.last - range.first) * percent).toInt()

                            onValueChange(newValue)
                        }
                    }
            ) {

                val centerY = size.height / 2

                // Inactive track
                drawLine(
                    color = inactiveColor,
                    start = Offset(0f, centerY),
                    end = Offset(size.width, centerY),
                    strokeWidth = 9f,
                    cap = StrokeCap.Round
                )

                // Active track
                drawLine(
                    color = activeColor,
                    start = Offset(0f, centerY),
                    end = Offset(size.width * progress, centerY),
                    strokeWidth = 9f,
                    cap = StrokeCap.Round
                )

                // Thumb
                drawCircle(
                    color = activeColor,
                    radius = thumbRadiusPx,
                    center = Offset(size.width * progress, centerY)
                )
            }
        }
    }
}

@Composable
fun HeightRangeSelector(
    height: Int,
    onHeightChange: (Int) -> Unit
) {
    _root_ide_package_.com.example.presentation.screens.authentication.register.components.CustomRangeSlider(
        title = "Height",
        unit = "cm",
        value = height,
        range = 120..220,
        onValueChange = onHeightChange,
        activeColor = Color(0xFF4CAF50)
    )
}
@Composable
fun WeightRangeSelector(
    weight: Int,
    onWeightChange: (Int) -> Unit
) {
    _root_ide_package_.com.example.presentation.screens.authentication.register.components.CustomRangeSlider(
        title = "Weight",
        unit = "kg",
        value = weight,
        range = 40..200,
        onValueChange = onWeightChange,
        activeColor = Color(0xFFFF5722)
    )
}
