package org.fitverse.project.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val user_name: String,
    val user_level: Int,
    val user_email: String,
    val user_gender: String,
    val user_weight: Double,
    val user_height: Double,
    val user_age: Int,
    val user_goals: String,
    val user_training_level: String,
    val user_is_validate_code: Boolean,
    val user_token: String? = null,
    val updated_at: Long? = null
)
