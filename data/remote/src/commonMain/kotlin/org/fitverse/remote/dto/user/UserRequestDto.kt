package org.fitverse.remote.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO para criar/atualizar usuário
 * Contém apenas os campos necessários para a request
 */
@Serializable
data class UserRequestDto(
    @SerialName("uid")              val uid             : String  = "",
    @SerialName("name")             val name            : String  = "",
    @SerialName("lastname")         val lastname        : String  = "",
    @SerialName("username")         val username        : String  = "",
    @SerialName("email")            val email           : String,
    @SerialName("gender")           val gender          : String,
    @SerialName("birth_date")       val birthDate       : String  = "",
    @SerialName("weight")           val weight          : Double  = 0.0,
    @SerialName("height")           val height          : Int,
    @SerialName("is_premium")       val isPremium       : Boolean = false,
    @SerialName("photo_url")        val photoUrl        : String  = "",
    @SerialName("created_at")       val createdAt       : Long    = 0L,
    @SerialName("updated_at")       val updatedAt       : Long    = 0L,
)


