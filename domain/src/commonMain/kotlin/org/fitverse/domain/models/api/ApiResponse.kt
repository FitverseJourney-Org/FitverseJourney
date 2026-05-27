package org.fitverse.domain.models.api

// ─── Estado reativo para UI — distinto do ApiResponse HTTP do servidor ───────

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String, val code: String? = null) : ApiResponse<Nothing>()
    data object Loading : ApiResponse<Nothing>()
}

// ─── Extensões fluentes ───────────────────────────────────────────────────────

inline fun <T> ApiResponse<T>.onSuccess(block: (T) -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Success) block(data)
    return this
}

inline fun <T> ApiResponse<T>.onError(
    block: (message: String, code: String?) -> Unit,
): ApiResponse<T> {
    if (this is ApiResponse.Error) block(message, code)
    return this
}

inline fun <T> ApiResponse<T>.onLoading(block: () -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Loading) block()
    return this
}

// ─── Ponte entre Result<T> e ApiResponse<T> ──────────────────────────────────

fun <T> Result<T>.toApiResponse(): ApiResponse<T> = fold(
    onSuccess = { ApiResponse.Success(it) },
    onFailure = { ApiResponse.Error(message = it.message ?: "Erro desconhecido") },
)
