package com.example.local.datasource.notifications

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.domain.repository.dbLocal.sqldelight.notifications.NotificationDao
import com.example.domain.repository.dbLocal.sqldelight.notifications.NotificationRecord
import com.journey.database.AppDatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NotificationDaoImpl(database: AppDatabase) : NotificationDao {

    private val queries = database.appDatabaseQueries

    override fun observeNotificationsByUser(userId: String): Flow<List<NotificationRecord>> =
        queries.selectNotificationsByUser(userId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list -> list.map { it.toRecord() } }

    override suspend fun getNotificationsByUser(userId: String): List<NotificationRecord> =
        withContext(Dispatchers.IO) {
            queries.selectNotificationsByUser(userId).executeAsList().map { it.toRecord() }
        }

    override suspend fun getUnreadByUser(userId: String): List<NotificationRecord> =
        withContext(Dispatchers.IO) {
            queries.selectUnreadByUser(userId).executeAsList().map { it.toRecord() }
        }

    override suspend fun insertNotification(notification: NotificationRecord): Unit =
        withContext(Dispatchers.IO) {
            queries.insertNotification(
                id          = notification.id,
                userId      = notification.userId,
                type        = notification.type,
                title       = notification.title,
                description = notification.description,
                isRead      = if (notification.isRead) 1L else 0L,
                createdAt   = notification.createdAt,
            )
        }

    override suspend fun markAsRead(id: String, userId: String): Unit =
        withContext(Dispatchers.IO) { queries.markAsRead(id = id, userId = userId) }

    override suspend fun markAllAsRead(userId: String): Unit =
        withContext(Dispatchers.IO) { queries.markAllAsRead(userId) }

    override suspend fun deleteNotification(id: String, userId: String): Unit =
        withContext(Dispatchers.IO) { queries.deleteNotification(id = id, userId = userId) }

    override suspend fun deleteOldNotifications(userId: String, beforeEpoch: Long): Unit =
        withContext(Dispatchers.IO) {
            queries.deleteOldNotifications(userId = userId, createdAt = beforeEpoch)
        }

    override suspend fun countUnread(userId: String): Long =
        withContext(Dispatchers.IO) { queries.countUnreadByUser(userId).executeAsOne() }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private fun com.journey.database.migrations.NotificationEntity.toRecord() = NotificationRecord(
        id          = id,
        userId      = userId,
        type        = type,
        title       = title,
        description = description,
        isRead      = isRead != 0L,
        createdAt   = createdAt,
    )
}
