package com.example.data.mappers

import com.example.data.models.entity.User
import com.fitverse.database.User as UserEntity

fun UserEntity.toDomain(): User {
    return User(
        id = user_id,
        name = user_name,
        level = user_level.toInt(),
        email = user_email.orEmpty(),
        gender = user_gender.orEmpty(),
        weight = user_weight,
        height = user_height,
        age = user_age.toInt(),
        goals = user_goals.orEmpty(),
        trainingLevel = user_training_level.orEmpty(),
        isValidateCode = user_is_validate_code != 0L,
        token = user_token.orEmpty()
    )
}
