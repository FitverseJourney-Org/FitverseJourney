package org.fitverse.project.features.user.db

import org.fitverse.project.features.user.models.UserDocument
import org.fitverse.project.plugins.FirestoreService

object Collections {
    const val USERS = "users"
}

class UserRepository {
    suspend fun saveUser(document: UserDocument): UserDocument {
        FirestoreService.set(
            collection = Collections.USERS,
            documentId = document.uid,
            data       = document,
        ).getOrThrow()
        return document
    }

    suspend fun getUser(uid: String): UserDocument? {
        return FirestoreService.get(
            collection = Collections.USERS,
            documentId = uid,
            type       = UserDocument::class.java,
        ).getOrThrow()
    }

    suspend fun updateUser(uid: String, document: UserDocument): UserDocument {
        FirestoreService.set(
            collection = Collections.USERS,
            documentId = uid,
            data       = document.copy(updatedAt = System.currentTimeMillis()),
        ).getOrThrow()
        return document
    }

    suspend fun deleteUser(uid: String) {
        FirestoreService.delete(
            collection = Collections.USERS,
            documentId = uid,
        ).getOrThrow()
    }
}