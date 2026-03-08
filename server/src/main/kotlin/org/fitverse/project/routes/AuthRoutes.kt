package org.fitverse.project.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.fitverse.project.models.AuthResponse
import org.fitverse.project.models.RegisterRequest
import org.fitverse.project.models.SignInRequest

fun Route.authRoutes() {

    route("/auth") {

        post("/register") {

            val request = call.receive<RegisterRequest>()

            if (request.email.isBlank() || request.password.length < 6) {
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid data")
                )
            }

            // aqui entraria persistência no banco
            val response = AuthResponse(
                userId = "user_123",
                email = request.email,
                token = "jwt_token_example"
            )

            call.respond(HttpStatusCode.Created, response)
        }

        post("/login") {

            val request = call.receive<SignInRequest>()

            if (request.email.isBlank() || request.password.isBlank()) {
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Email or password missing")
                )
            }

            // aqui entraria validação real (DB ou Firebase)
            val response = AuthResponse(
                userId = "user_123",
                email = request.email,
                token = "jwt_token_example"
            )

            call.respond(response)
        }

        get("/me") {

            val authHeader = call.request.headers["Authorization"]
                ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Missing token")
                )

            val token = authHeader.removePrefix("Bearer ").trim()

            // validação fictícia
            if (token.isBlank()) {
                return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Invalid token")
                )
            }

            call.respond(
                mapOf(
                    "userId" to "user_123",
                    "email" to "user@email.com"
                )
            )
        }
    }
}