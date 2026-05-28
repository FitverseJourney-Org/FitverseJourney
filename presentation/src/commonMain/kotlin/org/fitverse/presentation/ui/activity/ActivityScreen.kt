package org.fitverse.presentation.ui.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DirectionsBike
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Straighten
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.theme.FitColors

// ── Tipos de atividade ─────────────────────────────────────────────────────────
// Cada tipo carrega seu próprio ícone e cor — sem ambiguidade visual no seletor.

enum class ActivityType(
    val label: String,
    val icon: ImageVector,
    val color: Color,
) {
    Corrida  ("Corrida",   Icons.Rounded.DirectionsRun,  FitColors.Orange),
    Caminhada("Caminhada", Icons.Rounded.DirectionsWalk, FitColors.Green),
    Bike     ("Bike",      Icons.Rounded.DirectionsBike, FitColors.Blue),
    Outro    ("Outro",     Icons.Rounded.FitnessCenter,  FitColors.Purple),
}

// ── Data models ───────────────────────────────────────────────────────────────

data class DaySummary(
    val steps: Int,
    val calories: Int,
    val distanceKm: Float,
    val activeMinutes: Int,
)

data class WeekDay(
    val label: String,
    val isActive: Boolean,
    val isToday: Boolean,
)

data class SplitData(
    val km: Int,
    val pace: String,
    val heartRate: Int,
)

data class RecentActivity(
    val type: ActivityType,
    val durationMinutes: Int,
    val distanceKm: Float,
    val date: String,
    val calories: Int,
    val avgHeartRate: Int,
    val elevationGainM: Int,
    val splits: List<SplitData>,
)

// ── Mock data — substituir por ViewModel + StateFlow em produção ───────────────

private val todaySummary = DaySummary(
    steps         = 8_420,
    calories      = 347,
    distanceKm    = 6.2f,
    activeMinutes = 72,
)

private val weekDays = listOf(
    WeekDay("SEG", isActive = true,  isToday = false),
    WeekDay("TER", isActive = true,  isToday = false),
    WeekDay("QUA", isActive = true,  isToday = false),
    WeekDay("QUI", isActive = true,  isToday = true),
    WeekDay("SEX", isActive = false, isToday = false),
    WeekDay("SAB", isActive = false, isToday = false),
    WeekDay("DOM", isActive = false, isToday = false),
)

private val recentActivities = listOf(
    RecentActivity(
        type            = ActivityType.Corrida,
        durationMinutes = 35,
        distanceKm      = 5.1f,
        date            = "Hoje, 14:32",
        calories        = 312,
        avgHeartRate    = 152,
        elevationGainM  = 48,
        splits = listOf(
            SplitData(1, "6:32", 142),
            SplitData(2, "6:45", 148),
            SplitData(3, "6:28", 145),
            SplitData(4, "7:02", 155),
            SplitData(5, "6:58", 162),
        )
    ),
    RecentActivity(
        type            = ActivityType.Bike,
        durationMinutes = 52,
        distanceKm      = 18.4f,
        date            = "Ontem, 07:15",
        calories        = 498,
        avgHeartRate    = 134,
        elevationGainM  = 215,
        splits = listOf(
            SplitData(5,  "14:02", 128),
            SplitData(10, "13:48", 136),
            SplitData(15, "13:31", 138),
            SplitData(18, "8:22",  131),
        )
    ),
    RecentActivity(
        type            = ActivityType.Caminhada,
        durationMinutes = 28,
        distanceKm      = 2.8f,
        date            = "Ter, 06 Mai",
        calories        = 178,
        avgHeartRate    = 108,
        elevationGainM  = 12,
        splits = listOf(
            SplitData(1, "9:48",  104),
            SplitData(2, "10:12", 110),
        )
    ),
    RecentActivity(
        type            = ActivityType.Corrida,
        durationMinutes = 42,
        distanceKm      = 6.7f,
        date            = "Seg, 05 Mai",
        calories        = 398,
        avgHeartRate    = 158,
        elevationGainM  = 67,
        splits = listOf(
            SplitData(1, "6:12", 148),
            SplitData(2, "6:18", 152),
            SplitData(3, "6:25", 156),
            SplitData(4, "6:31", 161),
            SplitData(5, "6:28", 163),
            SplitData(6, "6:38", 165),
        )
    ),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onStartActivity: () -> Unit = {},
    onActivityTapped: (RecentActivity) -> Unit = {},
) {
    var selectedType by remember { mutableStateOf(ActivityType.Corrida) }

    Scaffold(
        modifier            = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor      = Color.Transparent,
    ) { _ ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh    = onRefresh,
            modifier     = Modifier.fillMaxSize(),
        ) {
            LazyColumn(
                modifier       = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 40.dp),
            ) {
                item {
                    ActivityHeader(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
                    )
                }

                // Chips de seleção — feedback visual imediato no tap
                item {
                    TypeSelector(
                        selected = selectedType,
                        onSelect = { selectedType = it },
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }

                // CTA dominante — único elemento que pede ação nessa tela
                item {
                    StartCTA(
                        type    = selectedType,
                        onClick = onStartActivity,
                    )
                }

                // Resumo do dia — 4 métricas em grid 2x2, fonte ≥28sp (legível em pé)
                item {
                    DaySummarySection(
                        summary  = todaySummary,
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }

                item { Spacer(Modifier.height(28.dp)) }

                // Meta semanal — progresso passivo, urgência ou conquista ao primeiro olhar
                item {
                    WeeklyGoalSection(
                        days     = weekDays,
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }

                item { Spacer(Modifier.height(28.dp)) }

                // Atividades recentes — feed compacto, escaneável (referência: Strava)
                item {
                    SectionLabel(
                        text     = "ATIVIDADES RECENTES",
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                    Spacer(Modifier.height(12.dp))
                }

                items(recentActivities) { activity ->
                    ActivityRow(
                        activity = activity,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 3.dp),
                        onClick  = { onActivityTapped(activity) },
                    )
                }
            }
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun ActivityHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            "HOJE",
            color         = FitColors.TextMuted,
            fontSize      = 11.sp,
            fontWeight    = FontWeight.Medium,
            letterSpacing = 1.5.sp,
        )
        Text(
            "ATIVIDADE",
            color         = FitColors.TextPrimary,
            fontSize      = 28.sp,
            fontWeight    = FontWeight.Black,
            letterSpacing = (-0.5).sp,
        )
    }
}

// ── Type selector ─────────────────────────────────────────────────────────────
// Chip selecionado: background sólido na cor do tipo, texto escuro — contraste máximo.
// Chip inativo: Surface2, texto muted — claramente secundário.

@Composable
private fun TypeSelector(
    selected: ActivityType,
    onSelect: (ActivityType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier              = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ActivityType.entries.forEach { type ->
            val isSelected = type == selected
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) type.color else FitColors.Surface2)
                    .clickable { onSelect(type) }
                    .padding(vertical = 11.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector        = type.icon,
                    contentDescription = null,
                    tint               = if (isSelected) FitColors.Bg else FitColors.TextMuted,
                    modifier           = Modifier.size(15.dp),
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    type.label,
                    color      = if (isSelected) FitColors.Bg else FitColors.TextMuted,
                    fontSize   = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                )
            }
        }
    }
}

// ── Start CTA ─────────────────────────────────────────────────────────────────
// Hierarquia máxima — único elemento que pede ação na tela.
// Referência: Nike Run Club — botão central, sem ruído ao redor.
// Espaço vertical amplo (~96dp total) cria respiro e comunicaa importância.

@Composable
private fun StartCTA(
    type: ActivityType,
    onClick: () -> Unit,
) {
    Column(
        modifier              = Modifier.fillMaxWidth().padding(vertical = 44.dp),
        horizontalAlignment   = Alignment.CenterHorizontally,
        verticalArrangement   = Arrangement.spacedBy(0.dp),
    ) {
        // Anel de glow — profundidade sem poluição visual
        Box(
            modifier         = Modifier
                .size(164.dp)
                .clip(CircleShape)
                .background(type.color.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center,
        ) {
            // Botão primário — sem borda, sem sombra, só cor e ícone
            Box(
                modifier = Modifier
                    .size(124.dp)
                    .clip(CircleShape)
                    .background(type.color)
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Icon(
                        imageVector        = Icons.Rounded.PlayArrow,
                        contentDescription = "Iniciar atividade",
                        tint               = FitColors.Bg,
                        modifier           = Modifier.size(44.dp),
                    )
                    Text(
                        "INICIAR",
                        color         = FitColors.Bg,
                        fontSize      = 12.sp,
                        fontWeight    = FontWeight.Black,
                        letterSpacing = 2.sp,
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // Label do tipo selecionado — confirma contexto sem CTAs extras
        Text(
            type.label.uppercase(),
            color         = type.color,
            fontSize      = 12.sp,
            fontWeight    = FontWeight.SemiBold,
            letterSpacing = 1.5.sp,
        )
        Text(
            "toque para iniciar",
            color    = FitColors.TextDisabled,
            fontSize = 11.sp,
        )
    }
}

// ── Day summary ───────────────────────────────────────────────────────────────
// Grid 2x2 — referência: Apple Fitness+ rings / Garmin dashboard.
// Sem bordas nos cards — fundo SurfaceCard já cria separação visual suficiente.
// Fonte de métrica ≥28sp: usuário pode estar em pé, suado, longe da tela.

@Composable
private fun DaySummarySection(
    summary: DaySummary,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SectionLabel("RESUMO DO DIA")
        Spacer(Modifier.height(12.dp))

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            MetricCard(
                modifier     = Modifier.weight(1f),
                icon         = Icons.Rounded.DirectionsWalk,
                value        = formatSteps(summary.steps),
                label        = "Passos",
                accentColor  = FitColors.Accent,
            )
            MetricCard(
                modifier     = Modifier.weight(1f),
                icon         = Icons.Rounded.LocalFireDepartment,
                value        = "${summary.calories}",
                unit         = "kcal",
                label        = "Calorias ativas",
                accentColor  = FitColors.Orange,
            )
        }

        Spacer(Modifier.height(10.dp))

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            MetricCard(
                modifier    = Modifier.weight(1f),
                icon        = Icons.Rounded.Straighten,
                value       = summary.distanceKm.toString().replace(".", ","),
                unit        = "km",
                label       = "Distância",
                accentColor = FitColors.Blue,
            )
            MetricCard(
                modifier    = Modifier.weight(1f),
                icon        = Icons.Rounded.Schedule,
                value       = formatTime(summary.activeMinutes),
                label       = "Tempo ativo",
                accentColor = FitColors.Purple,
            )
        }
    }
}

// Card individual de métrica — gradient strip + icon box com accentColor (padrão PostCard)
@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    unit: String = "",
    label: String,
    accentColor: Color,
) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .border(1.dp, Color(0xFF2a2a35), RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cs.surface),
    ) {
        // Gradient top strip — mesma técnica do PostCard
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            accentColor.copy(alpha = 0.5f),
                            accentColor.copy(alpha = 0.85f),
                            accentColor.copy(alpha = 0.5f),
                            Color.Transparent,
                        )
                    )
                )
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp)) {
            // Icon box com fundo + borda na accentColor — mesmo padrão do avatar do PostCard
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.12f))
                    .border(1.dp, accentColor.copy(alpha = 0.30f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector        = icon,
                    contentDescription = null,
                    tint               = accentColor,
                    modifier           = Modifier.size(18.dp),
                )
            }

            Spacer(Modifier.height(14.dp))

            // Métrica em fonte grande — legível em movimento (referência: Garmin watch)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    value,
                    color         = FitColors.TextPrimary,
                    fontSize      = 28.sp,
                    fontWeight    = FontWeight.Black,
                    letterSpacing = (-0.5).sp,
                )
                if (unit.isNotEmpty()) {
                    Spacer(Modifier.width(3.dp))
                    Text(
                        unit,
                        color    = FitColors.TextMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }
            }

            Text(
                label,
                color    = FitColors.TextMuted,
                fontSize = 11.sp,
            )
        }
    }
}

// ── Weekly goal ───────────────────────────────────────────────────────────────
// Referência: Whoop weekly strain goal.
// Dias ativos: check accent. Hoje inativo: borda sutil accent.
// Futuro: Surface2 apagado — urgência passiva sem texto extra.

@Composable
private fun WeeklyGoalSection(
    days: List<WeekDay>,
    modifier: Modifier = Modifier,
) {
    val activeCount = days.count { it.isActive }
    val weeklyGoal  = 5

    Column(modifier = modifier) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            SectionLabel("META SEMANAL")
            Text(
                "$activeCount de $weeklyGoal dias",
                color      = FitColors.Accent,
                fontSize   = 13.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(Modifier.height(14.dp))

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            days.forEach { day -> DayPill(day = day) }
        }
    }
}

@Composable
private fun DayPill(day: WeekDay) {
    // Comunicação visual passiva — ativo=conquistado, hoje=urgência, futuro=disponível
    val bgColor = if (day.isActive) FitColors.Accent else FitColors.Surface2

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(bgColor)
                .then(
                    // Borda sutil apenas no dia atual inativo — sinaliza "sua vez"
                    if (day.isToday && !day.isActive)
                        Modifier.background(FitColors.Surface2)
                    else Modifier
                ),
            contentAlignment = Alignment.Center,
        ) {
            when {
                day.isActive -> Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    tint     = FitColors.Bg,
                    modifier = Modifier.size(17.dp),
                )
                day.isToday -> Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(FitColors.Accent),
                )
            }
        }
        Text(
            day.label,
            color      = when {
                day.isActive -> FitColors.TextPrimary
                day.isToday  -> FitColors.Accent
                else         -> FitColors.TextDisabled
            },
            fontSize   = 10.sp,
            fontWeight = if (day.isActive || day.isToday) FontWeight.Bold else FontWeight.Normal,
        )
    }
}

// ── Recent activities ─────────────────────────────────────────────────────────
// Referência: Strava feed compacto — ícone · tipo · duração · distância · data.
// Sem fotos, sem descrições — só dado. Escaneável em 1 segundo por linha.

@Composable
private fun ActivityRow(
    activity: RecentActivity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .border(1.dp, Color(0xFF2a2a35), RoundedCornerShape(25.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cs.surface)
            .clickable { onClick() },
    ) {
        // Gradient top strip — padrão PostCard
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            activity.type.color.copy(alpha = 0.5f),
                            activity.type.color.copy(alpha = 0.85f),
                            activity.type.color.copy(alpha = 0.5f),
                            Color.Transparent,
                        )
                    )
                )
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Ícone do tipo — cor específica por categoria
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(activity.type.color.copy(alpha = 0.12f))
                    .border(1.dp, activity.type.color.copy(alpha = 0.30f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector        = activity.type.icon,
                    contentDescription = null,
                    tint               = activity.type.color,
                    modifier           = Modifier.size(20.dp),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    activity.type.label,
                    color      = FitColors.TextPrimary,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    activity.date,
                    color    = FitColors.TextMuted,
                    fontSize = 12.sp,
                )
            }

            // Dados à direita — hierarquia: duração primária, distância secundária
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${activity.durationMinutes} min",
                    color      = FitColors.TextPrimary,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "${activity.distanceKm.toString().replace(".", ",")} km",
                    color    = FitColors.TextMuted,
                    fontSize = 12.sp,
                )
            }
        }
    }
}

// ── Shared ────────────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier      = modifier,
        color         = FitColors.TextMuted,
        fontSize      = 10.sp,
        fontWeight    = FontWeight.Bold,
        letterSpacing = 1.2.sp,
    )
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun formatSteps(steps: Int): String {
    if (steps < 1000) return steps.toString()
    val whole   = steps / 1000
    val decimal = (steps % 1000) / 100
    return "$whole.${decimal}k"
}

private fun formatTime(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return if (h > 0) "${h}h ${m}m" else "${m}min"
}
