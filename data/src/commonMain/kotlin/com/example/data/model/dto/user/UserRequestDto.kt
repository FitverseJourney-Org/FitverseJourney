package com.example.data.model.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ============================================================
// Request DTO (para POST/PUT)
// ============================================================
/**
 * DTO para criar/atualizar usuário
 * Contém apenas os campos necessários para a request
 */
@Serializable
data class UserRequestDto(
    @SerialName("name")
    val name: String,

    @SerialName("email")
    val email: String,

    @SerialName("gender")
    val gender: String,

    @SerialName("birth_date")
    val birthDate: String,

    @SerialName("height_cm")
    val heightCm: Int,

    @SerialName("weight_kg")
    val weightKg: Double,

    @SerialName("fitness_level")
    val fitnessLevel: String,

    @SerialName("fitness_goal")
    val fitnessGoal: String
)
