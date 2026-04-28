package com.example.remote.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO para criar/atualizar usuário
 * Contém apenas os campos necessários para a request
 */
@Serializable
data class UserRequestDto(
    @SerialName("uid")              val uid: String,  // ✅ necessário no POST /register
    @SerialName("name")             val name: String,
    @SerialName("email")            val email: String,
    @SerialName("lastname")         val lastname: String,
    @SerialName("username")         val username: String,
    @SerialName("birth_date")       val birthDate: String,
    @SerialName("gender")           val gender: String,
    @SerialName("class_type")       val classType: String,
    @SerialName("height")           val height: Int,
    @SerialName("weight")           val weight: Double,
    @SerialName("experience_level") val experienceLevel: String,
    @SerialName("goals")            val goals: String,
    @SerialName("is_premium")       val isPremium: Boolean,
    @SerialName("target_weight")    val targetWeight: Double? = null,
    @SerialName("target_calories")  val targetCalories: Int? = null,
    @SerialName("target_protein")   val targetProtein: Double? = null,
    @SerialName("target_carbs")     val targetCarbs: Double? = null,
    @SerialName("target_fat")       val targetFat: Double? = null,
    @SerialName("created_at")       val createdAt: String,
    @SerialName("updated_at")       val updatedAt: String
)