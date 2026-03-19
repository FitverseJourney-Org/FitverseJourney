package com.example.presentation.screens.ui.device

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.device.onSurfaceVariant
import kotlin.math.roundToInt

/* ===================== MODELS ===================== */

data class DeviceUi(
    val id: String,
    val name: String,
    val vendor: String,
    val connected: Boolean = false
)

/* ===================== SCREEN ===================== */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceSettingsScreenPro(
    navigateBack: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val metrics = remember {
        mutableStateListOf(
            MetricUi(
                "steps",
                "Passos Diários",
                true,
                true,
                "Sincronizado via Telefone",
                "phone",
                Icons.Rounded.DirectionsWalk
            ),
            _root_ide_package_.com.example.presentation.screens.ui.device.MetricUi(
                "heart",
                "Frequência Cardíaca",
                false,
                false,
                "Nenhum sensor pareado",
                "watch",
                Icons.Rounded.Favorite
            ),
            _root_ide_package_.com.example.presentation.screens.ui.device.MetricUi(
                "distance",
                "Distância Percorrida",
                true,
                false,
                "GPS Ativo",
                "phone",
                Icons.Rounded.Route
            ),
            _root_ide_package_.com.example.presentation.screens.ui.device.MetricUi(
                "calories",
                "Calorias",
                true,
                false,
                "Baseado em Biometria",
                "phone",
                Icons.Rounded.LocalFireDepartment
            ),
            _root_ide_package_.com.example.presentation.screens.ui.device.MetricUi(
                "sleep",
                "Monitoramento de Sono",
                false,
                false,
                "Aguardando conexão",
                "platform",
                Icons.Rounded.Bedtime
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dispositivos e Saúde", fontWeight = FontWeight.Black, fontSize = 16.sp) },
                windowInsets = WindowInsets(0.dp),
                navigationIcon = {
                    IconButton(onClick = navigateBack) { Icon(Icons.Rounded.ChevronLeft, null) }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = cs.background)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Brush.verticalGradient(listOf(cs.background, cs.surfaceVariant.copy(alpha = 0.3f)))),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Hero Section (Agora como item da lista para melhor UX)
            item {
                _root_ide_package_.com.example.presentation.screens.ui.device.SettingsHero(
                    steps = 8420,
                    goal = 10000,
                    calories = 450,
                    activeMinutes = 32
                )
            }

            // 2. Título da Seção
            item {
                Column(Modifier.padding(top = 8.dp, start = 4.dp)) {
                    Text("Gerenciar Medidores", fontWeight = FontWeight.Black, fontSize = 18.sp, color = cs.onBackground)
                    Text("Escolha como os dados são coletados", fontSize = 12.sp, color = cs.onSurfaceVariant)
                }
            }

            // 3. Lista de Métricas
            items(metrics) { metric ->
                _root_ide_package_.com.example.presentation.screens.ui.device.MetricCardPro(
                    metric = metric,
                    onToggle = { metric.enabled = it },
                    onConnectAction = { /* Simular conexão */ },
                    onSourceSelect = { metric.source = it }
                )
            }

            item { _root_ide_package_.com.example.presentation.screens.ui.device.FooterLinks() }
        }
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
                    _root_ide_package_.com.example.presentation.screens.ui.device.MiniMetric(
                        "Passos",
                        steps.toString(),
                        Icons.Default.DirectionsWalk
                    )
                    _root_ide_package_.com.example.presentation.screens.ui.device.MiniMetric(
                        "Cal",
                        calories.toString(),
                        Icons.Default.LocalFireDepartment
                    )
                    _root_ide_package_.com.example.presentation.screens.ui.device.MiniMetric(
                        "Min",
                        activeMinutes.toString(),
                        Icons.Default.Timer
                    )
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
fun MetricCardPro(
    metric: com.example.presentation.screens.ui.device.MetricUi,
    onToggle: (Boolean) -> Unit,
    onConnectAction: () -> Unit,
    onSourceSelect: (String) -> Unit
) {
    val cs = MaterialTheme.colorScheme
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f)),
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Ícone com Background Dinâmico
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (metric.enabled) cs.primary.copy(0.1f) else cs.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        metric.icon, null,
                        tint = if (metric.enabled) cs.primary else cs.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column(Modifier.weight(1f)) {
                    Text(metric.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    _root_ide_package_.com.example.presentation.screens.ui.device.StatusTag(metric)
                }

                Switch(
                    checked = metric.enabled,
                    onCheckedChange = onToggle,
                    thumbContent = if (metric.enabled) {
                        { Icon(Icons.Rounded.Check, null, Modifier.size(12.dp)) }
                    } else null
                )
            }

            AnimatedVisibility(visible = metric.enabled) {
                Column {
                    Divider(Modifier.padding(vertical = 16.dp), thickness = 0.5.dp, color = cs.outline.copy(0.1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Botão de Conexão Estilizado
                        Button(
                            onClick = onConnectAction,
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (metric.connected) cs.secondaryContainer else cs.primary,
                                contentColor = if (metric.connected) cs.onSecondaryContainer else cs.onPrimary
                            )
                        ) {
                            Icon(if (metric.connected) Icons.Rounded.Sync else Icons.Rounded.Add, null, Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(if (metric.connected) "Re-sincronizar" else "Conectar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        // Botão de Opções
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.Settings,
                                null, tint = cs.primary
                            )
                        }
                    }

                    if (expanded) {
                        Spacer(Modifier.height(12.dp))
                        _root_ide_package_.com.example.presentation.screens.ui.device.SourceSelectorPro(
                            metric.source,
                            onSourceSelect
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusTag(metric: com.example.presentation.screens.ui.device.MetricUi) {
    val cs = MaterialTheme.colorScheme
    val (text, color) = when {
        !metric.enabled -> "Desativado" to cs.onSurfaceVariant.copy(0.6f)
        metric.connected -> "Conectado" to Color(0xFF4CAF50)
        else -> "Aguardando Fonte" to Color(0xFFFF9800)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(6.dp).background(color, CircleShape))
        Spacer(Modifier.width(6.dp))
        Text(text, fontSize = 11.sp, color = color, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SourceSelectorPro(current: String, onSelect: (String) -> Unit) {
    val sources = listOf("phone" to "Telefone", "watch" to "Relógio", "platform" to "Cloud")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        sources.forEach { (id, label) ->
            val isSelected = current == id
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(0.5f))
                    .clickable { onSelect(id) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    label,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.onSurfaceVariant()
                )
            }
        }
    }
}

// Extensão apenas para exemplo de cor
@Composable
fun MaterialTheme.onSurfaceVariant() = MaterialTheme.colorScheme.onSurfaceVariant

/* ===================== MODELS ATUALIZADOS ===================== */

class MetricUi(
    val id: String,
    val title: String,
    enabled: Boolean,
    connected: Boolean,
    val subtitle: String?,
    sourceInitial: String,
    val icon: ImageVector // Ícone agora faz parte do modelo para personalização
) {
    var enabled by mutableStateOf(enabled)
    var connected by mutableStateOf(connected)
    var source by mutableStateOf(sourceInitial)
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