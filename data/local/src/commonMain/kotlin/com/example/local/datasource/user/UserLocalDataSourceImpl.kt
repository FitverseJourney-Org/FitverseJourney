package com.example.local.datasource.user

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.journey.database.AppDatabase.AppDatabase
import com.journey.database.migrations.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Implementação com SQLDelight
 * Responsável por todas as operações de banco de dados
 */
class UserLocalDataSourceImpl(
    database: AppDatabase,
) : UserLocalDataSource {

    private val queries = database.appDatabaseQueries

    override suspend fun insertUser(entity: UserEntity): Unit = withContext(Dispatchers.IO) {
        queries.insertUser(
            uid             = entity.uid,          // ✅ String
            name            = entity.name,
            email           = entity.email,
            lastname        = entity.lastname,
            username        = entity.username,
            birthDate       = entity.birthDate,
            gender          = entity.gender,
            classType       = entity.classType,
            weight          = entity.weight,
            height          = entity.height,
            experienceLevel = entity.experienceLevel,
            goals           = entity.goals,
            isPremium       = entity.isPremium,
            targetWeight    = entity.targetWeight,
            targetCalories  = entity.targetCalories,
            targetProtein   = entity.targetProtein,
            targetCarbs     = entity.targetCarbs,
            targetFat       = entity.targetFat,
            createdAt       = entity.createdAt,
            updatedAt       = entity.updatedAt,
        )
    }

    override suspend fun updateUser(entity: UserEntity): Unit = withContext(Dispatchers.IO) {
        queries.updateUser(
            uid             = entity.uid,
            name            = entity.name,
            email           = entity.email,
            lastname        = entity.lastname,
            username        = entity.username,
            birthDate       = entity.birthDate,
            gender          = entity.gender,
            classType       = entity.classType,
            weight          = entity.weight,
            height          = entity.height,
            experienceLevel = entity.experienceLevel,
            goals           = entity.goals,
            isPremium       = entity.isPremium,
            targetWeight    = entity.targetWeight,
            targetCalories  = entity.targetCalories,
            targetProtein   = entity.targetProtein,
            targetCarbs     = entity.targetCarbs,
            targetFat       = entity.targetFat,
            updatedAt       = entity.updatedAt,
        )
    }

    override suspend fun deleteUser(userId: String): Unit = withContext(Dispatchers.IO) {
        queries.deleteUser(userId)                 // ✅ String
    }

    override suspend fun deleteAllUsers(): Unit = withContext(Dispatchers.IO) {
        queries.deleteAllUsers()
    }

    // ✅ retorna UserEntity — sem mapper aqui
    override suspend fun getUser(userId: String): UserEntity? = withContext(Dispatchers.IO) {
        queries.selectUserByUid(userId).executeAsOneOrNull()
    }

    // ✅ retorna Flow<UserEntity?> — sem mapper aqui
    override fun observeUser(userId: String): Flow<UserEntity?> =
        queries.selectUserByUid(userId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
}