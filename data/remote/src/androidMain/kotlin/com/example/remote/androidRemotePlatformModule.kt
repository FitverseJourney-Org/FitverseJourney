package com.example.remote


import com.example.remote.expect.NetworkMonitor
import com.example.domain.repository.authentication.AuthRepository
import com.example.remote.expect.AndroidNetworkMonitor
import com.example.remote.mapper.AuthMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidRemotePlatformModule = module {
    single<NetworkMonitor> { AndroidNetworkMonitor(androidContext()) }
    single<AuthMapper>{ AuthMapper() }
    single<FirebaseAuth> { FirebaseAuth.getInstance() }
    single<FirebaseUser?> { FirebaseAuth.getInstance().currentUser }
    single<AuthRepository> {FirebaseAuthRepositoryImpl(firebaseAuth = get(), authMapper = get())}
}