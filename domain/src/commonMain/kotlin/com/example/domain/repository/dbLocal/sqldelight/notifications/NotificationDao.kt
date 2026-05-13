package com.example.domain.repository.dbLocal.sqldelight.notifications

import kotlinx.coroutines.flow.Flow

interface NotificationDao {
    fun observeNotificationsByUser(userId: String): Flow<List<NotificationRecord>>
    suspend fun getNotificationsByUser(userId: String): List<NotificationRecord>
    suspend fun getUnreadByUser(userId: String): List<NotificationRecord>
    suspend fun insertNotification(notification: NotificationRecord)
    suspend fun markAsRead(id: String, userId: String)
    suspend fun markAllAsRead(userId: String)
    suspend fun deleteNotification(id: String, userId: String)
    suspend fun deleteOldNotifications(userId: String, beforeEpoch: Long)
    suspend fun countUnread(userId: String): Long
}

data class NotificationRecord(
    val id: String,
    val userId: String,
    val type: String,
    val title: String,
    val description: String,
    val isRead: Boolean,
    val createdAt: Long,
)
