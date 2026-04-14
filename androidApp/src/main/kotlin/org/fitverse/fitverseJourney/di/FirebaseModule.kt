package org.fitverse.fitverseJourney.di

import com.example.datasource.FirebaseAuthDataSource
import com.example.domain.repository.authentication.AuthRemoteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.dsl.module

val firebaseModule = module {

    single<AuthRemoteRepository>{
        FirebaseAuthDataSource(
            auth = get()
        )
    }
    single<FirebaseAuth>{ FirebaseAuth.getInstance() }
}