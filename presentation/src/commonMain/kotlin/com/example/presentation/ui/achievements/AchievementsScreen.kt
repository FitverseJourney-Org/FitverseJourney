package com.example.presentation.ui.achievements

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expect.format
import com.example.presentation.theme.FVExtension
import com.example.presentation.ui.achievements.viewmodel.AchievementsEvent
import com.example.presentation.ui.achievements.viewmodel.AchievementsIntent
import com.example.presentation.ui.achievements.viewmodel.AchievementsViewModel
import com.example.presentation.widgets.FVCard
import com.example.presentation.widgets.FVFilterPill
import com.example.presentation.widgets.FVSectionLabel
import com.example.presentation.widgets.FitVerseSpacer
import com.example.presentation.widgets.FitverseTopAppBar

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
    AchievementRarity.COMMON    -> FVExtension.primary
    AchievementRarity.RARE      -> FVExtension.xp
    AchievementRarity.EPIC      -> Color(0xFFF5C518)
    AchievementRarity.LEGENDARY -> FVExtension.danger
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
        modifier = modifier,
        onBack   = { viewModel.onIntent(AchievementsIntent.NavigateBack) },
    )
}

// ── Tela principal ────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(
    modifier: Modifier = Modifier,
    onBack:   () -> Unit
) {
    var cat      by remember { mutableStateOf<AchievementCategory?>(null) }
    var statusF  by remember { mutableStateOf<AchievementStatus?>(null) }
    var selected by remember { mutableStateOf<Achievement?>(null) }

    // Filtragem memoizada — só recalcula quando os filtros mudam
    val filtered by remember(cat, statusF) {
        derivedStateOf {
            AchievementsData.all.filter { a ->
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
    val unlocked = remember { AchievementsData.all.count { it.status == AchievementStatus.UNLOCKED } }
    val totalXp  = remember { AchievementsData.all.filter { it.status == AchievementStatus.UNLOCKED }.sumOf { it.xp } }
    val pct      = unlocked.toFloat() / AchievementsData.all.size

    Scaffold(
        modifier            = modifier,
        containerColor      = FVExtension.bg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FitverseTopAppBar(
                title    = "CONQUISTAS",
                onBack   = onBack,
                subtitle = {
                    Text(
                        text     = "Você desbloqueou $unlocked / ${AchievementsData.all.size} conquistas",
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
            contentPadding = PaddingValues(bottom = 32.dp, top = 16.dp)
        ) {

            // ── 1. Card XP ────────────────────────────────────────────
            item(key = "xp_card") {
                FitVerseSpacer(
                    vertical = true,
                    value = 12.dp
                )
                Column(Modifier.padding(horizontal = FVExtension.margin)) {
                    FVCard(glowColor = FVExtension.xp) {
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text          = "XP DE CONQUISTAS",
                                    fontSize      = 10.sp,
                                    color         = FVExtension.textMuted,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text       = "%,d XP".format(totalXp),
                                    fontSize   = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color      = FVExtension.primary
                                )
                            }
                            Text(
                                text       = "%.0f%%".format(pct * 100),
                                fontSize   = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color      = FVExtension.xp
                            )
                        }
                        FitVerseSpacer(
                            vertical = true,
                            value = 10.dp
                        )
                        val animPct by animateFloatAsState(
                            targetValue   = pct,
                            animationSpec = tween(1000, easing = FastOutSlowInEasing),
                            label         = "progressAnim"
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(FVExtension.radiusPill))
                                .background(FVExtension.surface2)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(animPct)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(FVExtension.radiusPill))
                                    .background(
                                        Brush.horizontalGradient(listOf(FVExtension.xp, FVExtension.primary))
                                    )
                            )
                        }
                    }
                }
            }

            // ── 2. Filtros ────────────────────────────────────────────
            item(key = "filters") {
                FitVerseSpacer(
                    vertical = true,
                    value = 14.dp
                )
                Column(Modifier.padding(horizontal = FVExtension.margin)) {

                    // Filtro por categoria
                    Row(
                        modifier              = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FVFilterPill(label = "Todos", selected = cat == null) { cat = null }
                        AchievementCategory.entries.forEach { c ->
                            FVFilterPill(
                                label    = when (c) {
                                    AchievementCategory.TREINO    -> "🏋️ Treino"
                                    AchievementCategory.NUTRICAO  -> "🥗 Nutrição"
                                    AchievementCategory.STREAK    -> "🔥 Streak"
                                    AchievementCategory.SOCIAL    -> "👥 Social"
                                    AchievementCategory.ESPECIAIS -> "⭐ Especiais"
                                },
                                selected = cat == c
                            ) { cat = c }
                        }
                    }

                    FitVerseSpacer(
                        vertical = true,
                        value = 8.dp
                    )

                    // Filtro por status
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FVFilterPill(label = "Todas", selected = statusF == null) {
                            statusF = null
                        }
                        FVFilterPill(
                            label    = "Desbloqueadas",
                            selected = statusF == AchievementStatus.UNLOCKED
                        ) { statusF = AchievementStatus.UNLOCKED }

                        FVFilterPill(
                            label    = "Em progresso",
                            selected = statusF == AchievementStatus.IN_PROGRESS
                        ) { statusF = AchievementStatus.IN_PROGRESS }
                    }
                }
            }

            // ── 3. Label da seção ─────────────────────────────────────
            item(key = "section_label") {
                FitVerseSpacer(
                    vertical = true,
                    value = 14.dp
                )
                FVSectionLabel("${filtered.size} conquistas")
                FitVerseSpacer(
                    vertical = true,
                    value = 4.dp
                )
            }

            // ── 4. Grid 2 colunas via chunked ─────────────────────────
            //  • Cada item do LazyColumn = uma Row com até 2 cards
            //  • Sem LazyVerticalGrid aninhado — zero altura fixa manual
            items(
                items = rows,
                key   = { pair -> pair.first().id }
            ) { pair ->
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = FVExtension.margin)
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pair.forEach { achievement ->
                        AchievementCard(
                            achievement = achievement,
                            modifier    = Modifier.weight(1f),
                            onClick     = { selected = achievement }
                        )
                    }
                    // Célula fantasma: mantém alinhamento quando a linha tem 1 só item
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }

        // ── Bottom sheet ─────────────────────────────────────────────
        // Declarado fora do LazyColumn para que o ModalBottomSheet
        // gerencie seu próprio overlay e animação corretamente
        selected?.let { achievement ->
            AchievementDetailSheet(
                achievement = achievement,
                onDismiss   = { selected = null }
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
            .clip(RoundedCornerShape(FVExtension.radius))
            .background(if (locked) Color(0xFF0D0D12) else FVExtension.surface)
            .border(
                width = 1.dp,
                color = when (achievement.status) {
                    AchievementStatus.UNLOCKED    -> rColor.copy(alpha = 0.3f)
                    AchievementStatus.IN_PROGRESS -> rColor.copy(alpha = 0.2f)
                    AchievementStatus.LOCKED      -> FVExtension.outline.copy(alpha = 0.5f)
                },
                shape = RoundedCornerShape(FVExtension.radius)
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
                text       = achievement.name,
                fontSize   = 11.sp,
                fontWeight = FontWeight.Bold,
                color      = if (locked) FVExtension.textMuted else rColor,
                textAlign  = TextAlign.Center,
                lineHeight = 14.sp
            )

            Spacer(Modifier.height(4.dp))

            if (!locked) {
                Text(
                    text       = "+${achievement.xp} XP",
                    fontSize   = 9.sp,
                    color      = FVExtension.primary,
                    fontFamily = FontFamily.Monospace
                )
            } else {
                Text(
                    text       = achievement.condition.take(26),
                    fontSize   = 9.sp,
                    color      = FVExtension.textMuted,
                    textAlign  = TextAlign.Center,
                    lineHeight = 12.sp
                )
            }

            if (inProg) {
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress   = { achievement.progress },
                    modifier   = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(FVExtension.radiusPill)),
                    color      = rColor,
                    trackColor = FVExtension.surface2
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text     = achievement.condition,
                    fontSize = 8.sp,
                    color    = rColor
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
                text       = rarityLabel(achievement.rarity),
                fontSize   = 7.sp,
                color      = rColor,
                fontWeight = FontWeight.Bold
            )
        }

        // Ícone de cadeado (top-end)
        if (locked) {
            Text(
                text     = "🔒",
                fontSize = 10.sp,
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
        containerColor   = FVExtension.surface,
        tonalElevation   = 0.dp,
        shape            = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        dragHandle       = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(FVExtension.outline)
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
                text       = achievement.name,
                fontSize   = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = FVExtension.text,
                textAlign  = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))

            Text(
                text       = achievement.desc,
                fontSize   = 14.sp,
                color      = FVExtension.textMuted,
                textAlign  = TextAlign.Center,
                lineHeight = 20.sp
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
                    text     = "Desbloqueada em ${achievement.date}",
                    fontSize = 11.sp,
                    color    = FVExtension.textMuted
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
                color   = FVExtension.textMuted,
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
            .clip(RoundedCornerShape(FVExtension.radiusPill))
            .background(rColor.copy(alpha = 0.12f))
            .border(1.dp, rColor.copy(alpha = 0.3f), RoundedCornerShape(FVExtension.radiusPill))
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
        modifier          = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(FVExtension.primary.copy(alpha = 0.08f))
            .border(1.dp, FVExtension.primary.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "⭐", fontSize = 18.sp)
        Spacer(Modifier.width(8.dp))
        Text(
            text       = "+$xp XP",
            fontSize   = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color      = FVExtension.primary,
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
            color         = FVExtension.textMuted,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text      = condition,
            fontSize  = 13.sp,
            color     = FVExtension.textDim,
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
            progress   = { progress },
            modifier   = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(FVExtension.radiusPill)),
            color      = rColor,
            trackColor = FVExtension.surface2
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
        modifier         = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(FVExtension.radiusBtn))
            .background(if (filled) color else color.copy(alpha = 0.08f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(FVExtension.radiusBtn))
            .clickable(onClick = onClick)
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = label,
            fontSize   = 14.sp,
            fontWeight = FontWeight.Bold,
            color      = if (filled) FVExtension.surface else color
        )
    }
}