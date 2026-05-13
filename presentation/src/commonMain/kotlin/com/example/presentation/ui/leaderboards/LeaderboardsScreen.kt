package com.example.presentation.ui.leaderboards

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import com.example.expect.format
import com.example.presentation.theme.FVExtension
import com.example.presentation.ui.leaderboards.viewmodel.LeaderboardEntry
import com.example.presentation.ui.leaderboards.viewmodel.LeaderboardsEvent
import com.example.presentation.ui.leaderboards.viewmodel.LeaderboardsIntent
import com.example.presentation.ui.leaderboards.viewmodel.LeaderboardsViewModel
import com.example.presentation.ui.leaderboards.viewmodel.defaultLeaderboardEntries
import com.example.presentation.widgets.FVCard
import com.example.presentation.widgets.FVFilterPill
import com.example.presentation.widgets.FVSectionLabel
import com.example.presentation.widgets.FitverseTopAppBar
import kotlin.math.abs

// ── Dados estáticos centralizados ─────────────────────────────────────────────
// LeaderboardEntry is defined in viewmodel/LeaderboardsUiState.kt

private object LeaderboardData {
    val entries   = defaultLeaderboardEntries
    val podium    = entries.take(3)
    val rankList  = entries.drop(3)
    val meEntry   = entries.first { it.isMe }
    const val seasonDaysLeft = 6
}

// ── Root — wires ViewModel ────────────────────────────────────────────────────

@Composable
fun LeaderboardRoot(
    viewModel: LeaderboardsViewModel,
    onBack:    () -> Unit,
    modifier:  Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LeaderboardsEvent.NavigateBack -> onBack()
            }
        }
    }

    LeaderboardScreen(
        modifier  = modifier,
        onBack    = { viewModel.onIntent(LeaderboardsIntent.NavigateBack) },
        scope     = uiState.scope,
        metric    = uiState.metric,
        period    = uiState.period,
        onScope   = { viewModel.onIntent(LeaderboardsIntent.FilterScope(it)) },
        onMetric  = { viewModel.onIntent(LeaderboardsIntent.FilterMetric(it)) },
        onPeriod  = { viewModel.onIntent(LeaderboardsIntent.FilterPeriod(it)) },
    )
}

// ── Tela principal ────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    modifier: Modifier = Modifier,
    onBack:   () -> Unit,
    scope:    String = "GLOBAL",
    metric:   String = "XP TOTAL",
    period:   String = "SEMANA",
    onScope:  (String) -> Unit = {},
    onMetric: (String) -> Unit = {},
    onPeriod: (String) -> Unit = {},
) {
    Scaffold(
        modifier            = modifier,
        containerColor      = FVExtension.bg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FitverseTopAppBar(
                title    = "RANKING",
                onBack   = onBack,
                subtitle = {
                    Text(
                        text     = "Semana de 29 Abr – 5 Mai · atualizado há 2 min",
                        fontSize = 12.sp,
                        color    = FVExtension.textMuted
                    )
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .background(FVExtension.bg)
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            // ── 1. Banner de temporada ────────────────────────────────
            item(key = "season_banner") {
                Spacer(Modifier.height(12.dp))
                SeasonBanner()
                Spacer(Modifier.height(14.dp))
            }

            // ── 2. Pills de filtro (scope / métrica / período) ────────
            item(key = "filters") {
                FilterSection(
                    scope    = scope,
                    metric   = metric,
                    period   = period,
                    onScope  = onScope,
                    onMetric = onMetric,
                    onPeriod = onPeriod,
                )
                Spacer(Modifier.height(20.dp))
            }

            // ── 3. Pódio (top 3) ──────────────────────────────────────
            item(key = "podium") {
                Podium(
                    first  = LeaderboardData.podium[0],
                    second = LeaderboardData.podium[1],
                    third  = LeaderboardData.podium[2]
                )
                Spacer(Modifier.height(16.dp))
                HorizontalDivider(
                    color     = FVExtension.outline,
                    thickness = 0.5.dp,
                    modifier  = Modifier.padding(horizontal = FVExtension.margin)
                )
                Spacer(Modifier.height(8.dp))
                FVSectionLabel("Classificação Geral")
            }

            // ── 4. Linhas de ranking (4º em diante) ───────────────────
            // Cada entry como item separado para que o LazyColumn
            // só componha as linhas visíveis na tela
            items(
                items = LeaderboardData.rankList,
                key   = { it.rank }
            ) { entry ->
                RankRow(entry = entry)
            }

            // ── 5. Card "minha posição" ───────────────────────────────
            item(key = "me_card") {
                Spacer(Modifier.height(12.dp))
                MeCard(entry = LeaderboardData.meEntry)
            }
        }
    }
}

// ── Banner de temporada ───────────────────────────────────────────────────────

@Composable
private fun SeasonBanner() {
    Box(
        modifier = Modifier
            .padding(horizontal = FVExtension.margin)
            .fillMaxWidth()
            .clip(RoundedCornerShape(FVExtension.radius))
            .background(FVExtension.danger.copy(alpha = 0.07f))
            .border(1.dp, FVExtension.danger.copy(alpha = 0.2f), RoundedCornerShape(FVExtension.radius))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "⏳", fontSize = 18.sp)
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = "Temporada encerra em ${LeaderboardData.seasonDaysLeft} dias",
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = FVExtension.text
                )
                Text(
                    text     = "Mantenha sua posição para ganhar recompensas",
                    fontSize = 11.sp,
                    color    = FVExtension.textMuted
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(FVExtension.radiusPill))
                    .background(FVExtension.danger.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text       = "Ver prêmios",
                    fontSize   = 10.sp,
                    color      = FVExtension.danger,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ── Seção de filtros ──────────────────────────────────────────────────────────
// Extraída como composable — evita re-renderizar o LazyColumn inteiro ao mudar filtro

@Composable
private fun FilterSection(
    scope:    String,
    metric:   String,
    period:   String,
    onScope:  (String) -> Unit,
    onMetric: (String) -> Unit,
    onPeriod: (String) -> Unit
) {
    // horizontalScroll em cada Row evita overflow em telas pequenas
    Column(
        modifier      = Modifier.padding(horizontal = FVExtension.margin),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier              = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("GLOBAL", "AMIGOS", "REGIONAL", "MINHA CLASSE").forEach { s ->
                FVFilterPill(label = s, selected = s == scope) { onScope(s) }
            }
        }
        Row(
            modifier              = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("XP TOTAL", "STREAK", "VOLUME", "MISSÕES").forEach { m ->
                FVFilterPill(label = m, selected = m == metric) { onMetric(m) }
            }
        }
        Row(
            modifier              = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("SEMANA", "MÊS", "ALL TIME").forEach { p ->
                FVFilterPill(label = p, selected = p == period) { onPeriod(p) }
            }
        }
    }
}

// ── Pódio ─────────────────────────────────────────────────────────────────────

@Composable
private fun Podium(
    first:  LeaderboardEntry,
    second: LeaderboardEntry,
    third:  LeaderboardEntry
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = FVExtension.margin),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment     = Alignment.Bottom
    ) {
        PodiumEntry(entry = second, placeColor = FVExtension.silver, platformH = 48.dp)
        PodiumEntry(entry = first,  placeColor = FVExtension.gold,   platformH = 64.dp, isCrown = true)
        PodiumEntry(entry = third,  placeColor = FVExtension.bronze, platformH = 32.dp)
    }
}

@Composable
private fun PodiumEntry(
    entry:      LeaderboardEntry,
    placeColor: Color,
    platformH:  Dp,
    isCrown:    Boolean = false
) {
    val avatarSize = if (isCrown) 56.dp else 44.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier            = Modifier.width(100.dp)
    ) {
        if (isCrown) {
            val floatOffset by rememberInfiniteTransition("crown").animateFloat(
                initialValue  = 0f,
                targetValue   = -6f,
                animationSpec = infiniteRepeatable(
                    tween(1400, easing = FastOutSlowInEasing),
                    RepeatMode.Reverse
                ),
                label = "crownFloat"
            )
            Text(
                text     = "👑",
                fontSize = 20.sp,
                modifier = Modifier.offset(y = floatOffset.dp)
            )
        } else {
            Spacer(Modifier.height(24.dp))
        }

        Spacer(Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .size(avatarSize)
                .clip(CircleShape)
                .background(entry.classColor.copy(alpha = 0.15f))
                .border(2.dp, placeColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text     = entry.name.first().toString(),
                fontSize = if (isCrown) 22.sp else 17.sp,
                color    = FVExtension.text
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text       = entry.name,
            fontSize   = if (isCrown) 12.sp else 10.sp,
            color      = FVExtension.text,
            fontWeight = FontWeight.SemiBold,
            maxLines   = 1
        )
        Text(
            text       = "%,d".format(entry.score),
            fontSize   = 10.sp,
            color      = placeColor,
            fontFamily = FontFamily.Monospace
        )

        Spacer(Modifier.height(6.dp))

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
                color      = FVExtension.bg
            )
        }
    }
}

// ── Linha de ranking ──────────────────────────────────────────────────────────

@Composable
private fun RankRow(entry: LeaderboardEntry) {
    // Cores calculadas uma vez por composição da linha
    val borderColor     = if (entry.isMe) FVExtension.primary.copy(alpha = 0.25f) else FVExtension.outline
    val bgColor         = if (entry.isMe) FVExtension.primary.copy(alpha = 0.05f) else FVExtension.surface
    val rankColor       = if (entry.rank <= 5) FVExtension.text else FVExtension.textMuted
    val nameColor       = if (entry.isMe) FVExtension.primary else FVExtension.text
    val scoreColor      = if (entry.isMe) FVExtension.primary else FVExtension.text
    val avatarBorder    = if (entry.isMe) 2.dp else 1.dp
    val avatarBdrColor  = if (entry.isMe) FVExtension.primary.copy(alpha = 0.5f) else entry.classColor.copy(alpha = 0.3f)
    val deltaColor = when {
        entry.delta > 0 -> FVExtension.secondary
        entry.delta < 0 -> FVExtension.danger
        else            -> FVExtension.textMuted
    }
    val deltaText = when {
        entry.delta > 0 -> "▲${entry.delta}"
        entry.delta < 0 -> "▼${abs(entry.delta)}"
        else            -> "—"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = FVExtension.margin, vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text       = "#${entry.rank}",
            fontSize   = 11.sp,
            fontWeight = FontWeight.Bold,
            color      = rankColor,
            fontFamily = FontFamily.Monospace,
            modifier   = Modifier.width(28.dp)
        )
        Spacer(Modifier.width(8.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(entry.classColor.copy(alpha = 0.12f))
                .border(avatarBorder, avatarBdrColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = entry.name.first().toString(), fontSize = 14.sp, color = FVExtension.text)
        }

        Spacer(Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text       = entry.name,
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = nameColor
                )
                if (entry.isMe) {
                    Spacer(Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(FVExtension.radiusPill))
                            .background(FVExtension.primary.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text       = "VOCÊ",
                            fontSize   = 8.sp,
                            color      = FVExtension.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Text(
                text     = "Nível ${entry.level} · ${entry.className}",
                fontSize = 10.sp,
                color    = FVExtension.textMuted
            )
        }

        Text(
            text       = "%,d".format(entry.score),
            fontSize   = 12.sp,
            fontWeight = FontWeight.Bold,
            color      = scoreColor,
            fontFamily = FontFamily.Monospace
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text      = deltaText,
            fontSize  = 10.sp,
            color     = deltaColor,
            fontFamily = FontFamily.Monospace,
            modifier  = Modifier.width(28.dp),
            textAlign = TextAlign.End
        )
    }
}

// ── Card "minha posição" ──────────────────────────────────────────────────────

@Composable
private fun MeCard(entry: LeaderboardEntry) {
    Column(modifier = Modifier.padding(horizontal = FVExtension.margin)) {
        FVCard(glowColor = FVExtension.primary) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text          = "Sua posição atual",
                        fontSize      = 10.sp,
                        color         = FVExtension.textMuted,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text       = "#${entry.rank} no ranking global",
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color      = FVExtension.primary
                    )
                }
                Text(
                    text       = "%,d XP".format(entry.score),
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = FVExtension.text,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(Modifier.height(10.dp))

            // Progresso até a posição seguinte
            val nextEntry = LeaderboardData.entries.getOrNull(entry.rank - 2)
            if (nextEntry != null) {
                val gap      = nextEntry.score - entry.score
                val progress = 1f - (gap.toFloat() / nextEntry.score).coerceIn(0f, 1f)

                Text(
                    text     = "Faltam ${"%,d".format(gap)} XP para a posição #${nextEntry.rank}",
                    fontSize = 11.sp,
                    color    = FVExtension.textMuted
                )
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress   = { progress },        // API não-depreciada
                    modifier   = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(FVExtension.radiusPill)),
                    color      = FVExtension.primary,
                    trackColor = FVExtension.surface2
                )
            }
        }
    }
}