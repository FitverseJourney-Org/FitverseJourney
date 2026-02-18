package com.example.domain.repository

import com.example.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun observeSessions(): Flow<List<Session>>

    suspend fun saveSession(session: Session)

    suspend fun getPendingSessions(): List<Session>

    suspend fun markSessionAsSynced(id: String)
}