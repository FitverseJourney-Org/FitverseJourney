package org.fitverse.project.features.user.services

import org.fitverse.project.features.user.db.UserRepository
import org.fitverse.project.features.user.models.UserDocument
import org.fitverse.project.features.user.models.UserRequest
import org.fitverse.project.features.user.models.toDocument

class UserService(
    private val userRepository: UserRepository
) {
    suspend fun createUser(request: UserRequest): Result<UserDocument> = runCatching {
        userRepository.saveUser(request.toDocument())
    }
    suspend fun getUser(uid: String): Result<UserDocument> = runCatching {
        userRepository.getUser(uid) ?: throw Exception("Usuário não encontrado")
    }
    suspend fun updateUser(uid: String, request: UserRequest): Result<UserDocument> = runCatching {
        userRepository.updateUser(uid, request.toDocument())
    }
    suspend fun deleteUser(uid: String): Result<Unit> = runCatching {
        userRepository.deleteUser(uid)
    }
}