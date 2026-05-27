package org.fitverse.data.local.datasource.user

import com.journey.user.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interface para operações locais de User
 * Define o contrato para acesso ao banco de dados
 */
interface UserLocalDataSource {
    fun observeUser(userId: String): Flow<UserEntity?>
    suspend fun getUser(userId: String): UserEntity?
    suspend fun insertUser(entity: UserEntity)
    suspend fun updateUser(entity: UserEntity)
    suspend fun deleteUser(userId: String)
}