<<<<<<<< HEAD:data/remote/src/commonMain/kotlin/com/example/remote/util/ApiResponse.kt
package com.example.remote.util
========
package com.example.domain.model.user.dto
>>>>>>>> 41bd98c79cb51e69517fff1e65b81a74b26d6f18:domain/src/commonMain/kotlin/com/example/domain/model/user/dto/ApiResponse.kt

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