package org.fitverse.data.remote.util

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    @SerialName("success")
    val success: Boolean,

    @SerialName("data")
    val data: T,

    @SerialName("message")
    val message: String? = null,

    @SerialName("error")
    val error: ErrorResponse? = null
)

@Serializable
data class ErrorResponse(
    @SerialName("code")
    val code: String,

    @SerialName("message")
    val message: String,

    @SerialName("details")
    val details: String? = null
)