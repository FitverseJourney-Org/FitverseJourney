package org.fitverse.project

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.Application

fun Application.initFirebase() {
    val serviceAccount = object {}.javaClass.classLoader
        .getResourceAsStream("firebase-admin.json") // ✅ nome correto
        ?: throw Exception("❌ firebase-admin.json não encontrado")

    println("✅ Firebase inicializando...")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options)
        println("✅ Firebase iniciado com sucesso")
    }
}