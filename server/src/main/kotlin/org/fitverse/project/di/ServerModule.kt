package org.fitverse.project.di

import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import org.fitverse.project.features.auth.controller.AuthController
import org.fitverse.project.features.auth.db.AuthRepository
import org.fitverse.project.features.auth.services.AuthService
import org.fitverse.project.features.user.controller.UserController
import org.fitverse.project.features.user.db.UserRepository
import org.fitverse.project.features.user.services.UserService
import org.koin.dsl.module


val firebaseModule = module {
    single { FirebaseApp.getInstance() }
    single { FirestoreClient.getFirestore() }
}

val authModule = module {
    single { AuthRepository(get()) }
    single { AuthService(get()) }
    single { AuthController(get()) }
}

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