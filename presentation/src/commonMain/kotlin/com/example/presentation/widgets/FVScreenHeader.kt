package com.example.presentation.widgets

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.example.expect.format
import com.example.presentation.theme.FV
import kotlin.math.*

@Composable
fun FVScreenHeader(
    title: String,
    sub: String = "",
    onBack: () -> Unit,
    action: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(FV.surface)
            .padding(horizontal = FV.margin, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(36.dp)
                .background(FV.surface2, RoundedCornerShape(10.dp))
        ) {
            Text("←", color = FV.text, fontSize = 18.sp)
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = FV.text,
                letterSpacing = 1.sp
            )
            if (sub.isNotEmpty()) {
                Text(
                    text = sub,
                    fontSize = 11.sp,
                    color = FV.textMuted
                )
            }
        }
        action?.invoke()
    }
    Divider(color = FV.outline, thickness = 0.5.dp)
}


@Composable
fun FVFilterPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(FV.radiusPill))
            .background(if (selected) FV.primary else FV.surface2)
            .border(
                width = if (selected) 0.dp else 1.dp,
                color = if (selected) Color.Transparent else FV.outline,
                shape = RoundedCornerShape(FV.radiusPill)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) FV.bg else FV.textMuted
        )
    }
}

@Composable
fun FVCard(
    modifier: Modifier = Modifier,
    glowColor: Color? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val borderColor = glowColor?.copy(alpha = 0.3f) ?: FV.outline
    val shadow = if (glowColor != null)
        Modifier.shadow(0.dp, shape = RoundedCornerShape(FV.radius))
    else Modifier

    Column(
        modifier = modifier
            .then(shadow)
            .clip(RoundedCornerShape(FV.radius))
            .background(FV.surface)
            .border(1.dp, borderColor, RoundedCornerShape(FV.radius))
            .padding(16.dp),
        content = content
    )
}

@Composable
fun FVSectionLabel(title: String, action: String = "", onAction: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = FV.margin, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = FV.textMuted,
            letterSpacing = 1.sp,
            modifier = Modifier.weight(1f)
        )
        if (action.isNotEmpty()) {
            Text(
                text = action,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = FV.primary,
                modifier = Modifier.clickable(onClick = onAction)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 16 · PROGRESSION SCREEN
// ─────────────────────────────────────────────────────────────

private val PROG_EXERCISES = listOf(
    "Supino Reto", "Agachamento", "Levantamento Terra",
    "Rosca Direta", "Desenvolvimento", "Remada Curvada"
)

private data class LoadPoint(val week: Int, val weight: Float, val reps: Int)

private fun generateLoadData(seed: Int): List<LoadPoint> {
    val base = 40f + seed * 8f
    return (0..11).map { w ->
        LoadPoint(
            week   = w,
            weight = base + w * (3f + sin(w * 0.7f + seed)) + (seed % 3) * 2f,
            reps   = 12 - (w / 4)
        )
    }
}

private data class PRRecord(
    val exercise: String,
    val weight: Float,
    val isNewPR: Boolean,
    val delta: Float
)

private val PR_RECORDS = listOf(
    PRRecord("Supino Reto",         80f,  true,  +5f),
    PRRecord("Agachamento",        100f,  false, +10f),
    PRRecord("Levantamento Terra", 120f,  true,  +7.5f),
    PRRecord("Rosca Direta",        42f,  false, +2.5f),
    PRRecord("Desenvolvimento",     60f,  false, +5f),
    PRRecord("Remada Curvada",      75f,  true,  +5f)
)

@Composable
fun ProgressionScreen(onBack: () -> Unit) {
    var selectedExercise by remember { mutableStateOf(PROG_EXERCISES[0]) }
    var period           by remember { mutableStateOf("1M") }
    var comparing        by remember { mutableStateOf(false) }

    val data     = remember(selectedExercise) { generateLoadData(PROG_EXERCISES.indexOf(selectedExercise)) }
    val prevData = remember(selectedExercise) { generateLoadData(PROG_EXERCISES.indexOf(selectedExercise) + 5) }

    val current   = data.last().weight.roundToInt()
    val first     = data.first().weight.roundToInt()
    val evolution = current - first
    val pr        = data.maxOf { it.weight }.roundToInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FV.bg)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header
        FVScreenHeader(
            title  = "PROGRESSÃO",
            sub    = "Sua evolução em números",
            onBack = onBack,
            action = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(FV.surface2)
                        .border(1.dp, FV.outline, RoundedCornerShape(10.dp))
                        .clickable { /* share */ }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("↑", color = FV.text, fontSize = 16.sp)
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        // ── Exercise Selector
        Column(modifier = Modifier.padding(horizontal = FV.margin)) {
            FVCard {
                Text(
                    text = "EXERCÍCIO",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = FV.textMuted,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(FV.surface2)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedExercise,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = FV.text
                    )
                    Text("▾", color = FV.primary, fontSize = 16.sp)
                }
                Spacer(Modifier.height(12.dp))
                // Period pills
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("1S", "1M", "3M", "6M", "1A", "TOTAL").forEach { p ->
                        FVFilterPill(label = p, selected = p == period) { period = p }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Line Chart — Evolução de Carga
        FVSectionLabel("Evolução de Carga")
        Column(modifier = Modifier.padding(horizontal = FV.margin)) {
            FVCard(glowColor = FV.primary) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedExercise.uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = FV.text,
                        letterSpacing = 0.5.sp
                    )
                    // Compare toggle
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(FV.radiusPill))
                            .background(if (comparing) FV.primary.copy(0.15f) else FV.surface2)
                            .border(
                                1.dp,
                                if (comparing) FV.primary.copy(0.4f) else FV.outline,
                                RoundedCornerShape(FV.radiusPill)
                            )
                            .clickable { comparing = !comparing }
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("⇄", color = if (comparing) FV.primary else FV.textMuted, fontSize = 11.sp)
                        Text(
                            "Comparar",
                            fontSize = 11.sp,
                            color = if (comparing) FV.primary else FV.textMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(Modifier.height(14.dp))

                // SVG-style line chart via Canvas
                LoadLineChart(
                    data     = data,
                    prevData = if (comparing) prevData else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                Spacer(Modifier.height(14.dp))
                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatColumn(label = "ATUAL",    value = "${current}kg",    color = FV.text)
                    StatDivider()
                    StatColumn(label = "EVOLUÇÃO", value = "+${evolution}kg", color = FV.secondary)
                    StatDivider()
                    StatColumn(label = "RECORDE",  value = "${pr}kg",         color = FV.xp)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Bar Chart — Volume Semanal
        FVSectionLabel("Volume Semanal", action = "+12% vs semana ant.")
        Column(modifier = Modifier.padding(horizontal = FV.margin)) {
            FVCard {
                VolumeBarChart(
                    values   = listOf(2800f, 3500f, 4100f, 4800f, 5200f, 6100f),
                    labels   = listOf("S1","S2","S3","S4","S5","S6"),
                    modifier = Modifier.fillMaxWidth().height(110.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Heatmap — Frequência de Treino
        FVSectionLabel("Frequência de Treino")
        Column(modifier = Modifier.padding(horizontal = FV.margin)) {
            FVCard {
                TrainingHeatmap(modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                // Legend
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Menos", fontSize = 9.sp, color = FV.textMuted)
                    listOf(0.08f, 0.2f, 0.5f, 1.0f).forEach { alpha ->
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(FV.primary.copy(alpha = alpha), RoundedCornerShape(2.dp))
                        )
                    }
                    Text("Mais", fontSize = 9.sp, color = FV.textMuted)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── PR Board
        FVSectionLabel("Recordes Pessoais (PR)")
        Column(modifier = Modifier.padding(horizontal = FV.margin)) {
            FVCard {
                PR_RECORDS.forEachIndexed { i, pr ->
                    PRRow(record = pr)
                    if (i < PR_RECORDS.lastIndex) {
                        Divider(
                            color     = FV.outline,
                            thickness = 0.5.dp,
                            modifier  = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun LoadLineChart(
    data: List<LoadPoint>,
    prevData: List<LoadPoint>?,
    modifier: Modifier = Modifier
) {
    val animProgress by animateFloatAsState(
        targetValue    = 1f,
        animationSpec  = tween(900, easing = FastOutSlowInEasing),
        label          = "chartAnim"
    )
    Canvas(modifier = modifier) {
        val w     = size.width
        val h     = size.height
        val padH  = 20f
        val padV  = 10f
        val allW  = data.map { it.weight }
        val minW  = (allW.min() - 5f).coerceAtLeast(0f)
        val maxW  = allW.max() + 5f
        val range = maxW - minW

        fun xOf(i: Int) = padH + (i.toFloat() / (data.size - 1)) * (w - padH * 2)
        fun yOf(v: Float) = h - padV - ((v - minW) / range) * (h - padV * 2)

        // Grid lines
        listOf(0.25f, 0.5f, 0.75f).forEach { t ->
            val y = padV + t * (h - padV * 2)
            drawLine(
                color       = Color.White.copy(alpha = 0.04f),
                start       = Offset(padH, y),
                end         = Offset(w - padH, y),
                strokeWidth = 0.8f,
                pathEffect  = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
            )
        }

        // Previous period (dashed, violet)
        if (prevData != null) {
            val path = Path()
            prevData.forEachIndexed { i, pt ->
                val x = xOf(i)
                val y = yOf(pt.weight)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(
                path        = path,
                color       = FV.xp.copy(alpha = 0.5f),
                style       = Stroke(
                    width      = 1.5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f))
                )
            )
        }

        // Fill area
        val fillPath = Path()
        data.forEachIndexed { i, pt ->
            val x = xOf(i)
            val y = yOf(pt.weight)
            if (i == 0) fillPath.moveTo(x, y) else fillPath.lineTo(x, y)
        }
        fillPath.lineTo(xOf(data.lastIndex), h)
        fillPath.lineTo(xOf(0), h)
        fillPath.close()
        drawPath(
            path  = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(FV.primary.copy(alpha = 0.18f), Color.Transparent)
            )
        )

        // Main line (animated draw)
        val clippedPath = Path()
        val clipW = w * animProgress
        data.forEachIndexed { i, pt ->
            val x = xOf(i).coerceAtMost(clipW)
            val y = yOf(pt.weight)
            if (i == 0) clippedPath.moveTo(x, y) else clippedPath.lineTo(x, y)
        }
        drawPath(
            path  = clippedPath,
            color = FV.primary,
            style = Stroke(width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // PR point (max)
        val prIdx = data.indexOfFirst { it.weight == data.maxOf { d -> d.weight } }
        if (prIdx >= 0) {
            val px = xOf(prIdx)
            val py = yOf(data[prIdx].weight)
            drawCircle(color = FV.primary.copy(alpha = 0.15f), radius = 12f, center = Offset(px, py))
            drawCircle(color = FV.primary, radius = 5f, center = Offset(px, py))
        }

        // Last point dot
        val lastX = xOf(data.lastIndex)
        val lastY = yOf(data.last().weight)
        drawCircle(color = FV.primary.copy(alpha = 0.2f), radius = 10f, center = Offset(lastX, lastY))
        drawCircle(color = FV.primary, radius = 4f, center = Offset(lastX, lastY))
    }
}

@Composable
private fun VolumeBarChart(
    values: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    val maxVal = values.max()
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment     = Alignment.Bottom
        ) {
            values.forEachIndexed { i, v ->
                val fraction = v / maxVal
                val isLast   = i == values.lastIndex
                val animH by animateFloatAsState(
                    targetValue   = fraction,
                    animationSpec = tween(600 + i * 80, easing = FastOutSlowInEasing),
                    label         = "bar$i"
                )
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .fillMaxHeight(animH)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        .background(
                            if (isLast) FV.primary
                            else FV.primary.copy(alpha = 0.15f + fraction * 0.45f)
                        )
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            labels.forEachIndexed { i, l ->
                Text(
                    text     = l,
                    fontSize = 9.sp,
                    color    = if (i == labels.lastIndex) FV.primary else FV.textMuted,
                    fontWeight = if (i == labels.lastIndex) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun TrainingHeatmap(modifier: Modifier = Modifier) {
    val days = 7 * 8 // 8 weeks
    val intensities = remember {
        List(days) { i ->
            when {
                i % 7 == 6 -> 0f                           // Sunday rest
                i % 3 == 0 -> (0.3f + (i % 5) * 0.15f).coerceAtMost(1f)
                i % 5 == 0 -> 0f
                else       -> (0.1f + (i % 7) * 0.12f).coerceAtMost(1f)
            }
        }
    }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Seg","Ter","Qua","Qui","Sex","Sáb","Dom").forEach { d ->
                Text(d, fontSize = 8.sp, color = FV.textMuted, modifier = Modifier.width(32.dp))
            }
        }
        Spacer(Modifier.height(4.dp))
        (0 until 8).forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                (0 until 7).forEach { day ->
                    val intensity = intensities[week * 7 + day]
                    Box(
                        modifier = Modifier
                            .size(30.dp, 10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (intensity == 0f) Color(0xFF1A1A22)
                                else FV.primary.copy(alpha = intensity)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun PRRow(record: PRRecord) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text     = record.exercise,
            fontSize = 13.sp,
            color    = FV.text,
            modifier = Modifier.weight(1f)
        )
        Text(
            text       = "${record.weight.toInt()} kg",
            fontSize   = 14.sp,
            fontWeight = FontWeight.Bold,
            color      = FV.primary
        )
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(FV.radiusPill))
                .background(
                    if (record.isNewPR) FV.danger.copy(0.12f) else FV.secondary.copy(0.1f)
                )
                .border(
                    1.dp,
                    if (record.isNewPR) FV.danger.copy(0.3f) else FV.secondary.copy(0.3f),
                    RoundedCornerShape(FV.radiusPill)
                )
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text     = if (record.isNewPR) "PR" else "+${record.delta.toInt()}kg",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color    = if (record.isNewPR) FV.danger else FV.secondary
            )
        }
    }
}

@Composable
private fun StatColumn(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = color)
        Spacer(Modifier.height(2.dp))
        Text(text = label, fontSize = 9.sp, color = FV.textMuted, letterSpacing = 0.8.sp)
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .width(0.5.dp)
            .height(36.dp)
            .background(FV.outline)
    )
}

// ─────────────────────────────────────────────────────────────
// 17 · LEADERBOARD SCREEN
// ─────────────────────────────────────────────────────────────

private data class LeaderboardEntry(
    val rank:      Int,
    val name:      String,
    val level:     Int,
    val className: String,
    val score:     Int,
    val delta:     Int,     // +/- positions this week
    val isMe:      Boolean  = false,
    val isOnline:  Boolean  = false,
    val classColor: Color   = FV.primary
)

private val LEADERBOARD_DATA = listOf(
    LeaderboardEntry(1,  "Lucas M.",   18, "Titã",    12_580, +2,  classColor = Color(0xFFFF4D1C)),
    LeaderboardEntry(2,  "Ana S.",     14, "Sábio",    8_240, +1,  classColor = Color(0xFF7C6FFF)),
    LeaderboardEntry(3,  "Carla R.",   16, "Nômade",   7_100, -1,  classColor = Color(0xFF00C97A)),
    LeaderboardEntry(4,  "Pedro A.",   18, "Titã",     6_890, +3,  classColor = Color(0xFFFF4D1C)),
    LeaderboardEntry(5,  "Você",       12, "Titã",     6_240,  0,  isMe = true, classColor = Color(0xFFFF4D1C)),
    LeaderboardEntry(6,  "Julia F.",   11, "Nômade",   5_980, -2,  classColor = Color(0xFF00C97A)),
    LeaderboardEntry(7,  "Marco V.",   10, "Sábio",    5_610, +1,  classColor = Color(0xFF7C6FFF)),
    LeaderboardEntry(8,  "Sofia L.",   9,  "Nômade",   4_990, -1,  classColor = Color(0xFF00C97A)),
    LeaderboardEntry(9,  "Bruno T.",   8,  "Titã",     4_320, +4,  classColor = Color(0xFFFF4D1C)),
    LeaderboardEntry(10, "Lara C.",    7,  "Sábio",    3_870, -2,  classColor = Color(0xFF7C6FFF))
)

private val SEASON_DAYS_LEFT = 6

@Composable
fun LeaderboardScreen(onBack: () -> Unit) {
    var scope  by remember { mutableStateOf("GLOBAL") }
    var metric by remember { mutableStateOf("XP TOTAL") }
    var period by remember { mutableStateOf("SEMANA") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FV.bg)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header
        FVScreenHeader(
            title  = "RANKING",
            sub    = "Semana de 29 Abr – 5 Mai · atualizado há 2 min",
            onBack = onBack
        )

        Spacer(Modifier.height(12.dp))

        // ── Season countdown
        Box(
            modifier = Modifier
                .padding(horizontal = FV.margin)
                .fillMaxWidth()
                .clip(RoundedCornerShape(FV.radius))
                .background(FV.danger.copy(0.07f))
                .border(1.dp, FV.danger.copy(0.2f), RoundedCornerShape(FV.radius))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⏳", fontSize = 18.sp)
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = "Temporada encerra em $SEASON_DAYS_LEFT dias",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = FV.text
                    )
                    Text(
                        text     = "Mantenha sua posição para ganhar recompensas",
                        fontSize = 11.sp,
                        color    = FV.textMuted
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(FV.radiusPill))
                        .background(FV.danger.copy(0.15f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text("Ver prêmios", fontSize = 10.sp, color = FV.danger, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // ── Scope pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = FV.margin),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("GLOBAL", "AMIGOS", "REGIONAL", "MINHA CLASSE").forEach { s ->
                FVFilterPill(label = s, selected = s == scope) { scope = s }
            }
        }
        Spacer(Modifier.height(8.dp))

        // ── Metric pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = FV.margin),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("XP TOTAL", "STREAK", "VOLUME", "MISSÕES").forEach { m ->
                FVFilterPill(label = m, selected = m == metric) { metric = m }
            }
        }
        Spacer(Modifier.height(8.dp))

        // ── Period pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = FV.margin),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("SEMANA", "MÊS", "ALL TIME").forEach { p ->
                FVFilterPill(label = p, selected = p == period) { period = p }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Podium top 3
        Podium(
            first  = LEADERBOARD_DATA[0],
            second = LEADERBOARD_DATA[1],
            third  = LEADERBOARD_DATA[2]
        )

        Spacer(Modifier.height(16.dp))
        Divider(color = FV.outline, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = FV.margin))
        Spacer(Modifier.height(8.dp))

        // ── Rank list 4+
        FVSectionLabel("Classificação Geral")
        LEADERBOARD_DATA.drop(3).forEach { entry ->
            RankRow(entry = entry)
        }

        // ── My position sticky card
        Spacer(Modifier.height(12.dp))
        val me = LEADERBOARD_DATA.first { it.isMe }
        MeCard(entry = me)
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun Podium(first: LeaderboardEntry, second: LeaderboardEntry, third: LeaderboardEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = FV.margin),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment     = Alignment.Bottom
    ) {
        // 2nd
        PodiumEntry(entry = second, placeColor = FV.silver, platformH = 48.dp)
        // 1st (taller)
        PodiumEntry(entry = first, placeColor = FV.gold, platformH = 64.dp, isCrown = true)
        // 3rd
        PodiumEntry(entry = third, placeColor = FV.bronze, platformH = 32.dp)
    }
}

@Composable
private fun PodiumEntry(
    entry: LeaderboardEntry,
    placeColor: Color,
    platformH: Dp,
    isCrown: Boolean = false
) {
    val avatarSize = if (isCrown) 56.dp else 44.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier            = Modifier.width(100.dp)
    ) {
        if (isCrown) {
            val float by rememberInfiniteTransition("crown").animateFloat(
                initialValue   = 0f,
                targetValue    = -6f,
                animationSpec  = infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
                label          = "float"
            )
            Text("👑", fontSize = 20.sp, modifier = Modifier.offset(y = float.dp))
        } else {
            Spacer(Modifier.height(24.dp))
        }
        Spacer(Modifier.height(4.dp))
        // Avatar
        Box(
            modifier = Modifier
                .size(avatarSize)
                .clip(CircleShape)
                .background(entry.classColor.copy(0.15f))
                .border(2.dp, placeColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(entry.name.first().toString(), fontSize = if (isCrown) 22.sp else 17.sp, color = FV.text)
        }
        Spacer(Modifier.height(6.dp))
        Text(entry.name, fontSize = if (isCrown) 12.sp else 10.sp, color = FV.text, fontWeight = FontWeight.SemiBold, maxLines = 1)
        Text(
            text = "%,d".format(entry.score),
            fontSize = 10.sp,
            color = placeColor,
            fontFamily = FontFamily.Monospace
        )
        Spacer(Modifier.height(6.dp))
        // Platform
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(platformH)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(placeColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = entry.rank.toString(),
                fontSize   = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = FV.bg
            )
        }
    }
}

@Composable
private fun RankRow(entry: LeaderboardEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = FV.margin, vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (entry.isMe) FV.primary.copy(0.05f) else FV.surface)
            .border(
                1.dp,
                if (entry.isMe) FV.primary.copy(0.25f) else FV.outline,
                RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text       = "#${entry.rank}",
            fontSize   = 11.sp,
            fontWeight = FontWeight.Bold,
            color      = if (entry.rank <= 5) FV.text else FV.textMuted,
            fontFamily = FontFamily.Monospace,
            modifier   = Modifier.width(28.dp)
        )
        Spacer(Modifier.width(8.dp))
        // Avatar
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(entry.classColor.copy(0.12f))
                .border(
                    if (entry.isMe) 2.dp else 1.dp,
                    if (entry.isMe) FV.primary.copy(0.5f) else entry.classColor.copy(0.3f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(entry.name.first().toString(), fontSize = 14.sp, color = FV.text)
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = entry.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (entry.isMe) FV.primary else FV.text
                )
                if (entry.isMe) {
                    Spacer(Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(FV.radiusPill))
                            .background(FV.primary.copy(0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("VOCÊ", fontSize = 8.sp, color = FV.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Text(
                text = "Nível ${entry.level} · ${entry.className}",
                fontSize = 10.sp,
                color = FV.textMuted
            )
        }
        // Score
        Text(
            text = "%,d".format(entry.score),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (entry.isMe) FV.primary else FV.text,
            fontFamily = FontFamily.Monospace
        )
        Spacer(Modifier.width(8.dp))
        // Delta
        val deltaColor = when {
            entry.delta > 0 -> FV.secondary
            entry.delta < 0 -> FV.danger
            else -> FV.textMuted
        }
        Text(
            text = when {
                entry.delta > 0 -> "▲${entry.delta}"
                entry.delta < 0 -> "▼${abs(entry.delta)}"
                else -> "—"
            },
            fontSize = 10.sp,
            color = deltaColor,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.width(28.dp),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun MeCard(entry: LeaderboardEntry) {
    Column(modifier = Modifier.padding(horizontal = FV.margin)) {
        FVCard(glowColor = FV.primary) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Sua posição atual",
                        fontSize = 10.sp,
                        color = FV.textMuted,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        "#${entry.rank} no ranking global",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = FV.primary
                    )
                }
                Text(
                    "%,d XP".format(entry.score),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = FV.text,
                    fontFamily = FontFamily.Monospace
                )
            }
            Spacer(Modifier.height(10.dp))
            // Progress to next position
            val nextEntry = LEADERBOARD_DATA.getOrNull(entry.rank - 2)
            if (nextEntry != null) {
                val gap = nextEntry.score - entry.score
                Text(
                    text = "Faltam ${"%,d".format(gap)} XP para a posição #${nextEntry.rank}",
                    fontSize = 11.sp,
                    color = FV.textMuted
                )
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = 1f - (gap.toFloat() / nextEntry.score).coerceIn(0f, 1f),
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(FV.radiusPill)),
                    color = FV.primary,
                    trackColor = FV.surface2
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 18 · FRIENDS SCREEN
// ─────────────────────────────────────────────────────────────

private enum class FriendStatus { ONLINE, OFFLINE }
private enum class FriendActivity { TRAINING, NUTRITION, MISSION, IDLE }

private data class Friend(
    val name:      String,
    val id:        String,
    val level:     Int,
    val className: String,
    val classColor: Color,
    val status:    FriendStatus,
    val activity:  FriendActivity  = FriendActivity.IDLE,
    val streak:    Int             = 0,
    val lastSeen:  String          = ""
)

private data class FriendRequest(
    val name:      String,
    val id:        String,
    val level:     Int,
    val className: String,
    val classColor: Color
)

private val FRIENDS = listOf(
    Friend("Lucas M.", "@lucas.m",    18, "Titã",   Color(0xFFFF4D1C), FriendStatus.ONLINE,  FriendActivity.TRAINING,  streak = 14),
    Friend("Ana S.",   "@ana.sage",   14, "Sábio",  Color(0xFF7C6FFF), FriendStatus.ONLINE,  FriendActivity.NUTRITION, streak = 7),
    Friend("Carla R.", "@carla.r",    16, "Nômade", Color(0xFF00C97A), FriendStatus.ONLINE,  FriendActivity.MISSION,   streak = 21),
    Friend("Pedro A.", "@pedro.fit",  9,  "Titã",   Color(0xFFFF4D1C), FriendStatus.OFFLINE, lastSeen = "há 2h"),
    Friend("Julia F.", "@julia.f",    11, "Nômade", Color(0xFF00C97A), FriendStatus.OFFLINE, lastSeen = "há 5h"),
    Friend("Marco V.", "@marco.v",    10, "Sábio",  Color(0xFF7C6FFF), FriendStatus.OFFLINE, lastSeen = "há 1d")
)

private val FRIEND_REQUESTS = listOf(
    FriendRequest("Sofia L.", "@sofia.l",   7, "Nômade", Color(0xFF00C97A)),
    FriendRequest("Bruno T.", "@bruno.t",   8, "Titã",   Color(0xFFFF4D1C))
)

private enum class FriendsTab { FRIENDS, REQUESTS, SUGGESTED, SEARCH }

@Composable
fun FriendsScreen(onBack: () -> Unit) {
    var activeTab by remember { mutableStateOf(FriendsTab.FRIENDS) }
    var search    by remember { mutableStateOf("") }
    var dismissed by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FV.bg)
    ) {
        // ── Header
        FVScreenHeader(
            title  = "AMIGOS",
            sub    = "${FRIENDS.size} amigos",
            onBack = onBack,
            action = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(FV.radiusBtn))
                        .background(FV.primary.copy(0.1f))
                        .border(1.dp, FV.primary.copy(0.3f), RoundedCornerShape(FV.radiusBtn))
                        .clickable { }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Text("+ Adicionar", fontSize = 12.sp, color = FV.primary, fontWeight = FontWeight.Bold)
                }
            }
        )

        // ── Search bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = FV.margin, vertical = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(FV.surface2)
                .border(1.dp, FV.outline, RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🔍", fontSize = 14.sp, color = FV.textMuted)
            Spacer(Modifier.width(8.dp))
            BasicTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = TextStyle(fontSize = 13.sp, color = FV.text),
                decorationBox = { inner ->
                    if (search.isEmpty()) Text("Buscar por nome ou @id...", fontSize = 13.sp, color = FV.textMuted)
                    inner()
                }
            )
        }

        // ── Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = FV.margin),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FriendsTab.values().forEach { tab ->
                val label = when (tab) {
                    FriendsTab.FRIENDS   -> "Amigos"
                    FriendsTab.REQUESTS  -> "Pedidos (${FRIEND_REQUESTS.size - dismissed.size})"
                    FriendsTab.SUGGESTED -> "Sugeridos"
                    FriendsTab.SEARCH    -> "Buscar"
                }
                FVFilterPill(label = label, selected = tab == activeTab) { activeTab = tab }
            }
        }

        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            when (activeTab) {
                FriendsTab.FRIENDS   -> FriendsListTab(friends = FRIENDS)
                FriendsTab.REQUESTS  -> RequestsTab(
                    requests  = FRIEND_REQUESTS.filter { it.id !in dismissed },
                    onAccept  = { id -> dismissed = dismissed + id },
                    onDecline = { id -> dismissed = dismissed + id }
                )
                FriendsTab.SUGGESTED -> SuggestedTab()
                FriendsTab.SEARCH    -> AddFriendTab(query = search)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FriendsListTab(friends: List<Friend>) {
    val online  = friends.filter { it.status == FriendStatus.ONLINE }
    val offline = friends.filter { it.status == FriendStatus.OFFLINE }

    if (online.isNotEmpty()) {
        FVSectionLabel(title = "Online Agora", action = "${online.size}")
        online.forEach { f -> FriendRow(friend = f) }
        Spacer(Modifier.height(8.dp))
    }
    if (offline.isNotEmpty()) {
        FVSectionLabel(title = "Offline")
        offline.forEach { f -> FriendRow(friend = f) }
    }
}

@Composable
private fun FriendRow(friend: Friend) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = FV.margin, vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(FV.surface)
            .border(1.dp, FV.outline, RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar + presence dot
        Box(modifier = Modifier.size(42.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.TopStart)
                    .clip(CircleShape)
                    .background(friend.classColor.copy(0.12f))
                    .border(
                        if (friend.status == FriendStatus.ONLINE) 2.dp else 1.dp,
                        if (friend.status == FriendStatus.ONLINE) friend.classColor.copy(0.5f)
                        else friend.classColor.copy(0.2f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(friend.name.first().toString(), fontSize = 16.sp, color = FV.text)
            }
            // Presence dot
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(FV.bg)
                    .padding(1.5.dp)
                    .clip(CircleShape)
                    .background(
                        if (friend.status == FriendStatus.ONLINE) FV.secondary
                        else Color(0xFF4A4A52)
                    )
            )
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                friend.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = FV.text
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(friend.id, fontSize = 10.sp, color = FV.textMuted)
                if (friend.streak > 0) {
                    Spacer(Modifier.width(6.dp))
                    Text("🔥${friend.streak}", fontSize = 10.sp, color = FV.danger)
                }
            }
            if (friend.status == FriendStatus.ONLINE) {
                val actLabel = when (friend.activity) {
                    FriendActivity.TRAINING  -> "Treinando agora"
                    FriendActivity.NUTRITION -> "Na nutrição"
                    FriendActivity.MISSION   -> "Missão ativa"
                    else                     -> "Online"
                }
                val actColor = when (friend.activity) {
                    FriendActivity.TRAINING  -> FV.secondary
                    FriendActivity.NUTRITION -> Color(0xFFF5C518)
                    FriendActivity.MISSION   -> FV.primary
                    else                     -> FV.textMuted
                }
                Text(actLabel, fontSize = 10.sp, color = actColor)
            } else {
                Text("Visto ${friend.lastSeen}", fontSize = 10.sp, color = FV.textMuted)
            }
        }
        // Level badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(FV.radiusPill))
                .background(friend.classColor.copy(0.1f))
                .border(1.dp, friend.classColor.copy(0.25f), RoundedCornerShape(FV.radiusPill))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                "Lv ${friend.level}",
                fontSize = 10.sp,
                color = friend.classColor,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.width(6.dp))
        // Actions button
        Text("⋯", color = FV.textMuted, fontSize = 18.sp)
    }
}

@Composable
private fun RequestsTab(
    requests: List<FriendRequest>,
    onAccept: (String) -> Unit,
    onDecline: (String) -> Unit
) {
    FVSectionLabel("Solicitações Pendentes", action = "${requests.size}")
    if (requests.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Nenhuma solicitação pendente", color = FV.textMuted, fontSize = 13.sp)
        }
        return
    }
    requests.forEach { req ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = FV.margin, vertical = 4.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(FV.surface)
                .border(1.dp, FV.danger.copy(0.15f), RoundedCornerShape(10.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(req.classColor.copy(0.1f))
                    .border(1.dp, req.classColor.copy(0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(req.name.first().toString(), fontSize = 15.sp, color = FV.text)
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(req.name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = FV.text)
                Text(
                    text = "Nível ${req.level} · ${req.className}",
                    fontSize = 10.sp,
                    color = FV.textMuted
                )
                Text(
                    "Quer ser seu amigo no FitVerse",
                    fontSize = 10.sp,
                    color = FV.textMuted,
                    fontStyle = FontStyle.Italic
                )
            }
            // Accept
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(FV.primary.copy(0.1f))
                    .border(1.dp, FV.primary.copy(0.3f), RoundedCornerShape(8.dp))
                    .clickable { onAccept(req.id) },
                contentAlignment = Alignment.Center
            ) {
                Text("✓", fontSize = 14.sp, color = FV.primary, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(6.dp))
            // Decline
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(FV.danger.copy(0.1f))
                    .border(1.dp, FV.danger.copy(0.25f), RoundedCornerShape(8.dp))
                    .clickable { onDecline(req.id) },
                contentAlignment = Alignment.Center
            ) {
                Text("✕", fontSize = 14.sp, color = FV.danger, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SuggestedTab() {
    FVSectionLabel("Sugestões Para Você")
    val suggested = listOf(
        Triple("Lara C.",  "Nômade", Color(0xFF00C97A)),
        Triple("Felipe M.", "Titã",  Color(0xFFFF4D1C)),
        Triple("Bia S.",   "Sábio",  Color(0xFF7C6FFF)),
        Triple("Thiago R.", "Nômade", Color(0xFF00C97A))
    )
    LazyVerticalGrid(
        columns   = GridCells.Fixed(2),
        modifier  = Modifier
            .fillMaxWidth()
            .height(340.dp)
            .padding(horizontal = FV.margin),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement   = Arrangement.spacedBy(10.dp)
    ) {
        items(suggested) { (name, cls, color) ->
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(FV.radius))
                    .background(FV.surface)
                    .border(1.dp, FV.outline, RoundedCornerShape(FV.radius))
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(color.copy(0.1f))
                        .border(1.dp, color.copy(0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(name.first().toString(), fontSize = 20.sp, color = FV.text)
                }
                Spacer(Modifier.height(8.dp))
                Text(name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = FV.text)
                Text("2 amigos em comum", fontSize = 10.sp, color = FV.textMuted)
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(FV.radiusBtn))
                        .background(FV.primary.copy(0.1f))
                        .border(1.dp, FV.primary.copy(0.3f), RoundedCornerShape(FV.radiusBtn))
                        .clickable { }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+ Adicionar", fontSize = 11.sp, color = FV.primary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AddFriendTab(query: String) {
    FVSectionLabel("Buscar por @ID")
    Column(modifier = Modifier.padding(horizontal = FV.margin)) {
        FVCard {
            Text(
                "Digite o @id único do usuário para enviar uma solicitação de amizade",
                fontSize = 12.sp,
                color = FV.textMuted,
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(12.dp))
            if (query.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(FV.surface2)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(FV.primary.copy(0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("?", fontSize = 16.sp, color = FV.primary)
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(query, fontSize = 13.sp, color = FV.text)
                        Text("Procurando usuário...", fontSize = 10.sp, color = FV.textMuted)
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
            // QR option
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(FV.surface2)
                    .border(1.dp, FV.outline, RoundedCornerShape(10.dp))
                    .clickable { }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⬛", fontSize = 20.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Escanear QR Code", fontSize = 13.sp, color = FV.text, fontWeight = FontWeight.Medium)
                    Text("Aponte para o QR de um amigo", fontSize = 11.sp, color = FV.textMuted)
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(FV.surface2)
                    .border(1.dp, FV.outline, RoundedCornerShape(10.dp))
                    .clickable { }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🔗", fontSize = 20.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Compartilhar meu link", fontSize = 13.sp, color = FV.text, fontWeight = FontWeight.Medium)
                    Text("fitverse.app/@seuid · toque para copiar", fontSize = 11.sp, color = FV.textMuted)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// 19 · ACHIEVEMENTS SCREEN
// ─────────────────────────────────────────────────────────────

enum class AchievementRarity { COMMON, RARE, EPIC, LEGENDARY }
enum class AchievementStatus { UNLOCKED, IN_PROGRESS, LOCKED }
enum class AchievementCategory { TREINO, NUTRICAO, STREAK, SOCIAL, ESPECIAIS }

data class Achievement(
    val id:        String,
    val icon:      String,
    val name:      String,
    val desc:      String,
    val xp:        Int,
    val rarity:    AchievementRarity,
    val status:    AchievementStatus,
    val cat:       AchievementCategory,
    val progress:  Float = 0f,           // 0..1 for IN_PROGRESS
    val condition: String = "",
    val date:      String = ""
)

private fun rarityColor(r: AchievementRarity) = when (r) {
    AchievementRarity.COMMON    -> FV.primary
    AchievementRarity.RARE      -> FV.xp
    AchievementRarity.EPIC      -> Color(0xFFF5C518)
    AchievementRarity.LEGENDARY -> FV.danger
}

private fun rarityLabel(r: AchievementRarity) = when (r) {
    AchievementRarity.COMMON    -> "COMUM"
    AchievementRarity.RARE      -> "RARO"
    AchievementRarity.EPIC      -> "ÉPICO"
    AchievementRarity.LEGENDARY -> "LENDÁRIO"
}

private val ACHIEVEMENTS = listOf(
    Achievement("a01", "🏆", "Primeira Vitória",  "Complete seu 1º treino",                100, AchievementRarity.COMMON,    AchievementStatus.UNLOCKED,     AchievementCategory.TREINO,   date = "02 Mai 2025"),
    Achievement("a02", "🔥", "Chama Viva",         "7 dias de streak consecutivos",          150, AchievementRarity.COMMON,    AchievementStatus.UNLOCKED,     AchievementCategory.STREAK,   date = "10 Mai 2025"),
    Achievement("a03", "💪", "Levantador Iniciante","Complete 10 treinos",                    300, AchievementRarity.COMMON,    AchievementStatus.UNLOCKED,     AchievementCategory.TREINO,   date = "15 Mai 2025"),
    Achievement("a04", "⚡", "Modo Bestial",        "Novo PR em 3 exercícios no mesmo dia",   500, AchievementRarity.RARE,      AchievementStatus.IN_PROGRESS,  AchievementCategory.TREINO,   progress = 0.67f, condition = "2/3 PRs"),
    Achievement("a05", "🔩", "Ferro e Suor",        "50 treinos completos",                   800, AchievementRarity.RARE,      AchievementStatus.IN_PROGRESS,  AchievementCategory.TREINO,   progress = 0.28f, condition = "14/50 treinos"),
    Achievement("a06", "💧", "Hidratado",           "7 dias atingindo meta de água",          150, AchievementRarity.COMMON,    AchievementStatus.UNLOCKED,     AchievementCategory.NUTRICAO, date = "18 Mai 2025"),
    Achievement("a07", "🧪", "Alquimista",          "Macros perfeitos por 30 dias",          1000, AchievementRarity.EPIC,      AchievementStatus.LOCKED,       AchievementCategory.NUTRICAO, condition = "Registre macros perfeitos 30 dias"),
    Achievement("a08", "🌋", "Vulcão",              "100 dias de streak",                   2000, AchievementRarity.LEGENDARY, AchievementStatus.LOCKED,       AchievementCategory.STREAK,   condition = "Mantenha streak por 100 dias"),
    Achievement("a09", "👥", "Primeiro Aliado",     "Adicione 1 amigo",                       50, AchievementRarity.COMMON,    AchievementStatus.UNLOCKED,     AchievementCategory.SOCIAL,   date = "01 Mai 2025"),
    Achievement("a10", "🤝", "Guilda",              "Crie um grupo com 5+ membros",           400, AchievementRarity.RARE,      AchievementStatus.LOCKED,       AchievementCategory.SOCIAL,   condition = "Crie grupo com 5 ou mais membros"),
    Achievement("a11", "🏅", "Centurião",           "100 treinos completos",                 1500, AchievementRarity.EPIC,      AchievementStatus.LOCKED,       AchievementCategory.TREINO,   condition = "Complete 100 treinos"),
    Achievement("a12", "⭐", "Completista",          "Desbloqueie todas as conquistas",       5000, AchievementRarity.LEGENDARY, AchievementStatus.LOCKED,       AchievementCategory.ESPECIAIS, condition = "Desbloqueie todas as outras conquistas")
)

@Composable
fun AchievementsScreen(onBack: () -> Unit) {
    var cat      by remember { mutableStateOf<AchievementCategory?>(null) }
    var statusF  by remember { mutableStateOf<AchievementStatus?>(null) }
    var selected by remember { mutableStateOf<Achievement?>(null) }

    val unlocked  = ACHIEVEMENTS.count { it.status == AchievementStatus.UNLOCKED }
    val totalXp   = ACHIEVEMENTS.filter { it.status == AchievementStatus.UNLOCKED }.sumOf { it.xp }
    val pct       = unlocked.toFloat() / ACHIEVEMENTS.size
    val filtered  = ACHIEVEMENTS.filter { a ->
        (cat == null || a.cat == cat) && (statusF == null || a.status == statusF)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FV.bg)
        ) {
            // ── Header
            FVScreenHeader(
                title  = "CONQUISTAS",
                sub    = "$unlocked / ${ACHIEVEMENTS.size} desbloqueadas",
                onBack = onBack
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(12.dp))

                // ── XP + progress bar
                Column(modifier = Modifier.padding(horizontal = FV.margin)) {
                    FVCard(glowColor = FV.xp) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "XP DE CONQUISTAS",
                                    fontSize = 10.sp,
                                    color = FV.textMuted,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    "%,d XP".format(totalXp),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = FV.primary
                                )
                            }
                            Text(
                                "%.0f%%".format(pct * 100),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = FV.xp
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        val animPct by animateFloatAsState(
                            targetValue   = pct,
                            animationSpec = tween(1000, easing = FastOutSlowInEasing),
                            label         = "progressAnim"
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(FV.radiusPill))
                                .background(FV.surface2)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(animPct)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(FV.radiusPill))
                                    .background(
                                        Brush.horizontalGradient(listOf(FV.xp, FV.primary))
                                    )
                            )
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // ── Category filters
                Column(modifier = Modifier.padding(horizontal = FV.margin)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        FVFilterPill("Todos", cat == null) { cat = null }
                        AchievementCategory.values().forEach { c ->
                            val label = when (c) {
                                AchievementCategory.TREINO   -> "🏋️ Treino"
                                AchievementCategory.NUTRICAO -> "🥗 Nutrição"
                                AchievementCategory.STREAK   -> "🔥 Streak"
                                AchievementCategory.SOCIAL   -> "👥 Social"
                                AchievementCategory.ESPECIAIS-> "⭐ Especiais"
                            }
                            FVFilterPill(label, cat == c) { cat = c }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    // Status filters
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FVFilterPill("Todas", statusF == null) { statusF = null }
                        FVFilterPill("Desbloqueadas", statusF == AchievementStatus.UNLOCKED) {
                            statusF = AchievementStatus.UNLOCKED
                        }
                        FVFilterPill("Em progresso", statusF == AchievementStatus.IN_PROGRESS) {
                            statusF = AchievementStatus.IN_PROGRESS
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))
                FVSectionLabel("${filtered.size} conquistas")

                // ── Grid
                LazyVerticalGrid(
                    columns   = GridCells.Fixed(2),
                    modifier  = Modifier
                        .fillMaxWidth()
                        .height((((filtered.size + 1) / 2) * 170).dp)
                        .padding(horizontal = FV.margin),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement   = Arrangement.spacedBy(10.dp)
                ) {
                    items(filtered, key = { it.id }) { a ->
                        AchievementCard(achievement = a) { selected = a }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }

        // ── Detail modal
        AnimatedVisibility(
            visible = selected != null,
            enter   = slideInVertically { it } + fadeIn(),
            exit    = slideOutVertically { it } + fadeOut()
        ) {
            selected?.let { a ->
                AchievementDetailSheet(achievement = a, onDismiss = { selected = null })
            }
        }
    }
}

@Composable
private fun AchievementCard(achievement: Achievement, onClick: () -> Unit) {
    val rColor  = rarityColor(achievement.rarity)
    val locked  = achievement.status == AchievementStatus.LOCKED
    val inProg  = achievement.status == AchievementStatus.IN_PROGRESS

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(FV.radius))
            .background(
                if (locked) Color(0xFF0D0D12) else FV.surface
            )
            .border(
                1.dp,
                when (achievement.status) {
                    AchievementStatus.UNLOCKED    -> rColor.copy(0.3f)
                    AchievementStatus.IN_PROGRESS -> rColor.copy(0.2f)
                    AchievementStatus.LOCKED      -> FV.outline.copy(0.5f)
                },
                RoundedCornerShape(FV.radius)
            )
            .clickable(onClick = onClick)
            .alpha(if (locked) 0.55f else 1f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp).fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(rColor.copy(0.1f))
                    .border(1.dp, rColor.copy(0.25f), CircleShape)
                    .graphicsLayer { if (locked) colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }) },
                contentAlignment = Alignment.Center
            ) {
                Text(achievement.icon, fontSize = 22.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                achievement.name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (locked) FV.textMuted else rColor,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
            Spacer(Modifier.height(4.dp))
            if (!locked) {
                Text(
                    "+${achievement.xp} XP",
                    fontSize = 9.sp,
                    color = FV.primary,
                    fontFamily = FontFamily.Monospace
                )
            } else {
                Text(
                    achievement.condition.take(26),
                    fontSize = 9.sp,
                    color = FV.textMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp
                )
            }
            if (inProg) {
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress  = achievement.progress,
                    modifier  = Modifier.fillMaxWidth().height(3.dp).clip(RoundedCornerShape(FV.radiusPill)),
                    color     = rColor,
                    trackColor = FV.surface2
                )
                Spacer(Modifier.height(3.dp))
                Text(achievement.condition, fontSize = 8.sp, color = rColor)
            }
        }
        // Rarity badge
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(6.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(rColor.copy(0.15f))
                .border(0.5.dp, rColor.copy(0.3f), RoundedCornerShape(4.dp))
                .padding(horizontal = 5.dp, vertical = 2.dp)
        ) {
            Text(rarityLabel(achievement.rarity), fontSize = 7.sp, color = rColor, fontWeight = FontWeight.Bold)
        }
        // Lock icon
        if (locked) {
            Text(
                "🔒",
                fontSize = 10.sp,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementDetailSheet(
    achievement: Achievement,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val rColor     = rarityColor(achievement.rarity)

    ModalBottomSheet(
        onDismissRequest  = onDismiss,
        sheetState        = sheetState,
        containerColor    = FV.surface,
        tonalElevation    = 0.dp,
        shape             = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle        = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(FV.outline)
            )
        }
    ) {
        Column(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {

            /* ── Icon ─────────────────────────────────────────────── */
            AchievementIcon(achievement = achievement, rColor = rColor)

            Spacer(Modifier.height(14.dp))

            /* ── Rarity badge ─────────────────────────────────────── */
            RarityBadge(rarity = achievement.rarity, rColor = rColor)

            Spacer(Modifier.height(10.dp))

            /* ── Name + description ───────────────────────────────── */
            Text(
                text       = achievement.name,
                fontSize   = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = FV.text,
                textAlign  = TextAlign.Center
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text       = achievement.desc,
                fontSize   = 14.sp,
                color      = FV.textMuted,
                textAlign  = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(16.dp))

            /* ── XP reward ────────────────────────────────────────── */
            XpBadge(xp = achievement.xp)

            /* ── Condition ────────────────────────────────────────── */
            if (achievement.condition.isNotEmpty()) {
                Spacer(Modifier.height(14.dp))
                AchievementCondition(condition = achievement.condition)
            }

            /* ── Progress ─────────────────────────────────────────── */
            if (achievement.status == AchievementStatus.IN_PROGRESS) {
                Spacer(Modifier.height(12.dp))
                AchievementProgress(progress = achievement.progress, rColor = rColor)
            }

            /* ── Unlock date ──────────────────────────────────────── */
            if (achievement.date.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text     = "Desbloqueada em ${achievement.date}",
                    fontSize = 11.sp,
                    color    = FV.textMuted
                )
            }

            Spacer(Modifier.height(24.dp))

            /* ── Actions ──────────────────────────────────────────── */
            if (achievement.status == AchievementStatus.UNLOCKED) {
                SheetButton(
                    label    = "Compartilhar Conquista ↑",
                    color    = rColor,
                    filled   = false,
                    onClick  = { /* share */ }
                )
                Spacer(Modifier.height(10.dp))
            }

            SheetButton(
                label   = "Fechar",
                color   = FV.textMuted,
                filled  = false,
                onClick = onDismiss
            )
        }
    }
}

/* ════════════════════════════════════════════════════════════════════
   Sub-componentes
   ════════════════════════════════════════════════════════════════════ */

@Composable
private fun AchievementIcon(achievement: Achievement, rColor: Color) {
    val isLegendary = achievement.rarity == AchievementRarity.LEGENDARY

    Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {

        if (isLegendary) {
            val rot by rememberInfiniteTransition("ring").animateFloat(
                initialValue  = 0f,
                targetValue   = 360f,
                animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
                label         = "ringRotate"
            )
            Canvas(modifier = Modifier.size(80.dp)) {
                drawCircle(
                    brush  = Brush.sweepGradient(
                        listOf(rColor.copy(0f), rColor.copy(0.8f), rColor.copy(0f)),
                        center = Offset(size.width / 2, size.height / 2)
                    ),
                    radius = 36f,
                    style  = Stroke(width = 3f)
                )
            }
        }

        Box(
            modifier            = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(rColor.copy(0.12f))
                .border(1.5.dp, rColor.copy(0.4f), CircleShape),
            contentAlignment    = Alignment.Center
        ) {
            Text(achievement.icon, fontSize = 30.sp)
        }
    }
}

@Composable
private fun RarityBadge(rarity: AchievementRarity, rColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(FV.radiusPill))
            .background(rColor.copy(0.12f))
            .border(1.dp, rColor.copy(0.3f), RoundedCornerShape(FV.radiusPill))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text          = rarityLabel(rarity),
            fontSize      = 10.sp,
            color         = rColor,
            fontWeight    = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
    }
}

@Composable
private fun XpBadge(xp: Int) {
    Row(
        modifier            = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(FV.primary.copy(0.08f))
            .border(1.dp, FV.primary.copy(0.2f), RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment   = Alignment.CenterVertically
    ) {
        Text("⭐", fontSize = 18.sp)
        Spacer(Modifier.width(8.dp))
        Text(
            text       = "+$xp XP",
            fontSize   = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color      = FV.primary,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun AchievementCondition(condition: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text          = "COMO DESBLOQUEAR",
            fontSize      = 10.sp,
            color         = FV.textMuted,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text      = condition,
            fontSize  = 13.sp,
            color     = FV.textDim,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AchievementProgress(progress: Float, rColor: Color) {
    Column(
        modifier            = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress   = progress,
            modifier   = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(FV.radiusPill)),
            color      = rColor,
            trackColor = FV.surface2
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text     = "${(progress * 100).toInt()}% concluído",
            fontSize = 11.sp,
            color    = rColor
        )
    }
}

@Composable
private fun SheetButton(
    label:   String,
    color:   Color,
    filled:  Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier            = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FV.radiusBtn))
            .background(if (filled) color else color.copy(0.08f))
            .border(1.dp, color.copy(0.3f), RoundedCornerShape(FV.radiusBtn))
            .clickable(onClick = onClick)
            .padding(14.dp),
        contentAlignment    = Alignment.Center
    ) {
        Text(
            text       = label,
            fontSize   = 14.sp,
            fontWeight = FontWeight.Bold,
            color      = if (filled) FV.surface else color
        )
    }
}

// ─────────────────────────────────────────────────────────────
// NAVIGATION (opcional — conecta as 4 telas)
// ─────────────────────────────────────────────────────────────

sealed class FVScreen {
    object Progression   : FVScreen()
    object Leaderboard   : FVScreen()
    object Friends       : FVScreen()
    object Achievements  : FVScreen()
}

@Composable
fun FVScreensNavigator(initial: FVScreen = FVScreen.Progression) {
    var current by remember { mutableStateOf<FVScreen>(initial) }

    AnimatedContent(
        targetState   = current,
        transitionSpec = {
            (slideInHorizontally { it } + fadeIn()) togetherWith
                    (slideOutHorizontally { -it } + fadeOut())
        },
        label = "screenNav"
    ) { screen ->
        when (screen) {
            FVScreen.Progression  -> ProgressionScreen  { }
            FVScreen.Leaderboard  -> LeaderboardScreen  { }
            FVScreen.Friends      -> FriendsScreen      { }
            FVScreen.Achievements -> AchievementsScreen { }
        }
    }
}


