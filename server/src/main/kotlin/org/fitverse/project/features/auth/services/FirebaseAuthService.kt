package org.fitverse.project.features.auth.services

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.google.firebase.auth.UserRecord
import io.ktor.client.HttpClient
import io.ktor.http.HttpHeaders
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileInputStream
import java.io.InputStream

class FirebaseAuthService private constructor(private val client: HttpClient) {

    companion object {
        fun initialize(client: HttpClient): FirebaseAuthService {
            val saPath = System.getenv("FIREBASE_SERVICE_ACCOUNT_FILE")
                ?: throw IllegalStateException("FIREBASE_SERVICE_ACCOUNT_FILE must be set to service account JSON path")
            val serviceAccount: InputStream = FileInputStream(saPath)
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()
            // Avoid initializing multiple times in dev reloads
            if (FirebaseApp.getApps().isEmpty()) FirebaseApp.initializeApp(options)
            return FirebaseAuthService(client)
        }
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun createUser(email: String, password: String, name: String?): UserRecord =
        withContext(
            Dispatchers.IO
        ) {
            val req = UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(name)
                .setEmailVerified(false)
            auth.createUser(req)
        }

    suspend fun getUser(uid: String): UserRecord = withContext(Dispatchers.IO) {
        auth.getUser(uid)
    }

    suspend fun requireValidIdToken(call: ApplicationCall): FirebaseToken {
        val header = call.request.header(HttpHeaders.Authorization)
            ?: throw IllegalArgumentException("Authorization header missing")
        val token = header.removePrefix("Bearer ").trim()
        return verifyIdToken(token)
    }

    suspend fun verifyIdToken(idToken: String): FirebaseToken = withContext(Dispatchers.IO) {
        auth.verifyIdToken(idToken)
    }

    suspend fun generatePasswordResetLink(email: String): String = withContext(Dispatchers.IO) {
        auth.generatePasswordResetLink(email)
    }

    suspend fun deleteUser(uid: String) = withContext(Dispatchers.IO) {
        auth.deleteUser(uid)
    }
}