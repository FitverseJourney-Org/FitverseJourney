package org.fitverse.project.features.auth.routes

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.util.logging.error
import kotlinx.serialization.Serializable
import org.fitverse.project.features.auth.controller.AuthController
import org.fitverse.project.features.auth.services.FirebaseAuthService
import org.koin.ktor.ext.inject

fun Route.authRouting() {
    val authController by inject<AuthController>()

    route("/auth"){
        post("/register") {
            authController.register(call) // Apenas passa a bola
        }
        post("/login") {
            authController.login(call)
        }
    }

}
@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String? = null
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class ResetRequest(val email: String)

@Serializable
data class ResetConfirmRequest(val oobCode: String, val newPassword: String)