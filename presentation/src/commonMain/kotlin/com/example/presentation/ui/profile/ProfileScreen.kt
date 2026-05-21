package org.fitverse.presentation.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.ui.workout.FitChip
import org.fitverse.presentation.theme.FitColors
import org.fitverse.presentation.theme.FVTypography
import org.fitverse.presentation.ui.dashboard.components.SectionHeader
import org.fitverse.presentation.widgets.FitverseTopAppBar

private data class Achievement(val emoji: String, val title: String, val subtitle: String)

private val achievements = listOf(
    Achievement("🏆", "Campeão",       "30 dias streak"),
    Achievement("💪", "Iron Will",     "100 treinos"),
    Achievement("🔥", "On Fire",       "7 dias seguidos"),
    Achievement("⚡", "Speedster",     "Faster completo"),
    Achievement("🥗", "Nutrition Pro", "30 dias meta"),
    Achievement("👑", "Elite",         "Plano Premium"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBack: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0,0,0,0),
        containerColor = Color.Transparent,
        topBar = {
            FitverseTopAppBar(
                title = "PERFIL",
                onBack = onBack,
            )
        },
        content = {
            ContentProfileScreen(
                modifier = Modifier.fillMaxSize().padding(it)
            )
        }
    )
}


@Composable
fun ContentProfileScreen(
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp)
    ) {
        item { ProfileAvatar() }
        item { StatsCard() }
        item { XpProgressCard() }
        item {
            SectionHeader(title = "CONQUISTAS")
        }

        items(achievements.chunked(3)) { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { achievement ->
                    AchievementCard(
                        emoji = achievement.emoji,
                        title = achievement.title,
                        subtitle = achievement.subtitle,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}

// ── Sub-composables privados ──────────────────
@Composable
private fun ProfileAvatar() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .border(3.dp, FitColors.Purple, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(84.dp).clip(CircleShape).background(FitColors.Surface2),
                contentAlignment = Alignment.Center
            ) { Text("⚔️", fontSize = 36.sp) }
        }

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(FitColors.AccentDim.copy(alpha = 0.25f))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) { Text(text = "LVL 23", style = FVTypography.labelLarge, color = FitColors.Accent) }

        Spacer(Modifier.height(6.dp))
        Text(text = "ALEX RIVERS",            style = FVTypography.headlineLarge, color = FitColors.TextPrimary)
        Text(text = "WARRIOR CLASS · LEVEL 23", style = FVTypography.bodySmall,     color = FitColors.TextMuted)

        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FitChip("🔥 7 Streak", Color(0xFF7C1F0F), textColor = FitColors.Orange)
            FitChip(
                "⚡ 340 XP",
                FitColors.Purple.copy(alpha = 0.3f),
                textColor = FitColors.Purple
            )
            FitChip("🏆 Top 5%", Color(0xFF5C4000), textColor = Color(0xFFFFCC00))
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun StatsCard() {
    val cs = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cs.surface)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        ProfileStat("142",   "TREINOS")
        ProfileStatDivider()
        ProfileStat("328",   "MISSÕES")
        ProfileStatDivider()
        ProfileStat("42.3k", "TOTAL XP")
    }
    Spacer(Modifier.height(12.dp))
}

@Composable
fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = FVTypography.monoLarge, color = FitColors.TextPrimary)
        Text(text = label, style = FVTypography.overline,  color = FitColors.TextMuted)
    }
}

@Composable
fun ProfileStatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(FitColors.TextMuted)
    )
}

@Composable
fun AchievementCard(
    emoji: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = cs.surface
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(emoji, fontSize = 28.sp)
            Spacer(Modifier.height(4.dp))
            Text(text = title,    style = FVTypography.titleSmall, color = FitColors.TextPrimary, textAlign = TextAlign.Center)
            Text(text = subtitle, style = FVTypography.labelSmall, color = FitColors.TextMuted,   textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun XpProgressCard() {
    val cs = MaterialTheme.colorScheme
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = cs.surface
        ),
        shape = RoundedCornerShape(14.dp)
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                Text(
                    text  = "Progresso Level 23 → 24",
                    style = FVTypography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = FitColors.TextPrimary,
                )
                Text(text = "340 / 500 XP", style = FVTypography.monoMedium, color = FitColors.Accent)
            }
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier.fillMaxWidth().height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(FitColors.Surface2)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(0.68f)
                        .fillMaxHeight().clip(RoundedCornerShape(4.dp))
                        .background(Brush.horizontalGradient(listOf(FitColors.Accent, FitColors.AccentDim)))
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(text = "160 XP para o próximo nível", style = FVTypography.bodySmall, color = FitColors.TextMuted)
        }
    }
    Spacer(Modifier.height(20.dp))
}