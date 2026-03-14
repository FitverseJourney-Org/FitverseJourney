package org.fitverse.project.features.auth.controller

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.fitverse.project.features.auth.routes.LoginRequest
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

    suspend fun login(call: ApplicationCall) {
        // 1. Recebe o DTO do corpo da requisição
        val request = call.receive<LoginRequest>()

        // 2. Chama o serviço e captura o resultado
        val result = authService.authenticate(request)

        // 3. Responde de acordo com o sucesso ou falha
        result.onSuccess { token ->
            call.respond(HttpStatusCode.OK, token)
        }.onFailure { error ->
            call.respond(HttpStatusCode.Unauthorized, mapOf("error" to (error.message ?: "Credenciais inválidas")))
        }
    }
}