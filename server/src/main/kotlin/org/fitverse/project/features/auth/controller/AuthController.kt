package org.fitverse.project.features.auth.controller

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.fitverse.project.features.auth.routes.RegisterRequest
import org.fitverse.project.features.auth.services.AuthService

class AuthController(
    private val authService: AuthService
) {
    suspend fun register(call: ApplicationCall) {
        val request = call.receive<RegisterRequest>() // Recebe o DTO
        val result = authService.createUser(request) // Chama a lógica

        result.onSuccess {
            call.respond(HttpStatusCode.Created, it) // Responde 201
        }.onFailure {
            call.respond(HttpStatusCode.BadRequest, it.message ?: "Erro")
        }
    }
}