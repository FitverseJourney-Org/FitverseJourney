package com.example.data

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import com.example.DbHelper

interface UserDao {
    suspend fun insertUser(name: String, price: Double)
    suspend fun selectAllUsers(): List<User>?
    suspend fun selectUserById(id: Long): User?
    suspend fun deleteUser(id: Long)
}


class UserDaoImpl(
    private val dbHelper: DbHelper
) : UserDao {

    override suspend fun insertUser(name: String, price: Double) {
        dbHelper.withDatabase { db ->
            db.userQueries.insertUser(name, price)
        }
    }

    override suspend fun selectAllUsers(): List<User>? {
        return dbHelper.withDatabase { db ->
            db.userQueries.selectAllUsers().awaitAsList().map {
                User(it.id, it.name, it.price)
            }
        }
    }

    override suspend fun selectUserById(id: Long): User? {
        return dbHelper.withDatabase { db ->
            db.userQueries.selectUserById(id).awaitAsOneOrNull()?.let {
                User(it.id, it.name, it.price)
            }
        }
    }

    override suspend fun deleteUser(id: Long) {
        dbHelper.withDatabase { db ->
            db.userQueries.deleteUser(id)
        }
    }

}