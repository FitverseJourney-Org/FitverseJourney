package com.example.remote

import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.authentication.AuthResultDto
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await


class FirebaseAuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthResultDto {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

        val user = result.user ?: throw Exception("Login falhou: usuário nulo")
        val token = user.getIdToken(false).await().token

        return AuthResultDto(
            uid = user.uid,
            email = user.email,
            token = token
        )
    }

    override suspend fun register(email: String, password: String): AuthResultDto {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

        val user = result.user ?: throw Exception("Registro falhou: usuário nulo")
        val token = user.getIdToken(false).await().token

        return AuthResultDto(
            uid = user.uid,
            email = user.email,
            token = token
        )
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
}