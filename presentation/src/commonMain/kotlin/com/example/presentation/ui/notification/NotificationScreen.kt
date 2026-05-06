@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.presentation.ui.notification


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.widgets.FitverseTopAppBar
import com.example.presentation.theme.FitverseColors
import com.example.presentation.theme.ShapeCard

// ── Models ────────────────────────────────────────────────────────────────────

data class Notification(
    val icon: String,
    val iconBg: Color,
    val title: String,
    val description: String,
    val time: String,
    val isUnread: Boolean = false,
    val dotColor: Color? = null,
    val borderAccent: Color? = null,
)

private val sampleNotifications = listOf(
    Notification(
        icon        = "✨",
        iconBg      = FitverseColors.PurpleDim,
        title       = "XP Ganho!",
        description = "Você ganhou +30 XP pela missão de cardio.",
        time        = "2 min",
        isUnread    = true,
        dotColor    = FitverseColors.Purple,
        borderAccent = Color(0x4D9D6FFF),
    ),
    Notification(
        icon        = "🔥",
        iconBg      = FitverseColors.OrangeDim,
        title       = "Streak Ativo!",
        description = "Você está há 7 dias em sequência. Incrível!",
        time        = "10 min",
        isUnread    = true,
        dotColor    = FitverseColors.Orange,
        borderAccent = Color(0x4DFF6B35),
    ),
    Notification(
        icon        = "❤️",
        iconBg      = FitverseColors.RedDim,
        title       = "Luna curtiu seu post",
        description = "\"Treino de peito arrasado! 💪\"",
        time        = "1h",
    ),
    Notification(
        icon        = "💪",
        iconBg      = FitverseColors.GreenDim,
        title       = "Hora do treino!",
        description = "Seu treino Hypertrophy A aguarda você.",
        time        = "2h",
    ),
    Notification(
        icon        = "🏆",
        iconBg      = Color(0x1AC8FF00),
        title       = "Nova Conquista!",
        description = "Você desbloqueou \"Iron Will\" — 100 treinos!",
        time        = "1 dia",
    ),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun NotificationScreen(onBack: () -> Unit) {
    val unreadCount = sampleNotifications.count { it.isUnread }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FitverseColors.Bg),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    ) {
        item {
            FitverseTopAppBar(
                title = "Notificações",
                onBack = onBack
            )

            // Title row with unread badge
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Text(
                    text       = "NOTIFICAÇÕES",
                    style      = MaterialTheme.typography.displayLarge,
                    color      = FitverseColors.TextPrimary,
                )
                if (unreadCount > 0) {
                    UnreadBadge(count = unreadCount)
                }
            }

            Spacer(Modifier.height(16.dp))
        }

        items(sampleNotifications, key = { it.title }) { notif ->
            NotificationRow(
                notif    = notif,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        item { Spacer(Modifier.height(24.dp)) }
    }
}

// ── Unread badge ──────────────────────────────────────────────────────────────

@Composable
private fun UnreadBadge(count: Int) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(FitverseColors.Orange)
            .padding(horizontal = 12.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text       = "$count novas",
            fontSize   = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color      = Color.White,
            letterSpacing = 0.3.sp,
        )
    }
}

// ── Notification row ──────────────────────────────────────────────────────────

@Composable
private fun NotificationRow(
    notif: Notification,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width  = 1.dp,
                color  = notif.borderAccent ?: FitverseColors.Border,
                shape  = ShapeCard,
            )
            .clip(ShapeCard)
            .background(FitverseColors.Surface)
            .padding(13.dp),
    ) {
        Row(
            verticalAlignment = Alignment.Top,
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(notif.iconBg),
                contentAlignment = Alignment.Center,
            ) {
                Text(notif.icon, fontSize = 20.sp)
            }

            Spacer(Modifier.width(12.dp))

            // Content
            Column(Modifier.weight(1f)) {
                Text(
                    text       = notif.title,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = FitverseColors.TextPrimary,
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text       = notif.description,
                    fontSize   = 12.sp,
                    color      = FitverseColors.TextMuted,
                    lineHeight = 17.sp,
                )
            }

            Spacer(Modifier.width(8.dp))
            Text(
                text  = notif.time,
                fontSize = 11.sp,
                color = FitverseColors.TextMuted2,
            )
        }

        // Unread dot
        if (notif.dotColor != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(notif.dotColor),
            )
        }
    }
}
