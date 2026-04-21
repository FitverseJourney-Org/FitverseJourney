package com.example.remote.expect

import kotlinx.coroutines.flow.Flow

/**
 * Interface para monitorar conectividade
 * Implementação específica por plataforma
 */
expect interface NetworkMonitor {
    fun isConnected(): Boolean
    fun observeConnectivity(): Flow<Boolean>
}