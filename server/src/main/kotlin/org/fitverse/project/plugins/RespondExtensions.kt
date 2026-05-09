package org.fitverse.project.plugins

import org.fitverse.project.models.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondText
import io.ktor.http.ContentType
import io.ktor.server.response.respond
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.fitverse.project.models.ApiResponse

// Com data (get, update)
/**
 * Resposta de sucesso com tipo preservado em runtime via reified.
 * Sem reified, o Ktor serializa como Any? e perde o serializer da classe concreta.
 */
suspend inline fun <reified T : Any> ApplicationCall.respondSuccess(
    data    : T?            = null,
    message : String?       = null,
    status  : HttpStatusCode = HttpStatusCode.OK,
) {
    respond(
        status  = status,
        message = ApiResponse.success(data = data, message = message)
    )
}

// Sem data (create, delete)
suspend fun ApplicationCall.respondSuccess(
    status: HttpStatusCode = HttpStatusCode.OK
) {
    respondText(
        text        = Json.encodeToString(ApiResponse<Unit>(success = true, data = null)),
        contentType = ContentType.Application.Json,
        status      = status
    )
}

/**
 * Resposta de erro — não precisa de reified pois ApiResponse<Unit> é concreto.
 */
suspend fun ApplicationCall.respondError(
    code    : String,
    message : String,
    details : String?        = null,
    status  : HttpStatusCode = HttpStatusCode.InternalServerError,
) {
    respond(
        status  = status,
        message = ApiResponse.error(code = code, message = message, details = details)
    )
}