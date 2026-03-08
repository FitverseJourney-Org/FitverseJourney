package com.example.presentation.screens.ui.device

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/* ===================== MODELS ===================== */

class MetricUi(
    val id: String,
    val title: String,
    enabled: Boolean,
    connected: Boolean,
    val subtitle: String?,
    sourceInitial: String
) {
    var enabled by mutableStateOf(enabled)
    var connected by mutableStateOf(connected)
    var connecting by mutableStateOf(false)
    var source by mutableStateOf(sourceInitial)
    var expanded by mutableStateOf(false)
}

data class DeviceUi(
    val id: String,
    val name: String,
    val vendor: String,
    val connected: Boolean = false
)

/* ===================== SCREEN ===================== */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenInMemoryEnhancedV2_Extended(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit = {}
) {
    val cs = MaterialTheme.colorScheme
    val backgroundBrush = Brush.verticalGradient(listOf(cs.background, cs.surface))

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val snackbar = remember { SnackbarHostState() }
    val metrics = remember {
        mutableStateListOf(
            MetricUi("steps", "Passos", true, true, "Sensor do telefone", "phone"),
            MetricUi("heart", "Frequência cardíaca", false, false, "Sensor óptico", "watch"),
            MetricUi("distance", "Distância", true, false, "GPS / Sensor", "phone"),
            MetricUi("calories", "Calorias", false, false, "Estimativa baseada em passos", "phone"),
            MetricUi("sleep", "Sono", false, false, "Dados de sono", "platform")
        )
    }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            IconButton(onClick = {
                onClickBack()
            }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription =  null,
                    tint = Color.White
                )
            }
            Text(
                text = "Medidores e Dispositivos",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                ),

                )
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier.background(backgroundBrush).fillMaxSize(),
        ) {

            item {
                SettingsSectionTitle(
                    title = "Configure seus dispositivos",
                    description = "Habilite medidores, conecte relógios e ajuste sincronização."
                )
            }

            items(metrics) { metric ->
                MetricCard(
                    metric = metric,
                    onToggle = { metric.enabled = it },
                    onConnectAction = { /* connect */ },
                    onToggleExpand = { metric.expanded = !metric.expanded },
                    onSourceSelect = { metric.source = it }
                )
            }

            item {
                Spacer(Modifier.height(24.dp))
                FooterLinks()
            }
        }
    }
}

/* ===================== HERO (AJUSTES) ===================== */

@Composable
fun SettingsHero(
    steps: Int,
    goal: Int,
    calories: Int,
    activeMinutes: Int
) {
    val cs = MaterialTheme.colorScheme
    val progress = (steps.toFloat() / goal).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(88.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = progress,
                    color = cs.primary,
                    strokeWidth = 10.dp,
                    modifier = Modifier.matchParentSize()
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${(progress * 100).roundToInt()}%",
                        color = cs.onSurface,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "da meta",
                        color = cs.onSurface.copy(alpha = 0.75f),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(Modifier.width(14.dp))

            Column {
                Text(
                    "Hoje",
                    color = cs.onSurface,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MiniMetric("Passos", steps.toString(), Icons.Default.DirectionsWalk)
                    MiniMetric("Cal", calories.toString(), Icons.Default.LocalFireDepartment)
                    MiniMetric("Min", activeMinutes.toString(), Icons.Default.Timer)
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    "Toque para ver detalhes da atividade",
                    color = cs.onSurface.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun MiniMetric(label: String, value: String, icon: ImageVector) {
    val cs = MaterialTheme.colorScheme
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = cs.primary, modifier = Modifier.size(16.dp))
        Spacer(Modifier.height(4.dp))
        Text(value, color = cs.onSurface, style = MaterialTheme.typography.bodySmall)
        Text(label, color = cs.onSurface.copy(alpha = 0.8f), style = MaterialTheme.typography.labelSmall)
    }
}

/* ===================== METRIC CARD ===================== */

@Composable
fun MetricCard(
    metric: MetricUi,
    onToggle: (Boolean) -> Unit,
    onConnectAction: () -> Unit,
    onToggleExpand: () -> Unit,
    onSourceSelect: (String) -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
        shape = RoundedCornerShape(18.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(cs.primary.copy(alpha = 0.10f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.FitnessCenter,
                        null,
                        tint = cs.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {

                    Text(
                        metric.title,
                        color = cs.onSurface,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    metric.subtitle?.let {
                        Text(
                            it,
                            color = cs.onSurface.copy(alpha = 0.75f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    StatusBadge(
                        text = when {
                            metric.connecting -> "Conectando..."
                            metric.connected -> "Conectado"
                            metric.enabled -> "Ativado"
                            else -> "Desativado"
                        },
                        color = if (metric.connected) cs.primary else cs.outline
                    )
                }

                Switch(
                    checked = metric.enabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = cs.primary,
                        checkedTrackColor = cs.primary.copy(alpha = 0.3f)
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                ConnectButton(metric, onConnectAction)

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onToggleExpand() }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Mais opções",
                        color = cs.primary,
                        style = MaterialTheme.typography.labelMedium
                    )

                    Spacer(Modifier.width(4.dp))

                    Icon(
                        if (metric.expanded)
                            Icons.Default.KeyboardArrowUp
                        else
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = cs.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            if (metric.expanded) {
                Spacer(Modifier.height(12.dp))
                SourceSelector(metric.source, onSourceSelect)
            }
        }
    }
}

/* ===================== SMALL COMPOSABLES ===================== */

@Composable
fun StatusBadge(text: String, color: Color) {
    val cs = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        color = color,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text,
            color = cs.onPrimary.takeIf { color == cs.primary } ?: cs.onSurface,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ConnectButton(state: MetricUi, onClick: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (state.connected) cs.secondary else cs.primary,
            contentColor = if (state.connected) cs.onSecondary else cs.onPrimary
        ),
        enabled = !state.connecting,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            if (state.connected) "Desconectar" else "Conectar",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SourceSelector(current: String, onSelect: (String) -> Unit) {
    val cs = MaterialTheme.colorScheme
    Row(
        Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("phone" to "Telefone", "watch" to "Relógio", "platform" to "Plataforma").forEach {
            AssistChip(
                onClick = { onSelect(it.first) },
                label = {
                    Text(
                        it.second,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (current == it.first) cs.onSecondary else cs.onSurface.copy(alpha = 0.9f)
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (current == it.first) cs.secondary.copy(alpha = 0.16f) else cs.surfaceVariant,
                    labelColor = if (current == it.first) cs.onSecondary else cs.onSurface
                ),
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String, description: String) {
    val cs = MaterialTheme.colorScheme
    Column(Modifier.padding(16.dp)) {
        Text(title, color = cs.onSurface, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        Text(description, color = cs.onSurface.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun FooterLinks() {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            TextButton(onClick = {}) { Text("Termos", color = cs.onSurface.copy(alpha = 0.85f), style = MaterialTheme.typography.labelSmall) }
            TextButton(onClick = {}) { Text("Privacidade", color = cs.onSurface.copy(alpha = 0.85f), style = MaterialTheme.typography.labelSmall) }
            TextButton(onClick = {}) { Text("Suporte", color = cs.onSurface.copy(alpha = 0.85f), style = MaterialTheme.typography.labelSmall) }
        }
    }
}

/* ===================== TOP BAR ===================== */

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TopAppBarDefault(
//    title: String? = null,
//    onClickBack: () -> Unit
//) {
//    val cs = MaterialTheme.colorScheme
//    TopAppBar(
//        title = {
//            if (title != null) {
//                Text(
//                    text = title,
//                    color = cs.onSurface,
//                    style = MaterialTheme.typography.titleMedium,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
//        },
//        navigationIcon = {
//            IconButton(onClickBack) {
//                Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = cs.onSurface)
//            }
//        },
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = Color.Transparent
//        )
//    )
//}