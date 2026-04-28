package com.example.presentation.screens.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.FitverseColors
import androidx.compose.ui.graphics.Color


data class Post(
    val emoji: String,
    val user: String,
    val time: String,
    val tag: String,
    val tagColor: Color,
    val content: String,
    val likes: Int,
    val comments: Int,
    val xp: String
)

// Dados de exemplo — substituir por ViewModel + Flow em produção
private val samplePosts = listOf(
    Post("🧑", "Luna Fitness",  "12min atrás", "TREINO",    FitverseColors.Accent,
        "Treino de pernas destruído! 💪 +180 XP ganhos hoje. Quem mais tá na missão de legs?", 34, 8,  "+180XP"),
    Post("👨", "Marcos Power",  "1h atrás",    "CONQUISTA", FitverseColors.Purple,
        "Novo recorde pessoal no supino: 120kg! 🏆 Level UP alcançado!", 72, 21, "+250XP"),
    Post("👩", "Sofia Healthy", "3h atrás",    "NUTRIÇÃO",  FitverseColors.Blue,
        "Café da manhã pré-treino 🥗 Proteínas, carbos complexos e muita água. Prontos pro dia!", 28, 5, "+48XP"),
    Post("🧔", "Pedro Strong",  "5h atrás",    "TREINO",    FitverseColors.Accent,
        "Semana 4 do programa concluída! Sensação incrível de evolução 🔥", 55, 14, "+120XP"),
)

private val stories = listOf(
    "Você" to true,
    "Luna"   to false,
    "Marcos" to false,
    "Sofia"  to false,
    "Pedro"  to false,
)

@Composable
fun CommunityScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Text(
                "COMUNIDADE",
                color = FitverseColors.TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)
            )
        }

        item { StoriesRow() }

        items(samplePosts) { post ->
            PostCard(post)
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun PostCard(post: Post) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
            .border(width = 1.dp, color = Color(0xFF2a2a35), shape = RoundedCornerShape(16.dp))
            .fillMaxWidth().clip(RoundedCornerShape(16.dp))
            .background(cs.surface).padding(16.dp)
    ) {
        // Header: avatar + user info + tag
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(FitverseColors.Surface2),
                contentAlignment = Alignment.Center
            ) { Text(post.emoji, fontSize = 20.sp) }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(post.user, color = FitverseColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(post.time, color = FitverseColors.TextMuted,   fontSize = 11.sp)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(post.tagColor.copy(alpha = 0.2f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) { Text(post.tag, color = post.tagColor, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
        }

        // Content
        Spacer(Modifier.height(12.dp))
        Text(
            post.content,
            color = FitverseColors.TextPrimary.copy(alpha = 0.9f),
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        // Footer: likes + comments + xp
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PostStat(icon = "♥", count = post.likes,    iconSize = 16)
                PostStat(icon = "💬", count = post.comments, iconSize = 14)
            }
            Spacer(Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(FitverseColors.AccentDim.copy(alpha = 0.2f))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) { Text(post.xp, color = FitverseColors.Accent, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun PostStat(icon: String, count: Int, iconSize: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(icon,   color = FitverseColors.TextMuted, fontSize = iconSize.sp)
        Text("$count", color = FitverseColors.TextMuted, fontSize = 13.sp)
    }
}
@Composable
private fun StoriesRow() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(stories) { (name, isMe) ->
            StoryAvatar(name = name, isMe = isMe)
        }
    }
    Spacer(Modifier.height(20.dp))
}

@Composable
private fun StoryAvatar(name: String, isMe: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .border(2.dp, if (isMe) FitverseColors.Accent else FitverseColors.TextMuted, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(FitverseColors.Surface2),
                contentAlignment = Alignment.Center
            ) { Text(if (isMe) "🧑" else "👤", fontSize = 22.sp) }
        }
        Spacer(Modifier.height(4.dp))
        Text(name, color = FitverseColors.TextMuted, fontSize = 11.sp)
    }
}