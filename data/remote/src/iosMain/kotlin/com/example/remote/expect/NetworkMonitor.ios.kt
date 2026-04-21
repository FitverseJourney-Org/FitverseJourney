package com.example.remote.expect

import com.example.remote.expect.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import platform.Network.*

class IOSNetworkMonitor : NetworkMonitor {

    private val monitor = nw_path_monitor_create()

    override fun isConnected(): Boolean {
        // Implementação básica para iOS
        // Para produção, usar NWPathMonitor corretamente
        return true
    }

    override fun observeConnectivity(): Flow<Boolean> {
        // Implementação básica
        // Para produção, implementar observação real
        return flowOf(true)
    }
}

actual interface NetworkMonitor {
    actual fun isConnected(): Boolean
    actual fun observeConnectivity(): Flow<Boolean>
}