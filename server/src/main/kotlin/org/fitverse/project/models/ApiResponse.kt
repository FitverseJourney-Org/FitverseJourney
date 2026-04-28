package org.fitverse.project.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val error: ErrorResponse? = null,
) {
    companion object {
        fun <T> success(data: T? = null, message: String? = null) =
            ApiResponse(success = true, data = data, message = message)

        fun error(code: String, message: String, details: String? = null) =
            ApiResponse<Unit>(
                success = false,
                error = ErrorResponse(code = code, message = message, details = details)
            )
    }
}

@Serializable
data class ErrorResponse(
    val code: String,
    val message: String,
    val details: String? = null
)