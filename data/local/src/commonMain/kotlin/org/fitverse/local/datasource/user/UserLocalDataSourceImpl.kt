package org.fitverse.local.datasource.user

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.journey.AppDatabase
import com.journey.user.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.fitverse.data.local.datasource.user.UserLocalDataSource

class UserLocalDataSourceImpl(
    database: AppDatabase,
) : UserLocalDataSource {

    private val queries = database.userEntityQueries

    override suspend fun insertUser(entity: UserEntity): Unit = withContext(Dispatchers.IO) {
        queries.insertUser(
            uid       = entity.uid,
            name      = entity.name,
            lastname  = entity.lastname,
            username  = entity.username,
            email     = entity.email,
            gender    = entity.gender,
            birthDate = entity.birthDate,
            weight    = entity.weight,
            height    = entity.height,
            isPremium = entity.isPremium,
            photoUrl  = entity.photoUrl,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }

    override suspend fun updateUser(entity: UserEntity): Unit = withContext(Dispatchers.IO) {
        queries.updateUser(
            uid       = entity.uid,
            name      = entity.name,
            lastname  = entity.lastname,
            username  = entity.username,
            email     = entity.email,
            gender    = entity.gender,
            birthDate = entity.birthDate,
            weight    = entity.weight,
            height    = entity.height,
            isPremium = entity.isPremium,
            photoUrl  = entity.photoUrl,
            updatedAt = entity.updatedAt,
        )
    }

    override suspend fun deleteUser(userId: String): Unit = withContext(Dispatchers.IO) {
        queries.deleteUser(userId)
    }

    override suspend fun getUser(userId: String): UserEntity? = withContext(Dispatchers.IO) {
        queries.selectUserByUid(userId).executeAsOneOrNull()
    }

    override fun observeUser(userId: String): Flow<UserEntity?> =
        queries.selectUserByUid(userId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
}
