// SettingsInMemoryUiEnhancedV2_Extended_FontsAdjusted.kt
package com.example.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


private val TextPrimary = Color(0xFFEAF6F0)
private val TextSecondary = Color(0xFFB8D5C6)
private val TextTertiary = Color(0xFF8FB3A2)
private val TextDisabled = Color(0xFF6E8F80)
private val TextAccent = Color(0xFF7CF2B2)

private val BaseGreen = Color(0xFF0E2015)
private val DeepGreen = Color(0xFF08160F)
private val CardBgDefaultColor = Color(0xFF10261A)
private val AccentGreen = Color(0xFF7CF2B2)

private val backgroundBrush = Brush.verticalGradient(
    listOf(BaseGreen, DeepGreen)
)

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
    val metrics = remember {
        mutableStateListOf(
            MetricUi("steps", "Passos", true, true, "Sensor do telefone", "phone"),
            MetricUi("heart", "Frequência cardíaca", false, false, "Sensor óptico", "watch"),
            MetricUi("distance", "Distância", true, false, "GPS / Sensor", "phone"),
            MetricUi("calories", "Calorias", false, false, "Estimativa baseada em passos", "phone"),
            MetricUi("sleep", "Sono", false, false, "Dados de sono", "platform")
        )
    }

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBarDefault(
                title = "Medidores & Dispositivos",
                onClickBack = onClickBack
            )
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .background(backgroundBrush)
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                SettingsSectionTitle(
                    title = "Configure seus dispositivos",
                    description = "Habilite medidores, conecte relógios e ajuste sincronização."
                )
            }

            items(metrics, key = { it.id }) { metric ->
                MetricCard(
                    metric = metric,
                    onToggle = {
                        metric.enabled = it
                        if (!it) metric.connected = false
                    },
                    onConnectAction = {
                        scope.launch {
                            metric.connecting = true
                            delay(900)
                            metric.connecting = false
                            metric.connected = !metric.connected
                            snackbar.showSnackbar(
                                if (metric.connected) "${metric.title} conectado" else "${metric.title} desconectado"
                            )
                        }
                    },
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

/* ===================== HERO (FONTES AJUSTADAS) ===================== */

@Composable
fun SettingsHero(
    steps: Int,
    goal: Int,
    calories: Int,
    activeMinutes: Int
) {
    val progress = (steps.toFloat() / goal).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBgDefaultColor),
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
                    color = AccentGreen,
                    strokeWidth = 10.dp,
                    modifier = Modifier.matchParentSize()
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${(progress * 100).roundToInt()}%",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium // menor que titleLarge
                    )
                    Text(
                        "da meta",
                        color = TextSecondary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(Modifier.width(14.dp))

            Column {
                Text(
                    "Hoje",
                    color = TextPrimary,
                    style = MaterialTheme.typography.labelMedium // reduzido
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
                    color = TextTertiary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun MiniMetric(label: String, value: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = TextAccent, modifier = Modifier.size(16.dp))
        Spacer(Modifier.height(4.dp))
        Text(value, color = TextPrimary, style = MaterialTheme.typography.bodySmall) // menor
        Text(label, color = TextSecondary, style = MaterialTheme.typography.labelSmall)
    }
}

/* ===================== METRIC CARD (FONTES AJUSTADAS) ===================== */

@Composable
fun MetricCard(
    metric: MetricUi,
    onToggle: (Boolean) -> Unit,
    onConnectAction: () -> Unit,
    onToggleExpand: () -> Unit,
    onSourceSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBgDefaultColor),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column {

            StatusBadge(
                text = when {
                    metric.connecting -> "Conectando..."
                    metric.connected -> "Conectado"
                    metric.enabled -> "Ativado"
                    else -> "Desativado"
                },
                color = if (metric.connected) AccentGreen else TextSecondary
            )

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = TextAccent,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        metric.title,
                        color = TextPrimary,
                        style = MaterialTheme.typography.bodyMedium
                    ) // reduzido
                    metric.subtitle?.let {
                        Text(it, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    }
                }

                ConnectButton(metric, onConnectAction)

                Spacer(Modifier.width(8.dp))

                Switch(
                    checked = metric.enabled,
                    onCheckedChange = onToggle
                )
            }

            if (metric.expanded) {
                SourceSelector(metric.source, onSourceSelect)
            } else {
                Text(
                    "Mais opções",
                    color = TextAccent,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 12.dp)
                        .clickable { onToggleExpand() }
                )
            }
        }
    }
}

/* ===================== SMALL COMPOSABLES (FONTES AJUSTADAS) ===================== */

@Composable
fun StatusBadge(text: String, color: Color) {
    Surface(
        modifier = Modifier.padding(12.dp),
        color = color.copy(alpha = 0.18f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text,
            color = TextPrimary,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun ConnectButton(state: MetricUi, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentGreen,
            contentColor = DeepGreen
        ),
        enabled = !state.connecting,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            if (state.connected) "Desconectar" else "Conectar",
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun SourceSelector(current: String, onSelect: (String) -> Unit) {
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
                        color = if (current == it.first) DeepGreen else TextSecondary
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (current == it.first) AccentGreen else CardBgDefaultColor
                )
            )
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String, description: String) {
    Column(Modifier.padding(16.dp)) {
        Text(title, color = TextPrimary, style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(6.dp))
        Text(description, color = TextSecondary, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun FooterLinks() {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            TextButton(
                onClick = {}
            ) {
                Text("Termos", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
            }
            TextButton(
                onClick = {}
            ) {
                Text(
                    "Privacidade",
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            TextButton(
                onClick = {}
            ) {
                Text("Suporte", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

/* ===================== TOP BAR (TAMANHO AJUSTADO) ===================== */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarDefault(
    title: String? = null,
    onClickBack: () -> Unit
) {
    TopAppBar(
        title = {
            if (title != null) {
                Text(
                    text = title,
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium, // reduzido
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        navigationIcon = {
            IconButton(onClickBack) {
                Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = TextPrimary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

