package com.example.presentation.screens.ui.device

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.FitverseColors
import com.example.presentation.ui.components.FitverseScreenTitle
import com.example.presentation.ui.components.FitverseTopBar
import com.example.presentation.ui.components.SectionLabel
import com.example.presentation.ui.components.ShapeCard


// ── Models ────────────────────────────────────────────────────────────────────

data class LiveMetric(val value: String, val unit: String, val isHighlighted: Boolean = false)

data class WearableDevice(
    val name: String,
    val icon: String,
    val statusDetail: String?,   // null = "Disponível"
    val isConnected: Boolean,
)

private val liveMetrics = listOf(
    LiveMetric("72",  "BPM"),
    LiveMetric("98",  "SPO2\n%"),
    LiveMetric("6.5k","Passos\nhoje"),
    LiveMetric("340", "Cal\nkcal", isHighlighted = true),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun DevicesScreen(onBack: () -> Unit) {
    var devices by remember {
        mutableStateOf(
            listOf(
                WearableDevice("Apple Watch",     "⌚", "Conectado · 72 BPM", isConnected = true),
                WearableDevice("Garmin Forerunner","⌚", null, isConnected = false),
                WearableDevice("Fitbit Charge",   "⌚", null, isConnected = false),
                WearableDevice("Mi Band 8",       "⌚", null, isConnected = false),
            )
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            FitverseTopBar(onBack = onBack)
        }
    ){
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(FitverseColors.Bg),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        ) {
            item {
                FitverseScreenTitle(
                    title    = "Dispositivos",
                    subtitle = "Conecte seus wearables e apps",
                )
                Spacer(Modifier.height(18.dp))
                LiveMetricsCard(metrics = liveMetrics)
                SectionLabel("Wearables")
            }

            items(devices, key = { it.name }) { device ->
                DeviceRow(
                    device   = device,
                    onToggle = { toggled ->
                        devices = devices.map {
                            if (it.name == device.name) it.copy(isConnected = toggled) else it
                        }
                    },
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }

            item { Spacer(Modifier.height(24.dp)) }
        }

    }
}

// ── Live metrics card ─────────────────────────────────────────────────────────

@Composable
private fun LiveMetricsCard(metrics: List<LiveMetric>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, FitverseColors.Accent, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(FitverseColors.Surface2)
            .padding(16.dp),
    ) {
        Text(
            text          = "⚡ MÉTRICAS AO VIVO",
            fontSize      = 10.sp,
            fontWeight    = FontWeight.ExtraBold,
            letterSpacing = 1.sp,
            color         = FitverseColors.Accent,
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            metrics.forEach { metric ->
                MetricItem(metric)
            }
        }
    }
}

@Composable
private fun MetricItem(metric: LiveMetric) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text       = metric.value,
            fontSize   = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color      = if (metric.isHighlighted) FitverseColors.Accent else FitverseColors.TextPrimary,
            lineHeight = 24.sp,
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text      = metric.unit,
            fontSize  = 10.sp,
            color     = FitverseColors.TextMuted,
            lineHeight = 14.sp,
        )
    }
}

// ── Device row ────────────────────────────────────────────────────────────────

@Composable
private fun DeviceRow(
    device: WearableDevice,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, FitverseColors.Border, ShapeCard)
            .clip(ShapeCard)
            .background(FitverseColors.Surface)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(FitverseColors.Surface2),
            contentAlignment = Alignment.Center,
        ) {
            Text(device.icon, fontSize = 20.sp)
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text       = device.name,
                fontSize   = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color      = FitverseColors.TextPrimary,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text  = device.statusDetail ?: "Disponível",
                fontSize = 12.sp,
                color = if (device.statusDetail != null) FitverseColors.Green else FitverseColors.TextMuted,
            )
        }

        FitverseToggle(checked = device.isConnected, onCheckedChange = onToggle)
    }
}

// ── Animated Toggle ───────────────────────────────────────────────────────────

@Composable
fun FitverseToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    activeColor: Color = FitverseColors.Green,
) {
    val trackColor by animateColorAsState(
        targetValue    = if (checked) activeColor else FitverseColors.Surface2,
        animationSpec  = tween(200),
        label          = "toggle_track",
    )
    val thumbOffset by animateDpAsState(
        targetValue   = if (checked) 18.dp else 3.dp,
        animationSpec = tween(200),
        label         = "toggle_thumb",
    )

    Switch(
        checked         = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedTrackColor   = trackColor,
            uncheckedTrackColor = FitverseColors.Surface2,
            checkedThumbColor   = Color.White,
            uncheckedThumbColor = Color.White,
            checkedBorderColor  = Color.Transparent,
            uncheckedBorderColor = FitverseColors.Border,
        ),
    )
}
