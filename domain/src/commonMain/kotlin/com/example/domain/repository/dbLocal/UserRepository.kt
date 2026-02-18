package com.example.domain.repository.dbLocal

import com.example.domain.model.dbLocal.User

interface UserRepository {
    suspend fun saveUser(user: User)
}