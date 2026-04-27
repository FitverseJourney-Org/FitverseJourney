package org.fitverse.project.plugins

import org.fitverse.project.models.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respondText
import io.ktor.http.ContentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.fitverse.project.models.ApiResponse

suspend inline fun <reified T> ApplicationCall.respondSuccess(
    data: T? = null,
    status: HttpStatusCode = HttpStatusCode.OK
) {
    respondText(
        text = Json.encodeToString(ApiResponse(success = true, data = data)),
        contentType = ContentType.Application.Json,
        status = status
    )
}

suspend fun ApplicationCall.respondError(
    code: String,
    message: String,
    status: HttpStatusCode = HttpStatusCode.BadRequest
) {
    respondText(
        text = Json.encodeToString(
            ApiResponse<Unit>(
                success = false,
                error = ErrorResponse(code = code, message = message)
            )
        ),
        contentType = ContentType.Application.Json,
        status = status
    )
}