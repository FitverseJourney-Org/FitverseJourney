package org.fitverse.presentation.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoFixHigh
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Construction
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.ui.community.viewmodel.GroupHomeEvent
import org.fitverse.presentation.ui.community.viewmodel.GroupHomeIntent
import org.fitverse.presentation.ui.community.viewmodel.GroupHomeUiState
import org.fitverse.presentation.ui.community.viewmodel.GroupHomeViewModel
import org.fitverse.presentation.ui.community.viewmodel.GroupTab
import org.fitverse.presentation.widgets.FitverseTopAppBar

// ── Data Models ───────────────────────────────────────────────────────────────

enum class MissionType { FIXED, SURPRISE }

data class GroupMission(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val type: MissionType,
    val progress: Float,
    val rewardXp: Int,
    val isCompleted: Boolean = false,
    val changeTime: String? = null,
)

data class GroupSeason(
    val name: String,
    val daysLeft: Int,
    val currentXp: Int,
    val targetXp: Int,
    val milestones: List<Int>,
)

val mockFixedMissions = listOf(
    GroupMission("m1", "Treino de força",       Icons.Rounded.FitnessCenter, MissionType.FIXED, 1.0f, 180, true),
    GroupMission("m2", "Registrar alimentação", Icons.Rounded.Restaurant,    MissionType.FIXED, 1.0f, 120, true),
    GroupMission("m3", "30 min de cardio",      Icons.Rounded.DirectionsRun, MissionType.FIXED, 0.5f, 150, false),
)

val mockSurpriseMission = GroupMission(
    id          = "ms1",
    title       = "100 agachamentos",
    icon        = Icons.Rounded.AutoFixHigh,
    type        = MissionType.SURPRISE,
    progress    = 0.0f,
    rewardXp    = 250,
    isCompleted = false,
    changeTime  = "11h 48min",
)

// ── Group post model ──────────────────────────────────────────────────────────

data class GroupPost(
    val id: String,
    val authorName: String,
    val authorInitials: String,
    val authorColor: Color,
    val timeAgo: String,
    val content: String,
    val tag: String,
    val tagColor: Color,
    val xpGained: String,
    val likes: Int,
    val comments: Int,
    val isLiked: Boolean = false,
    val hasImage: Boolean = false,
)

val mockGroupPosts = listOf(
    GroupPost(
        id             = "p1",
        authorName     = "Luna Fitness",
        authorInitials = "LF",
        authorColor    = FitColors.Accent,
        timeAgo        = "12min atrás",
        content        = "Treino de pernas destruído! +180 XP ganhos hoje. Quem mais tá na missão de legs? Vamos juntos! 💪",
        tag            = "TREINO",
        tagColor       = FitColors.Accent,
        xpGained       = "+180XP",
        likes          = 34,
        comments       = 8,
    ),
    GroupPost(
        id             = "p2",
        authorName     = "Pedro Strong",
        authorInitials = "PS",
        authorColor    = FitColors.Orange,
        timeAgo        = "45min atrás",
        content        = "Semana 4 do programa concluída! Sensação incrível de evolução. O grupo me motiva demais!",
        tag            = "CONQUISTA",
        tagColor       = FitColors.Amber,
        xpGained       = "+120XP",
        likes          = 22,
        comments       = 5,
        hasImage       = true,
    ),
    GroupPost(
        id             = "p3",
        authorName     = "Sofia Healthy",
        authorInitials = "SH",
        authorColor    = FitColors.Blue,
        timeAgo        = "2h atrás",
        content        = "Café da manhã pré-treino: proteínas, carbos complexos e muita água. Prontos pro dia! 🥗",
        tag            = "NUTRIÇÃO",
        tagColor       = FitColors.Purple,
        xpGained       = "+48XP",
        likes          = 15,
        comments       = 3,
        hasImage       = true,
    ),
    GroupPost(
        id             = "p4",
        authorName     = "Marcos Power",
        authorInitials = "MP",
        authorColor    = FitColors.Purple,
        timeAgo        = "4h atrás",
        content        = "Novo recorde pessoal no supino: 120kg! Level UP alcançado no grupo! Que semana incrível. 🏆",
        tag            = "RECORDE",
        tagColor       = FitColors.Teal,
        xpGained       = "+250XP",
        likes          = 56,
        comments       = 18,
        isLiked        = true,
    ),
)

// ── Root ──────────────────────────────────────────────────────────────────────

@Composable
fun GroupHomeRoot(
    groupName:    String,
    viewModel:    GroupHomeViewModel,
    toBack:       () -> Unit,
    onPostTapped: (GroupPost) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(groupName) { viewModel.setGroupName(groupName) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                GroupHomeEvent.NavigateBack -> toBack()
            }
        }
    }

    GroupHomeScreen(
        uiState      = uiState,
        onBack       = viewModel::navigateBack,
        onIntent     = viewModel::onIntent,
        onPostTapped = onPostTapped,
    )
}

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun GroupHomeScreen(
    uiState:      GroupHomeUiState          = GroupHomeUiState(),
    onBack:       () -> Unit                = {},
    onIntent:     (GroupHomeIntent) -> Unit = {},
    onPostTapped: (GroupPost) -> Unit       = {},
) {
    Scaffold(
        modifier            = Modifier.fillMaxSize(),
        containerColor      = FitColors.Bg,
        topBar = {
            GroupTopBar(
                groupName = uiState.groupName,
                onBack    = onBack,
                modifier  = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) {
        LazyColumn(
            modifier       = Modifier.fillMaxSize().padding(it),
            contentPadding = PaddingValues(bottom = 40.dp, top = 20.dp),
        ) {
            // ── Season card ───────────────────────────────────────────
            item {
                SeasonCard(
                    season   = uiState.season,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            item { Spacer(Modifier.height(16.dp)) }

            // ── Tab row ───────────────────────────────────────────────
            item {
                GroupTabRow(
                    selectedTab   = uiState.selectedTab,
                    onTabSelected = { onIntent(GroupHomeIntent.SelectTab(it)) },
                )
            }

            item { Spacer(Modifier.height(16.dp)) }

            // ── Tab content ───────────────────────────────────────────
            when (uiState.selectedTab) {
                GroupTab.MISSIONS -> {
                    item {
                        TodayXpCard(
                            xp          = uiState.todayXp,
                            completed   = uiState.completedMissions,
                            total       = uiState.totalMissions,
                            xpCollected = uiState.xpCollected,
                            onCollect   = { onIntent(GroupHomeIntent.CollectXp) },
                            modifier    = Modifier.padding(horizontal = 16.dp),
                        )
                    }

                    item { Spacer(Modifier.height(22.dp)) }

                    item {
                        MissionsSectionHeader(
                            title      = "MISSÕES FIXAS — HOJE",
                            badge      = "Reset em ${uiState.resetTime}",
                            badgeColor = FitColors.Orange,
                            modifier   = Modifier.padding(horizontal = 16.dp),
                        )
                    }

                    item { Spacer(Modifier.height(10.dp)) }

                    items(uiState.fixedMissions) { mission ->
                        FixedMissionCard(
                            mission  = mission,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        )
                    }

                    item { Spacer(Modifier.height(22.dp)) }

                    item {
                        MissionsSectionHeader(
                            title      = "MISSÃO SURPRESA",
                            badge      = "Rotativa diária",
                            badgeColor = FitColors.Purple,
                            modifier   = Modifier.padding(horizontal = 16.dp),
                        )
                    }

                    item { Spacer(Modifier.height(10.dp)) }

                    uiState.surpriseMission?.let { surprise ->
                        item {
                            SurpriseMissionCard(
                                mission  = surprise,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                            )
                        }
                    }

                    item { Spacer(Modifier.height(22.dp)) }

                    item {
                        ParticipationCard(
                            percent       = uiState.participationPercent,
                            activeMembers = uiState.activeMembers,
                            totalMembers  = uiState.totalMembers,
                            modifier      = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                }

                GroupTab.POSTS -> {
                    item {
                        PostsTabHeader(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                    item { Spacer(Modifier.height(12.dp)) }
                    items(uiState.groupPosts, key = { it.id }) { post ->
                        GroupPostCard(
                            post     = post,
                            onLike   = { onIntent(GroupHomeIntent.ToggleLike(post.id)) },
                            onClick  = { onPostTapped(post) },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
                        )
                    }
                }

                else -> {
                    item {
                        PlaceholderTab(tab = uiState.selectedTab)
                    }
                }
            }
        }
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
private fun GroupTopBar(
    groupName: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        Row(
            modifier             = modifier,
            verticalAlignment    = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(FitColors.Surface2)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = "Voltar",
                    tint               = FitColors.TextPrimary,
                    modifier           = Modifier.size(20.dp),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "MINHA COMUNIDADE",
                    color         = FitColors.Accent,
                    fontSize      = 10.sp,
                    fontWeight    = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                )
                Text(
                    groupName,
                    color      = FitColors.TextPrimary,
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Black,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis,
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TopBarIconButton(icon = Icons.Rounded.Notifications, onClick = {})
                TopBarIconButton(icon = Icons.Rounded.MoreVert,      onClick = {})
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF1C1C1C).copy(alpha = .75f),
                            Color(0xFF1C1C1C),
                            Color(0xFF1C1C1C).copy(alpha = .75f)
                        )
                    )
                )
        )
    }
}

@Composable
private fun TopBarIconButton(icon: ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(FitColors.Surface2)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = FitColors.TextMuted, modifier = Modifier.size(20.dp))
    }
}

// ── Season card ───────────────────────────────────────────────────────────────

@Composable
private fun SeasonCard(season: GroupSeason, modifier: Modifier = Modifier) {
    val progress        = (season.currentXp.toFloat() / season.targetXp.toFloat()).coerceIn(0f, 1f)
    val progressPercent = (progress * 100).toInt()
    val passedCount     = season.milestones.count { season.currentXp >= it }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, FitColors.Accent.copy(alpha = 0.20f), RoundedCornerShape(20.dp))
            .background(FitColors.Surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // Header: season name + days left
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(FitColors.AccentDim),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Rounded.Link, contentDescription = null, tint = FitColors.Accent, modifier = Modifier.size(16.dp))
            }
            Spacer(Modifier.width(10.dp))
            Text(
                season.name,
                color      = FitColors.TextPrimary,
                fontSize   = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier   = Modifier.weight(1f),
            )
            Spacer(Modifier.width(8.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(FitColors.OrangeDim)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment    = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(Icons.Rounded.AccessTime, contentDescription = null, tint = FitColors.Orange, modifier = Modifier.size(11.dp))
                Text("${season.daysLeft} dias", color = FitColors.Orange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        // XP numbers + progress bar
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Text(
                    "${formatXp(season.currentXp)}     ${formatXp(season.targetXp)} XP",
                    color    = FitColors.TextMuted,
                    fontSize = 11.sp,
                )
                Text(
                    "$progressPercent%",
                    color      = FitColors.Accent,
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(FitColors.Surface2),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(FitColors.Accent.copy(alpha = 0.6f), FitColors.Accent)
                            )
                        )
                )
            }
        }

        // Milestones
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            season.milestones.forEachIndexed { idx, milestone ->
                val isPassed  = idx < passedCount
                val isCurrent = idx == passedCount
                MilestoneLabel(
                    label     = formatXpShort(milestone),
                    isPassed  = isPassed,
                    isCurrent = isCurrent,
                )
            }
        }
    }
}

@Composable
private fun MilestoneLabel(label: String, isPassed: Boolean, isCurrent: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(if (isCurrent) 10.dp else 8.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isPassed  -> FitColors.Accent
                        isCurrent -> FitColors.TextPrimary
                        else      -> FitColors.Surface3
                    }
                )
        )
        Text(
            label,
            color      = when {
                isPassed  -> FitColors.Accent
                isCurrent -> FitColors.TextPrimary
                else      -> FitColors.TextMuted
            },
            fontSize   = 9.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
        )
    }
}

// ── Tab row ───────────────────────────────────────────────────────────────────

@Composable
private fun GroupTabRow(
    selectedTab: GroupTab,
    onTabSelected: (GroupTab) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            GroupTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication        = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick           = { onTabSelected(tab) },
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        tab.label,
                        color         = if (isSelected) FitColors.Accent else FitColors.TextMuted,
                        fontSize      = 12.sp,
                        fontWeight    = if (isSelected) FontWeight.Black else FontWeight.Medium,
                        letterSpacing = 0.5.sp,
                        modifier      = Modifier.padding(vertical = 10.dp),
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(if (isSelected) FitColors.Accent else FitColors.Surface2),
                    )
                }
            }
        }
    }
}

// ── Today XP card ─────────────────────────────────────────────────────────────

@Composable
private fun TodayXpCard(
    xp: Int,
    completed: Int,
    total: Int,
    xpCollected: Boolean,
    onCollect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, FitColors.Surface3, RoundedCornerShape(20.dp))
            .background(FitColors.Surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            "MEU XP HOJE",
            color         = FitColors.TextMuted,
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Bold,
            letterSpacing = 1.2.sp,
        )
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "+$xp XP",
                color      = FitColors.Accent,
                fontSize   = 38.sp,
                fontWeight = FontWeight.Black,
                modifier   = Modifier.weight(1f),
            )
            OutlinedButton(
                onClick  = onCollect,
                enabled  = !xpCollected,
                shape    = RoundedCornerShape(12.dp),
                modifier = Modifier.height(44.dp),
                colors   = ButtonDefaults.outlinedButtonColors(
                    contentColor         = FitColors.TextPrimary,
                    disabledContentColor = FitColors.TextDisabled,
                ),
            ) {
                Text(
                    if (xpCollected) "Coletado" else "Coletar",
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
        Text(
            "$completed de $total missões concluídas",
            color    = FitColors.TextMuted,
            fontSize = 12.sp,
        )
    }
}

// ── Section header ────────────────────────────────────────────────────────────

@Composable
private fun MissionsSectionHeader(
    title: String,
    badge: String,
    badgeColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier          = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            title,
            color         = FitColors.TextMuted,
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            modifier      = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(badgeColor.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 3.dp),
        ) {
            Text(badge, color = badgeColor, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ── Fixed mission card ────────────────────────────────────────────────────────

@Composable
private fun FixedMissionCard(mission: GroupMission, modifier: Modifier = Modifier) {
    val barColor = if (mission.isCompleted) FitColors.Green else FitColors.Accent

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, FitColors.Outline, RoundedCornerShape(16.dp))
            .background(FitColors.Surface)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(barColor.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(mission.icon, contentDescription = null, tint = barColor, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(10.dp))
            Text(
                mission.title,
                color      = FitColors.TextPrimary,
                fontSize   = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier   = Modifier.weight(1f),
            )
            Spacer(Modifier.width(8.dp))
            // FIXA badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(FitColors.GreenDim)
                    .padding(horizontal = 6.dp, vertical = 3.dp),
            ) {
                Text("FIXA", color = FitColors.Green, fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
            }
            Spacer(Modifier.width(8.dp))
            // Completion indicator
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (mission.isCompleted) FitColors.Green else Color.Transparent)
                    .border(1.5.dp, if (mission.isCompleted) FitColors.Green else FitColors.Outline, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                if (mission.isCompleted) {
                    Icon(Icons.Rounded.Check, contentDescription = null, tint = FitColors.Bg, modifier = Modifier.size(14.dp))
                }
            }
        }

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(FitColors.Surface2),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(mission.progress.coerceIn(0f, 1f))
                    .clip(RoundedCornerShape(2.dp))
                    .background(barColor)
            )
        }

        Text("Recompensa: +${mission.rewardXp} XP", color = FitColors.TextMuted, fontSize = 11.sp)
    }
}

// ── Surprise mission card ─────────────────────────────────────────────────────

@Composable
private fun SurpriseMissionCard(mission: GroupMission, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, FitColors.Purple.copy(alpha = 0.30f), RoundedCornerShape(16.dp))
            .background(FitColors.PurpleDark.copy(alpha = 0.40f))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(FitColors.PurpleDim),
                contentAlignment = Alignment.Center,
            ) {
                Icon(mission.icon, contentDescription = null, tint = FitColors.Purple, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(10.dp))
            Text(
                mission.title,
                color      = FitColors.TextPrimary,
                fontSize   = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier   = Modifier.weight(1f),
            )
            Spacer(Modifier.width(8.dp))
            // SURPRESA badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(FitColors.PurpleDim)
                    .padding(horizontal = 6.dp, vertical = 3.dp),
            ) {
                Text("SURPRESA", color = FitColors.Purple, fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
            }
            Spacer(Modifier.width(8.dp))
            // Empty completion circle
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, FitColors.Outline, CircleShape),
            )
        }

        // Progress bar (0%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(FitColors.Surface2),
        ) {
            if (mission.progress > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(mission.progress.coerceIn(0f, 1f))
                        .clip(RoundedCornerShape(2.dp))
                        .background(FitColors.Purple)
                )
            }
        }

        // Footer: bonus XP + change time
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Bônus: +${mission.rewardXp} XP", color = FitColors.Purple, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.weight(1f))
            mission.changeTime?.let { time ->
                Row(
                    verticalAlignment    = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(Icons.Rounded.AccessTime, contentDescription = null, tint = FitColors.TextMuted, modifier = Modifier.size(11.dp))
                    Text("Muda em $time", color = FitColors.TextMuted, fontSize = 10.sp)
                }
            }
        }
    }
}

// ── Participation card ────────────────────────────────────────────────────────

@Composable
private fun ParticipationCard(
    percent: Int,
    activeMembers: Int,
    totalMembers: Int,
    modifier: Modifier = Modifier,
) {
    val avatarColors = listOf(FitColors.Accent, FitColors.Purple, FitColors.Blue, FitColors.Orange, FitColors.Green)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, FitColors.Outline, RoundedCornerShape(16.dp))
            .background(FitColors.Surface)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Stacked avatar circles
        Box(modifier = Modifier.width(72.dp).height(28.dp)) {
            avatarColors.forEachIndexed { idx, color ->
                Box(
                    modifier = Modifier
                        .offset(x = (idx * 14).dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.25f))
                        .border(1.5.dp, FitColors.Surface, CircleShape),
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("Participação hoje", color = FitColors.TextMuted, fontSize = 11.sp)
            Text("$activeMembers de $totalMembers membros ativos", color = FitColors.TextDisabled, fontSize = 10.sp)
        }
        Text(
            "$percent%",
            color      = FitColors.Accent,
            fontSize   = 26.sp,
            fontWeight = FontWeight.Black,
        )
    }
}

// ── Posts tab ─────────────────────────────────────────────────────────────────

@Composable
private fun PostsTabHeader(modifier: Modifier = Modifier) {
    Row(
        modifier          = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "POSTS DO GRUPO",
            color         = FitColors.TextMuted,
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            modifier      = Modifier.weight(1f),
        )
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(FitColors.AccentDim)
                .clickable(onClick = {})
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment    = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(Icons.Rounded.Add, contentDescription = null, tint = FitColors.Accent, modifier = Modifier.size(12.dp))
            Text("PUBLICAR", color = FitColors.Accent, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
        }
    }
}

@Composable
private fun GroupPostCard(
    post:     GroupPost,
    onLike:   () -> Unit,
    onClick:  () -> Unit   = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, FitColors.Outline, RoundedCornerShape(20.dp))
            .background(FitColors.Surface)
            .clickable(onClick = onClick),
    ) {
        // Accent top strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            post.tagColor.copy(alpha = 0.5f),
                            post.tagColor,
                            post.tagColor.copy(alpha = 0.5f),
                            Color.Transparent,
                        )
                    )
                )
        )

        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            // ── Author row ────────────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(post.authorColor.copy(alpha = 0.12f))
                        .border(1.dp, post.authorColor.copy(alpha = 0.30f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        post.authorInitials,
                        color      = post.authorColor,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Black,
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(post.authorName, color = FitColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(post.timeAgo,    color = FitColors.TextDisabled, fontSize = 11.sp)
                }
                // Tag badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(post.tagColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text(
                        post.tag,
                        color         = post.tagColor,
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.Black,
                        letterSpacing = 0.5.sp,
                    )
                }
            }

            // ── Content ───────────────────────────────────────────────
            Text(
                post.content,
                color      = FitColors.TextPrimary.copy(alpha = 0.9f),
                fontSize   = 14.sp,
                lineHeight = 21.sp,
            )

            // ── Photo placeholder ─────────────────────────────────────
            if (post.hasImage) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    post.tagColor.copy(alpha = 0.20f),
                                    post.authorColor.copy(alpha = 0.10f),
                                )
                            )
                        )
                        .border(1.dp, post.tagColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Icon(
                            Icons.Rounded.CameraAlt,
                            contentDescription = null,
                            tint     = post.tagColor.copy(alpha = 0.55f),
                            modifier = Modifier.size(28.dp),
                        )
                        Text("Foto do treino", color = post.tagColor.copy(alpha = 0.55f), fontSize = 10.sp)
                    }
                }
            }

            // ── Footer: actions + XP ──────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                // Like
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onLike)
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment    = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        imageVector        = if (post.isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Curtir",
                        tint               = if (post.isLiked) FitColors.Red else FitColors.TextMuted,
                        modifier           = Modifier.size(16.dp),
                    )
                    Text(
                        "${post.likes}",
                        color      = if (post.isLiked) FitColors.Red else FitColors.TextMuted,
                        fontSize   = 13.sp,
                        fontWeight = if (post.isLiked) FontWeight.SemiBold else FontWeight.Normal,
                    )
                }

                Spacer(Modifier.width(4.dp))

                // Comment
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = {})
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment    = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(Icons.Rounded.Forum, contentDescription = "Comentar", tint = FitColors.TextMuted, modifier = Modifier.size(15.dp))
                    Text("${post.comments}", color = FitColors.TextMuted, fontSize = 13.sp)
                }

                Spacer(Modifier.width(4.dp))

                // Share
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = {})
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                ) {
                    Icon(Icons.Rounded.Share, contentDescription = "Compartilhar", tint = FitColors.TextMuted, modifier = Modifier.size(15.dp))
                }

                Spacer(Modifier.weight(1f))

                // XP badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FitColors.AccentDim.copy(alpha = 0.20f))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment    = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(Icons.Rounded.EmojiEvents, contentDescription = null, tint = FitColors.Accent, modifier = Modifier.size(13.dp))
                    Text(post.xpGained, color = FitColors.Accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ── Placeholder tabs ──────────────────────────────────────────────────────────

@Composable
private fun PlaceholderTab(tab: GroupTab) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Rounded.Construction, contentDescription = null, tint = FitColors.TextDisabled, modifier = Modifier.size(36.dp))
        Spacer(Modifier.height(10.dp))
        Text(tab.label, color = FitColors.TextDisabled, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Em breve", color = FitColors.TextDisabled.copy(alpha = 0.6f), fontSize = 12.sp)
    }
}

// ── Utilities ─────────────────────────────────────────────────────────────────

private fun formatXp(xp: Int): String {
    val str = xp.toString()
    return buildString {
        str.reversed().forEachIndexed { i, c ->
            if (i > 0 && i % 3 == 0) append(".")
            append(c)
        }
    }.reversed()
}

private fun formatXpShort(xp: Int): String = if (xp >= 1000) "${xp / 1000}k" else xp.toString()
