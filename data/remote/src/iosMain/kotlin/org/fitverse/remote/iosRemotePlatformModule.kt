package org.fitverse.data.remote

import org.fitverse.data.remote.expect.IOSNetworkMonitor
import org.fitverse.data.remote.expect.NetworkMonitor
import org.koin.dsl.module

val iosRemotePlatformModule = module {
    single<NetworkMonitor> { IOSNetworkMonitor() }
}