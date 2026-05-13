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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.notification.NotificationStyle
import com.example.domain.models.notification.NotificationType
import com.example.presentation.ui.notification.viewmodel.NotificationEvent
import com.example.presentation.ui.notification.viewmodel.NotificationIntent
import com.example.presentation.widgets.FitverseTopAppBar
import com.example.presentation.theme.FitverseColors
import com.example.presentation.theme.ShapeCard

// ── Models ────────────────────────────────────────────────────────────────────
data class Notification(
    val type: NotificationType,          // novo — fonte do estilo
    val title: String,
    val description: String,
    val time: String,
    val isUnread: Boolean = false,
    // visuais derivados do tipo (não precisam ser definidos manualmente)
    val icon: String        = NotificationStyles.from(type).icon,
    val iconBg: Color       = NotificationStyles.from(type).iconBg,
    val dotColor: Color?    = if (isUnread) NotificationStyles.from(type).dotColor else null,
    val borderAccent: Color? = if (isUnread) NotificationStyles.from(type).borderAccent else null,
)

object NotificationStyles {
    fun from(type: NotificationType): NotificationStyle = when (type) {
        NotificationType.XP -> NotificationStyle(
            icon        = "✨",
            iconBg      = FitverseColors.PurpleDim,
            dotColor    = FitverseColors.Purple,
            borderAccent = FitverseColors.Purple.copy(alpha = 0.30f),
        )
        NotificationType.STREAK -> NotificationStyle(
            icon        = "🔥",
            iconBg      = FitverseColors.OrangeDim,
            dotColor    = FitverseColors.Orange,
            borderAccent = FitverseColors.Orange.copy(alpha = 0.30f),
        )
        NotificationType.CURTIDA -> NotificationStyle(
            icon        = "❤️",
            iconBg      = FitverseColors.RedDim,
            dotColor    = null,
            borderAccent = null,
        )
        NotificationType.COMENTARIO -> NotificationStyle(
            icon        = "💬",
            iconBg      = FitverseColors.BlueDim,
            dotColor    = FitverseColors.Blue,
            borderAccent = FitverseColors.Blue.copy(alpha = 0.30f),
        )
        NotificationType.TREINO -> NotificationStyle(
            icon        = "💪",
            iconBg      = FitverseColors.GreenDim,
            dotColor    = null,
            borderAccent = null,
        )
        NotificationType.CONQUISTA -> NotificationStyle(
            icon        = "🏆",
            iconBg      = Color(0x1AC8FF00),
            dotColor    = null,
            borderAccent = null,
        )
        NotificationType.DESAFIO -> NotificationStyle(
            icon        = "🎯",
            iconBg      = FitverseColors.TealDim,
            dotColor    = FitverseColors.Teal,
            borderAccent = FitverseColors.Teal.copy(alpha = 0.30f),
        )
        NotificationType.RANKING -> NotificationStyle(
            icon        = "👑",
            iconBg      = FitverseColors.AmberDim,
            dotColor    = FitverseColors.Amber,
            borderAccent = FitverseColors.Amber.copy(alpha = 0.30f),
        )
        NotificationType.SISTEMA -> NotificationStyle(
            icon        = "⚙️",
            iconBg      = FitverseColors.GrayDim,
            dotColor    = null,
            borderAccent = null,
        )
    }
}
val sampleNotifications = listOf(
    Notification(
        type        = NotificationType.XP,
        title       = "XP Ganho!",
        description = "Você ganhou +30 XP pela missão de cardio.",
        time        = "2 min",
        isUnread    = true,
    ),
    Notification(
        type        = NotificationType.STREAK,
        title       = "Streak Ativo!",
        description = "Você está há 7 dias em sequência. Incrível!",
        time        = "10 min",
        isUnread    = true,
    ),
    Notification(
        type        = NotificationType.COMENTARIO,
        title       = "Max comentou seu post",
        description = "\"Que treino insano cara! Como você faz?\"",
        time        = "5 min",
        isUnread    = true,
    ),
    Notification(
        type        = NotificationType.CURTIDA,
        title       = "Luna curtiu seu post",
        description = "\"Treino de peito arrasado! 💪\"",
        time        = "1h",
    ),
    Notification(
        type        = NotificationType.TREINO,
        title       = "Hora do treino!",
        description = "Seu treino Hypertrophy A aguarda você.",
        time        = "2h",
    ),
    Notification(
        type        = NotificationType.CONQUISTA,
        title       = "Nova Conquista!",
        description = "Você desbloqueou \"Iron Will\" — 100 treinos!",
        time        = "1 dia",
    ),
    Notification(
        type        = NotificationType.DESAFIO,
        title       = "Desafio disponível!",
        description = "30 dias de treino — participe e ganhe 500 XP!",
        time        = "agora",
        isUnread    = true,
    ),
    Notification(
        type        = NotificationType.RANKING,
        title       = "Subiu no ranking!",
        description = "Você está em 5º na Liga Ouro desta semana.",
        time        = "3h",
        isUnread    = true,
    ),
    Notification(
        type        = NotificationType.SISTEMA,
        title       = "Atualização disponível",
        description = "Versão 2.4 com novidades no treino e nutrição.",
        time        = "hoje",
    ),
)

// ── Root — wires ViewModel ────────────────────────────────────────────────────

@Composable
fun NotificationRoot(
    viewModel: NotificationViewModel,
    onBack:    () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NotificationEvent.NavigateBack -> onBack()
            }
        }
    }

    NotificationScreen(
        onBack        = { viewModel.onIntent(NotificationIntent.NavigateBack) },
        notifications = uiState.notifications,
    )
}

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun NotificationScreen(
    onBack:        () -> Unit,
    notifications: List<Notification> = sampleNotifications,
) {
    Scaffold(
        containerColor = FitverseColors.Bg,
        topBar = {
            FitverseTopAppBar(
                title = "Notificações",
                onBack = onBack
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(it),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(notifications, key = { it.title }) { notif ->
                    NotificationRow(notif = notif)
                }
            }
        }
    )
}

// ── Notification row ──────────────────────────────────────────────────────────

@Composable
fun NotificationRow(
    notif: Notification,
    modifier: Modifier = Modifier,
) {
    val style = NotificationStyles.from(notif.type)
    val accentColor = style.dotColor ?: FitverseColors.TextMuted2

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)          // faz a barra esticar com o card
            .clip(ShapeCard)
            .border(0.5.dp, FitverseColors.Border, ShapeCard)
            .background(FitverseColors.Surface),
    ) {

        // ── Left accent bar ───────────────────────────────────
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(accentColor),
        )

        // ── Icon + Content ────────────────────────────────────
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(13.dp),
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text       = notif.title,
                        fontSize   = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        color      = FitverseColors.TextPrimary,
                        modifier   = Modifier.weight(1f),
                    )
                    Spacer(Modifier.width(6.dp))

                    // Dot — apenas unread
                    if (notif.isUnread && style.dotColor != null) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(accentColor),
                        )
                        Spacer(Modifier.width(4.dp))
                    }

                    Text(
                        text     = notif.time,
                        fontSize = 11.sp,
                        color    = FitverseColors.TextMuted2,
                    )
                }

                Spacer(Modifier.height(3.dp))

                Text(
                    text       = notif.description,
                    fontSize   = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color      = FitverseColors.TextMuted,
                    lineHeight = 17.sp,
                )

                Spacer(Modifier.height(8.dp))

                NotificationTypeChip(notif.type, style)
            }
        }
    }
}

// ── Type chip ─────────────────────────────────────────────────────────────────

@Composable
private fun NotificationTypeChip(
    type: NotificationType,
    style: NotificationStyle,
) {
    val label = when (type) {
        NotificationType.XP          -> "#xp"
        NotificationType.STREAK      -> "#streak"
        NotificationType.CURTIDA     -> "#curtida"
        NotificationType.COMENTARIO  -> "#comentário"
        NotificationType.TREINO      -> "#treino"
        NotificationType.CONQUISTA   -> "#conquista"
        NotificationType.DESAFIO     -> "#desafio"
        NotificationType.RANKING     -> "#ranking"
        NotificationType.SISTEMA     -> "#sistema"
    }

    val chipColor = style.dotColor ?: FitverseColors.TextMuted2

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(chipColor.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(
            text       = label,
            fontSize   = 10.sp,
            color      = chipColor,
            fontWeight = FontWeight.Medium,
        )
    }
}