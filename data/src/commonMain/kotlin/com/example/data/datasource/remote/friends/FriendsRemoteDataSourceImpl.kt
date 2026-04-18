package com.example.data.datasource.remote.friends

import com.example.domain.friends.UserProfile
import com.example.domain.repository.FakeFriendsRepository
import com.example.domain.repository.FriendsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

/**
 * Implementação real com Retrofit/Ktor
 */
class FriendsRemoteDataSourceImpl(
//    private val apiService: FriendsApiService,
    private val localDataSource: FakeFriendsRepository
) : FriendsRepository {

    override fun getFriends(): Flow<List<UserProfile>> {
        return localDataSource.getFriends()
            .onStart {
                // Sincronizar com backend
                try {
//                    val remoteFriends = apiService.getFriends()
//                    localDataSource.saveFriends(remoteFriends)
                } catch (e: Exception) {
                    // Log error, continua com dados locais
                }
            }
    }

    override fun getSuggestions(): Flow<List<UserProfile>> {
        return flow {
            try {
//                val suggestions = apiService.getSuggestions()
//                emit(suggestions)
            } catch (e: Exception) {
                emit(emptyList())
            }
        }
    }

    override suspend fun addFriend(userId: String): Result<Unit> {
        return try {
//            apiService.addFriend(userId)
            localDataSource.addFriend(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addFriendByCode(code: String): Result<Unit> {
        return try {
//            val user = apiService.getUserByCode(code)
//            apiService.addFriend(user.id)
//            localDataSource.addFriend(user.id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFriend(userId: String): Result<Unit> {
        return try {
//            apiService.removeFriend(userId)
            localDataSource.removeFriend(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchUsers(query: String): Result<List<UserProfile>> {
//        return try {
////            val results = apiService.searchUsers(query)
//            Result.success(results)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
        return localDataSource.searchUsers(query)
    }
}