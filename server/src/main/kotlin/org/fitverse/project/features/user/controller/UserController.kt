package org.fitverse.project.features.user.controller

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.fitverse.project.features.user.models.UserDocument
import org.fitverse.project.features.user.models.UserRequest
import org.fitverse.project.features.user.services.UserService
import org.fitverse.project.plugins.respondError
import org.fitverse.project.plugins.respondSuccess

class UserController(
    private val userService: UserService
) {

    suspend fun get(call: ApplicationCall) {
        val uid = call.parameters["uid"]
            ?: return call.respondError(code = "BAD_REQUEST", message = "uid obrigatório")

        val result = userService.getUser(uid)

        if (result.isSuccess) {
            // ✅ tipo explícito — Ktor encontra o serializer da UserDocument
            call.respondSuccess<UserDocument>(data = result.getOrNull())
        } else {
            call.respondError(
                code    = "NOT_FOUND",
                message = "Usuário não encontrado",
                status  = HttpStatusCode.NotFound
            )
        }
    }
    suspend fun create(call: ApplicationCall) {
        val request = call.receive<UserRequest>()
        val result  = userService.createUser(request)

        if (result.isSuccess) {
            call.respondSuccess<Unit>(status = HttpStatusCode.Created)
        } else {
            call.respondError(
                code    = "CREATE_ERROR",
                message = result.exceptionOrNull()?.message ?: "Erro ao criar usuário"
            )
        }
    }
    suspend fun update(call: ApplicationCall) {
        val uid = call.parameters["uid"]
            ?: return call.respondError(code = "BAD_REQUEST", message = "uid obrigatório")

        val request = call.receive<UserRequest>()
        val result  = userService.updateUser(uid, request)

        if (result.isSuccess) {
            // ✅ tipo explícito
            call.respondSuccess<UserDocument>(data = result.getOrNull())
        } else {
            call.respondError(
                code    = "UPDATE_ERROR",
                message = result.exceptionOrNull()?.message ?: "Erro ao atualizar usuário"
            )
        }
    }
    suspend fun delete(call: ApplicationCall) {
        val uid = call.parameters["uid"]
            ?: return call.respondError(code = "BAD_REQUEST", message = "uid obrigatório")

        val result = userService.deleteUser(uid)

        if (result.isSuccess) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respondError(
                code    = "DELETE_ERROR",
                message = result.exceptionOrNull()?.message ?: "Erro ao deletar usuário"
            )
        }
    }
}