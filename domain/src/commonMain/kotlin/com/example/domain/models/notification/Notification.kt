package com.example.domain.models.notification

import androidx.compose.ui.graphics.Color

enum class NotificationType {
    XP,
    STREAK,
    CURTIDA,
    COMENTARIO,
    TREINO,
    CONQUISTA,
    DESAFIO,
    RANKING,
    SISTEMA,
}
data class NotificationStyle(
    val icon: String,
    val iconBg: Color,
    val dotColor: Color?,
    val borderAccent: Color?,
)


