package org.fitverse.project.di

import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import org.fitverse.project.features.user.controller.UserController
import org.fitverse.project.features.user.db.UserRepository
import org.fitverse.project.features.user.services.UserService
import org.koin.dsl.module

val firebaseModule = module {
    single { FirebaseApp.getInstance() }
    single { FirestoreClient.getFirestore() }
}

val authModule = module {}

val userModule = module {
    single { UserRepository() }
    single { UserService(get()) }
    single { UserController(get()) }
}

val appModule = listOf(
    firebaseModule,
    authModule,
    userModule,
)