package org.fitverse.presentation.ui.achievements

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.expect.format
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.theme.FVTypography
import org.fitverse.presentation.ui.achievements.viewmodel.AchievementsEvent
import org.fitverse.presentation.ui.achievements.viewmodel.AchievementsIntent
import org.fitverse.presentation.ui.achievements.viewmodel.AchievementsUiState
import org.fitverse.presentation.ui.achievements.viewmodel.AchievementsViewModel
import org.fitverse.presentation.widgets.FVCard
import org.fitverse.presentation.widgets.FVFilterPill
import org.fitverse.presentation.widgets.FVSectionLabel
import org.fitverse.presentation.widgets.FitVerseSpacer
import org.fitverse.presentation.widgets.FitverseTopAppBar

// ── Modelos ───────────────────────────────────────────────────────────────────

enum class AchievementRarity   { COMMON, RARE, EPIC, LEGENDARY }
enum class AchievementStatus   { UNLOCKED, IN_PROGRESS, LOCKED }
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
    val progress:  Float  = 0f,
    val condition: String = "",
    val date:      String = ""
)

// ── Dados estáticos isolados ──────────────────────────────────────────────────

object AchievementsData {
    val all = listOf(
        Achievement("a01", "🏆", "Primeira Vitória",    "Complete seu 1º treino",                 100,  AchievementRarity.COMMON,    AchievementStatus.UNLOCKED,    AchievementCategory.TREINO,    date = "02 Mai 2025"),
        Achievement("a02", "🔥", "Chama Viva",           "7 dias de streak consecutivos",           150,  AchievementRarity.COMMON,    AchievementStatus.UNLOCKED,    AchievementCategory.STREAK,    date = "10 Mai 2025"),
        Achievement("a03", "💪", "Levantador Iniciante",  "Complete 10 treinos",                    300,  AchievementRarity.COMMON,    AchievementStatus.UNLOCKED,    AchievementCategory.TREINO,    date = "15 Mai 2025"),
        Achievement("a04", "⚡", "Modo Bestial",           "Novo PR em 3 exercícios no mesmo dia",  500,  AchievementRarity.RARE,      AchievementStatus.IN_PROGRESS, AchievementCategory.TREINO,    progress = 0.67f, condition = "2/3 PRs"),
        Achievement("a05", "🔩", "Ferro e Suor",           "50 treinos completos",                  800,  AchievementRarity.RARE,      AchievementStatus.IN_PROGRESS, AchievementCategory.TREINO,    progress = 0.28f, condition = "14/50 treinos"),
        Achievement("a06", "💧", "Hidratado",             "7 dias atingindo meta de água",           150,  AchievementRarity.COMMON,    AchievementStatus.UNLOCKED,    AchievementCategory.NUTRICAO,  date = "18 Mai 2025"),
        Achievement("a07", "🧪", "Alquimista",            "Macros perfeitos por 30 dias",          1000,  AchievementRarity.EPIC,      AchievementStatus.LOCKED,      AchievementCategory.NUTRICAO,  condition = "Registre macros perfeitos 30 dias"),
        Achievement("a08", "🌋", "Vulcão",                "100 dias de streak",                    2000,  AchievementRarity.LEGENDARY, AchievementStatus.LOCKED,      AchievementCategory.STREAK,    condition = "Mantenha streak por 100 dias"),
        Achievement("a09", "👥", "Primeiro Aliado",        "Adicione 1 amigo",                        50,  AchievementRarity.COMMON,    AchievementStatus.UNLOCKED,    AchievementCategory.SOCIAL,    date = "01 Mai 2025"),
        Achievement("a10", "🤝", "Guilda",                "Crie um grupo com 5+ membros",            400,  AchievementRarity.RARE,      AchievementStatus.LOCKED,      AchievementCategory.SOCIAL,    condition = "Crie grupo com 5 ou mais membros"),
        Achievement("a11", "🏅", "Centurião",             "100 treinos completos",                 1500,  AchievementRarity.EPIC,      AchievementStatus.LOCKED,      AchievementCategory.TREINO,    condition = "Complete 100 treinos"),
        Achievement("a12", "⭐", "Completista",            "Desbloqueie todas as conquistas",       5000,  AchievementRarity.LEGENDARY, AchievementStatus.LOCKED,      AchievementCategory.ESPECIAIS, condition = "Desbloqueie todas as outras conquistas")
    )
}

// ── Helpers ───────────────────────────────────────────────────────────────────

fun rarityColor(r: AchievementRarity): Color = when (r) {
    AchievementRarity.COMMON    -> FitColors.Accent
    AchievementRarity.RARE      -> FitColors.Xp
    AchievementRarity.EPIC      -> Color(0xFFF5C518)
    AchievementRarity.LEGENDARY -> FitColors.Red
}

fun rarityLabel(r: AchievementRarity): String = when (r) {
    AchievementRarity.COMMON    -> "COMUM"
    AchievementRarity.RARE      -> "RARO"
    AchievementRarity.EPIC      -> "ÉPICO"
    AchievementRarity.LEGENDARY -> "LENDÁRIO"
}

// ── Root — wires ViewModel ────────────────────────────────────────────────────

@Composable
fun AchievementsRoot(
    viewModel: AchievementsViewModel,
    onBack:    () -> Unit,
    modifier:  Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AchievementsEvent.NavigateBack -> onBack()
            }
        }
    }

    AchievementsScreen(
        modifier  = modifier,
        state     = uiState,
        onIntent  = viewModel::onIntent,
        onBack    = { viewModel.onIntent(AchievementsIntent.NavigateBack) },
    )
}

// ── Tela principal ────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    state:    AchievementsUiState,
    onIntent: (AchievementsIntent) -> Unit,
    modifier: Modifier = Modifier,
    onBack:   () -> Unit,
) {
    val cat     = state.selectedCategory
    val statusF = state.selectedStatus

    // Filtragem memoizada — só recalcula quando os filtros ou a lista mudam
    val filtered by remember(state.achievements, cat, statusF) {
        derivedStateOf {
            state.achievements.filter { a ->
                (cat == null || a.cat == cat) &&
                        (statusF == null || a.status == statusF)
            }
        }
    }

    // Pares para o grid de 2 colunas — só recalcula quando filtered muda
    val rows by remember(filtered) {
        derivedStateOf { filtered.chunked(2) }
    }

    // Métricas globais (independem dos filtros)
    val unlocked by remember(state.achievements) {
        derivedStateOf { state.achievements.count { it.status == AchievementStatus.UNLOCKED } }
    }
    val totalXp by remember(state.achievements) {
        derivedStateOf { state.achievements.filter { it.status == AchievementStatus.UNLOCKED }.sumOf { it.xp } }
    }
    val total = state.achievements.size.coerceAtLeast(1)
    val pct   = unlocked.toFloat() / total

    Scaffold(
        modifier            = modifier,
        containerColor      = FitColors.Bg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FitverseTopAppBar(
                title    = "CONQUISTAS",
                onBack   = onBack,
                subtitle = {
                    Text(
                        text  = "Você desbloqueou $unlocked / $total conquistas",
                        style = FVTypography.bodySmall,
                        color = FitColors.TextMuted,
                    )
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FitColors.Bg)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 32.dp)
        ) {

            // ── 1. Card XP ────────────────────────────────────────────
            FitVerseSpacer(vertical = true, value = 12.dp)
            Column(Modifier.padding(horizontal = 20.dp)) {
                FVCard(glowColor = FitColors.Xp, modifier = Modifier) {
                    Row(
                        modifier              = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text  = "XP DE CONQUISTAS",
                                style = FVTypography.overline,
                                color = FitColors.TextMuted,
                            )
                            Text(
                                text  = "%,d XP".format(totalXp),
                                style = FVTypography.monoLarge,
                                color = FitColors.Accent,
                            )
                        }
                        Text(
                            text  = "%.0f%%".format(pct * 100),
                            style = FVTypography.displayMedium,
                            color = FitColors.Xp,
                        )
                    }
                    FitVerseSpacer(vertical = true, value = 5.dp)
                    val animPct by animateFloatAsState(
                        targetValue   = pct,
                        animationSpec = tween(1000, easing = FastOutSlowInEasing),
                        label         = "progressAnim"
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(FitColors.Surface2)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animPct)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(100.dp))
                                .background(
                                    Brush.horizontalGradient(listOf(FitColors.Xp, FitColors.Accent))
                                )
                        )
                    }
                }
            }

            // ── 2. Filtros ────────────────────────────────────────────
            FitVerseSpacer(vertical = true, value = 14.dp)
            Column(modifier = Modifier){
                LazyRow(
                    modifier              = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding        = PaddingValues(horizontal = 20.dp)
                ) {
                    item {
                        FVFilterPill(label = "Todos", selected = cat == null) {
                            onIntent(AchievementsIntent.FilterByCategory(null))
                        }
                    }
                    items(AchievementCategory.entries){c ->
                        FVFilterPill(
                            label    = when (c) {
                                AchievementCategory.TREINO    -> "🏋️ Treino"
                                AchievementCategory.NUTRICAO  -> "🥗 Nutrição"
                                AchievementCategory.STREAK    -> "🔥 Streak"
                                AchievementCategory.SOCIAL    -> "👥 Social"
                                AchievementCategory.ESPECIAIS -> "⭐ Especiais"
                            },
                            selected = cat == c
                        ) { onIntent(AchievementsIntent.FilterByCategory(c)) }

                    }
                }
                FitVerseSpacer(vertical = true, value = 8.dp)
                LazyRow(
                    modifier              = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding        = PaddingValues(horizontal = 20.dp)
                ) {
                    item {
                        FVFilterPill(label = "Todas", selected = statusF == null) {
                            onIntent(AchievementsIntent.FilterByStatus(null))
                        }
                    }
                    item {
                        FVFilterPill(
                            label    = "Desbloqueadas",
                            selected = statusF == AchievementStatus.UNLOCKED
                        ) { onIntent(AchievementsIntent.FilterByStatus(AchievementStatus.UNLOCKED)) }
                    }
                    item {
                        FVFilterPill(
                            label    = "Em progresso",
                            selected = statusF == AchievementStatus.IN_PROGRESS
                        ) { onIntent(AchievementsIntent.FilterByStatus(AchievementStatus.IN_PROGRESS)) }
                    }
                }
            }

            // ── 3. Label da seção ─────────────────────────────────────
            FitVerseSpacer(vertical = true, value = 14.dp)
            FVSectionLabel("${filtered.size} conquistas")
            FitVerseSpacer(vertical = true, value = 4.dp)

            // ── 4. Grid 2 colunas via chunked ─────────────────────────
            rows.forEach { pair ->
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pair.forEach { achievement ->
                        AchievementCard(
                            achievement = achievement,
                            modifier    = Modifier.weight(1f),
                            onClick     = { onIntent(AchievementsIntent.SelectAchievement(achievement)) }
                        )
                    }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }

        state.selectedAchievement?.let { achievement ->
            AchievementDetailSheet(
                achievement = achievement,
                onDismiss   = { onIntent(AchievementsIntent.SelectAchievement(null)) }
            )
        }
    }
}

// ── Card de conquista ─────────────────────────────────────────────────────────

@Composable
private fun AchievementCard(
    achievement: Achievement,
    modifier:    Modifier = Modifier,
    onClick:     () -> Unit
) {
    val rColor = rarityColor(achievement.rarity)
    val locked = achievement.status == AchievementStatus.LOCKED
    val inProg = achievement.status == AchievementStatus.IN_PROGRESS

    // Criada uma única vez por composição — evita alocação por recomposição
    val grayscaleMatrix = remember { ColorMatrix().apply { setToSaturation(0f) } }

    Box(
        modifier = modifier
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (locked) Color(0xFF0D0D12) else FitColors.Surface)
            .border(
                width = 1.dp,
                color = when (achievement.status) {
                    AchievementStatus.UNLOCKED    -> rColor.copy(alpha = 0.3f)
                    AchievementStatus.IN_PROGRESS -> rColor.copy(alpha = 0.2f)
                    AchievementStatus.LOCKED      -> FitColors.Outline.copy(alpha = 0.5f)
                },
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .alpha(if (locked) 0.55f else 1f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(rColor.copy(alpha = 0.1f))
                    .border(1.dp, rColor.copy(alpha = 0.25f), CircleShape)
                    .graphicsLayer {
                        colorFilter = if (locked) {
                            ColorFilter.colorMatrix(grayscaleMatrix)
                        } else null
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = achievement.icon, fontSize = 22.sp)
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text      = achievement.name,
                style     = FVTypography.titleSmall,
                color     = if (locked) FitColors.TextMuted else rColor,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(4.dp))

            if (!locked) {
                Text(
                    text  = "+${achievement.xp} XP",
                    style = FVTypography.monoSmall,
                    color = FitColors.Accent,
                )
            } else {
                Text(
                    text      = achievement.condition.take(26),
                    style     = FVTypography.caption,
                    color     = FitColors.TextMuted,
                    textAlign = TextAlign.Center,
                )
            }

            if (inProg) {
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress   = { achievement.progress },
                    modifier   = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(100.dp)),
                    color      = rColor,
                    trackColor = FitColors.Surface2
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text  = achievement.condition,
                    style = FVTypography.caption,
                    color = rColor,
                )
            }
        }

        // Badge de raridade (top-start)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(6.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(rColor.copy(alpha = 0.15f))
                .border(0.5.dp, rColor.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                .padding(horizontal = 5.dp, vertical = 2.dp)
        ) {
            Text(
                text  = rarityLabel(achievement.rarity),
                style = FVTypography.captionBold,
                color = rColor,
            )
        }

        // Ícone de cadeado (top-end)
        if (locked) {
            Text(
                text     = "🔒",
                style    = FVTypography.labelSmall,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
            )
        }
    }
}

// ── Bottom sheet de detalhe ───────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementDetailSheet(
    achievement: Achievement,
    onDismiss:   () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val rColor     = rarityColor(achievement.rarity)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = sheetState,
        containerColor   = FitColors.Surface,
        tonalElevation   = 0.dp,
        shape            = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle       = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(FitColors.Outline)
            )
        }
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AchievementIcon(achievement = achievement, rColor = rColor)
            Spacer(Modifier.height(14.dp))

            RarityBadge(rarity = achievement.rarity, rColor = rColor)
            Spacer(Modifier.height(10.dp))

            Text(
                text      = achievement.name,
                style     = FVTypography.headlineLarge,
                color     = FitColors.TextPrimary,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(6.dp))

            Text(
                text      = achievement.desc,
                style     = FVTypography.bodyLarge,
                color     = FitColors.TextMuted,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(16.dp))

            XpBadge(xp = achievement.xp)

            if (achievement.condition.isNotEmpty()) {
                Spacer(Modifier.height(14.dp))
                AchievementCondition(condition = achievement.condition)
            }

            if (achievement.status == AchievementStatus.IN_PROGRESS) {
                Spacer(Modifier.height(12.dp))
                AchievementProgress(progress = achievement.progress, rColor = rColor)
            }

            if (achievement.date.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text  = "Desbloqueada em ${achievement.date}",
                    style = FVTypography.labelMedium,
                    color = FitColors.TextMuted,
                )
            }

            Spacer(Modifier.height(24.dp))

            if (achievement.status == AchievementStatus.UNLOCKED) {
                SheetButton(
                    label   = "Compartilhar Conquista ↑",
                    color   = rColor,
                    filled  = false,
                    onClick = { /* share */ }
                )
                Spacer(Modifier.height(10.dp))
            }

            SheetButton(
                label   = "Fechar",
                color   = FitColors.TextMuted,
                filled  = false,
                onClick = onDismiss
            )
        }
    }
}

// ── Sub-componentes ───────────────────────────────────────────────────────────

@Composable
private fun AchievementIcon(achievement: Achievement, rColor: Color) {
    val isLegendary = achievement.rarity == AchievementRarity.LEGENDARY

    Box(
        modifier         = Modifier.size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLegendary) {
            val rot by rememberInfiniteTransition("ring").animateFloat(
                initialValue  = 0f,
                targetValue   = 360f,
                animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
                label         = "ringRotate"
            )
            // graphicsLayer aplica a rotação no Canvas — anel realmente gira
            Canvas(
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer { rotationZ = rot }
            ) {
                drawCircle(
                    brush  = Brush.sweepGradient(
                        colors = listOf(
                            rColor.copy(alpha = 0f),
                            rColor.copy(alpha = 0.8f),
                            rColor.copy(alpha = 0f)
                        ),
                        center = Offset(size.width / 2, size.height / 2)
                    ),
                    radius = 36f,
                    style  = Stroke(width = 3f)
                )
            }
        }

        Box(
            modifier         = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(rColor.copy(alpha = 0.12f))
                .border(1.5.dp, rColor.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = achievement.icon, fontSize = 30.sp)
        }
    }
}

@Composable
private fun RarityBadge(rarity: AchievementRarity, rColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(rColor.copy(alpha = 0.12f))
            .border(1.dp, rColor.copy(alpha = 0.3f), RoundedCornerShape(100.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text  = rarityLabel(rarity),
            style = FVTypography.captionBold,
            color = rColor,
        )
    }
}

@Composable
private fun XpBadge(xp: Int) {
    Row(
        modifier          = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(FitColors.Accent.copy(alpha = 0.08f))
            .border(1.dp, FitColors.Accent.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "⭐", fontSize = 18.sp)
        Spacer(Modifier.width(8.dp))
        Text(
            text  = "+$xp XP",
            style = FVTypography.monoLarge,
            color = FitColors.Accent,
        )
    }
}

@Composable
private fun AchievementCondition(condition: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text  = "COMO DESBLOQUEAR",
            style = FVTypography.overline,
            color = FitColors.TextMuted,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text      = condition,
            style     = FVTypography.bodyMedium,
            color     = FitColors.TextDisabled,
            textAlign = TextAlign.Center,
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
            progress   = { progress },
            modifier   = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(100.dp)),
            color      = rColor,
            trackColor = FitColors.Surface2
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text  = "${(progress * 100).toInt()}% concluído",
            style = FVTypography.labelMedium,
            color = rColor,
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
        modifier         = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (filled) color else color.copy(alpha = 0.08f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text  = label,
            style = FVTypography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = if (filled) FitColors.Surface else color,
        )
    }
}