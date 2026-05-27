package org.fitverse.domain.repository

import org.fitverse.domain.models.dashboard.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Interface de repositório seguindo princípios SOLID
 * Permite fácil substituição para testes
 */
interface FriendsRepository {
    fun getFriends(): Flow<List<UserProfile>>
    fun getSuggestions(): Flow<List<UserProfile>>
    suspend fun addFriend(userId: String): Result<Unit>
    suspend fun addFriendByCode(code: String): Result<Unit>
    suspend fun removeFriend(userId: String): Result<Unit>
    suspend fun searchUsers(query: String): Result<List<UserProfile>>
}

/**
 * Implementação fake para preview e testes
 */
class FakeFriendsRepository : FriendsRepository {

    private val mockFriends = listOf(
        UserProfile("1", "Carlos Silva", "@carlos_fit", streak = 12),
        UserProfile("2", "Ana Paula", "@ana.paula", streak = 5),
        UserProfile("3", "Marcos Vinícius", "@marcos_v", streak = 30),
        UserProfile("4", "Juliana Costa", "@juju_costa", streak = 7),
        UserProfile("5", "Pedro Santos", "@pedro.santos", streak = 15)
    )

    private val mockSuggestions = listOf(
        UserProfile("6", "Julia Costa", "@juliac", mutualConnections = 4),
        UserProfile("7", "Pedro Lima", "@pedrol", mutualConnections = 2),
        UserProfile("8", "Rafael Dias", "@rafael_d", mutualConnections = 8),
        UserProfile("9", "Mariana Silva", "@mari_fit", mutualConnections = 3)
    )

    override fun getFriends(): Flow<List<UserProfile>> {
        return kotlinx.coroutines.flow.flowOf(mockFriends)
    }

    override fun getSuggestions(): Flow<List<UserProfile>> {
        return kotlinx.coroutines.flow.flowOf(mockSuggestions)
    }

    override suspend fun addFriend(userId: String): Result<Unit> {
        kotlinx.coroutines.delay(500) // Simula network delay
        return Result.success(Unit)
    }

    override suspend fun addFriendByCode(code: String): Result<Unit> {
        kotlinx.coroutines.delay(500)
        return if (code.startsWith("FIT-")) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid code"))
        }
    }

    override suspend fun removeFriend(userId: String): Result<Unit> {
        kotlinx.coroutines.delay(500)
        return Result.success(Unit)
    }

    override suspend fun searchUsers(query: String): Result<List<UserProfile>> {
        kotlinx.coroutines.delay(300)
        return Result.success(
            mockSuggestions.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.username.contains(query, ignoreCase = true)
            }
        )
    }
}