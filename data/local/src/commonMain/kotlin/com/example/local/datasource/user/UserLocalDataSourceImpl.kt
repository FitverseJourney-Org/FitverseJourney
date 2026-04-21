package com.example.local.datasource.user

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.coroutines.mapToList
import com.example.domain.model.local.User
import com.example.local.mapper.UserMapper
import com.journey.database.AppDatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Implementação com SQLDelight
 * Responsável por todas as operações de banco de dados
 */
class UserLocalDataSourceImpl(
    database: AppDatabase,
    private val userMapper: UserMapper
) : UserLocalDataSource {

    private val queries = database.appDatabaseQueries

    override suspend fun insertUser(user: User): Unit = withContext(Dispatchers.IO) {
        val entity = userMapper.mapDomainToEntity(user)
        queries.insertUser(
            id = entity.id,
            name = entity.name,
            email = entity.email,
            gender = entity.gender,
            age = entity.age,
            weight = entity.weight,
            height = entity.height,
            experienceLevel = entity.experienceLevel,
            goals = entity.goals,
            targetWeight = entity.targetWeight,
            targetCalories = entity.targetCalories,
            targetProtein = entity.targetProtein,
            targetCarbs = entity.targetCarbs,
            targetFat = entity.targetFat,
            isPremium = entity.isPremium,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    override suspend fun updateUser(user: User) : Unit = withContext(Dispatchers.IO) {
        queries.updateUser(
            id = user.id ?: 0L,
            name = user.name,
            email = user.email,
            gender = user.gender,
            age = user.age.toLong(),
            weight = user.weight,
            height = user.height.toDouble(),
            experienceLevel = user.experienceLevel,
            goals = user.goals,
            targetCalories = user.targetCalories.toLong(),
            targetProtein = user.targetProtein,
            targetCarbs = user.targetCarbs,
            targetFat = user.targetFat,
            isPremium = if (user.isPremium) 1L else 0L,
            updatedAt = user.updatedAt,
            targetWeight = user.weight
        )
    }

    override suspend fun deleteUser(userId: Long): Unit = withContext(Dispatchers.IO) {
        queries.deleteUser(userId)
    }

    override suspend fun deleteAllUsers(): Unit = withContext(Dispatchers.IO) {
        queries.deleteAllUsers()
    }

    // ✅ Corrigido: .executeAsOneOrNull() + retorno nullable User?
    override suspend fun getUserById(userId: Long): User? = withContext(Dispatchers.IO) {
        queries.selectUserById(userId)
            .executeAsOneOrNull()
            ?.let { userMapper.mapEntityToDomain(it) }
    }

    // ✅ Corrigido: mapper agora recebe o tipo SQLDelight correto
    override suspend fun getAllUsers(): List<User> = withContext(Dispatchers.IO) {
        queries.selectAllUsers()
            .executeAsList()
            .let { userMapper.mapEntityListToDomainList(it) }
    }

    // OBSERVE — Flow reativo
    override fun observeUser(userId: Long): Flow<User?> {
        return queries.selectUserById(userId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { entity -> entity?.let { userMapper.mapEntityToDomain(it) } }
    }

    override fun observeAllUsers(): Flow<List<User>> {
        return queries.selectAllUsers()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { userMapper.mapEntityListToDomainList(it) }
    }
}