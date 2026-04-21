package com.example.remote

import com.example.remote.expect.IOSNetworkMonitor
import com.example.remote.expect.NetworkMonitor
import org.koin.dsl.module

val iosRemotePlatformModule = module {
    single<NetworkMonitor> { IOSNetworkMonitor() }
}