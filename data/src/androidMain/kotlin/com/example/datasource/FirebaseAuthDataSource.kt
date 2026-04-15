package com.example.datasource

import com.example.domain.model.authentication.login.UserToken
import com.example.domain.model.authentication.register.RegisterUser
import com.example.domain.repository.authentication.AuthRemoteRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthDataSource(
    private val auth: FirebaseAuth
) : AuthRemoteRepository {

    override suspend fun login(email: String, password: String): Result<UserToken> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: throw IllegalStateException("User is null")

            val tokenResult = user.getIdToken(true).await()
            val token = tokenResult.token ?: ""

            Result.success(UserToken(token))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(data: RegisterUser): Result<Unit> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(data.email, data.password).await()
            val user = authResult.user ?: throw IllegalStateException("User is null after register")

            val tokenResult = user.getIdToken(true).await()
            val userToken = UserToken(tokenResult.token ?: "")

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}