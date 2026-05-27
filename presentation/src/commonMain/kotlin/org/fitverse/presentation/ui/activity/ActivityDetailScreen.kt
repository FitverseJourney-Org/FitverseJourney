package org.fitverse.presentation.ui.activity

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.Terrain
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import org.fitverse.presentation.theme.FitColors

// ── Entry point ───────────────────────────────────────────────────────────────

@Composable
fun ActivityDetailScreen(
    activity: RecentActivity,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = activity.type.color

    Scaffold(
        modifier            = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor      = Color.Transparent,
    ) { _ ->
        LazyColumn(
            modifier       = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 52.dp),
        ) {
            item { DetailHero(activity = activity, onBack = onBack) }

            if (activity.splits.isNotEmpty()) {
                item { Spacer(Modifier.height(24.dp)) }
                item {
                    ChartsSection(
                        activity = activity,
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
                item { Spacer(Modifier.height(24.dp)) }
                item {
                    SplitsSection(
                        splits      = activity.splits,
                        accentColor = accent,
                        modifier    = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
            item {
                DetailStatsGrid(
                    activity = activity,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }
    }
}

// ── Hero ──────────────────────────────────────────────────────────────────────

@Composable
private fun DetailHero(activity: RecentActivity, onBack: () -> Unit) {
    val accent = activity.type.color

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 28.dp),
    ) {
        // Back button
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(FitColors.Surface2)
                .clickable { onBack() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Voltar",
                tint     = FitColors.TextPrimary,
                modifier = Modifier.size(18.dp),
            )
        }

        Spacer(Modifier.height(26.dp))

        // Type icon + title + date
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(accent.copy(alpha = 0.14f))
                    .border(1.dp, accent.copy(alpha = 0.30f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    activity.type.icon,
                    contentDescription = null,
                    tint     = accent,
                    modifier = Modifier.size(24.dp),
                )
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    text          = activity.type.label.uppercase(),
                    color         = FitColors.TextPrimary,
                    fontSize      = 22.sp,
                    fontWeight    = FontWeight.Black,
                    letterSpacing = 0.6.sp,
                )
                Text(
                    text     = activity.date,
                    color    = FitColors.TextMuted,
                    fontSize = 13.sp,
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        // Big 3 metrics row
        val pace = computePace(activity.durationMinutes, activity.distanceKm, activity.type)
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            HeroMetric(
                value      = "${activity.distanceKm.toString().replace(".", ",")} km",
                label      = "DISTÂNCIA",
                valueColor = accent,
                modifier   = Modifier.weight(1f),
            )
            HeroDivider()
            HeroMetric(
                value    = formatDetailTime(activity.durationMinutes),
                label    = "DURAÇÃO",
                modifier = Modifier.weight(1f),
            )
            HeroDivider()
            HeroMetric(
                value    = pace,
                label    = if (activity.type == ActivityType.Bike) "VEL. MÉDIA" else "PACE MÉDIO",
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun HeroMetric(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    valueColor: Color = FitColors.TextPrimary,
) {
    Column(
        modifier            = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text          = value,
            color         = valueColor,
            fontSize      = 20.sp,
            fontWeight    = FontWeight.Black,
            letterSpacing = (-0.5).sp,
            textAlign     = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text          = label,
            color         = FitColors.TextDisabled,
            fontSize      = 9.sp,
            fontWeight    = FontWeight.Bold,
            letterSpacing = 1.sp,
            textAlign     = TextAlign.Center,
        )
    }
}

@Composable
private fun HeroDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(FitColors.Outline)
    )
}

// ── Route Map Card ────────────────────────────────────────────────────────────

@Composable
private fun RouteMapCard(accentColor: Color, modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cs.surface)
            .border(1.dp, FitColors.Outline, RoundedCornerShape(20.dp)),
    ) {
        // Top color strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            accentColor.copy(0.5f),
                            accentColor.copy(0.85f),
                            accentColor.copy(0.5f),
                            Color.Transparent,
                        )
                    )
                )
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(185.dp)
                    .padding(horizontal = 18.dp, vertical = 18.dp),
            ) {
                val w = size.width
                val h = size.height

                // Subtle grid
                for (i in 0..4) drawLine(FitColors.Outline, Offset(0f, h * i / 4), Offset(w, h * i / 4), 0.5f)
                for (i in 0..5) drawLine(FitColors.Outline, Offset(w * i / 5, 0f), Offset(w * i / 5, h), 0.5f)

                // Route path — organic bezier simulating a real run trace
                val path = Path().apply {
                    moveTo(w * 0.06f, h * 0.70f)
                    cubicTo(w * 0.14f, h * 0.18f, w * 0.24f, h * 0.84f, w * 0.34f, h * 0.46f)
                    cubicTo(w * 0.42f, h * 0.18f, w * 0.52f, h * 0.72f, w * 0.60f, h * 0.38f)
                    cubicTo(w * 0.68f, h * 0.10f, w * 0.76f, h * 0.60f, w * 0.82f, h * 0.30f)
                    cubicTo(w * 0.88f, h * 0.08f, w * 0.93f, h * 0.42f, w * 0.95f, h * 0.34f)
                }

                // Outer glow
                drawPath(path, accentColor.copy(0.10f), style = Stroke(18f, cap = StrokeCap.Round, join = StrokeJoin.Round))
                // Mid glow
                drawPath(path, accentColor.copy(0.22f), style = Stroke(6f,  cap = StrokeCap.Round, join = StrokeJoin.Round))
                // Route line
                drawPath(path, accentColor,              style = Stroke(2.4f, cap = StrokeCap.Round, join = StrokeJoin.Round))

                // Start marker (A)
                drawCircle(Color.White.copy(0.90f), 7f, Offset(w * 0.06f, h * 0.70f))
                drawCircle(accentColor,             4f, Offset(w * 0.06f, h * 0.70f))

                // End marker (B)
                drawCircle(Color.White.copy(0.90f),  7f, Offset(w * 0.95f, h * 0.34f))
                drawCircle(FitColors.TextDisabled,   4f, Offset(w * 0.95f, h * 0.34f))
            }

            Text(
                text          = "ROTA ESTIMADA",
                modifier      = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 12.dp),
                color         = FitColors.TextDisabled,
                fontSize      = 9.sp,
                fontWeight    = FontWeight.Bold,
                letterSpacing = 0.8.sp,
            )
        }
    }
}

// ── Splits ────────────────────────────────────────────────────────────────────

@Composable
private fun SplitsSection(
    splits: List<SplitData>,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    val cs = MaterialTheme.colorScheme
    val bestKm = splits.minByOrNull { splitPaceSeconds(it.pace) }?.km

    Column(modifier = modifier) {
        DetailSectionLabel("SPLITS")
        Spacer(Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(cs.surface)
                .border(1.dp, FitColors.Outline, RoundedCornerShape(20.dp)),
        ) {
            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 11.dp),
            ) {
                Text("KM",   color = FitColors.TextDisabled, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp, modifier = Modifier.weight(1.2f))
                Text("PACE", color = FitColors.TextDisabled, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp, modifier = Modifier.weight(1.5f))
                Text("BPM",  color = FitColors.TextDisabled, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            }
            HorizontalDivider(thickness = 0.5.dp, color = FitColors.Outline)

            splits.forEachIndexed { i, split ->
                SplitRow(split = split, isBest = split.km == bestKm, accentColor = accentColor)
                if (i < splits.lastIndex) {
                    HorizontalDivider(
                        modifier  = Modifier.padding(start = 18.dp),
                        thickness = 0.5.dp,
                        color     = FitColors.Outline,
                    )
                }
            }
        }
    }
}

@Composable
private fun SplitRow(split: SplitData, isBest: Boolean, accentColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isBest) accentColor.copy(0.07f) else Color.Transparent)
            .padding(horizontal = 18.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // KM marker
        Text(
            text       = "${split.km} km",
            color      = if (isBest) accentColor else FitColors.TextMuted,
            fontSize   = 12.sp,
            fontWeight = if (isBest) FontWeight.Bold else FontWeight.Normal,
            modifier   = Modifier.weight(1.2f),
        )

        // Pace + "melhor" badge
        Row(
            modifier          = Modifier.weight(1.5f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text       = split.pace,
                color      = FitColors.TextPrimary,
                fontSize   = 14.sp,
                fontWeight = if (isBest) FontWeight.Black else FontWeight.SemiBold,
            )
            if (isBest) {
                Spacer(Modifier.width(6.dp))
                Surface(
                    color = accentColor.copy(0.15f),
                    shape = RoundedCornerShape(5.dp),
                ) {
                    Text(
                        text          = "melhor",
                        modifier      = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                        color         = accentColor,
                        fontSize      = 8.sp,
                        fontWeight    = FontWeight.Black,
                        letterSpacing = 0.2.sp,
                    )
                }
            }
        }

        // BPM
        Row(
            modifier              = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Rounded.Favorite,
                contentDescription = null,
                tint     = FitColors.Red.copy(if (isBest) 0.85f else 0.45f),
                modifier = Modifier.size(11.dp),
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text       = "${split.heartRate}",
                color      = FitColors.TextPrimary,
                fontSize   = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

// ── Stats Grid ────────────────────────────────────────────────────────────────

private data class StatItem(val icon: ImageVector, val value: String, val accent: Color)

@Composable
private fun DetailStatsGrid(activity: RecentActivity, modifier: Modifier = Modifier) {
    val stats: List<StatItem> = when (activity.type) {
        ActivityType.Corrida -> listOf(
            StatItem(Icons.Rounded.LocalFireDepartment, "${activity.calories} kcal",              FitColors.Orange),
            StatItem(Icons.Rounded.Favorite,            "${activity.avgHeartRate} bpm",           FitColors.Red),
            StatItem(Icons.Rounded.Terrain,             "${activity.elevationGainM} m ↑",         FitColors.Green),
            StatItem(Icons.Rounded.Speed,               "${cadenceRunning(activity)} spm",         FitColors.Purple),
        )
        ActivityType.Bike -> listOf(
            StatItem(Icons.Rounded.LocalFireDepartment, "${activity.calories} kcal",              FitColors.Orange),
            StatItem(Icons.Rounded.Favorite,            "${activity.avgHeartRate} bpm",           FitColors.Red),
            StatItem(Icons.Rounded.Terrain,             "${activity.elevationGainM} m ↑",         FitColors.Green),
            StatItem(Icons.Rounded.Speed,               "${maxSpeedBike(activity)} km/h max",     FitColors.Blue),
        )
        ActivityType.Caminhada -> listOf(
            StatItem(Icons.Rounded.LocalFireDepartment, "${activity.calories} kcal",              FitColors.Orange),
            StatItem(Icons.Rounded.Favorite,            "${activity.avgHeartRate} bpm",           FitColors.Red),
            StatItem(Icons.Rounded.DirectionsWalk,      "${stepsWalk(activity)} passos",          FitColors.Accent),
            StatItem(Icons.Rounded.Speed,               "${avgSpeedKmh(activity)} km/h",          FitColors.Purple),
        )
        ActivityType.Outro -> listOf(
            StatItem(Icons.Rounded.LocalFireDepartment, "${activity.calories} kcal",              FitColors.Orange),
            StatItem(Icons.Rounded.Favorite,            "${activity.avgHeartRate} bpm",           FitColors.Red),
            StatItem(Icons.Rounded.Schedule,            formatDetailTime(activity.durationMinutes), FitColors.Purple),
            StatItem(Icons.Rounded.FitnessCenter,       "Treino livre",                           FitColors.Blue),
        )
    }

    Column(modifier = modifier) {
        DetailSectionLabel("ESTATÍSTICAS")
        Spacer(Modifier.height(10.dp))
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            StatCard(stats[0], modifier = Modifier.weight(1f))
            StatCard(stats[1], modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            StatCard(stats[2], modifier = Modifier.weight(1f))
            StatCard(stats[3], modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatCard(stat: StatItem, modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .border(1.dp, FitColors.Outline, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(cs.surface)
            .padding(14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(stat.accent.copy(0.12f))
                .border(1.dp, stat.accent.copy(0.25f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(stat.icon, null, tint = stat.accent, modifier = Modifier.size(16.dp))
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text          = stat.value,
            color         = FitColors.TextPrimary,
            fontSize      = 18.sp,
            fontWeight    = FontWeight.Black,
            letterSpacing = (-0.3).sp,
        )
    }
}

// ── Charts Section ────────────────────────────────────────────────────────────

@Composable
private fun ChartsSection(activity: RecentActivity, modifier: Modifier = Modifier) {
    val accent = activity.type.color
    val paceLabel = if (activity.type == ActivityType.Bike) "VELOCIDADE" else "PACE"
    val paceValues = remember(activity.splits) {
        activity.splits.map { splitPaceSeconds(it.pace).toDouble() }
    }
    val hrValues = remember(activity.splits) {
        activity.splits.map { it.heartRate.toDouble() }
    }
    val kmLabels = remember(activity.splits) {
        activity.splits.map { "${it.km}km" }
    }

    Column(modifier = modifier) {
        DetailSectionLabel("DESEMPENHO")
        Spacer(Modifier.height(10.dp))

        // Pace / Speed chart
        ChartCard(title = paceLabel, accentColor = accent) {
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(top = 4.dp),
                data = remember(paceValues, accent) {
                    listOf(
                        Line(
                            label                  = paceLabel,
                            values                 = paceValues,
                            color                  = SolidColor(accent),
                            firstGradientFillColor = accent.copy(alpha = 0.28f),
                            secondGradientFillColor= Color.Transparent,
                            strokeAnimationSpec    = tween(1600, easing = EaseInOutCubic),
                            gradientAnimationDelay = 600,
                            drawStyle              = DrawStyle.Stroke(width = 2.dp),
                        )
                    )
                },
                gridProperties = GridProperties(
                    xAxisProperties = GridProperties.AxisProperties(
                        color     = SolidColor(FitColors.Outline),
                        thickness = 0.5.dp,
                    ),
                    yAxisProperties = GridProperties.AxisProperties(enabled = false),
                ),
                labelProperties = LabelProperties(
                    enabled   = true,
                    textStyle = TextStyle(color = FitColors.TextDisabled, fontSize = 9.sp),
                    labels    = kmLabels,
                ),
                labelHelperProperties = LabelHelperProperties(enabled = false),
                indicatorProperties   = HorizontalIndicatorProperties(enabled = false),
                animationMode         = AnimationMode.Together(delayBuilder = { it * 300L }),
                curvedEdges           = true,
            )
        }

        Spacer(Modifier.height(10.dp))

        // Heart Rate chart
        ChartCard(title = "FREQ. CARDÍACA", accentColor = FitColors.Red) {
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(top = 4.dp),
                data = remember(hrValues) {
                    listOf(
                        Line(
                            label                  = "BPM",
                            values                 = hrValues,
                            color                  = SolidColor(FitColors.Red),
                            firstGradientFillColor = FitColors.Red.copy(alpha = 0.22f),
                            secondGradientFillColor= Color.Transparent,
                            strokeAnimationSpec    = tween(1600, easing = EaseInOutCubic),
                            gradientAnimationDelay = 700,
                            drawStyle              = DrawStyle.Stroke(width = 2.dp),
                        )
                    )
                },
                gridProperties = GridProperties(
                    xAxisProperties = GridProperties.AxisProperties(
                        color     = SolidColor(FitColors.Outline),
                        thickness = 0.5.dp,
                    ),
                    yAxisProperties = GridProperties.AxisProperties(enabled = false),
                ),
                labelProperties = LabelProperties(
                    enabled   = true,
                    textStyle = TextStyle(color = FitColors.TextDisabled, fontSize = 9.sp),
                    labels    = kmLabels,
                ),
                labelHelperProperties = LabelHelperProperties(enabled = false),
                indicatorProperties   = HorizontalIndicatorProperties(enabled = false),
                animationMode         = AnimationMode.Together(delayBuilder = { it * 300L }),
                curvedEdges           = true,
            )
        }
    }
}

@Composable
private fun ChartCard(
    title: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cs.surface)
            .border(1.dp, FitColors.Outline, RoundedCornerShape(20.dp)),
    ) {
        // Top strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            accentColor.copy(0.5f),
                            accentColor.copy(0.85f),
                            accentColor.copy(0.5f),
                            Color.Transparent,
                        )
                    )
                )
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            Text(
                text          = title,
                color         = FitColors.TextMuted,
                fontSize      = 10.sp,
                fontWeight    = FontWeight.Bold,
                letterSpacing = 1.2.sp,
            )
            content()
        }
    }
}

// ── Shared ────────────────────────────────────────────────────────────────────

@Composable
private fun DetailSectionLabel(text: String) {
    Text(
        text,
        color         = FitColors.TextMuted,
        fontSize      = 10.sp,
        fontWeight    = FontWeight.Bold,
        letterSpacing = 1.2.sp,
    )
}

// ── Helpers ───────────────────────────────────────────────────────────────────

internal fun computePace(durationMinutes: Int, distanceKm: Float, type: ActivityType): String {
    if (distanceKm == 0f) return "--"
    return when (type) {
        ActivityType.Bike -> {
            val speed = distanceKm / (durationMinutes / 60f)
            "${speed.toInt()}.${((speed - speed.toInt()) * 10).toInt()} km/h"
        }
        else -> {
            val paceMin = durationMinutes / distanceKm
            val h = paceMin.toInt()
            val s = ((paceMin - h) * 60).toInt()
            "$h:${if (s < 10) "0$s" else "$s"}/km"
        }
    }
}

internal fun splitPaceSeconds(pace: String): Int {
    val parts = pace.split(":")
    if (parts.size != 2) return Int.MAX_VALUE
    return (parts[0].toIntOrNull() ?: 99) * 60 + (parts[1].toIntOrNull() ?: 99)
}

private fun cadenceRunning(a: RecentActivity) = 168

private fun maxSpeedBike(a: RecentActivity): String {
    val avg = a.distanceKm / (a.durationMinutes / 60f)
    val max = avg * 1.35f
    return "${max.toInt()}.${((max - max.toInt()) * 10).toInt()}"
}

private fun stepsWalk(a: RecentActivity) = (a.distanceKm * 1_300).toInt()

private fun avgSpeedKmh(a: RecentActivity): String {
    val s = a.distanceKm / (a.durationMinutes / 60f)
    return "${s.toInt()}.${((s - s.toInt()) * 10).toInt()}"
}

internal fun formatDetailTime(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return if (h > 0) "${h}h ${m}m" else "${m}min"
}
