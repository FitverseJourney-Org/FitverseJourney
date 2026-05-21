package org.fitverse.presentation.ui.notification

import androidx.compose.ui.graphics.Color
import org.fitverse.domain.models.notification.Notification
import org.fitverse.domain.models.notification.NotificationType
import org.fitverse.domain.repository.dbLocal.sqldelight.notifications.NotificationRecord
import org.fitverse.presentation.ui.notification.helpers.NotificationStyles

data class NotificationUiModel(
    val id: String,
    val type: NotificationType,
    val title: String,
    val description: String,
    val isRead: Boolean,
    val createdAt: Long,
    // visuais — calculados no mapper
    val icon: String,
    val iconBg: Color,
    val dotColor: Color?,
    val borderAccent: Color?,
)


fun Notification.toUiModel(): NotificationUiModel {
    val style = NotificationStyles.from(type)
    return NotificationUiModel(
        id           = id,
        type         = type,
        title        = title,
        description  = description,
        isRead       = isRead,
        createdAt    = createdAt,
        icon         = style.icon,
        iconBg       = style.iconBg,
        dotColor     = if (!isRead) style.dotColor else null,
        borderAccent = if (!isRead) style.borderAccent else null,
    )
}

fun NotificationRecord.toUiModel(): NotificationUiModel {
    val notifType = runCatching { NotificationType.valueOf(type) }.getOrDefault(NotificationType.SISTEMA)
    val style     = NotificationStyles.from(notifType)
    return NotificationUiModel(
        id           = id,
        type         = notifType,
        title        = title,
        description  = description,
        isRead       = isRead,
        createdAt    = createdAt,
        icon         = style.icon,
        iconBg       = style.iconBg,
        dotColor     = if (!isRead) style.dotColor else null,
        borderAccent = if (!isRead) style.borderAccent else null,
    )
}