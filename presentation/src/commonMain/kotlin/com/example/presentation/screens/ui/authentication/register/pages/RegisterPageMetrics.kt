package com.example.presentation.screens.ui.authentication.register.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.Height
import androidx.compose.material.icons.rounded.MonitorWeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.presentation.screens.ui.authentication.register.actions.RegisterAction
import com.example.presentation.screens.ui.authentication.register.components.DatePicker
import com.example.presentation.screens.ui.authentication.register.components.FormHeader
import com.example.presentation.screens.ui.authentication.register.components.HeightPicker
import com.example.presentation.screens.ui.authentication.register.components.WeightPicker
import com.example.presentation.screens.ui.authentication.register.state.RegisterState

@Composable
fun RegisterPageMetrics(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormHeader(
            title = "Biometria do Avatar",
            subtitle = "Calibre seus dados para receber missões de treino adequadas e calcular seu gasto calórico com precisão."
        )
        // 1. Bloco de Data de Nascimento
        MetricSectionCard(
            title = "Data de Nascimento",
            icon = Icons.Rounded.Cake
        ) {
            DatePicker(
                onDateSelected = { day, month, year ->
                    onAction(RegisterAction.DateOfBirthChanged(day, month, year))
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Bloco de Peso
        MetricSectionCard(
            title = "Peso Corporal",
            icon = Icons.Rounded.MonitorWeight
        ) {
            WeightPicker(
                initialWeight = 70.0,
                onWeightSelected = { weight ->
                    onAction(RegisterAction.WeightChanged(weight))
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Bloco de Altura
        MetricSectionCard(
            title = "Altura",
            icon = Icons.Rounded.Height
        ) {
            HeightPicker(
                initialHeight = 170,
                onHeightSelected = { height ->
                    onAction(RegisterAction.HeightChanged(height))
                }
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
private fun MetricSectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 20.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            content()
        }
    }
}