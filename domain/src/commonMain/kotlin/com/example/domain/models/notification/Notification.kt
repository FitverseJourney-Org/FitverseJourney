package org.fitverse.domain.models.notification


data class Notification(
    val id: String,
    val userId: String,
    val type: NotificationType,
    val title: String,
    val description: String,
    val isRead: Boolean = false,
    val createdAt: Long,
)


