package org.fitverse.presentation.ui.notification.helpers

import androidx.compose.ui.graphics.Color
import org.fitverse.domain.models.notification.NotificationType
import org.fitverse.presentation.theme.FitColors

data class NotificationStyle(
    val icon: String,
    val iconBg: Color,
    val dotColor: Color?,
    val borderAccent: Color?,
)

object NotificationStyles {
    fun from(type: NotificationType): NotificationStyle = when (type) {
        NotificationType.XP -> NotificationStyle(
            icon        = "✨",
            iconBg      = FitColors.PurpleDim,
            dotColor    = FitColors.Purple,
            borderAccent = FitColors.Purple.copy(alpha = 0.30f),
        )
        NotificationType.STREAK -> NotificationStyle(
            icon        = "🔥",
            iconBg      = FitColors.OrangeDim,
            dotColor    = FitColors.Orange,
            borderAccent = FitColors.Orange.copy(alpha = 0.30f),
        )
        NotificationType.CURTIDA -> NotificationStyle(
            icon        = "❤️",
            iconBg      = FitColors.RedDim,
            dotColor    = null,
            borderAccent = null,
        )
        NotificationType.COMENTARIO -> NotificationStyle(
            icon        = "💬",
            iconBg      = FitColors.BlueDim,
            dotColor    = FitColors.Blue,
            borderAccent = FitColors.Blue.copy(alpha = 0.30f),
        )
        NotificationType.TREINO -> NotificationStyle(
            icon        = "💪",
            iconBg      = FitColors.GreenDim,
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
            iconBg      = FitColors.TealDim,
            dotColor    = FitColors.Teal,
            borderAccent = FitColors.Teal.copy(alpha = 0.30f),
        )
        NotificationType.RANKING -> NotificationStyle(
            icon        = "👑",
            iconBg      = FitColors.AmberDim,
            dotColor    = FitColors.Amber,
            borderAccent = FitColors.Amber.copy(alpha = 0.30f),
        )
        NotificationType.SISTEMA -> NotificationStyle(
            icon        = "⚙️",
            iconBg      = FitColors.GrayDim,
            dotColor    = null,
            borderAccent = null,
        )
    }
}