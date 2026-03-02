package org.fitverse.fitverseJourney.di

import com.example.datasource.FirebaseAuthRemoteDataSource
import com.example.domain.repository.authentication.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val firebaseModule = module {

    single<AuthRemoteDataSource>{
        FirebaseAuthRemoteDataSource(get())
    }
    single<FirebaseAuth> {
        FirebaseAuth.getInstance()
    }
}