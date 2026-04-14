package org.fitverse.project.features.auth.routes

import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import org.fitverse.project.features.auth.controller.AuthController
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