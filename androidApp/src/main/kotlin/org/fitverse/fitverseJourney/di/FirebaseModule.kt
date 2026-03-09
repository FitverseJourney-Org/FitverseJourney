package org.fitverse.fitverseJourney.di

import com.example.datasource.FirebaseAuthRemoteRepository
import com.example.domain.repository.authentication.AuthRemoteRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val firebaseModule = module {

    single<AuthRemoteRepository>{
        FirebaseAuthRemoteRepository(get())
    }
    single<FirebaseAuth> {
        FirebaseAuth.getInstance()
    }
}