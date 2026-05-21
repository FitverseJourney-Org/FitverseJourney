package org.fitverse.data.remote

import org.fitverse.domain.repository.authentication.AuthRepository
import org.fitverse.data.remote.FirebaseAuthRepositoryImpl
import org.fitverse.data.remote.expect.AndroidNetworkMonitor
import org.fitverse.data.remote.expect.NetworkMonitor
import org.fitverse.data.remote.mapper.AuthMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidRemotePlatformModule = module {
    single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }
    single<AuthMapper> { AuthMapper() }
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<FirebaseUser?> { FirebaseAuth.getInstance().currentUser }
    single<AuthRepository> {
        FirebaseAuthRepositoryImpl(
            firebaseAuth              = get(),
            authMapper                = get(),
            appAuthenticateRepository = get(),
        )
    }
}