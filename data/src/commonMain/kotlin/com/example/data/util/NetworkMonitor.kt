package com.example.data.util

import kotlinx.coroutines.flow.Flow

/**
 * Interface para monitorar conectividade
 * Implementação específica por plataforma
 */
interface NetworkMonitor {
    fun isConnected(): Boolean
    fun observeConnectivity(): Flow<Boolean>
}