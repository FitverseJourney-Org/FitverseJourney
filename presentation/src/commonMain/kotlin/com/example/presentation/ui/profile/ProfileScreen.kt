package com.example.presentation.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import com.example.presentation.ui.workout.FitChip
import com.example.presentation.theme.FitverseColors
import com.example.presentation.ui.dashboard.components.SectionHeader

private data class Achievement(val emoji: String, val title: String, val subtitle: String)

private val achievements = listOf(
    Achievement("🏆", "Campeão",       "30 dias streak"),
    Achievement("💪", "Iron Will",     "100 treinos"),
    Achievement("🔥", "On Fire",       "7 dias seguidos"),
    Achievement("⚡", "Speedster",     "Faster completo"),
    Achievement("🥗", "Nutrition Pro", "30 dias meta"),
    Achievement("👑", "Elite",         "Plano Premium"),
)

@Composable
fun ProfileScreen(
    onSettingsClick: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0,0,0,0),
            containerColor = Color.Transparent,
            content = {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item { ProfileTopBar(onSettingsClick) }
                    item { ProfileAvatar() }
                    item { StatsCard() }
                    item { XpProgressCard() }
                    item {
                        SectionHeader(title = "CONQUISTAS", actionText = "TODAS")
                    }

                    items(achievements.chunked(3)) { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            row.forEach { achievement ->
                                AchievementCard(
                                    emoji    = achievement.emoji,
                                    title    = achievement.title,
                                    subtitle = achievement.subtitle,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        )
    }
}

// ── Sub-composables privados ──────────────────

@Composable
private fun ProfileTopBar(onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "PERFIL",
            color = FitverseColors.TextPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp
        )
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(FitverseColors.Surface2),
            contentAlignment = Alignment.Center
        ) { Text("⚙️", fontSize = 18.sp) }
    }
}

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
                .border(3.dp, FitverseColors.Purple, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(84.dp).clip(CircleShape).background(FitverseColors.Surface2),
                contentAlignment = Alignment.Center
            ) { Text("⚔️", fontSize = 36.sp) }
        }

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(FitverseColors.AccentDim.copy(alpha = 0.25f))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) { Text("LVL 23", color = FitverseColors.Accent, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

        Spacer(Modifier.height(6.dp))
        Text("ALEX RIVERS", color = FitverseColors.TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Black)
        Text("WARRIOR CLASS · LEVEL 23", color = FitverseColors.TextMuted, fontSize = 12.sp)

        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FitChip("🔥 7 Streak", Color(0xFF7C1F0F), textColor = FitverseColors.Orange)
            FitChip(
                "⚡ 340 XP",
                FitverseColors.Purple.copy(alpha = 0.3f),
                textColor = FitverseColors.Purple
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
        Text(value, color = FitverseColors.TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Black)
        Text(label, color = FitverseColors.TextMuted,   fontSize = 10.sp, letterSpacing = 0.5.sp)
    }
}

@Composable
fun ProfileStatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(FitverseColors.TextMuted)
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
            Text(title,    color = FitverseColors.TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            Text(subtitle, color = FitverseColors.TextMuted,   fontSize = 10.sp, textAlign = TextAlign.Center)
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
                    "Progresso Level 23 → 24",
                    color = FitverseColors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text("340 / 500 XP", color = FitverseColors.Accent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier.fillMaxWidth().height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(FitverseColors.Surface2)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(0.68f)
                        .fillMaxHeight().clip(RoundedCornerShape(4.dp))
                        .background(Brush.horizontalGradient(listOf(FitverseColors.Accent, FitverseColors.AccentDim)))
                )
            }
            Spacer(Modifier.height(8.dp))
            Text("160 XP para o próximo nível", color = FitverseColors.TextMuted, fontSize = 12.sp)
        }
    }
    Spacer(Modifier.height(20.dp))
}