package com.example.domain.repository.dbLocal.sqldelight.user

import com.example.domain.model.local.User

interface UserRepositoryDao {
    suspend fun insertUser(name: String, price: Double)
    suspend fun selectAllUsers(): List<User>?
    suspend fun selectUserById(id: Long): User?
    suspend fun deleteUser(id: Long)
}