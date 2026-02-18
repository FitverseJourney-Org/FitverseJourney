package com.example.data.dbLocal.repositories

import com.example.domain.model.dbLocal.User
import com.example.domain.repository.dbLocal.UserRepository
import com.example.expect.currentTimeMillis
import com.fitverse.database.UserQueries

class UserRepositoryImpl(
    private val queries: UserQueries
): UserRepository {


    override suspend fun saveUser(user: User) {
        val createdAtSeconds = currentTimeMillis()

        queries.insertUser(
            user_id = user.id,
            user_name = user.name,
            user_level = user.level.toLong(),
            user_email = user.email,
            user_gender = user.gender,
            user_weight = user.weight,
            user_height = user.height,
            user_age = user.age.toLong(),
            user_goals = user.goals,
            user_training_level = user.trainingLevel,
            user_is_validate_code = if (user.isValidateCode) 1 else 0,
            user_token = user.token,
            updated_at = createdAtSeconds * 1000,
            is_synced = 0,
            is_deleted = 0
        )
    }

}