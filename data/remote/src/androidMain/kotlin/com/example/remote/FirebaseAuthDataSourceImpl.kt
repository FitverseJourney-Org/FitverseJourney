package com.example.remote

import com.example.domain.models.auth.AuthResult
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.authentication.AuthResultDto
import com.example.remote.mapper.AuthMapper
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val authMapper: AuthMapper,
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthResult {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val user   = result.user ?: throw Exception("Login falhou: usuário nulo")
        val token  = user.getIdToken(false).await().token

        return authMapper.mapDtoToDomain(
            AuthResultDto(
                uid   = user.uid,
                email = user.email,
                token = token,
            )
        )
    }

    override suspend fun register(email: String, password: String): AuthResult {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user   = result.user ?: throw Exception("Registro falhou: usuário nulo")
        val token  = user.getIdToken(false).await().token

        return authMapper.mapDtoToDomain(
            AuthResultDto(
                uid   = user.uid,
                email = user.email,
                token = token,
            )
        )
    }

    override suspend fun resetPassword(email: String): Result<Unit> = runCatching {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUserId(): String? =
        firebaseAuth.currentUser?.uid
}