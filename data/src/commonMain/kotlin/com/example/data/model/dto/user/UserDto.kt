package com.example.data.model.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ============================================================
// DTOs (Data Transfer Objects) - API Models
// ============================================================
/**
 * DTO que representa o User vindo da API
 * Usa anotações de serialização (kotlinx.serialization)
 */
@Serializable
data class UserDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("gender") val gender: String,
    @SerialName("age") val age: Int, // ISO8601: "1990-05-15"
    @SerialName("height") val height: Int,   // Mudado para Double para bater com o Domain
    @SerialName("weight") val weight: Double,
    @SerialName("experienceLevel") val experienceLevel: String,
    @SerialName("goals") val goals: String,
    @SerialName("is_premium") val isPremium: Boolean,
    // Novos campos adicionados para bater com o Domain
    @SerialName("target_weight") val targetWeight: Double? = null,
    @SerialName("target_calories") val targetCalories: Int? = null,
    @SerialName("target_protein") val targetProtein: Double? = null,
    @SerialName("target_carbs") val targetCarbs: Double? = null,
    @SerialName("target_fat") val targetFat: Double? = null,
    @SerialName("created_at") val createdAt: Long? = null, // "2023-10-27T10:00:00Z"
    @SerialName("updated_at") val updatedAt: Long? = null
)
