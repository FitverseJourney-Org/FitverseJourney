package com.example.data.models.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("user_id")
    val id: String,
    @SerialName("user_name")
    val name: String,
    @SerialName("user_level")
    val level: Int,
    @SerialName("user_email")
    val email: String,
    @SerialName("user_gender")
    val gender: String,
    @SerialName("user_weight")
    val weight: Double,
    @SerialName("user_height")
    val height: Double,
    @SerialName("user_age")
    val age: Int,
    @SerialName("user_goals")
    val goals: String,
    @SerialName("user_training_level")
    val trainingLevel: String,
    @SerialName("user_IsValidateCode")
    val isValidateCode: Boolean,
    @SerialName("user_token")
    val token: String
)