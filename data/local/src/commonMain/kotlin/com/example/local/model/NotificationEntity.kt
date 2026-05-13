package com.example.local.model

data class NotificationEntity(
    val id: String,
    val userId: String,
    val type: String,        // NotificationType name
    val title: String,
    val description: String,
    val isRead: Boolean,
    val createdAt: Long,     // epoch ms
)
