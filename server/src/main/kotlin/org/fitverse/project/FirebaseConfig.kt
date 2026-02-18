package org.fitverse.project

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

fun initFirebase() {

    val serviceAccount =
        Thread.currentThread()
            .contextClassLoader
            .getResourceAsStream("firebase-admin.json")
            ?: throw IllegalStateException("firebase-admin.json não encontrado em resources")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    FirebaseApp.initializeApp(options)

    println("🔥 Firebase Admin inicializado com sucesso")
}
