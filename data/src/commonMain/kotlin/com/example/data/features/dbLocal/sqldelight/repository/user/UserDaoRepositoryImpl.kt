package com.example.data.features.dbLocal.sqldelight.repository.user

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import com.example.data.database.DatabaseSqlDeLightHelper
import com.example.domain.model.local.User
import com.example.domain.repository.dbLocal.sqldelight.user.UserRepositoryDao

class UserDaoRepositoryImpl(
    private val databaseSqlDeLightHelper: DatabaseSqlDeLightHelper
) : UserRepositoryDao {

    override suspend fun insertUser(name: String, price: Double) {
        databaseSqlDeLightHelper.withDatabase { db ->
            db.userQueries.insertUser(name, price)
        }
    }

    override suspend fun selectAllUsers(): List<User>? {
        return databaseSqlDeLightHelper.withDatabase { db ->
            db.userQueries.selectAllUsers().awaitAsList().map {
                User(it.id, it.name, it.price)
            }
        }
    }

    override suspend fun selectUserById(id: Long): User? {
        return databaseSqlDeLightHelper.withDatabase { db ->
            db.userQueries.selectUserById(id).awaitAsOneOrNull()?.let {
                User(it.id, it.name, it.price)
            }
        }
    }

    override suspend fun deleteUser(id: Long) {
        databaseSqlDeLightHelper.withDatabase { db ->
            db.userQueries.deleteUser(id)
        }
    }

}